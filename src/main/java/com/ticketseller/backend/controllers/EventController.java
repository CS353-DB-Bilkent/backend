package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.NoAuthRequired;
import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.event.CreateEventRequest;
import com.ticketseller.backend.dto.request.event.FilterEventsRequest;
import com.ticketseller.backend.dto.request.ticket.BuyTicketRequest;
import com.ticketseller.backend.dto.request.venue.CreateVenueRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.*;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.EventService;
import com.ticketseller.backend.services.TicketService;
import com.ticketseller.backend.services.UserService;
import com.ticketseller.backend.services.VenueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final VenueService venueService;

    @GetMapping("/me")
    @RequiredRole({ Role.EVENT_ORGANIZER })
    public ResponseEntity<ApiResponse<List<Event>>> getMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<List<Event>>builder()
                        .operationResultData(eventService.getEventsByOrganizerId(user.getUserId()))
                        .build()

        );
    }

    @PostMapping("/create")
    @RequiredRole({ Role.EVENT_ORGANIZER })
    public ResponseEntity<ApiResponse<Event>> createEvent(HttpServletRequest request, @Valid @RequestBody CreateEventRequest createEventRequest) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<Event>builder()
                        .operationResultData(eventService.saveEvent(
                                createEventRequest.getName(),
                                createEventRequest.getDetails(),
                                createEventRequest.getStartDate(),
                                createEventRequest.getEndDate(),
                                createEventRequest.getTicketPrice(),
                                createEventRequest.getNumberOfTickets(),
                                createEventRequest.getEventType(),
                                createEventRequest.getMinAgeAllowed(),
                                user.getUserId(),
                                createEventRequest.getVenueId(),
                                createEventRequest.getBrandId(),
                                createEventRequest.getBrandName(),
                                createEventRequest.getEventPersonId(),
                                createEventRequest.getEventPersonName()
                        ))
                        .build()
        );
    }

    @GetMapping("/all")
    @NoAuthRequired
    public ResponseEntity<ApiResponse<List<Event>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.<List<Event>>builder()
                        .operationResultData(eventService.getAllEvents())
                        .build()
        );
    }


    @PostMapping("/filter")
    @NoAuthRequired
    public ResponseEntity<ApiResponse<List<Event>>> filterEvents(@Valid @RequestBody FilterEventsRequest filterEventsRequest) {
        return ResponseEntity.ok(
                ApiResponse.<List<Event>>builder()
                        .operationResultData(eventService.getFilteredEvents(
                                filterEventsRequest.getSearchTerm(),
                                filterEventsRequest.getArtistName(),
                                filterEventsRequest.getBrandName(),
                                filterEventsRequest.getVenueName(),
                                filterEventsRequest.getLocation(),
                                filterEventsRequest.getType(),
                                filterEventsRequest.getMinAgeAllowed(),
                                filterEventsRequest.getStartDate(),
                                filterEventsRequest.getOrderBy(),
                                filterEventsRequest.getOrderDirection()
                        ))
                        .build()
        );
    }

    @GetMapping("/{eventId}/details")
    @NoAuthRequired
    public ResponseEntity<ApiResponse<Event>> getEventDetails(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                ApiResponse.<Event>builder()
                        .operationResultData(eventService.getEventById(eventId))
                        .build()
        );
    }

    @PostMapping("/{eventId}/buyTicket")
    @RequiredRole({Role.USER})
    public ResponseEntity<String> buyTicket(HttpServletRequest request, @PathVariable Long eventId, @RequestBody BuyTicketRequest buyTicketRequest) {
        User user = (User) request.getAttribute("user");


        boolean result = ticketService.buyTicket(user.getUserId(), eventId, buyTicketRequest.isBuyerVisible());
        if (result) {
            return ResponseEntity.ok("Ticket purchased successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to purchase ticket. Please check the ticket availability and your balance.");
        }
    }


    @GetMapping("/approveEvent/{eventId}")
    @RequiredRole({ Role.ADMIN })
    public ResponseEntity<ApiResponse<Event>> approveEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return eventService.approveEvent(event.getEventId()) ? ResponseEntity.ok(ApiResponse.<Event>builder()
                .operationResultData(event)
                .build()): ResponseEntity.internalServerError().build();
    }


    @GetMapping("/rejectEvent/{eventId}")
    @RequiredRole({ Role.ADMIN })
    public ResponseEntity<ApiResponse<Event>> rejectEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }


        return eventService.rejectEvent(event.getEventId()) ? ResponseEntity.ok(ApiResponse.<Event>builder()
                .operationResultData(event)
                .build()): ResponseEntity.internalServerError().build();
    }


    @GetMapping("/getAllTickets/{userId}")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<List<Ticket>>> getTicketsByUserId(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<List<Ticket>>builder()
                        .operationResultData(ticketService.getTicketsByUserId(user.getUserId(), request))
                        .build()
        );
    }
    @PostMapping("/postReview")
    @RequiredRole({Role.USER})
    public void postReview(@RequestBody Review review, HttpServletRequest request){
        review.setUserId(((User)request.getAttribute("user")).getUserId());
        eventService.addReview(review); //TODO: userid must be added
    }

    @GetMapping("/getAllMyEvents")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public ResponseEntity<ApiResponse<List<Event>>> getAllMyEvents(HttpServletRequest request){

        return ResponseEntity.ok(
                ApiResponse.<List<Event>>builder()
                        .operationResultData(eventService.getAllMyEvents(((User)request.getAttribute("user")).getUserId()))
                        .build()
        );
    }

    @PostMapping("/reportEvent/{eventId}")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public boolean reportOfEvent(@PathVariable Long eventId ,HttpServletRequest request){
        return eventService.reportEvent(eventId, ((User)request.getAttribute("user")).getUserId());
    }

    @PostMapping("/cancelEvent/{eventId}")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public boolean cancelEvent(@PathVariable Long eventId ,HttpServletRequest request){
        if(Objects.equals(eventService.getEventById(eventId).getOrganizerId(), ((User) request.getAttribute("user")).getUserId())) {
            return eventService.cancelEvent(eventId);
        }
        return false;
    }

    @PostMapping("/createVenue")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public ResponseEntity<ApiResponse<Venue>> createVenue(@Valid @RequestBody CreateVenueRequest createVenueRequest) {
        return ResponseEntity.ok(
                ApiResponse.<Venue>builder()
                        .operationResultData(venueService.saveVenue(
                                createVenueRequest.getVenueName(),
                                createVenueRequest.getVenueAddress(),
                                createVenueRequest.getVenueCity(),
                                createVenueRequest.getVenueCapacity()
                        ))
                        .build()
        );
    }
}
