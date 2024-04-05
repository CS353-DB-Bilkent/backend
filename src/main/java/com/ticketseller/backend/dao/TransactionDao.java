package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TransactionDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveTransaction(Transaction transaction) {

        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("transaction_amount", transaction.getTransactionAmount());
        params.put("transaction_type", transaction.getTransactionType().name());
        params.put("transaction_date", transaction.getTransactionDate());
        params.put("user_id", transaction.getUserId());
        params.put("event_id", transaction.getEventId());

        String sql =
            "INSERT INTO TRANSACTION (TRANSACTION_AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, USER_ID, EVENT_ID) " +
            "VALUES (:transaction_amount, :transaction_type, :transaction_date, :user_id, :event_id)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<Transaction> getTransactionByTransactionId(Long transactionId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("transaction_id", transactionId);

        String sql =
            "SELECT * " +
             "FROM TRANSACTION t WHERE t.TRANSACTION_ID = :transaction_id";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Transaction.builder()
                    .transactionId(rsw.getLong("TRANSACTION_ID"))
                    .eventId(rsw.getLong("EVENT_ID"))
                    .userId(rsw.getLong("USER_ID"))
                    .transactionAmount(rsw.getLong("TRANSACTION_AMOUNT"))
                    .transactionType(TransactionType.getTransactionTypeFromStringValue(rsw.getString("TRANSACTION_TYPE")))
                    .transactionDate(rsw.getLocalDateTime("TRANSACTION_DATE"))
                    .build();
                }));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Transaction>> getTransactionsByUserId(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        String sql =
            "SELECT * " +
             "FROM TRANSACTION t WHERE t.USER_ID = :user_id";

        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Transaction.builder()
                        .transactionId(rsw.getLong("TRANSACTION_ID"))
                        .eventId(rsw.getLong("EVENT_ID"))
                        .userId(rsw.getLong("USER_ID"))
                        .transactionAmount(rsw.getLong("TRANSACTION_AMOUNT"))
                        .transactionType(TransactionType.getTransactionTypeFromStringValue(rsw.getString("TRANSACTION_TYPE")))
                        .transactionDate(rsw.getLocalDateTime("TRANSACTION_DATE"))
                        .build();
            }));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
