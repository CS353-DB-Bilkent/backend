package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.EventDao;
import com.ticketseller.backend.dao.TicketDao;
import com.ticketseller.backend.entity.Ticket;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketDao ticketDao;

    public List<Ticket> getTicketsByUserId(Long userId){
        return ticketDao.getTicketsByUserId(userId)
                .orElseThrow(() -> new EventRuntimeException("Tickets not found", 1, HttpStatus.NOT_FOUND));
    }
}
