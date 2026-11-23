/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AssignmentSubmission
 *  com.neel.Teacher.model.AssignmentSubmissionDto
 *  com.neel.Teacher.repo.AssignmentSubmissionRepository
 *  com.neel.Teacher.service.AssignmentSubmissionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.AssignmentSubmission;
import com.neel.Teacher.model.AssignmentSubmissionDto;
import com.neel.Teacher.repo.AssignmentSubmissionRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AssignmentSubmissionService {
    private final AssignmentSubmissionRepository submissionRepository;
    private final String UPLOAD_DIR = "uploads/submissions/";

    @Autowired
    public AssignmentSubmissionService(AssignmentSubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
        File directory = new File("uploads/submissions/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public AssignmentSubmission submitAssignment(AssignmentSubmissionDto submissionDto) throws IOException {
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentId(submissionDto.getAssignmentId());
        submission.setSchoolId(submissionDto.getSchoolId());
        submission.setStudentId(submissionDto.getStudentId());
        submission.setStudentName(submissionDto.getStudentName());
        submission.setSubmissionContent(submissionDto.getSubmissionContent());
        submission.setSubmissionDate(LocalDate.now());
        submission.setStatus("SUBMITTED");
        if (submissionDto.getAttachment() != null && !submissionDto.getAttachment().isEmpty()) {
            String filePath = this.saveFile(submissionDto.getAttachment());
            submission.setAttachmentPath(filePath);
        }
        return (AssignmentSubmission)this.submissionRepository.save(submission);
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads/submissions/" + fileName, new String[0]);
        Files.write(filePath, file.getBytes(), new OpenOption[0]);
        return filePath.toString();
    }

    public List<AssignmentSubmission> getSubmissionsByAssignment(String assignmentId) {
        return this.submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<AssignmentSubmission> getSubmissionsByStudent(String studentId) {
        return this.submissionRepository.findByStudentId(studentId);
    }

    public List<AssignmentSubmission> getSubmissionsBySchoolAndStudent(String schoolId, String studentId) {
        return this.submissionRepository.findBySchoolIdAndStudentId(schoolId, studentId);
    }

    public Optional<AssignmentSubmission> getSubmissionById(String id) {
        return this.submissionRepository.findById(id);
    }

    public AssignmentSubmission gradeSubmission(String id, String grade, String feedback) {
        Optional existingSubmission = this.submissionRepository.findById(id);
        if (existingSubmission.isPresent()) {
            AssignmentSubmission submission = (AssignmentSubmission)existingSubmission.get();
            submission.setGrade(grade);
            submission.setTeacherFeedback(feedback);
            submission.setStatus("GRADED");
            return (AssignmentSubmission)this.submissionRepository.save(submission);
        }
        return null;
    }

    public boolean deleteSubmission(String id) {
        if (this.submissionRepository.existsById(id)) {
            this.submissionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

