package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.entity.Wallet;
import com.ticketseller.backend.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class WalletDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveWallet(Wallet wallet) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("balance", wallet.getBalance());
        params.put("user_id", wallet.getUserId());
        params.put("wallet_id", wallet.getWalletId());

        String sql = "INSERT INTO WALLETS (balance, user_id, wallet_id) "
                + "VALUES (:balance, :user_id, :wallet_id)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<Wallet> getWalletByWalletId(Long walletId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("wallet_id", walletId);

        String sql =
            "SELECT * " +
             "FROM WALLETS w WHERE w.wallet_id = :wallet_id";
        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Wallet.builder()
                        .walletId(rsw.getLong("wallet_id"))
                        .balance(rsw.getLong("balance"))
                        .userId(rsw.getLong("user_id"))
                        .build();
                }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
