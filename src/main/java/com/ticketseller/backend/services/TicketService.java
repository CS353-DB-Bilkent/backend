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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

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
    public boolean buyTicket(Long userId, Long eventId, boolean buyerVisible, HttpServletRequest request) {
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

        Ticket ticket = Ticket.builder()
                .userId(userId)
                .eventId(eventId)
                .ticketStatus(TicketStatus.RESERVED)
                .purchaseDate(LocalDateTime.now())
                .price(event.getTicketPrice())
                .buyerVisible(buyerVisible)
                .qrCode(generateQrCode("Ticket for event"))
                .build();

        ticketDao.saveTicket(ticket);

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", user.getUserId());
        params.put("price", ticket.getPrice());
        params.put("organizer_id", event.getOrganizerId());

        String sql = "UPDATE users SET balance = CASE " +
                "WHEN USER_ID = :user_id THEN BALANCE - :price " +
                "WHEN USER_ID = :organizer_id THEN BALANCE + :price " +
                "END " +
                "WHERE USER_ID IN (:user_id, :organizer_id)";

        jdbcTemplate.update(sql, params);

        params = CustomSqlParameters.create();
        params.put("decrement", 1);
        params.put("event_id", eventId);

        sql = "UPDATE event SET number_of_tickets = number_of_tickets - :decrement WHERE event_id = :event_id";
        jdbcTemplate.update(sql, params);

        log.info("Ticket purchased successfully for user {} and event {}", userId, eventId);

        return true;
    }

    private String generateQrCode(String data) {
        int width = 200;
        int height = 200;
        StringBuilder qrCode = new StringBuilder();

        // Generate QR code pattern based on data
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i + j) % 2 == 0) {
                    qrCode.append("#"); // Black module
                } else {
                    qrCode.append(" "); // White module
                }
            }
            qrCode.append("\n");
        }

        return qrCode.toString();
    }


    public void refundTicket(Long ticketId, HttpServletRequest request){
        Ticket t = getTicketsByUserId(((User)request.getAttribute("user")).getUserId(), request).stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId))
                .findFirst()
                .orElseThrow(() -> new EventRuntimeException("Ticket not found", 1, HttpStatus.NOT_FOUND));;
        ticketDao.refundTicket(t);
    }
}