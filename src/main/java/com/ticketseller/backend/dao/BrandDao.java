package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Brand;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Ticket;
import com.ticketseller.backend.entity.Venue;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository

public class BrandDao {
    private final CustomJdbcTemplate jdbcTemplate;

    public void saveBrand(Brand brand) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("NAME", brand.getBrandName());

        String sql = "INSERT INTO BRAND (NAME) " +
                "VALUES (:NAME)";

        jdbcTemplate.update(sql, params);

    }

    public Optional<Brand> findBrandById(Long brandId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("BRAND_ID", brandId);

        String sql = "SELECT * FROM BRAND WHERE BRAND_ID = :BRAND_ID";

        try {
            return Optional.of((Brand) jdbcTemplate.query(sql, params, (rs, rnum) -> {
                    ResultSetWrapper rsw = new ResultSetWrapper(rs);

                    return Brand.builder()
                            .brandId(rsw.getLong("BRAND_ID"))
                            .brandName(rsw.getString("NAME"))
                            .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
