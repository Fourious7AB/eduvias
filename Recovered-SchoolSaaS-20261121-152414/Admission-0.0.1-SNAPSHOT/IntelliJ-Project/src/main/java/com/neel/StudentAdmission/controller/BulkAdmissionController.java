/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.BulkAdmissionController
 *  com.neel.StudentAdmission.service.BulkAdmissionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.io.InputStreamResource
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.service.BulkAdmissionService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/bulk-admission"})
@CrossOrigin(value={"*"})
public class BulkAdmissionController {
    private final BulkAdmissionService bulkAdmissionService;

    @Autowired
    public BulkAdmissionController(BulkAdmissionService bulkAdmissionService) {
        this.bulkAdmissionService = bulkAdmissionService;
    }

    @GetMapping(value={"/template"})
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        try {
            ByteArrayInputStream templateStream = this.bulkAdmissionService.generateBulkAdmissionTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=student_admission_template.xlsx");
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(headers)).contentType(MediaType.parseMediaType((String)"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(new InputStreamResource((InputStream)templateStream));
        }
        catch (IOException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

