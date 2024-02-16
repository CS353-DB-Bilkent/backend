package com.ticketseller.backend.controllers;

import com.ticketseller.backend.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<?>> health() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Healthy, Hi Mithat Çoruh");

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .operationResultData(map)
                        .build()
        );
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> empty() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Internship Management Application");

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .operationResultData(map)
                        .build()
        );
    }
}
