/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.dto.ResponseDto
 *  com.neel.Settings.exception.AlreadyExist
 *  com.neel.Settings.exception.GlobalExceptionHandler
 *  com.neel.Settings.exception.UniversalNotFound
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.neel.Settings.exception;

import com.neel.Settings.dto.ResponseDto;
import com.neel.Settings.exception.AlreadyExist;
import com.neel.Settings.exception.UniversalNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={AlreadyExist.class})
    ResponseEntity<ResponseDto> alreadyExistException(AlreadyExist exist) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(Boolean.valueOf(false), exist.getMessage(), null));
    }

    @ExceptionHandler(value={UniversalNotFound.class})
    ResponseEntity<ResponseDto> universalNotFound(UniversalNotFound exist) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(Boolean.valueOf(false), exist.getMessage(), null));
    }
}

