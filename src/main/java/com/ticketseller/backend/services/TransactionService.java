package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.TransactionDao;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.exceptions.runtimeExceptions.TransactionRuntimeException;
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
public class TransactionService {

    private final TransactionDao transactionDao;

    public List<Transaction> getTransactions(Long userId) {
        Optional<List<Transaction>> optionalTransactions = transactionDao.getTransactionsByUserId(userId);

        if (optionalTransactions.isEmpty()) {
            log.error("Transactions not found for user with id: {}", userId);
            throw new TransactionRuntimeException("Transactions not found", 1, HttpStatus.NOT_FOUND);
        }

        return optionalTransactions.get();
    }

}
