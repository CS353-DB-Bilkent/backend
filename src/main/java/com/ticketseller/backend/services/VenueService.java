package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.EventDao;
import com.ticketseller.backend.dao.TicketDao;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.dao.VenueDao;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Ticket;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.entity.Venue;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Slf4j

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueDao venueDao;

    public void saveVenue(String venueName, String venueAddress, String venueCity, Long venueCapacity) {
        Venue.builder()
                .venueName(venueName)
                .venueAddress(venueAddress)
                .venueCity(venueCity)
                .venueCapacity(venueCapacity)
                .build();
    }

    public Venue findVenueById(Long venueId) {
        return venueDao.findVenueById(venueId)
                .orElseThrow(() -> new EventRuntimeException("Venue not found", 1, HttpStatus.NOT_FOUND));
    }
}
