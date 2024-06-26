package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.*;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.services.BrandService;
import com.ticketseller.backend.services.EventPersonService;
import com.ticketseller.backend.services.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Repository
public class EventDao {

    private final CustomJdbcTemplate jdbcTemplate;
    private final VenueService venueService;
    private final BrandService brandService;
    private final EventPersonService eventPersonService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void saveEvent(Event event) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("NAME", event.getName());
        params.put("DETAILS", event.getDetails());
        params.put("START_DATE", event.getStartDate());
        params.put("END_DATE", event.getEndDate());
        params.put("TICKET_PRICE", event.getTicketPrice());
        params.put("NUMBER_OF_TICKETS", event.getNumberOfTickets());
        params.put("MIN_AGE_ALLOWED", event.getMinAgeAllowed());
        params.put("EVENT_TYPE", event.getEventType().name());
        params.put("EVENT_STATUS", event.getEventStatus().name());
        params.put("VENUE_ID", event.getVenueId());
        params.put("BRAND_ID", event.getBrandId());
        params.put("EVENT_PERSON_ID", event.getEventPersonId());
        params.put("ORGANIZER_ID", event.getOrganizerId());

        String sql = "INSERT INTO EVENT (NAME, DETAILS, START_DATE, END_DATE, TICKET_PRICE, NUMBER_OF_TICKETS, MIN_AGE_ALLOWED, EVENT_TYPE, EVENT_STATUS, VENUE_ID, BRAND_ID, EVENT_PERSON_ID, ORGANIZER_ID) " +
                "VALUES (:NAME, :DETAILS, :START_DATE, :END_DATE, :TICKET_PRICE, :NUMBER_OF_TICKETS, :MIN_AGE_ALLOWED, :EVENT_TYPE, :EVENT_STATUS, :VENUE_ID, :BRAND_ID, :EVENT_PERSON_ID, :ORGANIZER_ID)";

        jdbcTemplate.update(sql, params);

        String maxIdSql = "SELECT MAX(EVENT_ID) as MAX_EVENT_ID FROM EVENT";

        // Execute maxIdSql
        Long eventId = jdbcTemplate.queryForObject(maxIdSql, CustomSqlParameters.create(), (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getLong("MAX_EVENT_ID");
        });

        // Insert into hosts
        String insertHostsSql = "INSERT INTO HOSTS (EVENT_ID, BRAND_ID, EVENT_PERSON_ID) " +
                "VALUES (:EVENT_ID, :BRAND_ID, :EVENT_PERSON_ID)";

        System.out.println(eventId);

        CustomSqlParameters hostsParams = CustomSqlParameters.create();
        hostsParams.put("EVENT_ID", eventId);
        hostsParams.put("BRAND_ID", event.getBrandId());
        hostsParams.put("EVENT_PERSON_ID", event.getEventPersonId());

