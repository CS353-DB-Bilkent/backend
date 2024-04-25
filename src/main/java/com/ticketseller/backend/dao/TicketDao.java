package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Ticket;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.enums.TicketStatus;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import com.ticketseller.backend.services.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TicketDao {
    private final CustomJdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void saveTicket(Ticket ticket) {
        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("EVENT_ID", ticket.getEventId());
        params.put("USER_ID", ticket.getUserId());
        params.put("PURCHASE_DATE", ticket.getPurchaseDate());
        params.put("PRICE", ticket.getPrice());
        params.put("TICKET_STATUS", ticket.getTicketStatus().toString());
        params.put("QR_CODE", ticket.getQrCode());
        params.put("BUYER_VISIBLE", ticket.isBuyerVisible());

        String sql = "INSERT INTO TICKET (EVENT_ID, USER_ID, PURCHASE_DATE, PRICE, TICKET_STATUS, QR_CODE, BUYER_VISIBLE) " +
                "VALUES (:EVENT_ID, :USER_ID, :PURCHASE_DATE, :PRICE, :TICKET_STATUS, :QR_CODE, :BUYER_VISIBLE)";

        jdbcTemplate.update(sql, params);

    }
    public Optional<List<Ticket>> getTicketsByUserId(Long userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("USER_ID", userId);

        String sql =
                "SELECT TICKET.*" +
                        " FROM TICKET" +
                        " JOIN USERS ON TICKET.USER_ID = USERS.USER_ID" +
                        " WHERE USERS.USER_ID = :USER_ID;";


        try {

            return Optional.of(jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return Ticket.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .ticketId(rsw.getLong("TICKET_ID"))
                        .userId(rsw.getLong("USER_ID"))
                        .purchaseDate(rsw.getLocalDateTime("PURCHASE_DATE"))
                        .price(rsw.getDouble("PRICE"))
                        .ticketStatus(TicketStatus.getTicketStatusFromStringValue(rsw.getString("TICKET_STATUS")))
                        .qrCode(rsw.getString("QR_CODE"))
                        .buyerVisible(Boolean.TRUE.equals(rsw.getBoolean("BUYER_VISIBLE")))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public void refundTicket(Ticket t) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("EVENT_ID", t.getEventId());
        params.put("USER_ID", t.getUserId());
        String updateUserBalanceSql = "UPDATE USERS " +
                "SET BALANCE = BALANCE + ( " +
                "    SELECT TICKET_PRICE FROM EVENT WHERE EVENT_ID = :EVENT_ID " +
                ") " +
                "WHERE USER_ID = :USER_ID;";
        jdbcTemplate.update(updateUserBalanceSql, params);
        params = CustomSqlParameters.create();
        params.put("EVENT_ID", t.getEventId());
        params.put("TICKET_ID", t.getTicketId());
        String updateTicketSql = "UPDATE TICKET " +
                "SET TICKET_STATUS = 'EMPTY' " +
                "WHERE TICKET_ID = :TICKET_ID AND EVENT_ID = :EVENT_ID;";
        jdbcTemplate.update(updateTicketSql, params);
        params = CustomSqlParameters.create();
        params.put("EVENT_ID", t.getEventId());
        String updateEventOrgBalanceSql = "UPDATE USERS " +
                "SET BALANCE = BALANCE - ( " +
                "    SELECT TICKET_PRICE FROM EVENT WHERE EVENT_ID = :EVENT_ID " +
                ") " +
                "WHERE USER_ID = ( " +
                "    SELECT USER_ID FROM EVENT WHERE EVENT_ID = :EVENT_ID " +
                ");";
        jdbcTemplate.update(updateEventOrgBalanceSql, params);
        params = CustomSqlParameters.create();
        params.put("TRANSACTION_AMOUNT", t.getPrice());
        params.put("TRANSACTION_DATE", LocalDate.now());
        params.put("USER_ID", t.getUserId());
        params.put("EVENT_ID", t.getEventId());
        // Insert transaction record for user's balance update
        String insertUserTransactionSql = "INSERT INTO TRANSACTION (TRANSACTION_AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, USER_ID, EVENT_ID) " +
                "VALUES (:TRANSACTION_AMOUNT, 'TICKET REFUND', :TRANSACTION_DATE, :USER_ID, :EVENT_ID);";
        jdbcTemplate.update(insertUserTransactionSql, params);
        params = CustomSqlParameters.create();
        params.put("TRANSACTION_AMOUNT", -1 * t.getPrice());
        params.put("TRANSACTION_DATE", LocalDate.now());
        params.put("USER_ID", t.getUserId());
        params.put("EVENT_ID", t.getEventId());
        // Insert transaction record for event organizer's balance update
        String insertOrganizerTransactionSql = "INSERT INTO TRANSACTION (TRANSACTION_AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, USER_ID, EVENT_ID) " +
                "VALUES (:TRANSACTION_AMOUNT, 'TICKET REFUND', :TRANSACTION_DATE, :USER_ID, :EVENT_ID);";
        jdbcTemplate.update(insertUserTransactionSql, params);
    }


    /*
    MAY GIVE ERROR

    public Optional<Ticket> getTicketsByTicketId(Long ticketId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("TICKET_ID", ticketId);

        String sql =
                "SELECT * " +
                        "FROM TICKET e INNER JOIN HOSTS h ON h.TICKET_ID = e.TICKET_ID WHERE e.TICKET_ID = :TICKET_ID";


        try {

            return Optional.of((Ticket) jdbcTemplate.query(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return Ticket.builder()
                        .eventId(rsw.getLong("EVENT_ID"))
                        .ticketId(rsw.getLong("TICKET_ID"))
                        .userId(rsw.getLong("USER_ID"))
                        .purchaseDate(rsw.getLocalDateTime("PURCHASE_DATE"))
                        .price(rsw.getDouble("PRICE"))
                        .ticketStatus(TicketStatus.getTicketStatusFromStringValue(rsw.getString("TICKET_STATUS")))
                        .qrCode(rsw.getString("QR_CODE"))
                        .buyerVisible(Boolean.TRUE.equals(rsw.getBoolean("BUYER_VISIBLE")))
                        .build();
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
     */
}
