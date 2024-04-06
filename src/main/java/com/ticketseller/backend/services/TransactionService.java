package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.TransactionDao;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.TransactionType;
import com.ticketseller.backend.exceptions.runtimeExceptions.TransactionRuntimeException;
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
public class TransactionService {

    private final TransactionDao transactionDao;
    private final UserDao userDao;

    public List<Transaction> getTransactions(Long userId) {
        Optional<List<Transaction>> optionalTransactions = transactionDao.getTransactionsByUserId(userId);

        if (optionalTransactions.isEmpty()) {
            log.error("Transactions not found for user with id: {}", userId);
            throw new TransactionRuntimeException("Transactions not found", 1, HttpStatus.NOT_FOUND);
        }

        return optionalTransactions.get();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deposit(Long userId, Double transactionAmount) {
        if (transactionAmount <= 0) {
            log.error("Transaction amount must be greater than 0");
            throw new TransactionRuntimeException("Transaction amount must be greater than 0", 1, HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = Transaction.builder()
            .userId(userId)
            .transactionAmount(transactionAmount)
            .transactionType(TransactionType.DEPOSIT)
            .transactionDate(LocalDateTime.now())
            .build();

        transactionDao.saveTransaction(transaction);
        userDao.updateBalance(userId, transactionAmount);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void withdraw(Long userId, Double transactionAmount) {
        if (transactionAmount <= 0) {
            log.error("Transaction amount must be greater than 0");
            throw new TransactionRuntimeException("Transaction amount must be greater than 0", 1, HttpStatus.BAD_REQUEST);
        }

        User user = userDao.getUserByUserId(userId)
            .orElseThrow(() -> new TransactionRuntimeException("User not found", 1, HttpStatus.NOT_FOUND));

        if (user.getBalance() < transactionAmount) {
            log.error("Insufficient balance");
            throw new TransactionRuntimeException("Insufficient balance", 1, HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = Transaction.builder()
            .userId(userId)
            .transactionAmount(transactionAmount)
            .transactionType(TransactionType.WITHDRAWAL)
            .transactionDate(LocalDateTime.now())
            .build();

        transactionDao.saveTransaction(transaction);
        userDao.updateBalance(userId, -transactionAmount);
    }

}
