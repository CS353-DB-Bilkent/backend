package com.ticketseller.backend.services;

import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.exceptions.runtimeExceptions.AdminRuntimeException;
import com.ticketseller.backend.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public void createAdmin(String email, String password, Role role, String name, String phone, String IBAN, String companyName, LocalDateTime birthDate, Double salary) {
        Optional<User> optionalUser = userDao.getUserByEmailOrPhone(email, phone);

        if (optionalUser.isPresent())
            throw new AdminRuntimeException("User is already registered!", 1, HttpStatus.BAD_REQUEST);

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .name(name)
                .phone(phone)
                .IBAN(IBAN)
                .companyName(companyName)
                .registeredDate(LocalDateTime.now())
                .birthDate(birthDate)
                .salary(salary)
                .build();

        userDao.saveUser(user);
    }
}
