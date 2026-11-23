/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.EdpController
 *  com.neel.Teacher.model.Edp
 *  com.neel.Teacher.model.EdpDto
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.model.ResponseModel
 *  com.neel.Teacher.repo.LibrarianRepo
 *  com.neel.Teacher.repo.TeachersRepository
 *  com.neel.Teacher.service.EdpService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
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
 *  org.springframework.web.client.HttpClientErrorException
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.Edp;
import com.neel.Teacher.model.EdpDto;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.model.ResponseModel;
import com.neel.Teacher.repo.LibrarianRepo;
import com.neel.Teacher.repo.TeachersRepository;
import com.neel.Teacher.service.EdpService;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class EdpController {
    private final EdpService edpService;
    private final TeachersRepository teachersRepository;
    private final LibrarianRepo librarianRepo;
    @Value(value="${view.subscription.plan.url}")
    private String subscriptionPlanUrl;

    @Autowired
    public EdpController(EdpService edpService, TeachersRepository teachersRepository, LibrarianRepo librarianRepo) {
        this.edpService = edpService;
        this.teachersRepository = teachersRepository;
        this.librarianRepo = librarianRepo;
    }

    @GetMapping(value={"/edp"})
    public ResponseEntity<ResponseMessage<List<Edp>>> getAllTeachers() {
        List teachers = this.edpService.getAllTeachers();
        return ResponseEntity.ok(new ResponseMessage("EDP retrieved successfully!", teachers));
    }

    @GetMapping(value={"/edp-teacher-lib/count"})
    public ResponseEntity<ResponseMessage> getAllEDPAndTeacherAndLibrarian() {
        HashMap<String, Long> counts = new HashMap<String, Long>();
        counts.put("totalEdp", this.edpService.getAllEdpCount());
        counts.put("totalTeacher", this.teachersRepository.count());
        counts.put("totalLibrarian", this.librarianRepo.count());
        return ResponseEntity.ok(new ResponseMessage("EDP and Teacher and Librarian retrieved successfully!", counts));
    }

    @GetMapping(value={"/edp/{schoolId}"})
    public ResponseEntity<ResponseMessage<List<Edp>>> getTeachersBySchoolId(@PathVariable String schoolId) {
        List teachers = this.edpService.getTeachersBySchoolId(schoolId);
        if (teachers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("No EDPs found for this school!", null));
        }
        return ResponseEntity.ok(new ResponseMessage("EDPs retrieved successfully!", teachers));
    }

    @PostMapping(value={"/edp/{schoolId}"})
    public ResponseEntity<ResponseMessage<Edp>> addTeacher(@PathVariable String schoolId, @ModelAttribute EdpDto teacherDTO) {
        try {
            teacherDTO.setSchoolId(schoolId);
            if (this.edpService.isEmailExistsForSchool(teacherDTO.getEmail(), schoolId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email already exists for this school!", null));
            }
            String filePath = this.edpService.saveFile(teacherDTO.getEdpImage());
            Edp teacher = new Edp();
            teacher.setSchoolId(schoolId);
            teacher.setEdpName(teacherDTO.getEdpName());
            teacher.setEdpImage(filePath);
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setGender(teacherDTO.getGender());
            teacher.setBloodGroup(teacherDTO.getBloodGroup());
            teacher.setReligion(teacherDTO.getReligion());
            teacher.setPhone(teacherDTO.getPhone());
            teacher.setMaritialStatus(teacherDTO.getMaritialStatus());
            teacher.setJoiningDate(teacherDTO.getJoiningDate());
            teacher.setPinCode(teacherDTO.getPinCode());
            teacher.setState(teacherDTO.getState());
            teacher.setCity(teacherDTO.getCity());
            teacher.setDistrict(teacherDTO.getDistrict());
            teacher.setCountry(teacherDTO.getCountry());
            teacher.setPoliceStation(teacherDTO.getPoliceStation());
            teacher.setVillPost(teacherDTO.getVillPost());
            Edp savedTeacher = this.edpService.saveTeacher(teacher);
            return ResponseEntity.ok(new ResponseMessage("EDP added successfully!", savedTeacher));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseMessage(e.getResponseBodyAsString(), null));
        }
    }

    @GetMapping(value={"/edp/email/{email}/{schoolId}"})
    public ResponseEntity<ResponseModel> getTeacherByEmailAndSchool(@PathVariable String schoolId, @PathVariable String email) {
        Optional<Edp> edp = this.edpService.getEdpByEmailAndSchool(email, schoolId);
        if (edp.isPresent()) {
            return ResponseEntity.ok(new ResponseModel("Edp retrieved successfully", edp.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseModel("Edp not found with email: " + email + " for this school", null));
    }

    @GetMapping(value={"/edp/email/{email}"})
    public ResponseModel getTeacherByEmail(@PathVariable String email) {
        Optional<Edp> edp = this.edpService.getEdpByEmail(email);
        return edp.map(value -> new ResponseModel("Edp retrieved successfully", value)).orElseGet(() -> new ResponseModel("Edp not found with email: " + email, null));
    }

    @GetMapping(value={"/edp/id/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Edp>> getTeacherByIdAndSchool(@PathVariable String schoolId, @PathVariable String id) {
        Edp teacher = this.edpService.getTeacherById(id, schoolId);
        if (teacher != null) {
            return ResponseEntity.ok(new ResponseMessage("EDP details found!", teacher));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found for this school!", null));
    }

    @GetMapping(value={"/edp/id/{id}"})
    public ResponseEntity<ResponseMessage<Edp>> getTeacherById(@PathVariable String id) {
        Edp teacher = this.edpService.getTeacherById(id);
        if (teacher != null) {
            return ResponseEntity.ok(new ResponseMessage("EDP details found!", teacher));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found!", null));
    }

    @PutMapping(value={"/edp/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Edp>> updateTeacherWithSchool(@PathVariable String schoolId, @PathVariable String id, @ModelAttribute EdpDto teacherDTO) {
        Edp existingTeacher = this.edpService.getTeacherById(id, schoolId);
        if (existingTeacher == null) {
            return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found for this school!", null));
        }
        if (!existingTeacher.getEmail().equals(teacherDTO.getEmail()) && this.edpService.isEmailExistsForSchool(teacherDTO.getEmail(), schoolId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email already exists for another EDP in this school!", null));
        }
        Edp teacher = new Edp();
        teacher.setId(id);
        teacher.setSchoolId(schoolId);
        teacher.setEdpCode(existingTeacher.getEdpCode());
        if (teacherDTO.getEdpName() != null) {
            teacher.setEdpName(teacherDTO.getEdpName());
        }
        if (teacherDTO.getEmail() != null) {
            teacher.setEmail(teacherDTO.getEmail());
        }
        if (teacherDTO.getGender() != null) {
            teacher.setGender(teacherDTO.getGender());
        }
        if (teacherDTO.getBloodGroup() != null) {
            teacher.setBloodGroup(teacherDTO.getBloodGroup());
        }
        if (teacherDTO.getReligion() != null) {
            teacher.setReligion(teacherDTO.getReligion());
        }
        if (teacherDTO.getPhone() != null) {
            teacher.setPhone(teacherDTO.getPhone());
        }
        if (teacherDTO.getMaritialStatus() != null) {
            teacher.setMaritialStatus(teacherDTO.getMaritialStatus());
        }
        if (teacherDTO.getJoiningDate() != null) {
            teacher.setJoiningDate(teacherDTO.getJoiningDate());
        }
        if (teacherDTO.getPinCode() != null) {
            teacher.setPinCode(teacherDTO.getPinCode());
        }
        if (teacherDTO.getState() != null) {
            teacher.setState(teacherDTO.getState());
        }
        if (teacherDTO.getCity() != null) {
            teacher.setCity(teacherDTO.getCity());
        }
        if (teacherDTO.getDistrict() != null) {
            teacher.setDistrict(teacherDTO.getDistrict());
        }
        if (teacherDTO.getCountry() != null) {
            teacher.setCountry(teacherDTO.getCountry());
        }
        if (teacherDTO.getPoliceStation() != null) {
            teacher.setPoliceStation(teacherDTO.getPoliceStation());
        }
        if (teacherDTO.getVillPost() != null) {
            teacher.setVillPost(teacherDTO.getVillPost());
        }
        if (teacherDTO.getEdpImage() != null && !teacherDTO.getEdpImage().isEmpty()) {
            String filePath = this.edpService.saveFile(teacherDTO.getEdpImage());
            teacher.setEdpImage(filePath);
        } else {
            teacher.setEdpImage(existingTeacher.getEdpImage());
        }
        Edp updatedTeacher = this.edpService.updateTeacher(id, schoolId, teacher);
        return ResponseEntity.ok(new ResponseMessage("EDP updated successfully!", updatedTeacher));
    }

    @PutMapping(value={"/edp/{id}"})
    public ResponseEntity<ResponseMessage<Edp>> updateTeacher(@PathVariable String id, @ModelAttribute EdpDto teacherDTO) {
        Edp teacher = this.edpService.getTeacherById(id);
        if (teacher != null) {
            teacher.setEdpName(teacherDTO.getEdpName());
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setGender(teacherDTO.getGender());
            teacher.setBloodGroup(teacherDTO.getBloodGroup());
            teacher.setReligion(teacherDTO.getReligion());
            teacher.setPhone(teacherDTO.getPhone());
            teacher.setMaritialStatus(teacherDTO.getMaritialStatus());
            teacher.setJoiningDate(teacherDTO.getJoiningDate());
            teacher.setPinCode(teacherDTO.getPinCode());
            teacher.setState(teacherDTO.getState());
            teacher.setCity(teacherDTO.getCity());
            teacher.setDistrict(teacherDTO.getDistrict());
            teacher.setCountry(teacherDTO.getCountry());
            teacher.setPoliceStation(teacherDTO.getPoliceStation());
            teacher.setVillPost(teacherDTO.getVillPost());
            if (teacherDTO.getEdpImage() != null && !teacherDTO.getEdpImage().isEmpty()) {
                String filePath = this.edpService.saveFile(teacherDTO.getEdpImage());
                teacher.setEdpImage(filePath);
            }
            Edp updatedTeacher = this.edpService.saveTeacher(teacher);
            return ResponseEntity.ok(new ResponseMessage("EDP updated successfully!", updatedTeacher));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found!", null));
    }

    @DeleteMapping(value={"/edp/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacherWithSchool(@PathVariable String schoolId, @PathVariable String id) {
        boolean isDeleted = this.edpService.deleteTeacher(id, schoolId);
        if (isDeleted) {
            return ResponseEntity.ok(new ResponseMessage("EDP deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found for this school!", null));
    }

    @DeleteMapping(value={"/edp/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacher(@PathVariable String id) {
        boolean isDeleted = this.edpService.deleteTeacher(id);
        if (isDeleted) {
            return ResponseEntity.ok(new ResponseMessage("EDP deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("EDP not found!", null));
    }
}

