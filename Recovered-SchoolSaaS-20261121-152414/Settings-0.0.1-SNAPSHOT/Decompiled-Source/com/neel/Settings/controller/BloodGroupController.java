/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.controller.BloodGroupController
 *  com.neel.Settings.model.BloodGroup
 *  com.neel.Settings.model.ResponseMessage
 *  com.neel.Settings.service.BloodGroupService
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

import com.neel.Settings.model.BloodGroup;
import com.neel.Settings.model.ResponseMessage;
import com.neel.Settings.service.BloodGroupService;
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
public class BloodGroupController {
    @Autowired
    private BloodGroupService bloodGroupService;

    @GetMapping(value={"/bloodgroups"})
    public ResponseEntity<ResponseMessage> getAllBloodGroups() {
        List bloodGroups = this.bloodGroupService.getAllBloodGroups();
        return ResponseEntity.ok((Object)new ResponseMessage("Blood groups retrieved successfully.", (Object)bloodGroups));
    }

    @GetMapping(value={"/bloodgroups/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBloodGroupsBySchoolId(@PathVariable String schoolId) {
        List bloodGroups = this.bloodGroupService.getBloodGroupsBySchoolId(schoolId);
        if (bloodGroups.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No blood groups found for this school.", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Blood groups retrieved successfully.", (Object)bloodGroups));
    }

    @PostMapping(value={"/bloodgroups/create-defaults"})
    public ResponseEntity<ResponseMessage> createDefaultBloodGroups() {
        try {
            List defaultBloodGroups = this.bloodGroupService.createDefaultUniversalBloodGroups();
            return ResponseEntity.ok((Object)new ResponseMessage("Default blood groups created successfully for all schools.", (Object)defaultBloodGroups));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Failed to create default blood groups: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/universal-bloodgroups"})
    public ResponseEntity<ResponseMessage> getAllUniversalBloodGroups() {
        List universalBloodGroups = this.bloodGroupService.getAllUniversalBloodGroups();
        if (universalBloodGroups.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No universal blood groups found.", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Universal blood groups retrieved successfully.", (Object)universalBloodGroups));
    }

    @PostMapping(value={"/bloodgroups/{schoolId}"})
    public ResponseEntity<ResponseMessage> createBloodGroup(@PathVariable String schoolId, @RequestBody BloodGroup bloodGroup) {
        try {
            bloodGroup.setSchoolId(schoolId);
            bloodGroup.setUniversal(false);
            if (this.bloodGroupService.findBloodGroupByNameAndTypeAndSchool(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType(), schoolId).isPresent()) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Blood group already exists for this school.", null));
            }
            if (this.bloodGroupService.bloodGroupExistsAnywhere(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType())) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("This blood group already exists in the system.", null));
            }
            BloodGroup savedBloodGroup = this.bloodGroupService.saveBloodGroup(bloodGroup);
            return ResponseEntity.ok((Object)new ResponseMessage("Blood group created successfully.", (Object)savedBloodGroup));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/universal-bloodgroups"})
    public ResponseEntity<ResponseMessage> createUniversalBloodGroup(@RequestBody BloodGroup bloodGroup) {
        try {
            BloodGroup savedBloodGroup = this.bloodGroupService.saveUniversalBloodGroup(bloodGroup);
            return ResponseEntity.ok((Object)new ResponseMessage("Universal blood group created successfully.", (Object)savedBloodGroup));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/bloodgroups/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateBloodGroup(@PathVariable String schoolId, @PathVariable String id, @RequestBody BloodGroup updatedBloodGroup) {
        try {
            Optional existingGroup = this.bloodGroupService.findBloodGroupByNameAndTypeAndSchool(updatedBloodGroup.getBloodGroupName(), updatedBloodGroup.getBloodGroupType(), schoolId);
            if (existingGroup.isPresent() && !((BloodGroup)existingGroup.get()).getId().equals(id)) {
                return ResponseEntity.badRequest().body((Object)new ResponseMessage("Blood group already exists for this school.", null));
            }
            Optional result = this.bloodGroupService.updateBloodGroup(schoolId, id, updatedBloodGroup);
            return result.map(bloodGroup -> ResponseEntity.ok((Object)new ResponseMessage("Blood group updated successfully.", bloodGroup))).orElseGet(() -> ResponseEntity.badRequest().body((Object)new ResponseMessage("Blood group not found.", null)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/universal-bloodgroups/{id}"})
    public ResponseEntity<ResponseMessage> updateUniversalBloodGroup(@PathVariable String id, @RequestBody BloodGroup updatedBloodGroup) {
        try {
            Optional result = this.bloodGroupService.updateUniversalBloodGroup(id, updatedBloodGroup);
            return result.map(bloodGroup -> ResponseEntity.ok((Object)new ResponseMessage("Universal blood group updated successfully.", bloodGroup))).orElseGet(() -> ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal blood group not found.", null)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/bloodgroups/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteBloodGroup(@PathVariable String schoolId, @PathVariable String id) {
        try {
            boolean deleted = this.bloodGroupService.deleteBloodGroup(schoolId, id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Blood group deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Blood group not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/universal-bloodgroups/{id}"})
    public ResponseEntity<ResponseMessage> deleteUniversalBloodGroup(@PathVariable String id) {
        try {
            boolean deleted = this.bloodGroupService.deleteUniversalBloodGroup(id);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Universal blood group deleted successfully.", null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Universal blood group not found.", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/bloodgroups/default-json"})
    public ResponseEntity<String> getDefaultBloodGroupsJsonData() {
        String jsonData = this.bloodGroupService.getDefaultBloodGroupsJsonData();
        return ResponseEntity.ok((Object)jsonData);
    }
}

