package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Repository
public class ReviewDao {
    private final CustomJdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public void addReview(Review review) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("RATING", review.getRating());
        params.put("DESCRIPTION", review.getDescription());
        params.put("REVIEW_DATE", review.getReviewDate());

        String sql = "INSERT INTO REVIEW (RATING, DESCRIPTION, REVIEW_DATE) " +
                "VALUES (:RATING, :DESCRIPTION, :REVIEW_DATE)";

        jdbcTemplate.update(sql, params);
    }

}
