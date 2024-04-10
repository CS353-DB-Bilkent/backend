package com.ticketseller.backend.services;

import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.exceptions.runtimeExceptions.UserRuntimeException;
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
public class UserService {

    private final UserDao userDao;

    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    public User getUserByBilkentId(String bilkentId) {
//        Optional<User> optionalUser = userDao.findByBilkentId(bilkentId);
//
//        if (optionalUser.isEmpty()) throw new UserRuntimeException("User not found!", ErrorCodes.NO_SUCH_USER, HttpStatus.NOT_FOUND);
//
//        return optionalUser.get();
        return null;
    }

    public List<User> getUsersByBilkentIds(List<String> bilkentIds) {
//        Optional<List<User>> optionalUserList = userDao.findAllByBilkentIds(bilkentIds);
//
//        if (optionalUserList.isEmpty())
//            throw new UserRuntimeException("Users could not be found!", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
//
//        return optionalUserList.get();
        return null;
    }

    public List<User> getUsersByEmails(List<String> emails) {
//        Optional<List<User>> optionalUserList = userDao.findAllByEmails(emails);
//
//        if (optionalUserList.isEmpty())
//            throw new UserRuntimeException("Users could not be found!", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
//
//        return optionalUserList.get();
        return null;
    }

    public User getUserById(Long userId) {
        Optional<User> optionalUser = userDao.getUserByUserId(userId);

        if (optionalUser.isEmpty()) throw new UserRuntimeException("User not found!", ErrorCodes.NO_SUCH_USER, HttpStatus.NOT_FOUND);

        return optionalUser.get();
    }
    public boolean updateUser(Long userId, String newName, String newEmail, String newPhone) {

        return userDao.updateUser(userId, newName, newEmail, newPhone);
    }
}
