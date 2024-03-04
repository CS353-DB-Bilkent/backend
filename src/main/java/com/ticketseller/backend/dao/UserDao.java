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
        params.put("phone", user.getPhone());
        params.put("registered_date", user.getRegisteredDate());
        params.put("birth_date", user.getBirthDate());
        params.put("IBAN", user.getIBAN());
        params.put("company_name", user.getCompanyName());

        String sql = "INSERT INTO USERS (name, email, password, role, phone, registered_date, birth_date, IBAN, company_name) "
                + "VALUES (:name, :email, :password, :role, :phone, :registered_date, :birth_date, :IBAN, :company_name)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<User> getUserByUserId(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        String sql =
            "SELECT * " +
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
                        .birthDate(rsw.getLocalDateTime("birth_date"))
                        .registeredDate(rsw.getLocalDateTime("registered_date"))
                        .phone(rsw.getString("phone"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("company_name") ? null : rsw.getString("company_name"))
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
                "SELECT * " +
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
                        .birthDate(rsw.getLocalDateTime("birth_date"))
                        .registeredDate(rsw.getLocalDateTime("registered_date"))
                        .phone(rsw.getString("phone"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("company_name") ? null : rsw.getString("company_name"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmailOrPhone(String email, String phone) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("email", email);
        params.put("phone", phone);

        String sql =
                "SELECT * " +
                        "FROM USERS u WHERE u.email = :email OR u.phone = :phone";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return User.builder()
                        .userId(rsw.getLong("user_id"))
                        .email(rsw.getString("email"))
                        .name(rsw.getString("name"))
                        .password(rsw.getString("password"))
                        .role(Role.getRoleFromStringValue(rsw.getString("role")))
                        .birthDate(rsw.getLocalDateTime("birth_date"))
                        .registeredDate(rsw.getLocalDateTime("registered_date"))
                        .phone(rsw.getString("phone"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("company_name") ? null : rsw.getString("company_name"))
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
