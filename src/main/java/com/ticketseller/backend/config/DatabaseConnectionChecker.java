package com.ticketseller.backend.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;

@Component
public class DatabaseConnectionChecker {

    private final CustomJdbcTemplate jdbcTemplate;

    public DatabaseConnectionChecker(CustomJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void checkDatabaseConnection() {
        try {
            CustomSqlParameters params = CustomSqlParameters.create();
            params.put("user_id", 1);

            String sql =
                    "SELECT u.user_id, u.name, u.email, u.password ,u.role " +
                            "FROM USERS u WHERE u.user_id = :user_id";

            jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return 1;
            });

            System.out.println("Database connection is successful!");
        } catch (Exception e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }
}
