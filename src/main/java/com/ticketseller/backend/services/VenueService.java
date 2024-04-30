package com.ticketseller.backend.services;

import com.ticketseller.backend.dao.VenueDao;
import com.ticketseller.backend.entity.Venue;
import com.ticketseller.backend.exceptions.runtimeExceptions.EventRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueDao venueDao;

    public void saveVenue(Venue venue) {
        venueDao.saveVenue(venue);
    }

    public Venue findVenueById(Long venueId) {
        return venueDao.findVenueById(venueId);
    }

    public List<Venue> getAllVenues() {
        return venueDao.getAllVenues();
    }
}
