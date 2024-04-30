package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.NoAuthRequired;
import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.event.CreateEventRequest;
import com.ticketseller.backend.dto.request.event.FilterEventsRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.*;
import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import com.ticketseller.backend.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final VenueService venueService;
    private final BrandService brandService;
    private final EventPersonService eventPersonService;

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
                                createEventRequest.getEventPersonId()
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

    @PostMapping("/buyTicket/{eventId}/{isBuyerVisible}")
    @RequiredRole({Role.USER})
    public ResponseEntity<String> buyTicket(HttpServletRequest request, @PathVariable Long eventId, @PathVariable boolean isBuyerVisible) {
        System.out.println("Buy Ticket");
        User user = (User) request.getAttribute("user");
        System.out.println(" Event ID: " + eventId + " Buyer Visible: " + isBuyerVisible);
        boolean result = ticketService.buyTicket(user.getUserId(), eventId, isBuyerVisible, request);
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
    @RequiredRole({ Role.USER })
    public void postReview(HttpServletRequest request, @RequestBody Review review) {
        review.setUserId(((User)request.getAttribute("user")).getUserId());
        review.setUserInitials(
                ((User) request.getAttribute("user")).getName().substring(0, 1) +
                        ((User) request.getAttribute("user")).getName().substring(1, 2)
        );
        review.setReviewDate(LocalDateTime.now());

        eventService.addReview(review);
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
    public ResponseEntity<ApiResponse<List<Report>>> reportOfEvent(@PathVariable Long eventId , HttpServletRequest request){
        return ResponseEntity.ok(
                ApiResponse.<List<Report>>builder()
                        .operationResultData(eventService.reportEvent(eventId, ((User)request.getAttribute("user")).getUserId()))
                        .build()
        );
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
    public void createVenue(@Valid @RequestBody Venue venue) {
        venueService.saveVenue(venue);
    }

    @GetMapping("/{eventId}/getVenue")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<Venue>> getVenueOfEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
//        if (event == null) {
//            return ResponseEntity.notFound().build();
//        }
        Long venueId = event.getVenueId();
        return ResponseEntity.ok(
                ApiResponse.<Venue>builder()
                        .operationResultData(venueService.findVenueById(venueId))
                        .build()
        );
    }

    @PostMapping("/createBrand")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public void createBrand(@Valid @RequestBody Brand brand) {
        brandService.saveBrand(brand);
    }

    @GetMapping("/{eventId}/getBrand")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<Brand>> getBrandOfEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
//        if (event == null) {
//            return ResponseEntity.notFound().build();
//        }
        Long brandId = event.getBrandId();
        return ResponseEntity.ok(
                ApiResponse.<Brand>builder()
                        .operationResultData(brandService.findBrandById(brandId))
                        .build()
        );
    }

    @PostMapping("/createEventPerson")
    @RequiredRole({Role.EVENT_ORGANIZER})
    public void createEventPerson(@Valid @RequestBody EventPerson eventPerson) {
        eventPersonService.saveEventPerson(eventPerson);
    }

    @GetMapping("/{eventId}/getEventPerson")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<EventPerson>> getEventPerson(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
//        if (event == null) {
//            return ResponseEntity.notFound().build();
//        }
        Long eventPersonId = event.getEventPersonId();
        return ResponseEntity.ok(
                ApiResponse.<EventPerson>builder()
                        .operationResultData(eventPersonService.findEventPersonById(eventPersonId))
                        .build()
        );
    }

    @GetMapping("/getAllBrands")
    @RequiredRole(Role.EVENT_ORGANIZER)
    public ResponseEntity<ApiResponse<List<Brand>>> getAllBrands(){
        return ResponseEntity.ok(
                ApiResponse.<List<Brand>>builder()
                        .operationResultData(brandService.getAllBrands())
                        .build()
        );
    }

    @GetMapping("/getAllVenues")
    @RequiredRole(Role.EVENT_ORGANIZER)
    public ResponseEntity<ApiResponse<List<Venue>>> getAllVenues(){
        return ResponseEntity.ok(
                ApiResponse.<List<Venue>>builder()
                        .operationResultData(venueService.getAllVenues())
                        .build()
        );
    }

    @GetMapping("/getAllEventPersons")
    @RequiredRole(Role.EVENT_ORGANIZER)
    public ResponseEntity<ApiResponse<List<EventPerson>>> getAllEventPersons(){
        return ResponseEntity.ok(
                ApiResponse.<List<EventPerson>>builder()
                        .operationResultData(eventPersonService.getAllEventPersons())
                        .build()
        );
    }

    @PostMapping("/refundTicket/{ticketId}")
    @RequiredRole(Role.USER)
    public void refundTicket(@PathVariable Long ticketId, HttpServletRequest request){
        if(ticketService.getTicketsByUserId(((User)request.getAttribute("user")).getUserId(), request).stream().anyMatch(t -> Objects.equals(t.getTicketId(), ticketId))){
            ticketService.refundTicket(ticketId, request);
        }
    }

    @GetMapping("/getUnapprovedEvents")
    @RequiredRole({ Role.ADMIN})
    public ResponseEntity<ApiResponse<List<Event>>> getUnapprovedEvents(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<List<Event>>builder()
                        .operationResultData(eventService.getUnApprovedEvents(user.getUserId()))
                        .build()

        );
    }

    @GetMapping("/{eventId}/getReviews")
    @NoAuthRequired
    public ResponseEntity<ApiResponse<List<Review>>> getReviews(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                ApiResponse.<List<Review>>builder()
                        .operationResultData(eventService.getReviewsByEventId(eventId))
                        .build()
        );
    }

    @GetMapping("/{eventId}/getEventAttendees")
    @NoAuthRequired
    public ResponseEntity<ApiResponse<List<User>>> getEventAttendees(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                ApiResponse.<List<User>>builder()
                        .operationResultData(eventService.getEventAttendeesByEventId(eventId))
                        .build()
        );
    }

}