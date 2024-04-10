package com.ticketseller.backend.services;

import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.dto.response.auth.LoginResponse;
import com.ticketseller.backend.dto.response.auth.RegisterResponse;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.exceptions.runtimeExceptions.AuthRuntimeException;
import com.ticketseller.backend.exceptions.runtimeExceptions.UserRuntimeException;
import com.ticketseller.backend.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(String email, String password) {
        Optional<User> optionalUser = userDao.getUserByEmail(email);

        if (optionalUser.isEmpty())
            throw new UserRuntimeException("User not found", ErrorCodes.NO_SUCH_USER, HttpStatus.NOT_FOUND);

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new AuthRuntimeException("Bad credentials", ErrorCodes.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);

        String accessToken = jwtTokenUtil.generateAccessToken(user);

        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .build();
    }

    public RegisterResponse register(String email, String password, Role role, String name, String phone, String IBAN, String companyName, LocalDateTime birthDate, Double salary) {
        Optional<User> optionalUser = userDao.getUserByEmailOrPhone(email, phone);

        if (optionalUser.isPresent())
            throw new UserRuntimeException("User is already registered!", 1, HttpStatus.BAD_REQUEST);

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .name(name)
                .phone(phone)
                .IBAN(IBAN)
                .companyName(companyName)
                .birthDate(birthDate)
                .registeredDate(LocalDateTime.now())
                .salary(salary)
                .balance(0.0)
                .build();

        userDao.saveUser(user);

        Optional<User> insertedUser = userDao.getUserByEmail(email);

        if (insertedUser.isEmpty())
            throw new UserRuntimeException("User could not have been created", ErrorCodes.NO_SUCH_USER, HttpStatus.NOT_FOUND);

        String accessToken = jwtTokenUtil.generateAccessToken(user);

        return RegisterResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public void logout(String accessToken) {
        // empty... used to hold redis blacklisting
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new UserRuntimeException("Bad credentials", ErrorCodes.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN);

        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new UserRuntimeException("New password cannot be same as the old password!", ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST);

        userDao.updatePassword(user.getUserId(), passwordEncoder.encode(newPassword));
    }
    public Long getCurrentUserId(){
        return jwtTokenUtil.getCurrentUserId();
    }
}
