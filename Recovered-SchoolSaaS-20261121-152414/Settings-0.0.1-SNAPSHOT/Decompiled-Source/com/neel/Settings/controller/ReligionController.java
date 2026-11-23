/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.controller.ReligionController
 *  com.neel.Settings.model.Religion
 *  com.neel.Settings.model.ResponseMessage
 *  com.neel.Settings.service.ReligionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.Settings.controller;

import com.neel.Settings.model.Religion;
import com.neel.Settings.model.ResponseMessage;
import com.neel.Settings.service.ReligionService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class ReligionController {
    @Autowired
    private ReligionService religionService;

    @GetMapping(value={"/religions"})
    public ResponseEntity<ResponseMessage> getAllReligions() {
        List religions = this.religionService.getAllReligions();
        return ResponseEntity.ok((Object)new ResponseMessage("All religions fetched successfully.", (Object)religions));
    }

    @GetMapping(value={"/religions/{schoolId}"})
    public ResponseEntity<ResponseMessage> getReligionsBySchoolId(@PathVariable String schoolId) {
        List religions = this.religionService.getReligionsBySchoolId(schoolId);
        if (religions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No religions found for this school.", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Religions retrieved successfully.", (Object)religions));
    }

    @PostMapping(value={"/religions/create-defaults"})
    public ResponseEntity<ResponseMessage> createDefaultReligions() {
        try {
            List defaultReligions = this.religionService.createDefaultUniversalReligions();
            return ResponseEntity.ok((Object)new ResponseMessage("Default religions created successfully for all schools.", (Object)defaultReligions));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Failed to create default religions: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/religions/default-json"})
    public ResponseEntity<String> getDefaultReligionsJsonData() {
        String jsonData = this.religionService.getDefaultReligionsJsonData();
        return ResponseEntity.ok((Object)jsonData);
    }

    @PostMapping(value={"/religions/{schoolId}"})
    public ResponseEntity<ResponseMessage> createReligion(@PathVariable String schoolId, @RequestBody Religion religion) {
        try {
            religion.setSchoolId(schoolId);
            String religionName = religion.getReligionName().toLowerCase();
            if (this.religionService.findReligionByNameAndSchool(religionName, schoolId).isPresent()) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Religion already exists for this school.", null));
            }
            if (this.religionService.religionExistsAnywhere(religionName)) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("This religion already exists in the system.", null));
            }
            Religion savedReligion = this.religionService.saveReligion(religion);
            return ResponseEntity.ok((Object)new ResponseMessage("Religion created successfully.", (Object)savedReligion));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/universal-religions"})
    public ResponseEntity<ResponseMessage> createUniversalReligion(@RequestBody Religion religion) {
        try {
            Religion savedReligion = this.religionService.saveUniversalReligion(religion);
            return ResponseEntity.ok((Object)new ResponseMessage("Universal religion created successfully.", (Object)savedReligion));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/religions/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateReligion(@PathVariable String schoolId, @PathVariable String id, @RequestBody Religion updatedReligion) {
        try {
            String updatedReligionName = updatedReligion.getReligionName().toLowerCase();
            Optional existingReligion = this.religionService.findReligionByNameAndSchool(updatedReligionName, schoolId);
            if (existingReligion.isPresent() && !((Religion)existingReligion.get()).getId().equals(id)) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Religion already exists for this school.", null));
            }
            Optional result = this.religionService.updateReligion(schoolId, id, updatedReligion);
            if (result.isPresent()) {
                return ResponseEntity.ok((Object)new ResponseMessage("Religion updated successfully.", result.get()));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Religion not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/universal-religions/{id}"})
    public ResponseEntity<ResponseMessage> updateUniversalReligion(@PathVariable String id, @RequestBody Religion updatedReligion) {
        try {
            Optional result = this.religionService.updateUniversalReligion(id, updatedReligion);
            if (result.isPresent()) {
                return ResponseEntity.ok((Object)new ResponseMessage("Universal religion updated successfully.", result.get()));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal religion not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/religions/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteReligion(@PathVariable String schoolId, @PathVariable String id) {
        try {
            boolean deleted = this.religionService.deleteReligion(schoolId, id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Religion deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Religion not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/universal-religions/{id}"})
    public ResponseEntity<ResponseMessage> deleteUniversalReligion(@PathVariable String id) {
        try {
            boolean deleted = this.religionService.deleteUniversalReligion(id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Universal religion deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal religion not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }
}

