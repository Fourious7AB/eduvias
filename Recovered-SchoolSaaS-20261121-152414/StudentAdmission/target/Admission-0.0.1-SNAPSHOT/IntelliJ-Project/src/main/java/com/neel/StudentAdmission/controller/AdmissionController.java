/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.AdmissionController
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AdmissionDto
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.service.AdmissionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.io.FileSystemResource
 *  org.springframework.core.io.Resource
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AdmissionDto;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.service.AdmissionService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping
public class AdmissionController {
    @Autowired
    private AdmissionService admissionService;
    @Autowired
    private AdmissionRepository admissionRepository;

    @GetMapping(value={"/admissions/total-students"})
    public ResponseMessage getTotalStudentCount() {
        try {
            long totalCount = this.admissionService.getTotalStudentCount();
            return new ResponseMessage("Total student count retrieved successfully", (Object)totalCount);
        }
        catch (Exception e) {
            return new ResponseMessage("Error retrieving total student count: " + e.getMessage(), null);
        }
    }

    @GetMapping(value={"/admissions/total-students/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentCountBySchool(@PathVariable String schoolId) {
        try {
            long totalCount = this.admissionService.getStudentCountBySchool(schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Student count for school retrieved successfully", (Object)totalCount));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error retrieving student count: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/admissions/{schoolId}"})
    public ResponseEntity<ResponseModel<Admission>> addAdmissionWithSchool(@PathVariable String schoolId, @ModelAttribute AdmissionDto admissionDto) {
        System.out.println("School id" + schoolId);
        try {
            String filePath = this.admissionService.saveFile(admissionDto.getStudentImage());
            String filePath2 = this.admissionService.saveFile(admissionDto.getJointImage());
            Admission admission = new Admission();
            admission.setStudentName(admissionDto.getStudentName());
            admission.setRollNo(admissionDto.getRollNo());
            admission.setAdmissionDate(admissionDto.getAdmissionDate());
            admission.setClassName(admissionDto.getClassName());
            admission.setSection(admissionDto.getSection());
            admission.setGender(admissionDto.getGender());
            admission.setBloodGroup(admissionDto.getBloodGroup());
            admission.setDob(admissionDto.getDob());
            admission.setReligion(admissionDto.getReligion());
            admission.setFatherName(admissionDto.getFatherName());
            admission.setMotherName(admissionDto.getMotherName());
            admission.setPhone(admissionDto.getPhone());
            admission.setAlternativePhone(admissionDto.getAlternativePhone());
            admission.setEmail(admissionDto.getEmail());
            admission.setPincode(admissionDto.getPincode());
            admission.setCity(admissionDto.getCity());
            admission.setState(admissionDto.getState());
            admission.setDistrict(admissionDto.getDistrict());
            admission.setCountry(admissionDto.getCountry());
            admission.setPoliceStation(admissionDto.getPoliceStation());
            admission.setVillagePost(admissionDto.getVillagePost());
            admission.setStatus(admission.getStatus());
            admission.setStudentImage(filePath);
            admission.setJointImage(filePath2);
            Admission createdAdmission = this.admissionService.createAdmission(admission, schoolId);
            return ResponseEntity.ok((Object)new ResponseModel("Student added successfully!", (Object)createdAdmission));
        }
        catch (IOException e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseModel("Error saving admission: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/validate/{admissionNumber}"})
    public ResponseEntity<Boolean> validateAdmission(@PathVariable String admissionNumber) {
        boolean exists = this.admissionRepository.existsByAdmissionNumber(admissionNumber);
        return new ResponseEntity((Object)exists, (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/admissions/validate/{admissionNumber}/{schoolId}"})
    public ResponseEntity<Boolean> validateAdmissionWithSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        boolean exists = this.admissionRepository.existsByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        return new ResponseEntity((Object)exists, (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/admissions"})
    public ResponseMessage getAllAdmissions() {
        Iterable admissions = this.admissionService.getAllAdmissions();
        return new ResponseMessage("Admissions retrieved successfully", (Object)admissions);
    }

    @GetMapping(value={"/admissions/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAdmissionsBySchoolId(@PathVariable String schoolId) {
        List admissions = this.admissionService.getAdmissionsBySchoolId(schoolId);
        if (admissions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No admissions found for this school", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }

    @GetMapping(value={"/admissions/number/{admissionNumber}"})
    public ResponseMessage getAdmissionByNumber(@PathVariable String admissionNumber) {
        Optional admission = this.admissionService.getAdmissionByNumber(admissionNumber);
        return admission.map(value -> new ResponseMessage("Admission retrieved successfully", value)).orElseGet(() -> new ResponseMessage("Admission not found with admission number: " + admissionNumber, null));
    }

    @GetMapping(value={"/admissions/number/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAdmissionByNumberAndSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        Optional admission = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
        if (admission.isPresent()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Admission retrieved successfully", admission.get()));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
    }

    @PutMapping(value={"/admissions/number/{admissionNumber}"})
    public ResponseMessage updateAdmissionByNumber(@PathVariable String admissionNumber, @RequestBody Admission updatedAdmission) {
        try {
            Admission admission = this.admissionService.updateAdmissionByNumber(admissionNumber, updatedAdmission);
            return new ResponseMessage("Admission updated successfully", (Object)admission);
        }
        catch (Exception e) {
            return new ResponseMessage(e.getMessage(), null);
        }
    }

    @PutMapping(value={"/admissions/number/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateAdmissionByNumberAndSchool(@PathVariable String schoolId, @PathVariable String admissionNumber, @RequestBody Admission updatedAdmission) {
        try {
            Admission admission = this.admissionService.updateAdmissionByNumberAndSchool(admissionNumber, schoolId, updatedAdmission);
            return ResponseEntity.ok((Object)new ResponseMessage("Admission updated successfully", (Object)admission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/admissions/number/{admissionNumber}"})
    public ResponseMessage deleteAdmissionByNumber(@PathVariable String admissionNumber) {
        try {
            this.admissionService.deleteAdmissionByNumber(admissionNumber);
            return new ResponseMessage("Admission deleted successfully", null);
        }
        catch (Exception e) {
            return new ResponseMessage(e.getMessage(), null);
        }
    }

    @DeleteMapping(value={"/admissions/number/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteAdmissionByNumberAndSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        boolean isDeleted = this.admissionService.deleteAdmissionByNumberAndSchool(admissionNumber, schoolId);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Admission deleted successfully", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
    }

    @GetMapping(value={"/admissions/generate-id-card/{admissionNumber}"})
    public ResponseEntity<Resource> generateAndDownloadIDCard(@PathVariable String admissionNumber) {
        String filePath = this.admissionService.generateAndSaveIDCard(admissionNumber);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Failed to generate ID card.");
        }
        FileSystemResource resource = new FileSystemResource(file);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body((Object)resource);
    }

    @GetMapping(value={"/admissions/generate-id-card/{admissionNumber}/{schoolId}"})
    public ResponseEntity<?> generateAndDownloadIDCardWithSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        try {
            String filePath = this.admissionService.generateAndSaveIDCardWithSchool(admissionNumber, schoolId);
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Failed to generate ID card: File not found after generation", null));
            }
            FileSystemResource resource = new FileSystemResource(file);
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body((Object)resource);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error generating ID card: " + e.getMessage(), null));
        }
    }

    @PutMapping(value={"/admissions/updateStatus"})
    public ResponseEntity<ResponseMessage> updateAdmissionStatus(@RequestBody Map<String, String> request) {
        String admissionNumber = request.get("admissionNumber");
        String status = request.get("status");
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumber(admissionNumber);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            admission.setStatus(status);
            this.admissionRepository.save((Object)admission);
            return ResponseEntity.ok((Object)new ResponseMessage("Admission status updated successfully", (Object)admission));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Error: Admission not found", null));
    }

    @PutMapping(value={"/admissions/updateStatus/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateAdmissionStatusWithSchool(@PathVariable String schoolId, @RequestBody Map<String, String> request) {
        System.out.println("from api" + schoolId);
        System.out.println("request" + request);
        String admissionNumber = request.get("admissionNumber");
        System.out.println(admissionNumber);
        String status = request.get("status");
        System.out.println(status);
        try {
            Admission updatedAdmission = this.admissionService.updateAdmissionStatus(admissionNumber, schoolId, status);
            return ResponseEntity.ok((Object)new ResponseMessage("Admission status updated successfully", (Object)updatedAdmission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage> getAdmissionsByClassAndSection(@PathVariable String className, @PathVariable String section) {
        List admissions = this.admissionRepository.findByClassNameAndSection(className, section);
        if (admissions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No admissions found for this class and section", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }

    @GetMapping(value={"/{schoolId}/admissions/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage> getAdmissionsByClassSectionAndSchool(@PathVariable String schoolId, @PathVariable String className, @PathVariable String section) {
        List admissions = this.admissionRepository.findByClassNameAndSectionAndSchoolId(className, section, schoolId);
        if (admissions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No admissions found for this class, section and school", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }
}

