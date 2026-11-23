/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.AdmissionController
 *  com.neel.StudentAdmission.dto.BulkAdmissionResponse
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AdmissionDto
 *  com.neel.StudentAdmission.model.Plan$Features
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.model.Services
 *  com.neel.StudentAdmission.model.Subjects
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.repo.SignUpRepository
 *  com.neel.StudentAdmission.service.AdmissionService
 *  com.neel.StudentAdmission.service.BulkAdmissionService
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.client.HttpClientErrorException
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.dto.BulkAdmissionResponse;
import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AdmissionDto;
import com.neel.StudentAdmission.model.Plan;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.model.Services;
import com.neel.StudentAdmission.model.Subjects;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.SchoolRepository;
import com.neel.StudentAdmission.repo.SignUpRepository;
import com.neel.StudentAdmission.service.AdmissionService;
import com.neel.StudentAdmission.service.BulkAdmissionService;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping
public class AdmissionController {
    @Autowired
    private AdmissionService admissionService;
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private BulkAdmissionService bulkAdmissionService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SignUpRepository signUpRepository;

    @GetMapping(value={"/total-students"})
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
            return ResponseEntity.ok(new ResponseMessage("Student count for school retrieved successfully", (Object)totalCount));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error retrieving student count: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/admissions/{schoolId}"})
    public ResponseEntity<ResponseModel<Admission>> addAdmissionWithSchool(@PathVariable String schoolId, @ModelAttribute AdmissionDto admissionDto) {
        System.out.println("School id : " + schoolId + " admissionDto : " + admissionDto);
        try {
            Optional alreadyExist = this.admissionService.viewAlreadyExistAdmission(admissionDto.getRollNo(), admissionDto.getClassName(), admissionDto.getSection(), schoolId);
            if (alreadyExist.isPresent()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ResponseModel(Boolean.valueOf(true), "Student already exists in this" + admissionDto.getRollNo() + " " + admissionDto.getClassName() + " And " + admissionDto.getSection(), null));
            }
            Optional existStudentInCurrentSession = this.admissionService.getAdmissionDetailsBySession(admissionDto.getAcademicYear(), schoolId, admissionDto.getEmail());
            if (existStudentInCurrentSession.isPresent()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ResponseModel(Boolean.valueOf(false), "Student already exists in current session", null));
            }
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
            if (admissionDto.getAcademicYear() != null && !admissionDto.getAcademicYear().isEmpty()) {
                admission.setAcademicYear(admissionDto.getAcademicYear());
            }
            if (admissionDto.getServices() != null && !admissionDto.getServices().isEmpty()) {
                admission.setServices(admissionDto.getServices());
            }
            if (admissionDto.getSubject() != null && !admissionDto.getSubject().isEmpty()) {
                admission.setSubject(admissionDto.getSubject());
            }
            Admission createdAdmission = this.admissionService.createAdmission(admission, schoolId);
            try {
                String idCardPath = this.admissionService.generateAndSaveIDCardWithSchool(createdAdmission.getAdmissionNumber(), schoolId);
                System.out.println("ID card path: " + idCardPath);
                createdAdmission.setIdCardPath(idCardPath);
                this.admissionRepository.save(createdAdmission);
            }
            catch (Exception e) {
                System.err.println("Error generating ID card: " + e.getMessage());
                e.printStackTrace();
            }
            return ResponseEntity.ok(new ResponseModel(Boolean.valueOf(true), "Student added successfully!", (Object)createdAdmission));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status((int)500).body(new ResponseModel(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @PutMapping(value={"/admissions/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseModel<Admission>> updateAdmissionWithSchool(@PathVariable String schoolId, @PathVariable String admissionNumber, @ModelAttribute AdmissionDto admissionDto) {
        try {
            Optional clash;
            Optional existingOpt = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
            if (existingOpt.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseModel(Boolean.valueOf(false), "No admission found with number " + admissionNumber + " for school " + schoolId, null));
            }
            Admission admission = (Admission)existingOpt.get();
            if (!(admission.getRollNo().equals(admissionDto.getRollNo()) && admission.getClassName().equals(admissionDto.getClassName()) && admission.getSection().equals(admissionDto.getSection()) || !(clash = this.admissionService.viewAlreadyExistAdmission(admissionDto.getRollNo(), admissionDto.getClassName(), admissionDto.getSection(), schoolId)).isPresent() || ((Admission)clash.get()).getAdmissionNumber().equals(admissionNumber))) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ResponseModel(Boolean.valueOf(false), "Another student with roll " + admissionDto.getRollNo() + " in class " + admissionDto.getClassName() + " section " + admissionDto.getSection() + " already exists.", null));
            }
            if (admissionDto.getStudentName() != null) {
                admission.setStudentName(admissionDto.getStudentName());
            }
            if (admissionDto.getStudentName() != null) {
                admission.setStudentName(admissionDto.getStudentName());
            }
            if (admissionDto.getRollNo() != null) {
                admission.setRollNo(admissionDto.getRollNo());
            }
            if (admissionDto.getAdmissionDate() != null) {
                admission.setAdmissionDate(admissionDto.getAdmissionDate());
            }
            if (admissionDto.getClassName() != null) {
                admission.setClassName(admissionDto.getClassName());
            }
            if (admissionDto.getSection() != null) {
                admission.setSection(admissionDto.getSection());
            }
            if (admissionDto.getGender() != null) {
                admission.setGender(admissionDto.getGender());
            }
            if (admissionDto.getBloodGroup() != null) {
                admission.setBloodGroup(admissionDto.getBloodGroup());
            }
            if (admissionDto.getDob() != null) {
                admission.setDob(admissionDto.getDob());
            }
            if (admissionDto.getReligion() != null) {
                admission.setReligion(admissionDto.getReligion());
            }
            if (admissionDto.getFatherName() != null) {
                admission.setFatherName(admissionDto.getFatherName());
            }
            if (admissionDto.getMotherName() != null) {
                admission.setMotherName(admissionDto.getMotherName());
            }
            if (admissionDto.getPhone() != null) {
                admission.setPhone(admissionDto.getPhone());
            }
            if (admissionDto.getAlternativePhone() != null) {
                admission.setAlternativePhone(admissionDto.getAlternativePhone());
            }
            if (admissionDto.getEmail() != null) {
                admission.setEmail(admissionDto.getEmail());
            }
            if (admissionDto.getPincode() != null) {
                admission.setPincode(admissionDto.getPincode());
            }
            if (admissionDto.getCity() != null) {
                admission.setCity(admissionDto.getCity());
            }
            if (admissionDto.getState() != null) {
                admission.setState(admissionDto.getState());
            }
            if (admissionDto.getDistrict() != null) {
                admission.setDistrict(admissionDto.getDistrict());
            }
            if (admissionDto.getCountry() != null) {
                admission.setCountry(admissionDto.getCountry());
            }
            if (admissionDto.getPoliceStation() != null) {
                admission.setPoliceStation(admissionDto.getPoliceStation());
            }
            if (admissionDto.getVillagePost() != null) {
                admission.setVillagePost(admissionDto.getVillagePost());
            }
            if (admissionDto.getAcademicYear() != null && !admissionDto.getAcademicYear().isEmpty()) {
                admission.setAcademicYear(admissionDto.getAcademicYear());
            }
            if (admissionDto.getServices() != null) {
                admission.setServices(admissionDto.getServices());
            }
            if (admissionDto.getSubject() != null) {
                admission.setSubject(admissionDto.getSubject());
            }
            if (admissionDto.getStudentImage() != null && !admissionDto.getStudentImage().isEmpty()) {
                String newPath = this.admissionService.saveFile(admissionDto.getStudentImage());
                admission.setStudentImage(newPath);
            }
            if (admissionDto.getJointImage() != null && !admissionDto.getJointImage().isEmpty()) {
                String newPath2 = this.admissionService.saveFile(admissionDto.getJointImage());
                admission.setJointImage(newPath2);
            }
            Admission updated = this.admissionService.updateAdmission(admission, schoolId);
            this.admissionService.updateStudentFee(admissionNumber, schoolId, admissionDto.getAcademicYear(), admissionDto.getClassName(), admissionDto.getSection(), admissionDto.getStudentName());
            try {
                String idCardPath = this.admissionService.generateAndSaveIDCardWithSchool(updated.getAdmissionNumber(), schoolId);
                updated.setIdCardPath(idCardPath);
                this.admissionRepository.save(updated);
            }
            catch (Exception e) {
                System.err.println("Error regenerating ID card: " + e.getMessage());
                e.printStackTrace();
            }
            return ResponseEntity.ok(new ResponseModel(Boolean.valueOf(true), "Admission updated successfully", (Object)updated));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(Boolean.valueOf(false), e.getMessage(), null));
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
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("No admissions found for this school", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }

    @GetMapping(value={"/admissions/number/{admissionNumber}"})
    public ResponseMessage getAdmissionByNumber(@PathVariable String admissionNumber) {
        Optional<Admission> admission = this.admissionService.getAdmissionByNumber(admissionNumber);
        return admission.map(value -> new ResponseMessage("Admission retrieved successfully", value)).orElseGet(() -> new ResponseMessage("Admission not found with admission number: " + admissionNumber, null));
    }

    @GetMapping(value={"/admissions/check-session/{admissionNumber}"})
    public ResponseEntity<ResponseMessage> checkStudentSession(@PathVariable String admissionNumber) {
        try {
            Optional existAdmission = this.admissionService.getAdmissionByNumber(admissionNumber);
            Admission admissions = (Admission)existAdmission.get();
            Optional admission = this.admissionService.getAdmissionByNumberAndSession(admissionNumber, admissions.getAcademicYear());
            System.out.println(" i m from add,ission " + admission);
            if (admission.isPresent()) {
                boolean isInSession = admissions.getAcademicYear().equals(((Admission)admission.get()).getAcademicYear());
                if (isInSession) {
                    return ResponseEntity.ok(new ResponseMessage("Student is enrolled in the specified session", (Object)admission));
                }
                return ResponseEntity.ok(new ResponseMessage("Student is not enrolled in the specified session", (Object)false));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error checking session: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/number/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAdmissionByNumberAndSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        Optional admission = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
        if (admission.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("Admission retrieved successfully", admission.get()));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
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
            return ResponseEntity.ok(new ResponseMessage("Admission updated successfully", (Object)admission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage(), null));
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
            return ResponseEntity.ok(new ResponseMessage("Admission deleted successfully", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
    }

    @GetMapping(value={"/admissions/generate-id-card/{admissionNumber}"})
    public ResponseEntity<Resource> generateAndDownloadIDCard(@PathVariable String admissionNumber) {
        String filePath = this.admissionService.generateAndSaveIDCard(admissionNumber);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Failed to generate ID card.");
        }
        FileSystemResource resource = new FileSystemResource(file);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body(resource);
    }

    @GetMapping(value={"/admissions/generate-id-card/{admissionNumber}/{schoolId}"})
    public ResponseEntity<?> generateAndDownloadIDCardWithSchool(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        try {
            String filePath = this.admissionService.generateAndSaveIDCardWithSchool(admissionNumber, schoolId);
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Failed to generate ID card: File not found after generation", null));
            }
            FileSystemResource resource = new FileSystemResource(file);
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body(resource);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error generating ID card: " + e.getMessage(), null));
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
            this.admissionRepository.save(admission);
            return ResponseEntity.ok(new ResponseMessage("Admission status updated successfully", (Object)admission));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error: Admission not found", null));
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
            return ResponseEntity.ok(new ResponseMessage("Admission status updated successfully", (Object)updatedAdmission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage> getAdmissionsByClassAndSection(@PathVariable String className, @PathVariable String section) {
        List admissions = this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCase(className, section);
        if (admissions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("No admissions found for this class and section", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }

    @GetMapping(value={"/{schoolId}/admissions/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage> getAdmissionsByClassSectionAndSchool(@PathVariable String schoolId, @PathVariable String className, @PathVariable String section) {
        List admissions = this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(className, section, schoolId);
        if (admissions.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("No admissions found for this class, section and school", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Admissions retrieved successfully", (Object)admissions));
    }

    @PostMapping(value={"/admissions/{admissionNumber}/subjects/{schoolId}"})
    public ResponseEntity<ResponseMessage> addSubjectsToStudent(@PathVariable String schoolId, @PathVariable String admissionNumber, @RequestBody List<Subjects> subjects) {
        try {
            Optional admissionOpt = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
            if (admissionOpt.isPresent()) {
                Admission admission = (Admission)admissionOpt.get();
                if (admission.getSubject() == null) {
                    admission.setSubject(new ArrayList());
                }
                admission.getSubject().addAll(subjects);
                Admission updatedAdmission = this.admissionService.updateAdmissionByNumberAndSchool(admissionNumber, schoolId, admission);
                return ResponseEntity.ok(new ResponseMessage("Subjects added successfully to student", (Object)updatedAdmission));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/{admissionNumber}/subjects/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentSubjects(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        try {
            Optional admissionOpt = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
            if (admissionOpt.isPresent()) {
                Admission admission = (Admission)admissionOpt.get();
                List subjects = admission.getSubject() != null ? admission.getSubject() : new ArrayList();
                return ResponseEntity.ok(new ResponseMessage("Subjects retrieved successfully", (Object)subjects));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/admissions/{admissionNumber}/subjects/{schoolId}/{subjectName}"})
    public ResponseEntity<ResponseMessage> removeSubjectFromStudent(@PathVariable String schoolId, @PathVariable String admissionNumber, @PathVariable String subjectName) {
        try {
            Optional admissionOpt = this.admissionService.getAdmissionByNumberAndSchool(admissionNumber, schoolId);
            if (admissionOpt.isPresent()) {
                Admission admission = (Admission)admissionOpt.get();
                if (admission.getSubject() != null) {
                    admission.getSubject().removeIf(subject -> subject.getSubjectName().equals(subjectName));
                    Admission updatedAdmission = this.admissionService.updateAdmissionByNumberAndSchool(admissionNumber, schoolId, admission);
                    return ResponseEntity.ok(new ResponseMessage("Subject removed successfully from student", (Object)updatedAdmission));
                }
                return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ResponseMessage("No subjects found for this student", null));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Admission not found with number: " + admissionNumber + " for this school", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/admissions/{admissionNumber}/services/{schoolId}"})
    public ResponseEntity<ResponseMessage> addServicesToStudent(@PathVariable String schoolId, @PathVariable String admissionNumber, @RequestBody List<Services> services) {
        try {
            Admission updatedAdmission = this.admissionService.addServicesToStudent(admissionNumber, schoolId, services);
            return ResponseEntity.ok(new ResponseMessage("Services added successfully to student", (Object)updatedAdmission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/{admissionNumber}/services/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentServices(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        try {
            List services = this.admissionService.getStudentServices(admissionNumber, schoolId);
            return ResponseEntity.ok(new ResponseMessage("Services retrieved successfully", (Object)services));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/admissions/{admissionNumber}/services/{schoolId}/{serviceType}"})
    public ResponseEntity<ResponseMessage> removeServiceFromStudent(@PathVariable String schoolId, @PathVariable String admissionNumber, @PathVariable String serviceType) {
        try {
            Admission updatedAdmission = this.admissionService.removeServiceFromStudent(admissionNumber, schoolId, serviceType);
            return ResponseEntity.ok(new ResponseMessage("Service removed successfully from student", (Object)updatedAdmission));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/admissions/{admissionNumber}/promote/{schoolId}"})
    public ResponseEntity<ResponseMessage> promoteStudent(@PathVariable String schoolId, @PathVariable String admissionNumber, @RequestBody Map<String, Object> promotionDetails) {
        try {
            String newClassName = (String)promotionDetails.get("newClassName");
            String newSection = (String)promotionDetails.get("newSection");
            String newSession = (String)promotionDetails.get("newSession");
            Boolean includePreviousDue = promotionDetails.get("includePreviousDue") != null ? (Boolean)promotionDetails.get("includePreviousDue") : false;
            if (newClassName == null || newSession == null) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ResponseMessage("New class name and session are required", null));
            }
            Admission promotedStudent = this.admissionService.promoteStudent(admissionNumber, schoolId, newClassName, newSection, newSession, includePreviousDue.booleanValue());
            return ResponseEntity.ok(new ResponseMessage("Student promoted successfully", (Object)promotedStudent));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/admissions/promote-bulk/{schoolId}"})
    public ResponseEntity<ResponseMessage> promoteStudentsInBulk(@PathVariable String schoolId, @RequestBody Map<String, Object> promotionDetails) {
        try {
            String currentClassName = (String)promotionDetails.get("currentClassName");
            String currentSection = (String)promotionDetails.get("currentSection");
            String currentSession = (String)promotionDetails.get("currentSession");
            String newClassName = (String)promotionDetails.get("newClassName");
            String newSection = (String)promotionDetails.get("newSection");
            String newSession = (String)promotionDetails.get("newSession");
            Boolean includePreviousDue = promotionDetails.get("includePreviousDue") != null ? (Boolean)promotionDetails.get("includePreviousDue") : false;
            if (currentClassName == null || newClassName == null || currentSession == null || newSession == null) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ResponseMessage("Current and new class names and sessions are required", null));
            }
            List admissionNumbers = null;
            if (promotionDetails.containsKey("admissionNumbers")) {
                admissionNumbers = (List)promotionDetails.get("admissionNumbers");
            }
            Map promotedStudents = this.admissionService.promoteStudentsInBulk(schoolId, currentClassName, currentSection, currentSession, newClassName, newSection, newSession, includePreviousDue, admissionNumbers);
            return ResponseEntity.ok(new ResponseMessage("Students promoted successfully.", (Object)promotedStudents));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error promoting students: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/admissions/{admissionNumber}/history/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentAcademicHistory(@PathVariable String schoolId, @PathVariable String admissionNumber) {
        try {
            List academicHistory = this.admissionService.getStudentAcademicHistory(admissionNumber, schoolId);
            return ResponseEntity.ok(new ResponseMessage("Academic history retrieved successfully", (Object)academicHistory));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Error retrieving academic history: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getPreviousAdmissionHistory(String admissionNumber, String schoolId) {
        List allHistory = this.admissionService.getStudentPromotionHistory(admissionNumber, schoolId);
        if (allHistory.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("No previous admission history found for this student", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Previous admission history retrieved successfully", (Object)allHistory));
    }

    @GetMapping(value={"/promotion-history/{admissionNumber}/{schoolId}"})
    public ResponseEntity<Map<String, Object>> getStudentPromotionHistory(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestParam String academicYear) {
        try {
            List history = academicYear != null && !academicYear.isEmpty() ? this.admissionService.getStudentPromotionHistoryByYear(admissionNumber, schoolId, academicYear) : this.admissionService.getStudentPromotionHistory(admissionNumber, schoolId);
            HashMap<String, Object> response = new HashMap<String, Object>();
            response.put("data", history);
            response.put("success", true);
            response.put("message", "Student promotion history retrieved successfully");
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            HashMap<String, Object> errorResponse = new HashMap<String, Object>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving promotion history: " + e.getMessage());
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value={"/admissions/bulk/{schoolId}"})
    public ResponseEntity<?> bulkUploadAdmissions(@PathVariable String schoolId, @RequestParam(value="file") MultipartFile file) {
        try {
            BulkAdmissionResponse admissionResponse = this.bulkAdmissionService.processExcelAdmissions(file, schoolId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Bulk upload successful", "data", admissionResponse));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(("Failed to upload: " + e.getMessage()));
        }
    }

    @GetMapping(value={"/admissions/generate-class-id-cards/{schoolId}/{className}/{section}"})
    public ResponseEntity<?> generateAndDownloadClassIDCards(@PathVariable String schoolId, @PathVariable String className, @PathVariable String section, @RequestParam(required=false) String academicYear) {
        try {
            String zipFilePath = this.admissionService.generateAndSaveBulkIDCardsByClass(className, section, schoolId, academicYear);
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Failed to generate class ID cards: ZIP file not found", null));
            }
            FileSystemResource resource = new FileSystemResource(zipFile);
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", new String[]{"attachment; filename=\"" + zipFile.getName() + "\""})).body(resource);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error generating class ID cards: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/social/{id}/{schoolId}"})
    public ResponseEntity<Map<String, Object>> getStudentDetails(@PathVariable String schoolId, @PathVariable String id) {
        Optional admissionOptional = this.admissionService.getAllAdmissionBySchoolIdAndStudentId(id, schoolId);
        if (admissionOptional.isEmpty()) {
            HashMap<String, Object> errorResponse = new HashMap<String, Object>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Student not found");
            errorResponse.put("data", null);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(errorResponse);
        }
        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("user", admissionOptional.get());
        request.put("role", "student");
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("success", true);
        response.put("message", "Student fetched successfully");
        response.put("data", request);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(response);
    }

    private int extractStudentLimit(List<Plan.Features> features) {
        for (Plan.Features feature : features) {
            String[] words;
            String desc = feature.getDescription().toLowerCase();
            if (!desc.contains("upto") || !desc.contains("students")) continue;
            for (String word : words = desc.split(" ")) {
                try {
                    System.out.println(" i m in word : " + Integer.parseInt(word));
                    return Integer.parseInt(word);
                }
                catch (NumberFormatException numberFormatException) {
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    @GetMapping(value={"/admissions/student-details"})
    public ResponseEntity<ResponseMessage> getStudentDetails(@RequestParam String rollNo, @RequestParam String className, @RequestParam String section, @RequestParam String schoolId) {
        Optional<Admission> admissionOptional = this.admissionRepository.findByRollNoAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(rollNo, className, section, schoolId);
        return admissionOptional.map(admission -> ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ResponseMessage("Student fetched successfully", admission))).orElseGet(() -> ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ResponseMessage("Student not found", null)));
    }
}

