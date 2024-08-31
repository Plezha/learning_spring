package com.example.repositories;

import com.example.entities.User;
import com.example.entities.User;
import kotlin.random.URandomKt;
import org.openapitools.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class UserRepository {
    JdbcTemplate jdbcTemplate;

    public UserRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("CREATE TABLE users(" +
                "uuid uuid NOT NULL PRIMARY KEY UNIQUE, " +
                "email VARCHAR(255), " +
                "password VARCHAR(255), " +
                "first_name VARCHAR(255), " +
                "last_name VARCHAR(255))"
        );

        List<Object[]> userData = Stream.of("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
                .map(name -> {
                    var list = new ArrayList<>();
                    list.add(UUID.randomUUID());
                    list.add(new Random().nextInt(1000) + "email@example.com");
                    list.add("password");
                    list.addAll(List.of(name.split(" ")));
                    return list.toArray();
                } )
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO users(uuid, email, password, first_name, last_name) VALUES (?,?,?,?,?)",
                userData
        );
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
                        result.add(
                                new User(
                                        UUID.fromString(rs.getString("uuid")),
                                        rs.getString("email"),
                                        rs.getString("password"),
                                        rs.getString("first_name"),
                                        rs.getString("last_name")
                                )
                        );
                    }
                    return result;
                }
        );
    }

    public User save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(uuid, email, password, first_name, last_name) VALUES (?,?,?,?,?)",
                user.getUuid(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName()
        );
        return findFirstByEmail(user.getEmail());
    }
}
