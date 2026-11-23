/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.EntityLookupController
 *  com.neel.Teacher.model.EntityLookupResult
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.service.EntityLookupService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.EntityLookupResult;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.service.EntityLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class EntityLookupController {
    private final EntityLookupService entityLookupService;

    @Autowired
    public EntityLookupController(EntityLookupService entityLookupService) {
        this.entityLookupService = entityLookupService;
    }

    @GetMapping(value={"/entity/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<EntityLookupResult>> findEntityById(@PathVariable String schoolId, @PathVariable String id) {
        EntityLookupResult result = this.entityLookupService.findEntityById(id, schoolId);
        if (result.isFound()) {
            return ResponseEntity.ok(new ResponseMessage("Entity found", result));
        }
        return ResponseEntity.ok(new ResponseMessage("No entity found with the provided ID and schoolId", result));
    }
}

