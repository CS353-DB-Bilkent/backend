package com.ticketseller.backend.services;


import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
@Repository
public class EventService {
    private final CustomJdbcTemplate jdbcTemplate;
    public void createEvent(EventDTO eventDTO){
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("eventID", String.valueOf(eventDTO.getEventID()));
        params.put("name", eventDTO.getName());
        params.put("start_date", eventDTO.getStart_date());
        params.put("end_date", eventDTO.getEnd_date());
        params.put("details", eventDTO.getDetails());
        params.put("min_age_allowed", eventDTO.getMin_age_allowed());
        params.put("status", eventDTO.getStatus());
        params.put("event_person_id", eventDTO.getEvent_person_id());
        params.put("event_type_id", eventDTO.getEvent_type_id());
        params.put("brand_id", eventDTO.getBrand_id());
        params.put("venue_id", eventDTO.getBrand_id());

        String sql = "INSERT INTO EVENTS (eventID, name, start_date, end_date, min_age_allowed, status, event_person_id, event_type_id, brand_id, venue_id) "
                + "VALUES (:eventID, :name, :start_date, :end_date, :min_age_allowed, :status, :event_person_id, :event_type_id, :brand_id, :venue_id)";

        jdbcTemplate.update(sql, params);
    }
    public void updateEvent(EventDTO eventDTO){
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("eventID", String.valueOf(eventDTO.getEventID()));
        params.put("name", eventDTO.getName());
        params.put("start_date", eventDTO.getStart_date());
        params.put("end_date", eventDTO.getEnd_date());
        params.put("details", eventDTO.getDetails());
        params.put("min_age_allowed", eventDTO.getMin_age_allowed());
        params.put("status", eventDTO.getStatus());
        params.put("event_person_id", eventDTO.getEvent_person_id());
        params.put("event_type_id", eventDTO.getEvent_type_id());
        params.put("brand_id", eventDTO.getBrand_id());
        params.put("venue_id", eventDTO.getBrand_id());


        String sql = "UPDATE EVENTS SET " +
                "name = :name, " +
                "start_date = :start_date, " +
                "end_date = :end_date, " +
                "details = :details, " +
                "min_age_allowed = :min_age_allowed, " +
                "status = :status, " +
                "event_person_id = :event_person_id, " +
                "event_type_id = :event_type_id, " +
                "brand_id = :brand_id, " +
                "venue_id = :venue_id " +
                "WHERE eventID = :eventID";

        jdbcTemplate.update(sql, params);
    }
    public void deleteEvent(String eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("eventId", eventId);

        String sql = "DELETE FROM EVENTS WHERE eventID = :eventId";

        jdbcTemplate.update(sql, params);
    }
}
