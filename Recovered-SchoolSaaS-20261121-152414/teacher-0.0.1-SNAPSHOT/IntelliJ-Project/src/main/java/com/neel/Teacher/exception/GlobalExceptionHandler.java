/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.dto.ApiResponse
 *  com.neel.Teacher.exception.GlobalExceptionHandler
 *  com.neel.Teacher.exception.ResourceNotFoundException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.multipart.MaxUploadSizeExceededException
 */
package com.neel.Teacher.exception;

import com.neel.Teacher.dto.ApiResponse;
import com.neel.Teacher.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error((String)ex.getMessage()));
    }

    @ExceptionHandler(value={MaxUploadSizeExceededException.class})
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ApiResponse.error((String)"File size exceeds the maximum allowed size"));
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error((String)("An unexpected error occurred: " + ex.getMessage())));
    }
}

