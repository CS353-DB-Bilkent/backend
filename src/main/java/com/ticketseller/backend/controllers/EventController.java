package com.ticketseller.backend.controllers;


import com.ticketseller.backend.dto.EventDTO;
import com.ticketseller.backend.security.JwtTokenUtil;
import com.ticketseller.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {
    private final JwtTokenUtil jwtTokenUtil;
    private final EventService eventService;
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(
            @RequestHeader("Authorization") String token,
            @RequestBody EventDTO eventDTO) {
        if (!jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Endpoint not implemented yet");
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateEvent(
            @RequestHeader("Authorization") String token,
            @RequestBody EventDTO eventDTO) {

        // Validate JWT token
        if (!jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        eventService.updateEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Endpoint not implemented yet");
    }
    @DeleteMapping("delete/{guid}")
    public ResponseEntity<String> deleteEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable("guid") String guid) {

        // Validate JWT token
        if (!jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        eventService.deleteEvent(guid);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Endpoint not implemented yet");
    }
}
