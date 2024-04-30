package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Brand;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Report;
import com.ticketseller.backend.entity.EventPerson;
import com.ticketseller.backend.entity.Venue;
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public Optional<List<Report>> createReport(Long eventId, Long organizerId){
        CustomSqlParameters params = CustomSqlParameters.create();
        CustomSqlParameters params2 = CustomSqlParameters.create();

        params2.put("EVENT_ID", eventId);
        params.put("EVENT_ID", eventId);
        params.put("ORGANIZER_ID", organizerId);
        // Insert or update the report
        String sqlUpdate = "UPDATE REPORT " +
                "SET " +
                "  REPORT_DATE = CURRENT_TIMESTAMP, " +
                "  TOTAL_SALES = (SELECT COUNT(*) FROM TICKET WHERE EVENT_ID = :EVENT_ID AND TICKET_STATUS = 'RESERVED'), " +
                "  TOTAL_REVENUE = (SELECT SUM(PRICE) FROM TICKET WHERE EVENT_ID = :EVENT_ID AND TICKET_STATUS = 'RESERVED') " +
                "WHERE EVENT_ID = :EVENT_ID";

        String sqlInsert = "INSERT INTO REPORT (REPORT_DATE, TOTAL_SALES, TOTAL_REVENUE, ORGANIZER_ID, EVENT_ID) " +
                "SELECT " +
                "  CURRENT_TIMESTAMP AS REPORT_DATE, " +
                "  COUNT(*) AS TOTAL_SALES, " +
                "  SUM(PRICE) AS TOTAL_REVENUE, " +
                "  :ORGANIZER_ID AS ORGANIZER_ID, " +
                "  :EVENT_ID AS EVENT_ID " +
                "FROM " +
                "  TICKET " +
                "WHERE " +
                "  EVENT_ID = :EVENT_ID AND " +
                "  TICKET_STATUS = 'RESERVED' " +
                "GROUP BY " +
                "  EVENT_ID";

        try {
            int rowsUpdated = jdbcTemplate.update(sqlUpdate, params2);
            if (rowsUpdated == 0) {
                // No rows were updated, insert a new report
                jdbcTemplate.update(sqlInsert, params);
            }
        } catch (DataAccessException e) {
            // Update failed, insert a new report
            jdbcTemplate.update(sqlInsert, params2);
        }
        // Select the inserted or updated values
        String selectSql = "SELECT REPORT_DATE, TOTAL_SALES, TOTAL_REVENUE " +
                "FROM REPORT " +
                "WHERE EVENT_ID = :EVENT_ID;";
        try {

            return Optional.of(jdbcTemplate.query(selectSql, params2, (rs, rnum) -> {
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
    public boolean cancelEvent(Long eventId){
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", eventId);
        String sql = "UPDATE EVENT " +
                "SET EVENT_STATUS='CANCELED' " +  // Use single quotes for string literals
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
        String sql = "SELECT * FROM EVENT e INNER JOIN HOSTS h ON e.EVENT_ID = h.EVENT_ID WHERE e.EVENT_STATUS = 'UNAPPROVED'";
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
                        .brandId(rsw.getLong("BRAND_ID"))
                        .eventPersonId(rsw.getLong("EVENT_PERSON_ID"))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}