package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.VenueDao;
import com.ticketseller.backend.entity.Venue;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Slf4j

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueDao venueDao;

    public Venue saveVenue(String venueName, String venueAddress, String venueCity, Long venueCapacity) {
        Venue venue = Venue.builder()
                .venueName(venueName)
                .venueAddress(venueAddress)
                .venueCity(venueCity)
                .venueCapacity(venueCapacity)
                .build();
        venueDao.saveVenue(venue);
        return venue;
    }

    public Venue findVenueById(Long venueId) {
        return venueDao.findVenueById(venueId)
                .orElseThrow(() -> new EventRuntimeException("Venue not found", 1, HttpStatus.NOT_FOUND));
    }
}
