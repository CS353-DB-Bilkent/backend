package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.*;
import com.ticketseller.backend.entity.*;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventDao eventDao;
    private final BrandService brandService;
    private final EventPersonService eventPersonService;
    private final VenueDao venueDao;
    private final BrandDao brandDao;
    private final VenueService venueService;
    private final ReviewDao reviewDao;
    private final EventPersonDao eventPersonDao;

    public Event saveEvent(String eventName, String eventDetails, LocalDateTime startDate, LocalDateTime endDate, Double ticketPrice, Integer numberOfTickets, String eventType, Integer minAgeAllowed, Long organizerId, Long venueId,  Long brandId, String brandName, Long eventPersonId, String eventPersonName) {
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

        Optional<Venue> venueOptional = Optional.ofNullable(venueService.findVenueById(venueId));
        if (!venueOptional.isPresent()) {
            log.error("Venue not found");
            throw new EventRuntimeException("Venue not found", 1, HttpStatus.BAD_REQUEST);
        }
        Venue venue = venueOptional.get();

        if (numberOfTickets > venue.getVenueCapacity()) {
            log.error("Number of tickets exceeds venue capacity");
            throw new EventRuntimeException("Number of tickets exceeds venue capacity", 1, HttpStatus.BAD_REQUEST);
        }

        Optional<Brand> brandOptional = Optional.ofNullable(brandService.findBrandById(brandId));
        Brand brand = new Brand();
        if (!brandOptional.isPresent()) {
            brand.setBrandId(brandId);
            brand.setBrandName(brandName);
            brandDao.saveBrand(brand);
        }
        else {
            brand = brandOptional.get();
        }

        EventPerson eventPerson = eventPersonService.findEventPersonById(eventPersonId);
        if (eventPerson == null) {
            eventPerson = new EventPerson();
            eventPerson.setEventPersonId(eventPersonId);
            eventPerson.setEventPersonName(eventPersonName);
            eventPersonDao.saveEventPerson(eventPerson);
        }

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
                .organizerId(organizerId)
                .venue(venue)
                .brand(brand)
                .eventPerson(eventPerson)
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

    public List<Event> getAllMyEvents(Long userId) {
        return eventDao.getMyEvents(userId)
                .orElseThrow(() -> new EventRuntimeException("Events not found", 1, HttpStatus.NOT_FOUND));
    }
    public boolean reportEvent(Long eventId, Long organizerId){
        return eventDao.createReport(eventId, organizerId);
    }
    public boolean cancelEvent(Long eventId){
        return eventDao.cancelEvent(eventId);
    }
}
