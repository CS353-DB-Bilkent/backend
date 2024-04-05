package com.ticketseller.backend.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;

import lombok.RequiredArgsConstructor;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Repository
public class UserDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void insertUserIfNotExists(User user) {
        if (getUserByEmail(user.getEmail()).isPresent()) {
            return;
        }

        saveUser(user);
    }

    public void saveUser(User user) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("email", user.getEmail());
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("role", user.getRole().name());
        params.put("birth_date", user.getBirthDate());
        params.put("registered_date", user.getRegisteredDate());
        params.put("phone", user.getPhone());
        params.put("IBAN", user.getIBAN());
        params.put("company_name", user.getCompanyName());
        params.put("salary", user.getSalary());

        String sql =
            "INSERT INTO USERS (EMAIL, NAME, PASSWORD, ROLE, BIRTH_DATE, REGISTERED_DATE, PHONE, IBAN, COMPANY_NAME, SALARY) " +
            "VALUES (:email, :name, :password, :role, :birth_date, :registered_date, :phone, :IBAN, :company_name, :salary)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<User> getUserByUserId(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("USER_ID", userId);

        String sql =
            "SELECT * " +
             "FROM USERS u WHERE u.USER_ID = :USER_ID";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return User.builder()
                        .userId(rsw.getLong("USER_ID"))
                        .email(rsw.getString("EMAIL"))
                        .name(rsw.getString("NAME"))
                        .password(rsw.getString("PASSWORD"))
                        .role(Role.getRoleFromStringValue(rsw.getString("ROLE")))
                        .birthDate(rsw.getLocalDateTime("BIRTH_DATE"))
                        .registeredDate(rsw.getLocalDateTime("REGISTERED_DATE"))
                        .phone(rsw.getString("PHONE"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("COMPANY_NAME") ? null : rsw.getString("COMPANY_NAME"))
                        .salary(rsw.isNull("SALARY") ? null : rsw.getDouble("SALARY"))
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
                        "FROM USERS u WHERE u.EMAIL = :email";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return User.builder()
                        .userId(rsw.getLong("USER_ID"))
                        .email(rsw.getString("EMAIL"))
                        .name(rsw.getString("NAME"))
                        .password(rsw.getString("PASSWORD"))
                        .role(Role.getRoleFromStringValue(rsw.getString("ROLE")))
                        .birthDate(rsw.getLocalDateTime("BIRTH_DATE"))
                        .registeredDate(rsw.getLocalDateTime("REGISTERED_DATE"))
                        .phone(rsw.getString("PHONE"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("COMPANY_NAME") ? null : rsw.getString("COMPANY_NAME"))
                        .salary(rsw.isNull("SALARY") ? null : rsw.getDouble("SALARY"))
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
                        "FROM USERS u WHERE u.EMAIL = :email OR u.PHONE = :phone";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return User.builder()
                        .userId(rsw.getLong("USER_ID"))
                        .email(rsw.getString("EMAIL"))
                        .name(rsw.getString("NAME"))
                        .password(rsw.getString("PASSWORD"))
                        .role(Role.getRoleFromStringValue(rsw.getString("ROLE")))
                        .birthDate(rsw.getLocalDateTime("BIRTH_DATE"))
                        .registeredDate(rsw.getLocalDateTime("REGISTERED_DATE"))
                        .phone(rsw.getString("PHONE"))
                        .IBAN(rsw.isNull("IBAN") ? null : rsw.getString("IBAN"))
                        .companyName(rsw.isNull("COMPANY_NAME") ? null : rsw.getString("COMPANY_NAME"))
                        .salary(rsw.isNull("SALARY") ? null : rsw.getDouble("SALARY"))
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
