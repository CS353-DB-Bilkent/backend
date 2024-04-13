package com.ticketseller.backend.services;

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

    public List<Ticket> getTicketsByUserId(Long userId, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        if(Objects.equals(user.getUserId(), userId)) {
            return ticketDao.getTicketsByUserId(userId)
                    .orElseThrow(() -> new EventRuntimeException("Tickets not found", 1, HttpStatus.NOT_FOUND));
        }
        return Collections.emptyList();
    }

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

        userDao.updateBalance(userId, user.getBalance() - event.getTicketPrice());
        userDao.saveUser(user);

        eventDao.updateNumberOfTickets(eventId, event.getNumberOfTickets() - 1);

        User organizer = userDao.getUserByUserId(event.getOrganizerId())
                .orElseThrow(() -> new EventRuntimeException("Organizer not found", 1, HttpStatus.NOT_FOUND));
        userDao.updateBalance(organizer.getUserId(), organizer.getBalance() + event.getTicketPrice());
        userDao.saveUser(organizer);


        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setTicketStatus(TicketStatus.RESERVED);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setPrice(event.getTicketPrice());
        //ticket.setQrCode(generateQrCode());
        ticket.setBuyerVisible(buyerVisible);
        ticketDao.saveTicket(ticket);

        log.info("Ticket purchased successfully for user {} and event {}", userId, eventId);

        return true;
    }

}