/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.controller.GenderController
 *  com.neel.Settings.model.Gender
 *  com.neel.Settings.model.ResponseMessage
 *  com.neel.Settings.service.GenderService
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

import com.neel.Settings.model.Gender;
import com.neel.Settings.model.ResponseMessage;
import com.neel.Settings.service.GenderService;
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
public class GenderController {
    @Autowired
    private GenderService genderService;

    @GetMapping(value={"/genders"})
    public ResponseEntity<ResponseMessage> getAllGenders() {
        List genders = this.genderService.getAllGenders();
        return ResponseEntity.ok((Object)new ResponseMessage("All genders fetched successfully.", (Object)genders));
    }

    @GetMapping(value={"/genders/{schoolId}"})
    public ResponseEntity<ResponseMessage> getGendersBySchoolId(@PathVariable String schoolId) {
        List genders = this.genderService.getGendersBySchoolId(schoolId);
        if (genders.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No genders found for this school.", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Genders retrieved successfully.", (Object)genders));
    }

    @PostMapping(value={"/genders/create-defaults"})
    public ResponseEntity<ResponseMessage> createDefaultGenders() {
        try {
            List defaultGenders = this.genderService.createDefaultUniversalGenders();
            return ResponseEntity.ok((Object)new ResponseMessage("Default genders created successfully for all schools.", (Object)defaultGenders));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Failed to create default genders: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/genders/default-json"})
    public ResponseEntity<String> getDefaultGendersJsonData() {
        String jsonData = this.genderService.getDefaultGendersJsonData();
        return ResponseEntity.ok((Object)jsonData);
    }

    @PostMapping(value={"/genders/{schoolId}"})
    public ResponseEntity<ResponseMessage> createGender(@PathVariable String schoolId, @RequestBody Gender gender) {
        try {
            gender.setSchoolId(schoolId);
            if (this.genderService.findGenderByNameAndSchool(gender.getGenderName(), schoolId).isPresent()) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Gender already exists for this school.", null));
            }
            if (this.genderService.genderExistsAnywhere(gender.getGenderName())) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("This gender already exists in the system.", null));
            }
            Gender savedGender = this.genderService.saveGender(gender);
            return ResponseEntity.ok((Object)new ResponseMessage("Gender created successfully.", (Object)savedGender));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/universal-genders"})
    public ResponseEntity<ResponseMessage> createUniversalGender(@RequestBody Gender gender) {
        try {
            Gender savedGender = this.genderService.saveUniversalGender(gender);
            return ResponseEntity.ok((Object)new ResponseMessage("Universal gender created successfully.", (Object)savedGender));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/genders/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateGender(@PathVariable String schoolId, @PathVariable String id, @RequestBody Gender updatedGender) {
        try {
            Optional existingGender = this.genderService.findGenderByNameAndSchool(updatedGender.getGenderName(), schoolId);
            if (existingGender.isPresent() && !((Gender)existingGender.get()).getId().equals(id)) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Gender already exists for this school.", null));
            }
            Optional result = this.genderService.updateGender(schoolId, id, updatedGender);
            if (result.isPresent()) {
                return ResponseEntity.ok((Object)new ResponseMessage("Gender updated successfully.", result.get()));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Gender not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/universal-genders/{id}"})
    public ResponseEntity<ResponseMessage> updateUniversalGender(@PathVariable String id, @RequestBody Gender updatedGender) {
        try {
            Optional result = this.genderService.updateUniversalGender(id, updatedGender);
            if (result.isPresent()) {
                return ResponseEntity.ok((Object)new ResponseMessage("Universal gender updated successfully.", result.get()));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal gender not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/genders/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteGender(@PathVariable String schoolId, @PathVariable String id) {
        try {
            boolean deleted = this.genderService.deleteGender(schoolId, id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Gender deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Gender not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/universal-genders/{id}"})
    public ResponseEntity<ResponseMessage> deleteUniversalGender(@PathVariable String id) {
        try {
            boolean deleted = this.genderService.deleteUniversalGender(id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Universal gender deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal gender not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }
}

