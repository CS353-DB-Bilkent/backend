package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.NoAuthRequired;
import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.event.CreateEventRequest;
import com.ticketseller.backend.dto.request.event.FilterEventsRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.EventService;
import com.ticketseller.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

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
                                user.getUserId()
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
                                filterEventsRequest.getStartDate()
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

    public void buyTicket(/* ... */) {
        // ...
    }

    // Fill in the rest


}
