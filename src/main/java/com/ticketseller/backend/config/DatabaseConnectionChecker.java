package com.ticketseller.backend.config;

import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseConnectionChecker {

    @Value("${spring.datasource.url}")
    private String JDBC_URL;

    @Value("${spring.datasource.username}")
    private String JDBC_USER;

    @Value("${spring.datasource.password}")
    private String JDBC_PASSWORD;

    private final UserDao userDao;

    @Value("${spring.admin.email}")
    private String SUPER_ADMIN_EMAIL;

    @Value("${spring.admin.password}")
    private String SUPER_ADMIN_PASSWORD;

    private final PasswordEncoder passwordEncoder;

    public void checkDatabaseConnection() {
        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                System.out.println("Connected to the database!");
                connection.close(); // Close the connection
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        }

        // Insert super admin account if not exists
        User user = User.builder()
                .email(SUPER_ADMIN_EMAIL)
                .password(passwordEncoder.encode(SUPER_ADMIN_PASSWORD))
                .role(Role.SUPER_ADMIN)
                .name("Super Admin")
                .phone("123456789")
                .IBAN("TR123456789")
                .companyName("Super Admin Company")
                .birthDate(LocalDateTime.now())
                .registeredDate(LocalDateTime.now())
                .salary(0.0)
                .build();


        userDao.insertUserIfNotExists(user);

    }
}
