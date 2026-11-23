/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.LibrarianController
 *  com.neel.Teacher.model.Librarian
 *  com.neel.Teacher.model.LibrarianDto
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.model.ResponseModel
 *  com.neel.Teacher.service.LibrarianService
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
 *  org.springframework.web.client.HttpClientErrorException
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.Librarian;
import com.neel.Teacher.model.LibrarianDto;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.model.ResponseModel;
import com.neel.Teacher.service.LibrarianService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class LibrarianController {
    private final LibrarianService librarianService;

    @Autowired
    public LibrarianController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @GetMapping(value={"/lib"})
    public ResponseEntity<ResponseMessage<List<Librarian>>> getAllTeachers() {
        List librarians = this.librarianService.getAllTeachers();
        return ResponseEntity.ok((Object)new ResponseMessage("Librarians retrieved successfully!", (Object)librarians));
    }

    @GetMapping(value={"/lib/{schoolId}"})
    public ResponseEntity<ResponseMessage<List<Librarian>>> getTeachersBySchoolId(@PathVariable String schoolId) {
        List librarians = this.librarianService.getTeachersBySchoolId(schoolId);
        if (librarians.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No librarians found for this school!", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Librarians retrieved successfully!", (Object)librarians));
    }

    @PostMapping(value={"/lib/{schoolId}"})
    public ResponseEntity<ResponseMessage<Librarian>> addTeacher(@PathVariable String schoolId, @ModelAttribute LibrarianDto teacherDTO) {
        try {
            teacherDTO.setSchoolId(schoolId);
            if (this.librarianService.isEmailExistsForSchool(teacherDTO.getEmail(), schoolId)) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists for this school!", null));
            }
            String filePath = this.librarianService.saveFile(teacherDTO.getLibrarianImage());
            Librarian librarian = new Librarian();
            librarian.setSchoolId(schoolId);
            librarian.setLibrarianName(teacherDTO.getLibrarianName());
            librarian.setLibrarianImage(filePath);
            librarian.setEmail(teacherDTO.getEmail());
            librarian.setGender(teacherDTO.getGender());
            librarian.setBloodGroup(teacherDTO.getBloodGroup());
            librarian.setReligion(teacherDTO.getReligion());
            librarian.setPhone(teacherDTO.getPhone());
            librarian.setMaritialStatus(teacherDTO.getMaritialStatus());
            librarian.setJoiningDate(teacherDTO.getJoiningDate());
            librarian.setPinCode(teacherDTO.getPinCode());
            librarian.setState(teacherDTO.getState());
            librarian.setCity(teacherDTO.getCity());
            librarian.setDistrict(teacherDTO.getDistrict());
            librarian.setCountry(teacherDTO.getCountry());
            librarian.setPoliceStation(teacherDTO.getPoliceStation());
            librarian.setVillPost(teacherDTO.getVillPost());
            Librarian savedLibrarian = this.librarianService.saveTeacher(librarian);
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian added successfully!", (Object)savedLibrarian));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status((HttpStatusCode)e.getStatusCode()).body((Object)new ResponseMessage(e.getResponseBodyAsString(), null));
        }
    }

    @PostMapping(value={"/lib"})
    public ResponseEntity<ResponseMessage<Librarian>> addTeacher(@ModelAttribute LibrarianDto teacherDTO) {
        try {
            if (this.librarianService.isEmailExists(teacherDTO.getEmail())) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists!", null));
            }
            String filePath = this.librarianService.saveFile(teacherDTO.getLibrarianImage());
            Librarian librarian = new Librarian();
            librarian.setLibrarianName(teacherDTO.getLibrarianName());
            librarian.setLibrarianImage(filePath);
            librarian.setEmail(teacherDTO.getEmail());
            librarian.setGender(teacherDTO.getGender());
            librarian.setBloodGroup(teacherDTO.getBloodGroup());
            librarian.setReligion(teacherDTO.getReligion());
            librarian.setPhone(teacherDTO.getPhone());
            librarian.setMaritialStatus(teacherDTO.getMaritialStatus());
            librarian.setJoiningDate(teacherDTO.getJoiningDate());
            librarian.setPinCode(teacherDTO.getPinCode());
            librarian.setState(teacherDTO.getState());
            librarian.setCity(teacherDTO.getCity());
            librarian.setDistrict(teacherDTO.getDistrict());
            librarian.setCountry(teacherDTO.getCountry());
            librarian.setPoliceStation(teacherDTO.getPoliceStation());
            librarian.setVillPost(teacherDTO.getVillPost());
            Librarian savedTeacher = this.librarianService.saveTeacher(librarian);
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian added successfully!", (Object)savedTeacher));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseMessage("Error saving teacher: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/lib/email/{email}/{schoolId}"})
    public ResponseEntity<ResponseModel> getTeacherByEmailAndSchool(@PathVariable String schoolId, @PathVariable String email) {
        Optional librarian = this.librarianService.getLibrarianByEmailAndSchool(email, schoolId);
        return librarian.map(value -> ResponseEntity.ok((Object)new ResponseModel("Librarian retrieved successfully", value))).orElseGet(() -> ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseModel("Librarian not found with email: " + email + " for this school", null)));
    }

    @GetMapping(value={"/lib/email/{email}"})
    public ResponseModel getTeacherByEmail(@PathVariable String email) {
        Optional librarian = this.librarianService.getLibrarianByEmail(email);
        return librarian.map(value -> new ResponseModel("Librarian retrieved successfully", value)).orElseGet(() -> new ResponseModel("Librarian not found with email: " + email, null));
    }

    @GetMapping(value={"/lib/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Librarian>> getTeacherByIdAndSchool(@PathVariable String schoolId, @PathVariable String id) {
        Librarian librarian = this.librarianService.getTeacherById(id, schoolId);
        if (librarian != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian details found!", (Object)librarian));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Librarian not found for this school!", null));
    }

    @GetMapping(value={"/lib/id/{id}"})
    public ResponseEntity<ResponseMessage<Librarian>> getTeacherById(@PathVariable String id) {
        Librarian librarian = this.librarianService.getTeacherById(id);
        if (librarian != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian details found!", (Object)librarian));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Librarian not found!", null));
    }

    @PutMapping(value={"/lib/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Librarian>> updateLibrarian(@PathVariable String schoolId, @PathVariable String id, @ModelAttribute LibrarianDto librarianDTO) {
        try {
            Optional existingOpt = this.librarianService.getByLibrarianIdAndSchoolId(id, schoolId);
            if (existingOpt.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Librarian not found!", null));
            }
            Librarian librarian = (Librarian)existingOpt.get();
            if (librarianDTO.getEmail() != null && !librarianDTO.getEmail().equalsIgnoreCase(librarian.getEmail())) {
                if (this.librarianService.isEmailExistsForSchool(librarianDTO.getEmail(), schoolId)) {
                    return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists for this school!", null));
                }
                librarian.setEmail(librarianDTO.getEmail());
            }
            if (librarianDTO.getLibrarianImage() != null && !librarianDTO.getLibrarianImage().isEmpty()) {
                String newPath = this.librarianService.saveFile(librarianDTO.getLibrarianImage());
                librarian.setLibrarianImage(newPath);
            }
            if (librarianDTO.getLibrarianName() != null) {
                librarian.setLibrarianName(librarianDTO.getLibrarianName());
            }
            if (librarianDTO.getGender() != null) {
                librarian.setGender(librarianDTO.getGender());
            }
            if (librarianDTO.getBloodGroup() != null) {
                librarian.setBloodGroup(librarianDTO.getBloodGroup());
            }
            if (librarianDTO.getReligion() != null) {
                librarian.setReligion(librarianDTO.getReligion());
            }
            if (librarianDTO.getPhone() != null) {
                librarian.setPhone(librarianDTO.getPhone());
            }
            if (librarianDTO.getMaritialStatus() != null) {
                librarian.setMaritialStatus(librarianDTO.getMaritialStatus());
            }
            if (librarianDTO.getJoiningDate() != null) {
                librarian.setJoiningDate(librarianDTO.getJoiningDate());
            }
            if (librarianDTO.getPinCode() != null) {
                librarian.setPinCode(librarianDTO.getPinCode());
            }
            if (librarianDTO.getState() != null) {
                librarian.setState(librarianDTO.getState());
            }
            if (librarianDTO.getCity() != null) {
                librarian.setCity(librarianDTO.getCity());
            }
            if (librarianDTO.getDistrict() != null) {
                librarian.setDistrict(librarianDTO.getDistrict());
            }
            if (librarianDTO.getCountry() != null) {
                librarian.setCountry(librarianDTO.getCountry());
            }
            if (librarianDTO.getPoliceStation() != null) {
                librarian.setPoliceStation(librarianDTO.getPoliceStation());
            }
            if (librarianDTO.getVillPost() != null) {
                librarian.setVillPost(librarianDTO.getVillPost());
            }
            Librarian updated = this.librarianService.updateLibrarian(librarian);
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian updated successfully!", (Object)updated));
        }
        catch (HttpClientErrorException ex) {
            return ResponseEntity.status((HttpStatusCode)ex.getStatusCode()).body((Object)new ResponseMessage(ex.getResponseBodyAsString(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error updating librarian: " + e.getMessage(), null));
        }
    }

    @PutMapping(value={"/lib/{id}"})
    public ResponseEntity<ResponseMessage<Librarian>> updateTeacher(@PathVariable String id, @ModelAttribute LibrarianDto teacherDTO) {
        try {
            Librarian librarian = this.librarianService.getTeacherById(id);
            if (librarian != null) {
                librarian.setLibrarianName(teacherDTO.getLibrarianName());
                librarian.setEmail(teacherDTO.getEmail());
                librarian.setGender(teacherDTO.getGender());
                librarian.setBloodGroup(teacherDTO.getBloodGroup());
                librarian.setReligion(teacherDTO.getReligion());
                librarian.setPhone(teacherDTO.getPhone());
                librarian.setMaritialStatus(teacherDTO.getMaritialStatus());
                librarian.setJoiningDate(teacherDTO.getJoiningDate());
                librarian.setPinCode(teacherDTO.getPinCode());
                librarian.setState(teacherDTO.getState());
                librarian.setCity(teacherDTO.getCity());
                librarian.setDistrict(teacherDTO.getDistrict());
                librarian.setCountry(teacherDTO.getCountry());
                librarian.setPoliceStation(teacherDTO.getPoliceStation());
                librarian.setVillPost(teacherDTO.getVillPost());
                if (teacherDTO.getLibrarianImage() != null && !teacherDTO.getLibrarianImage().isEmpty()) {
                    String filePath = this.librarianService.saveFile(teacherDTO.getLibrarianImage());
                    librarian.setLibrarianImage(filePath);
                }
                Librarian updatedLibrarian = this.librarianService.saveTeacher(librarian);
                return ResponseEntity.ok((Object)new ResponseMessage("Librarian updated successfully!", (Object)updatedLibrarian));
            }
            return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Librarian not found!", null));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseMessage("Error updating librarian: " + e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/lib/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacherWithSchool(@PathVariable String schoolId, @PathVariable String id) {
        boolean isDeleted = this.librarianService.deleteTeacher(id, schoolId);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Librarian not found for this school!", null));
    }

    @DeleteMapping(value={"/lib/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacher(@PathVariable String id) {
        boolean isDeleted = this.librarianService.deleteTeacher(id);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Librarian deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Librarian not found!", null));
    }
}

