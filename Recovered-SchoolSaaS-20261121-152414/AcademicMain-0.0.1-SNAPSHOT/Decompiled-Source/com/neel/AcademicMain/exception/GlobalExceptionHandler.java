/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  com.neel.AcademicMain.exception.AccessDenied
 *  com.neel.AcademicMain.exception.ClassNameAlreadyExist
 *  com.neel.AcademicMain.exception.ClassNotFound
 *  com.neel.AcademicMain.exception.GlobalExceptionHandler
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.neel.AcademicMain.exception;

import com.neel.AcademicMain.dto.ApiResponseDto;
import com.neel.AcademicMain.exception.AccessDenied;
import com.neel.AcademicMain.exception.ClassNameAlreadyExist;
import com.neel.AcademicMain.exception.ClassNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ClassNameAlreadyExist.class})
    ResponseEntity<ApiResponseDto> classAlreadyExist(ClassNameAlreadyExist exist) {
        String message = exist.getMessage();
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ApiResponseDto(Boolean.valueOf(false), message, null));
    }

    @ExceptionHandler(value={AccessDenied.class})
    ResponseEntity<ApiResponseDto> accessDenied(AccessDenied exist) {
        String message = exist.getMessage();
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body((Object)new ApiResponseDto(Boolean.valueOf(false), message, null));
    }

    @ExceptionHandler(value={ClassNotFound.class})
    ResponseEntity<ApiResponseDto> classNotFound(ClassNotFound exist) {
        String message = exist.getMessage();
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ApiResponseDto(Boolean.valueOf(false), message, null));
    }
}

