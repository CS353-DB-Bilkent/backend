package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.*;
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
public class EventPersonDao {
    private final CustomJdbcTemplate jdbcTemplate;

    public void saveEventPerson(EventPerson eventPerson) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("NAME", eventPerson.getEventPersonName());
    }

    public Optional<EventPerson> findEventPersonById(Long eventPersonId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_PERSON_ID", eventPersonId);

        String sql = "SELECT * FROM EVENT_PERSON WHERE EVENT_PERSON_ID = :EVENT_PERSON_ID";

        try {
            return Optional.of((EventPerson) jdbcTemplate.query(sql, params, (rs, rnum) -> {
                    ResultSetWrapper rsw = new ResultSetWrapper(rs);

                    return EventPerson.builder()
                            .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
                            .eventPersonName(rsw.getString("EVENT_PERSON_NAME"))
                            .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