        jdbcTemplate.update(insertHostsSql, hostsParams);
    }

    public Optional<List<Event>> getFilteredEvents(String searchTerm, String artistName, String brandName, String venueName, String location, String type, Integer minAgeAllowed, LocalDateTime startDate, String orderBy, String orderDirection) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("search_term", isNull(searchTerm) ? "" : searchTerm);
        params.put("artist_name", isNull(artistName) ? "" : artistName);
        params.put("brand_name", isNull(brandName) ? "" : brandName);
        params.put("venue_name", isNull(venueName) ? "" : venueName);
        params.put("location", isNull(location) ? "" : location);
        params.put("type", isNull(type) ? "" : type);
        // params.put("start_date", isNull(startDate) ? null : Timestamp.valueOf(startDate));
        params.put("min_age_allowed", isNull(minAgeAllowed) ? 0 : minAgeAllowed);

        String sql = "SELECT DISTINCT e.* " +
                "FROM EVENT e " +
                "INNER JOIN HOSTS h ON e.EVENT_ID = h.EVENT_ID " +
                "LEFT JOIN EVENT_PERSON ep ON h.EVENT_PERSON_ID = ep.EVENT_PERSON_ID " +
                "LEFT JOIN BRAND b ON h.BRAND_ID = b.BRAND_ID " +
                "LEFT JOIN VENUE ve ON e.VENUE_ID = ve.VENUE_ID " +
                "WHERE " +
                "(LOWER(e.NAME) LIKE LOWER(CONCAT('%', :search_term, '%')) OR :search_term IS NULL) " +
                "AND (LOWER(ep.EVENT_PERSON_NAME) LIKE LOWER(CONCAT('%', :artist_name, '%')) OR :artist_name IS NULL) " +
                "AND (LOWER(b.BRAND_NAME) LIKE LOWER(CONCAT('%', :brand_name, '%')) OR :brand_name IS NULL) " +
                "AND (LOWER(ve.NAME) LIKE LOWER(CONCAT('%', :venue_name, '%')) OR :venue_name IS NULL) " +
                "AND (LOWER(ve.ADDRESS) LIKE LOWER(CONCAT('%', :location, '%')) OR :location IS NULL) " +
                "AND (LOWER(e.EVENT_TYPE) LIKE LOWER(CONCAT('%', :type, '%')) OR :type IS NULL) " +
                // "AND (e.START_DATE > :start_date OR :start_date IS NULL) " +
                "AND (e.MIN_AGE_ALLOWED >= :min_age_allowed OR :min_age_allowed IS NULL) " +
                "AND e.EVENT_STATUS = 'ACTIVE' " +
                "ORDER BY e." + orderBy + " " + orderDirection + " " +
                "LIMIT 20";

        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Event> getEventByEventId(Long eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);

        String sql =
                "SELECT * " +
                        "FROM EVENT e INNER JOIN HOSTS h ON h.EVENT_ID = e.EVENT_ID WHERE e.EVENT_ID = :EVENT_ID";

        try {

            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Event>> getEventsByOrganizerId(Long organizerId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("ORGANIZER_ID", organizerId);

        String sql =
                "SELECT * " +
                        "FROM EVENT e INNER JOIN HOSTS h ON h.EVENT_ID = e.EVENT_ID WHERE e.ORGANIZER_ID = :ORGANIZER_ID";

        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Event>> getAllEvents() {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_STATUS", EventStatus.ACTIVE.name());

        String sql =
                "SELECT * " +
                        "FROM EVENT e INNER JOIN HOSTS h ON h.EVENT_ID = e.EVENT_ID WHERE e.EVENT_STATUS = :EVENT_STATUS LIMIT 20";

        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean approveEvent(Long eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);
        String sql = "UPDATE EVENT SET EVENT_STATUS = 'ACTIVE' WHERE EVENT_ID = :EVENT_ID";
        int rowsAffected = jdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public boolean rejectEvent(Long eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);
        String sql = "UPDATE EVENT SET EVENT_STATUS = 'REJECTED' WHERE EVENT_ID = :EVENT_ID";
        int rowsAffected = jdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public Optional<List<Event>> getMyEvents(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("USER_ID", userId);
        String sql = "SELECT * FROM EVENT e INNER JOIN HOSTS h ON e.EVENT_ID = h.EVENT_ID WHERE e.ORGANIZER_ID = :USER_ID";
        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public Optional<List<Report>> createReport(Long eventId, Long organizerId){
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("EVENT_ID", eventId);

        String sqlInsert = "SELECT *" +
                " FROM event_report" +
                " WHERE event_id = :EVENT_ID\n;";
        //jdbcTemplate.update(sqlInsert, params);
        try {

            return Optional.of(jdbcTemplate.query(sqlInsert, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Report.builder()
                        .reportDate(rsw.getLocalDateTime("REPORT_DATE"))
                        .totalRevenue(rsw.getDouble("TOTAL_REVENUE"))
                        .totalSales(rsw.getInteger("TOTAL_SALES"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public boolean cancelEvent(Long eventId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);
        String sql = "UPDATE EVENT " +
                "SET EVENT_STATUS='CANCELLED' " +  // Use single quotes for string literals
                "WHERE EVENT_ID = :EVENT_ID";
        int rowsAffected = jdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public void updateNumberOfTickets(Long eventId, int i) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);
        params.put("NUMBER_OF_TICKETS", i);
        String sql = "UPDATE EVENT SET NUMBER_OF_TICKETS = :NUMBER_OF_TICKETS WHERE EVENT_ID = :EVENT_ID";
        jdbcTemplate.update(sql, params);
    }

    public Optional<List<Event>> getUnApprovedEvents(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        String sql = "SELECT * FROM EVENT e INNER JOIN HOSTS h ON e.EVENT_ID = h.EVENT_ID WHERE e.EVENT_STATUS = 'WAITING_APPROVAL'";
        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                return Event.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .name(rsw.getString("NAME"))
                        .details(rsw.getString("DETAILS"))
                        .startDate(rsw.getLocalDateTime("START_DATE"))
                        .endDate(rsw.getLocalDateTime("END_DATE"))
                        .ticketPrice(rsw.getDouble("TICKET_PRICE"))
                        .numberOfTickets(rsw.getInteger("NUMBER_OF_TICKETS"))
                        .eventType(EventType.getEventTypeFromStringValue(rsw.getString("EVENT_TYPE")))
                        .eventStatus(EventStatus.getEventStatusFromStringValue(rsw.getString("EVENT_STATUS")))
                        .organizerId(rsw.getLong("ORGANIZER_ID"))
                        .minAgeAllowed(rsw.getInteger("MIN_AGE_ALLOWED"))
                        .venueId(rsw.getLong("VENUE_ID"))
                        .brandId(rsw.isNull("BRAND_ID") ? null : rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.isNull("EVENT_PERSON_ID") ? null : rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> getEventAttendeesByEventId(Long eventId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);

        String sql = "SELECT * " +
                "FROM TICKET t INNER JOIN USERS u ON t.USER_ID = u.USER_ID WHERE t.EVENT_ID = :EVENT_ID AND t.BUYER_VISIBLE = TRUE";

        try {

                return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                    ResultSetWrapper rsw = new ResultSetWrapper(rs);

                    return User.builder()
                            .userId(rsw.getLong("USER_ID"))
                            .email(rsw.getString("EMAIL"))
                            .name(rsw.getString("NAME"))
                            .phone(rsw.getString("PHONE"))
                            .password(rsw.getString("PASSWORD"))
                            .registeredDate(rsw.getLocalDateTime("REGISTERED_DATE"))
                            .birthDate(rsw.getLocalDateTime("BIRTH_DATE"))
                            .balance(rsw.getDouble("BALANCE"))
                            .build();
                }));
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
        }
    }
}