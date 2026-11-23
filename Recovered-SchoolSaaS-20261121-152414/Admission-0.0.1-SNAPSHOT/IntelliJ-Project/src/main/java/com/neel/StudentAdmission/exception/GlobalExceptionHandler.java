/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.exception.AlreadyExistException
 *  com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException
 *  com.neel.StudentAdmission.exception.GlobalExceptionHandler
 *  com.neel.StudentAdmission.exception.PlanNotExist
 *  com.neel.StudentAdmission.model.ResponseModel
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.neel.StudentAdmission.exception;

import com.neel.StudentAdmission.exception.AlreadyExistException;
import com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException;
import com.neel.StudentAdmission.exception.PlanNotExist;
import com.neel.StudentAdmission.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ClassNotFoundInAcademicServiceException.class})
    public ResponseEntity<ClassNotFoundInAcademicServiceException> handleClassNotFoundInAcademicServiceException(ClassNotFoundInAcademicServiceException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(value={AlreadyExistException.class})
    public ResponseEntity<ResponseModel> alreadyExistException(AlreadyExistException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ResponseModel(Boolean.valueOf(false), ex.getMessage(), null));
    }

    @ExceptionHandler(value={PlanNotExist.class})
    public ResponseEntity<ResponseModel> planNotExist(PlanNotExist e) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ResponseModel(Boolean.valueOf(false), e.getMessage(), null));
    }
}

