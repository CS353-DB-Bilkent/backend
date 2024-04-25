package com.ticketseller.backend.services;

import com.ticketseller.backend.core.CustomJdbcTemplate;
import com.ticketseller.backend.core.CustomSqlParameters;
import com.ticketseller.backend.dao.EventDao;
import com.ticketseller.backend.dao.TicketDao;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Ticket;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.TicketStatus;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketDao ticketDao;
    private final UserDao userDao;
    private final EventDao eventDao;

    private final CustomJdbcTemplate jdbcTemplate;

    public List<Ticket> getTicketsByUserId(Long userId, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        if(Objects.equals(user.getUserId(), userId)) {
            return ticketDao.getTicketsByUserId(userId)
                    .orElseThrow(() -> new EventRuntimeException("Tickets not found", 1, HttpStatus.NOT_FOUND));
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean buyTicket(Long userId, Long eventId, boolean buyerVisible) {
        User user = userDao.getUserByUserId(userId)
                .orElseThrow(() -> new EventRuntimeException("User not found", 1, HttpStatus.NOT_FOUND));

        Event event = eventDao.getEventByEventId(eventId)
                .orElseThrow(() -> new EventRuntimeException("Event not found", 1, HttpStatus.NOT_FOUND));

        if (event.getEventStatus().equals(EventStatus.CANCELLED)) {
            throw new EventRuntimeException("Event is cancelled", 2, HttpStatus.BAD_REQUEST);
        }
        else if(event.getNumberOfTickets() <= 0) {
            throw new EventRuntimeException("No tickets available", 2, HttpStatus.BAD_REQUEST);
        }
        else if (user.getBalance().compareTo(event.getTicketPrice()) < 0) {
            throw new EventRuntimeException("Insufficient balance", 3, HttpStatus.PAYMENT_REQUIRED);
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setTicketStatus(TicketStatus.RESERVED);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setPrice(event.getTicketPrice());
        //ticket.setQrCode(generateQrCode());
        ticket.setBuyerVisible(buyerVisible);
        ticketDao.saveTicket(ticket);

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", user.getUserId());
        params.put("price", ticket.getPrice());
        params.put("organizer_id", event.getOrganizerId());

        String sql = "UPDATE users SET balance = CASE " +
                "WHEN user_id = :user_id THEN balance - :price " +
                "WHEN user_id = :organizer_id THEN balance + :price " +
                "END " +
                "WHERE user_id IN (:user_id, :organizer_id)";
        jdbcTemplate.update(sql, params);

        params.put("decrement", 1);
        params.put("event_id", eventId);

        sql = "UPDATE event SET number_of_tickets = number_of_tickets - :decrement WHERE event_id = :event_id";
        jdbcTemplate.update(sql, params);

        log.info("Ticket purchased successfully for user {} and event {}", userId, eventId);

        return true;
    }

    public void refundTicket(Long ticketId, HttpServletRequest request){
        Ticket t = getTicketsByUserId(((User)request.getAttribute("user")).getUserId(), request).stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId))
                .findFirst()
                .orElseThrow(() -> new EventRuntimeException("Ticket not found", 1, HttpStatus.NOT_FOUND));;
        ticketDao.refundTicket(t);
    }
}