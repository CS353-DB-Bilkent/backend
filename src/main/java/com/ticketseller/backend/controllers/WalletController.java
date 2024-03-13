package com.ticketseller.backend.controllers;

import com.ticketseller.backend.dto.UserDto;
import com.ticketseller.backend.dto.WalletDto;
import com.ticketseller.backend.dto.request.auth.RegisterRequest;
import com.ticketseller.backend.dto.request.wallet.AddTransactionRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Transaction;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.services.UserService;
import com.ticketseller.backend.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WalletDto>> getMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<WalletDto>builder()
                        .operationResultData(walletService.getWalletMe(user.getUserId()))
                        .build()
        );
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactions(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<List<Transaction>>builder()
                        .operationResultData(walletService.getTransactions(user.getUserId()))
                        .build()
        );
    }

    @PostMapping("/transactions")
    public ResponseEntity<ApiResponse<Void>> addTransaction(HttpServletRequest request, @Valid @RequestBody AddTransactionRequest addTransactionRequest) {
        User user = (User) request.getAttribute("user");

        walletService.saveTransaction(
                user.getUserId(), addTransactionRequest.getTransactionAmount(), addTransactionRequest.getTransactionType());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .operationResultData(null)
                        .build()
        );
    }

}
