/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.SchoolController
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.model.SchoolDTO
 *  com.neel.StudentAdmission.repo.AdmissionRepository
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
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.model.SchoolDTO;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.service.SchoolService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/schools"})
public class SchoolController {
    private final SchoolService schoolService;
    private final AdmissionRepository admissionRepository;

    @Autowired
    public SchoolController(SchoolService schoolService, AdmissionRepository admissionRepository) {
        this.schoolService = schoolService;
        this.admissionRepository = admissionRepository;
    }

    @GetMapping(value={"/student-and-school-count"})
    public ResponseEntity<ResponseModel<Map<String, Long>>> getStudentAndSchoolCount() {
        HashMap<String, Long> counts = new HashMap<String, Long>();
        counts.put("totalSchools", this.schoolService.getSchoolCount());
        counts.put("totalStudents", this.admissionRepository.count());
        ResponseModel responseModel = new ResponseModel(Boolean.valueOf(true), "All counts retrieved successfully!", counts);
        return ResponseEntity.ok((Object)responseModel);
    }

    @PostMapping(value={"/{schoolId}"}, consumes={"multipart/form-data"})
    public ResponseEntity<ResponseModel<School>> addSchool(@PathVariable String schoolId, @ModelAttribute SchoolDTO schoolDTO) {
        try {
            Optional existSchoolById = this.schoolService.getBySchoolId(schoolId);
            if (existSchoolById.isPresent()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseModel(Boolean.valueOf(false), "Your school already exists. Please update the existing school record.", null));
            }
            Optional existSchool = this.schoolService.getBySchoolEmail(schoolDTO.getSchoolEmail());
            if (existSchool.isPresent()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseModel(Boolean.valueOf(false), "school already exist with this school email : " + schoolDTO.getSchoolEmail(), null));
            }
            String filePath = this.schoolService.saveFile(schoolDTO.getSchoolLogo());
            String filePath2 = this.schoolService.saveFile(schoolDTO.getSchoolImage());
            School school = new School();
            school.setSchoolId(schoolId);
            school.setSchoolName(schoolDTO.getSchoolName());
            school.setSchoolCode(schoolDTO.getSchoolCode());
            school.setSchoolWebsite(schoolDTO.getSchoolWebsite());
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
            return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "School added successfully!", (Object)savedSchool));
        }
        catch (IOException e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseModel(Boolean.valueOf(false), "Error saving school: " + e.getMessage(), null));
        }
    }

    @PutMapping(value={"/{schoolId}"}, consumes={"multipart/form-data"})
    public ResponseEntity<ResponseModel<School>> updateSchool(@PathVariable String schoolId, @ModelAttribute SchoolDTO schoolDTO) {
        try {
            Optional existingOpt = this.schoolService.getBySchoolId(schoolId);
            if (existingOpt.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseModel(Boolean.valueOf(false), "No school found with ID " + schoolId, null));
            }
            School school = (School)existingOpt.get();
            if (schoolDTO.getSchoolEmail() != null && !schoolDTO.getSchoolEmail().equals(school.getSchoolEmail())) {
                Optional byEmail = this.schoolService.getBySchoolEmail(schoolDTO.getSchoolEmail());
                if (byEmail.isPresent()) {
                    return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseModel(Boolean.valueOf(false), "Another school already exists with email " + schoolDTO.getSchoolEmail(), null));
                }
                school.setSchoolEmail(schoolDTO.getSchoolEmail());
            }
            if (schoolDTO.getSchoolLogo() != null && !schoolDTO.getSchoolLogo().isEmpty()) {
                String logoPath = this.schoolService.saveFile(schoolDTO.getSchoolLogo());
                school.setSchoolLogo(logoPath);
            }
            if (schoolDTO.getSchoolImage() != null && !schoolDTO.getSchoolImage().isEmpty()) {
                String imgPath = this.schoolService.saveFile(schoolDTO.getSchoolImage());
                school.setSchoolImage(imgPath);
            }
            if (schoolDTO.getSchoolName() != null) {
                school.setSchoolName(schoolDTO.getSchoolName());
            }
            if (schoolDTO.getSchoolCode() != null) {
                school.setSchoolCode(schoolDTO.getSchoolCode());
            }
            if (schoolDTO.getSchoolRegistrationNumber() != null) {
                school.setSchoolRegistrationNumber(schoolDTO.getSchoolRegistrationNumber());
            }
            if (schoolDTO.getSchoolAddress() != null) {
                school.setSchoolAddress(schoolDTO.getSchoolAddress());
            }
            if (schoolDTO.getSchoolWebsite() != null) {
                school.setSchoolWebsite(schoolDTO.getSchoolWebsite());
            }
            if (schoolDTO.getSchoolPhone() != null) {
                school.setSchoolPhone(schoolDTO.getSchoolPhone());
            }
            if (schoolDTO.getSchoolDescription() != null) {
                school.setSchoolDescription(schoolDTO.getSchoolDescription());
            }
            if (schoolDTO.getSchoolPrincipalName() != null) {
                school.setSchoolPrincipalName(schoolDTO.getSchoolPrincipalName());
            }
            if (schoolDTO.getSchoolPrincipalPhone() != null) {
                school.setSchoolPrincipalPhone(schoolDTO.getSchoolPrincipalPhone());
            }
            if (schoolDTO.getSchoolPrincipalEmail() != null) {
                school.setSchoolPrincipalEmail(schoolDTO.getSchoolPrincipalEmail());
            }
            School updated = this.schoolService.saveSchool(school);
            return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "School updated successfully!", (Object)updated));
        }
        catch (IOException ioe) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseModel(Boolean.valueOf(false), "Error saving files: " + ioe.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseModel(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel<School>> getSchoolById(@PathVariable String id) {
        School school = this.schoolService.getSchoolById(id);
        if (school != null) {
            return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "School details found!", (Object)school));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseModel(Boolean.valueOf(false), "School not found!", null));
    }

    @GetMapping
    public ResponseEntity<ResponseModel<List<School>>> getAllSchools() {
        List schools = this.schoolService.getAllSchools();
        return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "Schools retrieved successfully!", (Object)schools));
    }

    @GetMapping(value={"/sc/{schoolId}"})
    public ResponseEntity<ResponseModel<School>> gets(@PathVariable String schoolId) {
        Optional school = this.schoolService.getSchool(schoolId);
        if (school.isPresent()) {
            return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "School found successfully!", (Object)((School)school.get())));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseModel(Boolean.valueOf(false), "School not found with ID: " + schoolId, null));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel<Void>> deleteSchool(@PathVariable String id) {
        boolean isDeleted = this.schoolService.deleteSchool(id);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseModel(Boolean.valueOf(true), "School deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseModel(Boolean.valueOf(false), "School not found!", null));
    }
}

