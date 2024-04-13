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
import java.util.Optional;


@Slf4j

@Service
@RequiredArgsConstructor

public class BrandService {
    private final BrandDao brandDao;

    public Brand findBrandById(Long brandId) {
        return brandDao.findBrandById(brandId)
                .orElseThrow(() -> new EventRuntimeException("Brand not found", 1, HttpStatus.NOT_FOUND));
    }

}
