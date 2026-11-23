/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Assignment
 *  com.neel.Teacher.model.AssignmentDto
 *  com.neel.Teacher.repo.AssignmentRepository
 *  com.neel.Teacher.service.AssignmentService
 *  com.neel.Teacher.service.CloudinaryService
 *  com.neel.Teacher.service.EmailService
 *  com.neel.Teacher.service.StudentService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.Assignment;
import com.neel.Teacher.model.AssignmentDto;
import com.neel.Teacher.repo.AssignmentRepository;
import com.neel.Teacher.service.CloudinaryService;
import com.neel.Teacher.service.EmailService;
import com.neel.Teacher.service.StudentService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final String UPLOAD_DIR = "uploads/assignments/";
    private final EmailService emailService;
    private final StudentService studentService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, EmailService emailService, StudentService studentService, CloudinaryService cloudinaryService) {
        this.assignmentRepository = assignmentRepository;
        this.emailService = emailService;
        this.studentService = studentService;
        this.cloudinaryService = cloudinaryService;
        File directory = new File("uploads/assignments/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public Assignment createAssignment(AssignmentDto assignmentDto) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setSchoolId(assignmentDto.getSchoolId());
        assignment.setTeacherId(assignmentDto.getTeacherId());
        assignment.setTeacherName(assignmentDto.getTeacherName());
        assignment.setClassName(assignmentDto.getClassName());
        assignment.setSection(assignmentDto.getSection());
        assignment.setSubject(assignmentDto.getSubject());
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setAssignmentDate(assignmentDto.getAssignmentDate());
        assignment.setDueDate(assignmentDto.getDueDate());
        assignment.setStatus("ACTIVE");
        assignment.setCreatedAt(LocalDate.now());
        if (assignmentDto.getAttachment() != null && !assignmentDto.getAttachment().isEmpty()) {
            try {
                Map uploadResult = this.cloudinaryService.uploadFile(assignmentDto.getAttachment());
                if (uploadResult != null && uploadResult.containsKey("url")) {
                    assignment.setFilePath(uploadResult.get("url").toString());
                    System.out.println("File uploaded to Cloudinary: " + uploadResult.get("url").toString());
                }
            }
            catch (IOException e) {
                System.out.println("Failed to upload file to Cloudinary: " + e.getMessage());
                String localPath = this.saveFile(assignmentDto.getAttachment());
                assignment.setFilePath(localPath);
            }
        }
        Assignment savedAssignment = (Assignment)this.assignmentRepository.save(assignment);
        try {
            this.notifyStudentsAboutAssignment(savedAssignment);
        }
        catch (Exception e) {
            System.out.println("Failed to send email notifications: " + e.getMessage());
            e.printStackTrace();
        }
        return savedAssignment;
    }

    private void notifyStudentsAboutAssignment(Assignment assignment) {
        List studentEmails = this.studentService.getStudentEmails(assignment.getSchoolId(), assignment.getClassName(), assignment.getSection());
        List studentNames = this.studentService.getStudentNames(assignment.getSchoolId(), assignment.getClassName(), assignment.getSection());
        if (!studentEmails.isEmpty() && studentEmails.size() == studentNames.size()) {
            this.emailService.sendBulkAssignmentNotifications(studentEmails, studentNames, assignment.getClassName(), assignment.getSection(), assignment.getSubject(), assignment.getTitle(), assignment.getDueDate().toString(), assignment.getTeacherName());
            System.out.println("Sent assignment notifications to " + studentEmails.size() + " students");
        } else {
            System.out.println("No students found or email/name mismatch for class " + assignment.getClassName() + " " + assignment.getSection());
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads/assignments/" + fileName, new String[0]);
        Files.write(filePath, file.getBytes(), new OpenOption[0]);
        return filePath.toString();
    }

    public List<Assignment> getAllAssignments() {
        return this.assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsBySchool(String schoolId) {
        return this.assignmentRepository.findBySchoolId(schoolId);
    }

    public List<Assignment> getAssignmentsByTeacher(String teacherId) {
        return this.assignmentRepository.findByTeacherId(teacherId);
    }

    public List<Assignment> getAssignmentsBySchoolAndTeacher(String schoolId, String teacherId) {
        return this.assignmentRepository.findBySchoolIdAndTeacherId(schoolId, teacherId);
    }

    public List<Assignment> getAssignmentsByClass(String schoolId, String className, String section) {
        return this.assignmentRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCase(schoolId, className, section);
    }

    public List<Assignment> getAssignmentsByClassAndSubject(String schoolId, String className, String section, String subject) {
        return this.assignmentRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCase(schoolId, className, section, subject);
    }

    public List<Assignment> getAssignmentsByDate(String schoolId, LocalDate date) {
        return this.assignmentRepository.findBySchoolIdAndAssignmentDate(schoolId, date);
    }

    public Optional<Assignment> getAssignmentById(String id) {
        return this.assignmentRepository.findById(id);
    }

    public Assignment updateAssignment(String id, AssignmentDto assignmentDto) throws IOException {
        Optional existingAssignment = this.assignmentRepository.findById(id);
        if (existingAssignment.isPresent()) {
            Assignment assignment = (Assignment)existingAssignment.get();
            assignment.setTitle(assignmentDto.getTitle());
            assignment.setDescription(assignmentDto.getDescription());
            assignment.setDueDate(assignmentDto.getDueDate());
            return (Assignment)this.assignmentRepository.save(assignment);
        }
        return null;
    }

    public boolean deleteAssignment(String id) {
        if (this.assignmentRepository.existsById(id)) {
            this.assignmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Assignment closeAssignment(String id, String schoolId) {
        Optional existingAssignment = this.assignmentRepository.findByIdAndSchoolId(id, schoolId);
        if (existingAssignment.isPresent()) {
            Assignment assignment = (Assignment)existingAssignment.get();
            assignment.setStatus("CLOSED");
            return (Assignment)this.assignmentRepository.save(assignment);
        }
        return null;
    }

    public Assignment activeAssignment(String id, String schoolId) {
        Optional existingAssignment = this.assignmentRepository.findByIdAndSchoolId(id, schoolId);
        if (existingAssignment.isPresent()) {
            Assignment assignment = (Assignment)existingAssignment.get();
            assignment.setStatus("ACTIVE");
            return (Assignment)this.assignmentRepository.save(assignment);
        }
        return null;
    }

    public long countActiveAssignmentBySchool(String schoolId, String teacherId) {
        List<Assignment> allAssignment = this.assignmentRepository.findBySchoolIdAndTeacherId(schoolId, teacherId);
        return allAssignment.stream().filter(assignment -> "ACTIVE".equals(assignment.getStatus())).count();
    }

    public long countClosedAssignmentBySchool(String schoolId, String teacherId) {
        List<Assignment> allAssignment = this.assignmentRepository.findBySchoolIdAndTeacherId(schoolId, teacherId);
        return allAssignment.stream().filter(closedAssignment -> "CLOSED".equals(closedAssignment.getStatus())).count();
    }

    public long countAssignmentsDueToday(String schoolId, String teacherId) {
        LocalDate today = LocalDate.now();
        List assignmentsDueToday = this.assignmentRepository.findBySchoolIdAndTeacherIdAndDueDate(schoolId, teacherId, today);
        return assignmentsDueToday.size();
    }

    public long allAssignmentCountBySchool(String schoolId, String teacherId) {
        List<Assignment> allAssignment = this.assignmentRepository.findBySchoolIdAndTeacherId(schoolId, teacherId);
        return allAssignment.size();
    }
}

