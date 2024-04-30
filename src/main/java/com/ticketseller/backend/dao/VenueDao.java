package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
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

public class VenueDao {
    private final CustomJdbcTemplate jdbcTemplate;

    public void saveVenue(Venue venue) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("NAME", venue.getVenueName());
        params.put("CAPACITY", venue.getVenueCapacity());
        params.put("CITY", venue.getVenueCity());
        params.put("ADDRESS", venue.getVenueAddress());

        String sql = "INSERT INTO VENUE (NAME, CAPACITY, CITY, ADDRESS) " +
                "VALUES (:NAME, :CAPACITY, :CITY, :ADDRESS)";

        jdbcTemplate.update(sql, params);
    }

    public Venue findVenueById(Long venueId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("VENUE_ID", venueId);

        String sql = "SELECT * FROM VENUE WHERE VENUE_ID = :VENUE_ID";

        try {
            return jdbcTemplate.queryForObject(sql, params,(rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Venue.builder()
                        .venueId(rsw.getLong("VENUE_ID"))
                        .venueName(rsw.getString("NAME"))
                        .venueCapacity(rsw.getLong("CAPACITY"))
                        .venueCity(rsw.getString("CITY"))
                        .venueAddress(rsw.getString("ADDRESS"))
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Venue> getAllVenues() {
        CustomSqlParameters params = CustomSqlParameters.create();

        String sql = "SELECT * FROM VENUE";
        try {
            return jdbcTemplate.query(sql, params,(rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Venue.builder()
                        .venueId(rsw.getLong("VENUE_ID"))
                        .venueName(rsw.getString("NAME"))
                        .venueCapacity(rsw.getLong("CAPACITY"))
                        .venueCity(rsw.getString("CITY"))
                        .venueAddress(rsw.getString("ADDRESS"))
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}