package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Review;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        params.put("EVENT_ID", review.getEventId());
        params.put("USER_ID", review.getUserId());
        params.put("USER_INITIALS", review.getUserInitials());

        String sql = "INSERT INTO REVIEW (RATING, DESCRIPTION, REVIEW_DATE, EVENT_ID, USER_ID, USER_INITIALS) VALUES (:RATING, :DESCRIPTION, :REVIEW_DATE, :EVENT_ID, :USER_ID, :USER_INITIALS)";

        jdbcTemplate.update(sql, params);
    }

    public Optional<List<Review>> getReviewsByEventId(Long eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);

        String sql = "SELECT * FROM REVIEW WHERE EVENT_ID = :EVENT_ID";

        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Review.builder()
                        .rating(rsw.getInteger("RATING"))
                        .description(rsw.getString("DESCRIPTION"))
                        .reviewDate(rsw.getLocalDateTime("REVIEW_DATE"))
                        .eventId(rsw.getLong("EVENT_ID"))
                        .userId(rsw.getLong("USER_ID"))
                        .userInitials(rsw.getString("USER_INITIALS"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }



}
