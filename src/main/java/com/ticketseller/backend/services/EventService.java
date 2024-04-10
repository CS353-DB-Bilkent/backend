package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.EventDao;
import com.ticketseller.backend.dao.ReviewDao;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.Review;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventDao eventDao;
    private final ReviewDao reviewDao;
    public Event saveEvent(String eventName, String eventDetails, LocalDateTime startDate, LocalDateTime endDate, Double ticketPrice, Integer numberOfTickets, String eventType, Integer minAgeAllowed, Long organizerId) {
        EventType eventTypeEnum = EventType.getEventTypeFromStringValue(eventType);

        if (eventTypeEnum == EventType.UNRECOGNIZED) {
            log.error("Invalid event type or event status");
            throw new EventRuntimeException("Invalid event type or event status", 1, HttpStatus.BAD_REQUEST);
        }

        if (startDate.isAfter(endDate)) {
            log.error("Start date is after end date");
            throw new EventRuntimeException("Start date is after end date", 1, HttpStatus.BAD_REQUEST);
        }

        if (ticketPrice < 0) {
            log.error("Ticket price is negative");
            throw new EventRuntimeException("Ticket price is negative", 1, HttpStatus.BAD_REQUEST);
        }

        if (numberOfTickets < 0) {
            log.error("Number of tickets is negative");
            throw new EventRuntimeException("Number of tickets is negative", 1, HttpStatus.BAD_REQUEST);
        }

        if (minAgeAllowed < 0) {
            log.error("Minimum age allowed is negative");
            throw new EventRuntimeException("Minimum age allowed is negative", 1, HttpStatus.BAD_REQUEST);
        }

        // First check if venue exists using venueService
        // If not, throw error which will be caught in frontend. That'll trigger venue creation page.
        // Create the venue, then send the query again to this function. Now the event will be created.
        // Also, do the same for artist and brand. However, no need to throw error for those.
        // Just create the artist and brand if they don't exist.
        // Lastly, do not forget to insert into hosts relation.

        // Also check whether event tickets outnumber venue capacity. If so, throw error.

        Event event = Event.builder()
                .name(eventName)
                .details(eventDetails)
                .startDate(startDate)
                .endDate(endDate)
                .ticketPrice(ticketPrice)
                .numberOfTickets(numberOfTickets)
                .minAgeAllowed(minAgeAllowed)
                .eventType(eventTypeEnum)
                .eventStatus(EventStatus.WAITING_APPROVAL)
                .venueId(1L) // Change this when you implement venue service
                .organizerId(organizerId)
                .build();

        eventDao.saveEvent(event);

        return event;
    }

    public List<Event> getFilteredEvents(String searchTerm, String artistName, String brandName, String venueName, String location, String type, Integer minAgeAllowed, LocalDateTime startDate, String orderBy, String orderDirection) {
        return eventDao.getFilteredEvents(searchTerm, artistName, brandName, venueName, location, type, minAgeAllowed, startDate, orderBy, orderDirection)
                .orElseThrow(() -> new EventRuntimeException("Events not found", 1, HttpStatus.NOT_FOUND));
    }

    public List<Event> getEventsByOrganizerId(Long organizerId) {
        return eventDao.getEventsByOrganizerId(organizerId)
                .orElseThrow(() -> new EventRuntimeException("Events not found", 1, HttpStatus.NOT_FOUND));
    }

    public Event getEventById(Long eventId) {
        return eventDao.getEventByEventId(eventId)
                .orElseThrow(() -> new EventRuntimeException("Event not found", 1, HttpStatus.NOT_FOUND));
    }

    public List<Event> getAllEvents() {
        return eventDao.getAllEvents()
                .orElseThrow(() -> new EventRuntimeException("Events not found", 1, HttpStatus.NOT_FOUND));
    }

    public boolean approveEvent(Long eventId){
        return eventDao.approveEvent(eventId);
    }

    public boolean rejectEvent(Long eventId){
        return eventDao.rejectEvent(eventId);
    }
    public void addReview(Review review){
        reviewDao.addReview(review);
    }
}
