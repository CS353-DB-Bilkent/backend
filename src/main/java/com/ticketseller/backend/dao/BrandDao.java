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

        params.put("BRAND_NAME", brand.getBrandName());

        String sql = "INSERT INTO BRAND (BRAND_NAME) " +
                "VALUES (:BRAND_NAME)";

        jdbcTemplate.update(sql, params);
    }

    public Brand findBrandById(Long brandId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("BRAND_ID", brandId);

        String sql = "SELECT * FROM BRAND WHERE BRAND_ID = :BRAND_ID";

        try {
            return jdbcTemplate.queryForObject(sql, params,(rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Brand.builder()
                        .brandName(rsw.getString("BRAND_NAME"))
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Brand> getAllBrands() {
        CustomSqlParameters params = CustomSqlParameters.create();

        String sql = "SELECT * FROM BRAND";

        try {
            return jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Brand.builder()
                        .brandId(rsw.getLong("BRAND_ID"))
                        .brandName(rsw.getString("BRAND_NAME"))
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Brand findBrandByName(String brandName) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("BRAND_NAME", brandName);

        String sql = "SELECT * FROM BRAND WHERE BRAND_NAME = :BRAND_NAME";

        try {
            return jdbcTemplate.queryForObject(sql, params,(rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Brand.builder()
                        .brandName(rsw.getString("BRAND_NAME"))
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}