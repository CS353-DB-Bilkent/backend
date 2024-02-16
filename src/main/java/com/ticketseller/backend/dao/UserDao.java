package com.ticketseller.backend.dao;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveUser(User user) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("role", user.getRole().name());

        String sql =
            "INSERT INTO users (user_name, email, user_password, user_type) "
            + "VALUES (:user_name, :email, :user_password, :user_type)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<User> getUserByUserId(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        String sql =
            "SELECT u.user_id, u.name, u.email, u.password ,u.role " +
             "FROM USERS u WHERE u.user_id = :user_id";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                    return User.builder()
                    .userId(rsw.getLong("user_id"))
                    .email(rsw.getString("email"))
                    .name(rsw.getString("name"))
                    .password(rsw.getString("password"))
                    .role(Role.getRoleFromStringValue(rsw.getString("role")))
                    .build();
                }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmail(String email) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("email", email);

        String sql =
                "SELECT u.user_id, u.name, u.email, u.password, u.role " +
                        "FROM USERS u WHERE u.email = :email";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return User.builder()
                        .userId(rsw.getLong("user_id"))
                        .email(rsw.getString("email"))
                        .name(rsw.getString("name"))
                        .password(rsw.getString("password"))
                        .role(Role.getRoleFromStringValue(rsw.getString("role")))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Uncomment as needed
//    public Optional<User> getUserByEmailOrUsername(String emailOrUsername) {
//        CustomSqlParameters params = CustomSqlParameters.create();
//        params.put("emailOrUsername", emailOrUsername);
//
//        //@formatter:off
//        String sql =
//            "SELECT u.user_id, u.user_name, u.email, u.user_password, u.user_type " +
//            "FROM USERS u WHERE u.email = :emailOrUsername OR u.user_name = :emailOrUsername";
//        //@formatter:on
//
//        try {
//            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
//                ResultSetWrapper rsw = new ResultSetWrapper(rs);
//
//                //@formatter:off
//                return User.builder()
//                    .userId(rsw.getInteger("user_id"))
//                    .email(rsw.getString("email"))
//                    .userName(rsw.getString("user_name"))
//                    .userPassword(rsw.getString("user_password"))
//                    .userType(UserType.fromCode(rsw.getString("user_type")))
//                    .build();
//                //@formatter:on
//            }));
//        } catch (EmptyResultDataAccessException ex) {
//            return Optional.empty();
//        }
//    }
//
//    public void changeUserType(Integer userId, UserType userType) {
//        CustomSqlParameters params = CustomSqlParameters.create();
//        params.put("user_id", userId);
//        params.put("user_type", userType.getCode());
//
//        //@formatter:off
//        String sql =
//            "UPDATE USERS " +
//            "SET user_type = :user_type " +
//            "WHERE user_id = :user_id";
//        //@formatter:on
//
//        jdbcTemplate.update(sql, params);
//    }
}
