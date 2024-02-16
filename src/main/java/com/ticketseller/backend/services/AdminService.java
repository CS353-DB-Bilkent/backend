package com.ticketseller.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;

    private final FileService fileService;


    public byte[] getResultStatistics(String semesterId) {

        // return fileService.createSemesterResultStatistics(submissionList);
        return new byte[] {};
    }

    public byte[] getGradeStatistics(String semesterId) {
        // return fileService.createSubmissionStatisticsReport(submissionList,department);
        return new byte[] {};
    }
}
