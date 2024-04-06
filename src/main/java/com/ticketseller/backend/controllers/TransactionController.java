package com.ticketseller.backend.controllers;

import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.services.TransactionService;
import com.ticketseller.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/deposit/{amount}")
    public ResponseEntity<ApiResponse<?>> deposit(HttpServletRequest request, @RequestParam Double amount) {
        User user = (User) request.getAttribute("user");

        transactionService.deposit(user.getUserId(), amount);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .operationResultData(null)
                        .build()
        );
    }

    @PostMapping("/withdraw/{amount}")
    public ResponseEntity<ApiResponse<?>> withdraw(HttpServletRequest request, @RequestParam Double amount) {
        User user = (User) request.getAttribute("user");

        transactionService.withdraw(user.getUserId(), amount);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .operationResultData(null)
                        .build()
        );
    }


}
