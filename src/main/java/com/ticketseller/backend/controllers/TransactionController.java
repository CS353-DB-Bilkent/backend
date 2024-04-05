package com.ticketseller.backend.controllers;

import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.services.TransactionService;
import com.ticketseller.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<Transaction>>> getMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<List<Transaction>>builder()
                        .operationResultData(transactionService.getTransactions(user.getUserId()))
                        .build()
        );
    }

}
