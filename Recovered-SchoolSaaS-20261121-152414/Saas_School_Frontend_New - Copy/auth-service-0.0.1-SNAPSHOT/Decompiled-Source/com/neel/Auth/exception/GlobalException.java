/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.exception.AlreadyExistException
 *  com.neel.Auth.exception.GlobalException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.neel.Auth.exception;

import com.neel.Auth.exception.AlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value={AlreadyExistException.class})
    ResponseEntity<String> alreadyExistException(AlreadyExistException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)ex.getMessage());
    }
}

