/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.SchoolController
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.model.SchoolDTO
 *  com.neel.StudentAdmission.service.SchoolService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.model.SchoolDTO;
import com.neel.StudentAdmission.service.SchoolService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/schools"})
public class SchoolController {
    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping(value={"/{schoolId}"}, consumes={"multipart/form-data"})
    public ResponseEntity<ResponseModel<School>> addSchool(@PathVariable String schoolId, @ModelAttribute SchoolDTO schoolDTO) {
        try {
            String filePath = this.schoolService.saveFile(schoolDTO.getSchoolLogo());
            String filePath2 = this.schoolService.saveFile(schoolDTO.getSchoolImage());
            School school = new School();
            school.setSchoolId(schoolId);
            school.setSchoolName(schoolDTO.getSchoolName());
            school.setSchoolCode(schoolDTO.getSchoolCode());
            school.setSchoolRegistrationNumber(schoolDTO.getSchoolRegistrationNumber());
            school.setSchoolAddress(schoolDTO.getSchoolAddress());
            school.setSchoolEmail(schoolDTO.getSchoolEmail());
            school.setSchoolPhone(schoolDTO.getSchoolPhone());
            school.setSchoolDescription(schoolDTO.getSchoolDescription());
            school.setSchoolPrincipalName(schoolDTO.getSchoolPrincipalName());
            school.setSchoolPrincipalPhone(schoolDTO.getSchoolPrincipalPhone());
            school.setSchoolPrincipalEmail(schoolDTO.getSchoolPrincipalEmail());
            school.setSchoolLogo(filePath);
            school.setSchoolImage(filePath2);
            School savedSchool = this.schoolService.saveSchool(school);
            return ResponseEntity.ok((Object)new ResponseModel("School added successfully!", (Object)savedSchool));
        }
        catch (IOException e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseModel("Error saving school: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel<School>> getSchoolById(@PathVariable String id) {
        School school = this.schoolService.getSchoolById(id);
        if (school != null) {
            return ResponseEntity.ok((Object)new ResponseModel("School details found!", (Object)school));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseModel("School not found!", null));
    }

    @GetMapping
    public ResponseEntity<ResponseModel<List<School>>> getAllSchools() {
        List schools = this.schoolService.getAllSchools();
        return ResponseEntity.ok((Object)new ResponseModel("Schools retrieved successfully!", (Object)schools));
    }

    @GetMapping(value={"/sc/{schoolId}"})
    public ResponseEntity<ResponseModel<School>> gets(@PathVariable String schoolId) {
        Optional school = this.schoolService.getSchool(schoolId);
        if (school.isPresent()) {
            return ResponseEntity.ok((Object)new ResponseModel("School found successfully!", (Object)((School)school.get())));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseModel("School not found with ID: " + schoolId, null));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel<Void>> deleteSchool(@PathVariable String id) {
        boolean isDeleted = this.schoolService.deleteSchool(id);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseModel("School deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseModel("School not found!", null));
    }
}

