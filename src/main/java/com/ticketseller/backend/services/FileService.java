package com.ticketseller.backend.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {


//    public void saveFile(S3File s3file, MultipartFile file) throws IOException {
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//
//        s3Client.putObject(Buckets.BILKENT_INTERNSHIP_MANAGEMENT,
//                FileUtils.getRealFileName(s3file),
//                file.getInputStream(), metadata);
//
//        fileRepository.save(s3file);
//    }
//
//    public void deleteFile(String id) {
//        Optional<S3File> optionalS3File = fileRepository.findById(id);
//
//        if (optionalS3File.isEmpty())
//            throw new FileRuntimeException("File could not be found!", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
//
//        s3Client.deleteObject( optionalS3File.get().getBucketName(), FileUtils.getRealFileName(optionalS3File.get()));
//        fileRepository.deleteById(optionalS3File.get().getId());
//    }
//
//    public String getFullFileNameById(String id) {
//        Optional<S3File> optionalS3File = fileRepository.findById(id);
//
//        if (optionalS3File.isEmpty())
//            throw new FileRuntimeException("File could not be found!", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
//
//        return FileUtils.getRealFileName(optionalS3File.get());
//    }
//
//    public byte[] getFileById(String id) {
//        Optional<S3File> optionalS3File = fileRepository.findById(id);
//
//        if (optionalS3File.isEmpty())
//            throw new FileRuntimeException("File could not be found!", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
//
//        return this.loadFile(optionalS3File.get());
//    }
//
//    private byte[] loadFile(S3File s3File) {
//
//        try {
//            S3ObjectInputStream s3ObjectInputStream = s3Client.getObject(s3File.getBucketName(),
//                    FileUtils.getRealFileName(s3File)).getObjectContent();
//
//            return s3ObjectInputStream.readAllBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new FileRuntimeException(e.getMessage(), ErrorCodes.INTERNAL_SERVER_ERROR,
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (AmazonS3Exception e) {
//            log.error(e.toString());
//            throw new FileRuntimeException(e.getMessage(), ErrorCodes.INTERNAL_SERVER_ERROR,
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
}
