package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.Wallet;
import com.ticketseller.backend.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TransactionDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveTransaction(Transaction transaction) {

        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("wallet_id", transaction.getWalletId());
        params.put("transaction_amount", transaction.getTransactionAmount());
        params.put("transaction_type", transaction.getTransactionType().name());
        params.put("transaction_date", transaction.getTransactionDate());

        String sql = "INSERT INTO TRANSACTIONS (wallet_id, transaction_amount, transaction_type, transaction_date) "
                + "VALUES (:wallet_id, :transaction_amount, :transaction_type, :transaction_date)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<Transaction> getTransactionByTransactionId(Long transactionId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("transaction_id", transactionId);

        String sql =
            "SELECT * " +
             "FROM TRANSACTIONS t WHERE t.transaction_id = :transaction_id";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Transaction.builder()
                        .transactionId(rsw.getLong("transaction_id"))
                        .walletId(rsw.getLong("wallet_id"))
                        .transactionAmount(rsw.getLong("transaction_amount"))
                        .transactionType(TransactionType.getTransactionTypeFromStringValue(rsw.getString("transaction_type")))
                        .transactionDate(rsw.getLong("transaction_date"))
                        .build();
                }));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
