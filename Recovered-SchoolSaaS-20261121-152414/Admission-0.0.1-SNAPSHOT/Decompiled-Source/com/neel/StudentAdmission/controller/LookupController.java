/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.LookupController
 *  com.neel.StudentAdmission.model.LookUp
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.service.LookupService
 *  lombok.Generated
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.LookUp;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.service.LookupService;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value={"*"})
@RequestMapping(value={"/api/look-up"})
public class LookupController {
    private final LookupService lookupService;

    @GetMapping(value={"/entity/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> findEntityById(@PathVariable String schoolId, @PathVariable String id) {
        LookUp result = this.lookupService.findEntityById(id, schoolId);
        if (result.isFound()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Entity found", (Object)result));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("No entity found with the provided ID and schoolId", (Object)result));
    }

    @Generated
    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }
}

