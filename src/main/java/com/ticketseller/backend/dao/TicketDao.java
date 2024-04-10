package com.ticketseller.backend.dao;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.core.ResultSetWrapper;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Ticket;
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
public class TicketDao {
    private final CustomJdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
}