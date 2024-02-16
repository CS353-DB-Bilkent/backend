package com.ticketseller.backend.services;

import com.ticketseller.backend.constants.ErrorCodes;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final PasswordEncoder passwordEncoder;

    public void createAdmin(String bilkentId, String email, String name, String department) {
//        Optional<User> optionalUser = userRepository.findByBilkentIdOrEmail(bilkentId, email);
//
//        if (optionalUser.isPresent())
//            throw new AdminRuntimeException("Admin with bilkentId or Email already exists!", ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST);
//
//        User newUser = User.builder()
//                .bilkentId(bilkentId)
//                .name(name)
//                .email(email)
//                .role(Role.ADMIN)
//                .department(Department.valueOf(department))
//                .semesters(List.of())
//                .password(UserUtils.createRandomPassword())
//                .build();
//
//        mailService.sendWelcomeEmail(newUser.getBilkentId(), newUser.getName(), newUser.getEmail(), newUser.getPassword()); ;
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        userRepository.save(newUser);
    }
}
