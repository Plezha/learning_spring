package com.example.repositories;

import com.example.entities.User;
import com.example.entities.UserRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class UserRepository {
    private final PasswordEncoder passwordEncoder;

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jdbcTemplate = jdbcTemplate;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("DROP TYPE IF EXISTS user_role");
        jdbcTemplate.execute(
                "CREATE TYPE user_role AS ENUM ('%s', '%s');".formatted(UserRole.ADMIN.toString(), UserRole.USER.toString())
        );
        jdbcTemplate.execute("CREATE TABLE users(" +
                "uuid uuid NOT NULL PRIMARY KEY UNIQUE, " +
                "email VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "role user_role, " +
                "first_name VARCHAR(255), " +
                "last_name VARCHAR(255))"
        );

        var users = List.of(
                new User(
                        UUID.randomUUID(),
                        "admin@example.com",
                        passwordEncoder.encode("password"),
                        UserRole.ADMIN,
                        "John",
                        "Woo"
                ),
                new User(
                        UUID.randomUUID(),
                        "user1@example.com",
                        passwordEncoder.encode("password"),
                        UserRole.USER,
                        "Josh",
                        "Bloch"
                ),
                new User(
                        UUID.randomUUID(),
                        "user2@example.com",
                        passwordEncoder.encode("password"),
                        UserRole.USER,
                        "Josh",
                        "Long"
                )
        );
        for(User user: users) { save(user); }
    }

    public User findFirstByEmail(String email) {
        var all = findAllByEmail(email);
        if (all.isEmpty()) {
            return null;
        } else {
            return all.getFirst();
        }
    }

    public List<User> findAllByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE email = ?",
                preparedStatement -> preparedStatement.setString(1, email),
                rs -> {
                    var result = new ArrayList<User>();
                    while (rs.next()) {
                        result.add(extractUser(rs));
                    }
                    return result;
                }
        );
    }

    public User save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(uuid, email, password, role, first_name, last_name) " +
                        "VALUES (?, ?, ?, ?::user_role, ?, ?)",
                user.getUuid(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().toString(),
                user.getFirstName(),
                user.getLastName()
        );
        return findFirstByEmail(user.getEmail());
    }

    private static @NotNull User extractUser(ResultSet rs) throws SQLException {
        return new User(
                UUID.fromString(rs.getString("uuid")),
                rs.getString("email"),
                rs.getString("password"),
                UserRole.valueOf(rs.getString("role")),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
