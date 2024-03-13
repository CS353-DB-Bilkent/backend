package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.TransactionDao;
import com.ticketseller.backend.dao.WalletDao;
import com.ticketseller.backend.dto.WalletDto;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.Wallet;
import com.ticketseller.backend.enums.TransactionType;
import com.ticketseller.backend.exceptions.runtimeExceptions.WalletRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletDao walletDao;
    private final TransactionDao transactionDao;

    public WalletDto getWalletMe(Long userId) {
        Optional<WalletDto> wallet = walletDao.getWalletByUserId(userId).map(Wallet::toWalletDto);

        if (wallet.isEmpty()) {
            log.error("Wallet not found for user with id: {}", userId);
            throw new WalletRuntimeException("Wallet not found", 1, HttpStatus.NOT_FOUND);
        }

        return wallet.get();
    }

    public List<Transaction> getTransactions(Long userId) {
        Optional<WalletDto> wallet = walletDao.getWalletByUserId(userId).map(Wallet::toWalletDto);

        if (wallet.isEmpty()) {
            log.error("Wallet not found for user with id: {}", userId);
            throw new WalletRuntimeException("Wallet not found", 1, HttpStatus.NOT_FOUND);
        }

        Optional<List<Transaction>> transactions = transactionDao.getTransactionsByWalletId(wallet.get().getWalletId());

        if (transactions.isEmpty()) {
            log.error("Transactions not found for wallet with id: {}", wallet.get().getWalletId());
            throw new WalletRuntimeException("Transactions not found", 1, HttpStatus.NOT_FOUND);
        }

        return transactions.get();
    }

    public void saveTransaction(Long userId, Long transactionAmount, String transactionType) {
        Wallet wallet = walletDao.getWalletByUserId(userId)
                .orElseThrow(() -> new WalletRuntimeException("Wallet not found", 1, HttpStatus.NOT_FOUND));

        TransactionType transactionTypeEnum = TransactionType.getTransactionTypeFromStringValue(transactionType);

        if (transactionTypeEnum == TransactionType.UNRECOGNIZED) {
            log.error("Transaction type not found: {}", transactionType);
            throw new WalletRuntimeException("Transaction type not found", 1, HttpStatus.NOT_FOUND);
        }

        Transaction transaction = Transaction.builder()
                .walletId(wallet.getWalletId())
                .transactionAmount(transactionAmount)
                .transactionType(transactionTypeEnum)
                // Give me now localdatetime
                .transactionDate(LocalDateTime.now())
                .build();

        walletDao.updateBalance(wallet, transactionAmount);
        transactionDao.saveTransaction(transaction);
    }

    public void saveNewWalletForUser(Long userId) {
        Wallet wallet = Wallet.builder()
                .balance(0L)
                .userId(userId)
                .build();

        walletDao.saveWallet(wallet);
    }
}
