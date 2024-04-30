package com.ticketseller.backend.services;


import com.ticketseller.backend.dao.*;
import com.ticketseller.backend.entity.*;
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

public class EventPersonService {
    private final EventPersonDao eventPersonDao;

    public EventPerson findEventPersonById(Long eventPersonId) {
        return (EventPerson) eventPersonDao.findEventPersonById(eventPersonId);
    }

    public void saveEventPerson(EventPerson eventPerson) {
        eventPersonDao.saveEventPerson(eventPerson);
    }

    public List<EventPerson> getAllEventPersons() {
        return eventPersonDao.getAllEventPersons();
    }

    public EventPerson findEventPersonByName(String eventPersonName) {
        return (EventPerson) eventPersonDao.findEventPersonByName(eventPersonName);
    }
}