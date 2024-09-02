package com.example.repositories;

import com.example.entities.Competition;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.SqlValue;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;

@Repository
public class CompetitionRepository {
    private final JdbcTemplate jdbcTemplate;

    public CompetitionRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS competitions");
        jdbcTemplate.execute("CREATE TABLE competitions(" +
                "uuid uuid NOT NULL PRIMARY KEY UNIQUE, " +
                "name VARCHAR(255) NOT NULL, " +
                "owners uuid[] NOT NULL)"
        );

        var competitions = List.of(
                new Competition(
                        UUID.randomUUID(),
                        "Competition1",
                        List.of()
                ),
                new Competition(
                        UUID.randomUUID(),
                        "Competition2",
                        List.of(UUID.randomUUID())
                )
        );
        for (Competition competition : competitions) {
            save(competition);
        }
    }

    public List<Competition> getAllCompetitions() {
        return jdbcTemplate.query(
                "SELECT * FROM competitions",
                rs -> {
                    List<Competition> result = new ArrayList<>();
                    while(rs.next()) {
                        result.add(extractCompetition(rs));
                    }
                    return result;
                }
        );

    }

    public Competition changeCompetitionName(UUID competitionUUID, String newName) {
        return jdbcTemplate.query(
                "UPDATE competitions " +
                        "SET name = ? " +
                        "WHERE uuid = ? " +
                        "RETURNING *",
                ps -> {
                    ps.setString(1, newName);
                    ps.setObject(2, competitionUUID, Types.OTHER);
                },
                rs -> {
                    rs.next();
                    return extractCompetition(rs);
                }
        );
    }

    public Boolean doesUserOwnCompetition(UUID userId, UUID competitionId) {
        return jdbcTemplate.query(
                "SELECT EXISTS ( " +
                        "SELECT 1 " +
                        "FROM competitions " +
                        "WHERE owners @> ARRAY[?]::uuid[] " +
                        "  AND uuid = ?)",
                preparedStatement -> {
                    preparedStatement.setObject(1, userId, Types.OTHER);
                    preparedStatement.setObject(2, competitionId, Types.OTHER);
                },
                rs -> {
                    rs.next();
                    return rs.getBoolean("exists");
                }
        );
    }

    public Competition getByUuid(UUID uuid) {
        return jdbcTemplate.query(
                "SELECT * FROM competitions WHERE uuid = ? LIMIT 1",
                preparedStatement ->
                        preparedStatement.setObject(1, uuid, Types.OTHER),
                rs -> {
                    rs.next();
                    return extractCompetition(rs);
                }
        );
    }

    public Competition deleteByUuid(UUID uuid) {
        return jdbcTemplate.query(
                "DELETE FROM competitions WHERE uuid = ? RETURNING *",
                preparedStatement ->
                        preparedStatement.setObject(1, uuid, Types.OTHER),
                rs -> {
                    rs.next();
                    return extractCompetition(rs);
                }
        );
    }

    public Competition save(Competition competition) {
        List<String> stringOwners = competition.getOwners()
                .stream()
                .map(UUID::toString)
                .toList();
        jdbcTemplate.update(
                "INSERT INTO competitions(uuid, name, owners) " +
                        "VALUES (?, ?, ?)",
                competition.getUuid(),
                competition.getName(),
                new SqlValue() {
                    @Override
                    public void setValue(PreparedStatement ps, int paramIndex) throws SQLException {
                        final Array arrayValue = ps.getConnection().createArrayOf("uuid", stringOwners.toArray());
                        ps.setArray(paramIndex, arrayValue);
                    }
                    @Override
                    public void cleanup() {}
                }
        );
        return getByUuid(competition.getUuid());
    }

    private static @NotNull Competition extractCompetition(ResultSet rs) throws SQLException {
        List<UUID> ownersList = Arrays.stream(
                ((UUID[]) rs.getArray("owners").getArray())
                ).toList();
        return new Competition(
                UUID.fromString(rs.getString("uuid")),
                rs.getString("name"),
                ownersList
        );
    }
}
