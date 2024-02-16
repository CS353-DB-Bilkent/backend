package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.FileService;
import com.ticketseller.backend.utils.FileUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/generic/{fileId}")
    @RequiredRole({ Role.ADMIN })
    public ResponseEntity<byte[]> getFileById(@NotNull @NotBlank @PathVariable String fileId) {
        // String fileHeader = "attachment; filename=\"" + fileService.getFullFileNameById(fileId) + "\"";

        // return FileUtils.createFileResponse(fileHeader, fileService.getFileById(fileId));
        return ResponseEntity.ok(new byte[] {});
    }



}
