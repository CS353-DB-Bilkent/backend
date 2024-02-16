package com.ticketseller.backend.utils;

import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.exceptions.runtimeExceptions.FileRuntimeException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class FileUtils {
    // public static String getRealFileName(S3File s3File) {
    //     return s3File.getFolderPath() + s3File.getId() + "." + s3File.getFileExtension();
    // }

//    public static void checkFileExtension(MultipartFile file, String extension) {
//        if (!Objects.requireNonNull(FileNameUtils.getExtension(file.getOriginalFilename())).equalsIgnoreCase(extension))
//            throw new FileRuntimeException("File must be " + extension + " file.", ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST);
//    }
//
//    public static void checkFileExtension(MultipartFile file, List<String> extensions) {
//        if (extensions.stream().noneMatch(e->Objects.requireNonNull(FileNameUtils.getExtension(file.getOriginalFilename())).equalsIgnoreCase(e)))
//            throw new FileRuntimeException("File must be " + String.join(", ", extensions) + " file.", ErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST);
//    }

    public static ResponseEntity<byte[]> createFileResponse(String fileHeader, byte[] body) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("Content-disposition", fileHeader)
                .body(body);
    }
}
