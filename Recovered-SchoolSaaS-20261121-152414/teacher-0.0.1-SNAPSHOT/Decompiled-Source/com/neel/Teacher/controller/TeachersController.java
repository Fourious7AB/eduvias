/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.TeachersController
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.model.ResponseModel
 *  com.neel.Teacher.model.SubjectAssignment
 *  com.neel.Teacher.model.SubjectAssignmentDTO
 *  com.neel.Teacher.model.TeacherDTO
 *  com.neel.Teacher.model.Teachers
 *  com.neel.Teacher.service.TeachersService
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
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.client.HttpClientErrorException
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.model.ResponseModel;
import com.neel.Teacher.model.SubjectAssignment;
import com.neel.Teacher.model.SubjectAssignmentDTO;
import com.neel.Teacher.model.TeacherDTO;
import com.neel.Teacher.model.Teachers;
import com.neel.Teacher.service.TeachersService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class TeachersController {
    private final TeachersService teachersService;
    @Value(value="${view.subscription.plan.url}")
    private String subscriptionPlanUrl;

    @Autowired
    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @GetMapping(value={"/teachers"})
    public ResponseEntity<ResponseMessage<List<Teachers>>> getAllTeachers() {
        List teachers = this.teachersService.getAllTeachers();
        return ResponseEntity.ok((Object)new ResponseMessage("Teachers retrieved successfully!", (Object)teachers));
    }

    @GetMapping(value={"/teachers/{schoolId}"})
    public ResponseEntity<ResponseMessage<List<Teachers>>> getTeachersBySchoolId(@PathVariable String schoolId) {
        List teachers = this.teachersService.getTeachersBySchoolId(schoolId);
        if (teachers.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No teachers found for this school!", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Teachers retrieved successfully!", (Object)teachers));
    }

    @PostMapping(value={"/teachers/{schoolId}"})
    public ResponseEntity<ResponseMessage> addTeacherWithSchool(@PathVariable String schoolId, @ModelAttribute TeacherDTO teacherDTO) {
        try {
            teacherDTO.setSchoolId(schoolId);
            if (this.teachersService.isEmailExistsForSchool(teacherDTO.getEmail(), schoolId)) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists for this school!", null));
            }
            String filePath = this.teachersService.saveFile(teacherDTO.getTeachersImage());
            Teachers teacher = new Teachers();
            teacher.setSchoolId(schoolId);
            teacher.setTeachersName(teacherDTO.getTeachersName());
            teacher.setTeachersImage(filePath);
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setGender(teacherDTO.getGender());
            teacher.setBloodGroup(teacherDTO.getBloodGroup());
            teacher.setReligion(teacherDTO.getReligion());
            teacher.setPhone(teacherDTO.getPhone());
            teacher.setMaritalStatus(teacherDTO.getMaritalStatus());
            teacher.setSubject(teacherDTO.getSubject());
            teacher.setJoiningDate(teacherDTO.getJoiningDate());
            teacher.setPincode(teacherDTO.getPincode());
            teacher.setState(teacherDTO.getState());
            teacher.setCity(teacherDTO.getCity());
            teacher.setDistrict(teacherDTO.getDistrict());
            teacher.setCountry(teacherDTO.getCountry());
            teacher.setPoliceStation(teacherDTO.getPoliceStation());
            teacher.setVillagePost(teacherDTO.getVillagePost());
            Teachers savedTeacher = this.teachersService.saveTeacher(teacher);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ResponseMessage("Teacher added successfully", (Object)savedTeacher));
        }
        catch (HttpClientErrorException ex) {
            return ResponseEntity.status((HttpStatusCode)ex.getStatusCode()).body((Object)new ResponseMessage(ex.getResponseBodyAsString(), null));
        }
    }

    @PostMapping(value={"/teachers"})
    public ResponseEntity<ResponseMessage<Teachers>> addTeacher(@ModelAttribute TeacherDTO teacherDTO) {
        System.out.println(teacherDTO);
        try {
            if (this.teachersService.isEmailExists(teacherDTO.getEmail())) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists!", null));
            }
            String filePath = this.teachersService.saveFile(teacherDTO.getTeachersImage());
            Teachers teacher = new Teachers();
            teacher.setTeachersName(teacherDTO.getTeachersName());
            teacher.setTeachersImage(filePath);
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setGender(teacherDTO.getGender());
            teacher.setBloodGroup(teacherDTO.getBloodGroup());
            teacher.setReligion(teacherDTO.getReligion());
            teacher.setPhone(teacherDTO.getPhone());
            teacher.setMaritalStatus(teacherDTO.getMaritalStatus());
            teacher.setSubject(teacherDTO.getSubject());
            teacher.setJoiningDate(teacherDTO.getJoiningDate());
            teacher.setPincode(teacherDTO.getPincode());
            teacher.setState(teacherDTO.getState());
            teacher.setCity(teacherDTO.getCity());
            teacher.setDistrict(teacherDTO.getDistrict());
            teacher.setCountry(teacherDTO.getCountry());
            teacher.setPoliceStation(teacherDTO.getPoliceStation());
            teacher.setVillagePost(teacherDTO.getVillagePost());
            Teachers savedTeacher = this.teachersService.saveTeacher(teacher);
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher added successfully!", (Object)savedTeacher));
        }
        catch (Exception e) {
            System.out.println("Error saving teacher: " + e.getMessage());
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/teachers/email/{email}/{schoolId}"})
    public ResponseEntity<ResponseModel> getTeacherByEmailAndSchool(@PathVariable String schoolId, @PathVariable String email) {
        Optional teacher = this.teachersService.getTeacherByEmailAndSchool(email, schoolId);
        if (teacher.isPresent()) {
            return ResponseEntity.ok((Object)new ResponseModel("Teacher retrieved successfully", teacher.get()));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseModel("Teacher not found with email: " + email + " for this school", null));
    }

    @GetMapping(value={"/teachers/email/{email}"})
    public ResponseModel getTeacherByEmail(@PathVariable String email) {
        Optional teacher = this.teachersService.getTeacherByEmail(email);
        return teacher.map(value -> new ResponseModel("Teacher retrieved successfully", value)).orElseGet(() -> new ResponseModel("Teacher not found with email: " + email, null));
    }

    @GetMapping(value={"/teachers/id/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Teachers>> getTeacherByIdAndSchool(@PathVariable String schoolId, @PathVariable String id) {
        Teachers teacher = this.teachersService.getTeacherById(id, schoolId);
        if (teacher != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher details found!", (Object)teacher));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found for this school!", null));
    }

    @GetMapping(value={"/teachers/id/{id}"})
    public ResponseEntity<ResponseMessage<Teachers>> getTeacherById(@PathVariable String id) {
        Teachers teacher = this.teachersService.getTeacherById(id);
        if (teacher != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher details found!", (Object)teacher));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found!", null));
    }

    @GetMapping(value={"/teachers/search/{schoolId}"})
    public ResponseEntity<List<Teachers>> searchTeachersBySubjectAndSchool(@PathVariable String schoolId, @RequestParam String subject) {
        List teachers = this.teachersService.getTeachersBySubjectAndSchool(subject, schoolId);
        return ResponseEntity.ok((Object)teachers);
    }

    @GetMapping(value={"/teachers/search"})
    public ResponseEntity<List<Teachers>> searchTeachersBySubject(@RequestParam String subject) {
        List teachers = this.teachersService.getTeachersBySubject(subject);
        return ResponseEntity.ok((Object)teachers);
    }

    @PutMapping(value={"/teachers/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateTeacherWithSchool(@PathVariable String schoolId, @PathVariable String id, @ModelAttribute TeacherDTO teacherDTO) {
        System.out.println(" i m from update teacher with school :  " + schoolId + " " + id + " " + teacherDTO);
        try {
            Optional existingTeacherOpt = this.teachersService.getTeacherByIdAndSchoolId(id, schoolId);
            if (existingTeacherOpt.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Teacher not found!", null));
            }
            Teachers teacher = (Teachers)existingTeacherOpt.get();
            if (teacherDTO.getEmail() != null && !teacherDTO.getEmail().equalsIgnoreCase(teacher.getEmail())) {
                if (this.teachersService.isEmailExistsForSchool(teacherDTO.getEmail(), schoolId)) {
                    return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body((Object)new ResponseMessage("Email already exists for this school!", null));
                }
                teacher.setEmail(teacherDTO.getEmail());
            }
            if (teacherDTO.getTeachersImage() != null && !teacherDTO.getTeachersImage().isEmpty()) {
                String filePath = this.teachersService.saveFile(teacherDTO.getTeachersImage());
                teacher.setTeachersImage(filePath);
            }
            if (teacherDTO.getTeachersName() != null) {
                teacher.setTeachersName(teacherDTO.getTeachersName());
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
            if (teacherDTO.getMaritalStatus() != null) {
                teacher.setMaritalStatus(teacherDTO.getMaritalStatus());
            }
            if (teacherDTO.getSubject() != null) {
                teacher.setSubject(teacherDTO.getSubject());
            }
            if (teacherDTO.getJoiningDate() != null) {
                teacher.setJoiningDate(teacherDTO.getJoiningDate());
            }
            if (teacherDTO.getPincode() != null) {
                teacher.setPincode(teacherDTO.getPincode());
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
            if (teacherDTO.getVillagePost() != null) {
                teacher.setVillagePost(teacherDTO.getVillagePost());
            }
            teacher.setTeachersCode(teacher.getTeachersCode());
            Teachers updatedTeacher = this.teachersService.updateTeacher(teacher);
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher updated successfully", (Object)updatedTeacher));
        }
        catch (Exception ex) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error updating teacher: " + ex.getMessage(), null));
        }
    }

    @PutMapping(value={"/teachers/{id}"})
    public ResponseEntity<ResponseMessage<Teachers>> updateTeacher(@PathVariable String id, @ModelAttribute TeacherDTO teacherDTO) {
        try {
            Teachers teacher = this.teachersService.getTeacherById(id);
            if (teacher != null) {
                teacher.setTeachersName(teacherDTO.getTeachersName());
                teacher.setEmail(teacherDTO.getEmail());
                teacher.setGender(teacherDTO.getGender());
                teacher.setBloodGroup(teacherDTO.getBloodGroup());
                teacher.setReligion(teacherDTO.getReligion());
                teacher.setPhone(teacherDTO.getPhone());
                teacher.setMaritalStatus(teacherDTO.getMaritalStatus());
                teacher.setSubject(teacherDTO.getSubject());
                teacher.setJoiningDate(teacherDTO.getJoiningDate());
                teacher.setPincode(teacherDTO.getPincode());
                teacher.setState(teacherDTO.getState());
                teacher.setCity(teacherDTO.getCity());
                teacher.setDistrict(teacherDTO.getDistrict());
                teacher.setCountry(teacherDTO.getCountry());
                teacher.setPoliceStation(teacherDTO.getPoliceStation());
                teacher.setVillagePost(teacherDTO.getVillagePost());
                if (teacherDTO.getTeachersImage() != null && !teacherDTO.getTeachersImage().isEmpty()) {
                    String filePath = this.teachersService.saveFile(teacherDTO.getTeachersImage());
                    teacher.setTeachersImage(filePath);
                }
                Teachers updatedTeacher = this.teachersService.saveTeacher(teacher);
                return ResponseEntity.ok((Object)new ResponseMessage("Teacher updated successfully!", (Object)updatedTeacher));
            }
            return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found!", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseMessage("Error updating teacher: " + e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/teachers/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacherWithSchool(@PathVariable String schoolId, @PathVariable String id) {
        boolean isDeleted = this.teachersService.deleteTeacher(id, schoolId);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found for this school!", null));
    }

    @DeleteMapping(value={"/teachers/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteTeacher(@PathVariable String id) {
        boolean isDeleted = this.teachersService.deleteTeacher(id);
        if (isDeleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Teacher deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found!", null));
    }

    @PostMapping(value={"/teachers/{teacherId}/{schoolId}/assignments"})
    public ResponseEntity<ResponseMessage<Teachers>> addSubjectAssignment(@PathVariable String schoolId, @PathVariable String teacherId, @RequestBody SubjectAssignmentDTO assignmentDTO) {
        Teachers updatedTeacher = this.teachersService.addSubjectAssignment(teacherId, schoolId, assignmentDTO);
        if (updatedTeacher != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Subject assignment added successfully!", (Object)updatedTeacher));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found for this school!", null));
    }

    @PostMapping(value={"/teachers/{teacherId}/{schoolId}/assignments/multiple"})
    public ResponseEntity<ResponseMessage<Teachers>> addMultipleSubjectAssignments(@PathVariable String schoolId, @PathVariable String teacherId, @RequestBody List<SubjectAssignmentDTO> assignmentDTOs) {
        Teachers updatedTeacher = this.teachersService.addMultipleSubjectAssignments(teacherId, schoolId, assignmentDTOs);
        if (updatedTeacher != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Multiple subject assignments added successfully!", (Object)updatedTeacher));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher not found for this school!", null));
    }

    @GetMapping(value={"/teachers/{id}/{schoolId}/assignments"})
    public ResponseEntity<ResponseMessage<List<SubjectAssignment>>> getTeacherSubjectAssignments(@PathVariable String schoolId, @PathVariable String id) {
        List assignments = this.teachersService.getTeacherSubjectAssignments(id, schoolId);
        if (!assignments.isEmpty()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Subject assignments retrieved successfully!", (Object)assignments));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("No subject assignments found for this teacher!", null));
    }

    @PutMapping(value={"/teachers/{id}/{schoolId}/assignments/subject"})
    public ResponseEntity<ResponseMessage<Teachers>> updateSubjectAssignmentByName(@PathVariable String schoolId, @PathVariable String id, @RequestParam String subjectName, @RequestBody SubjectAssignmentDTO assignmentDTO) {
        System.out.println("Updating subject assignment: " + subjectName + " for teacher: " + id);
        System.out.println("New assignment data: " + assignmentDTO);
        Teachers updatedTeacher = this.teachersService.updateSubjectAssignmentByName(id, schoolId, subjectName, assignmentDTO);
        if (updatedTeacher != null) {
            System.out.println("Subject assignment updated successfully");
            return ResponseEntity.ok((Object)new ResponseMessage("Subject assignment updated successfully!", (Object)updatedTeacher));
        }
        System.out.println("Teacher or assignment not found for subject: " + subjectName);
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Teacher or assignment not found!", null));
    }

    @DeleteMapping(value={"/teachers/{id}/{schoolId}/assignments/subject"})
    public ResponseEntity<ResponseMessage<Teachers>> deleteSubjectAssignmentByName(@PathVariable String schoolId, @PathVariable String id, @RequestParam String subjectName) {
        Teachers updatedTeacher = this.teachersService.deleteSubjectAssignmentByName(id, schoolId, subjectName);
        if (updatedTeacher != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("assigned subject deleted successfully!", (Object)updatedTeacher));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("assign not found!", null));
    }

    @GetMapping(value={"/teacher-count/{schoolId}"})
    public ResponseEntity<ResponseMessage> countTeacherBySchool(@PathVariable String schoolId) {
        long countTeacher = this.teachersService.countTeacherBySchool(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("All teacher count retrieved successfully ", (Object)countTeacher), (HttpStatusCode)HttpStatus.OK);
    }
}

