/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.AssignmentSubmissionController
 *  com.neel.Teacher.model.AssignmentSubmission
 *  com.neel.Teacher.model.AssignmentSubmissionDto
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.service.AssignmentSubmissionService
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.AssignmentSubmission;
import com.neel.Teacher.model.AssignmentSubmissionDto;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.service.AssignmentSubmissionService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api"})
@CrossOrigin(origins={"*"})
public class AssignmentSubmissionController {
    private final AssignmentSubmissionService submissionService;

    @Autowired
    public AssignmentSubmissionController(AssignmentSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping(value={"/submissions"})
    public ResponseEntity<ResponseMessage<AssignmentSubmission>> submitAssignment(@ModelAttribute AssignmentSubmissionDto submissionDto) {
        try {
            AssignmentSubmission submission = this.submissionService.submitAssignment(submissionDto);
            return ResponseEntity.ok(new ResponseMessage("Assignment submitted successfully", submission));
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Error submitting assignment: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/submissions/assignment/{assignmentId}"})
    public ResponseEntity<ResponseMessage<List<AssignmentSubmission>>> getSubmissionsByAssignment(@PathVariable String assignmentId) {
        List submissions = this.submissionService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(new ResponseMessage("Submissions retrieved successfully", submissions));
    }

    @GetMapping(value={"/submissions/student/{studentId}"})
    public ResponseEntity<ResponseMessage<List<AssignmentSubmission>>> getSubmissionsByStudent(@PathVariable String studentId) {
        List submissions = this.submissionService.getSubmissionsByStudent(studentId);
        return ResponseEntity.ok(new ResponseMessage("Submissions retrieved successfully", submissions));
    }

    @GetMapping(value={"/submissions/school/{schoolId}/student/{studentId}"})
    public ResponseEntity<ResponseMessage<List<AssignmentSubmission>>> getSubmissionsBySchoolAndStudent(@PathVariable String schoolId, @PathVariable String studentId) {
        List submissions = this.submissionService.getSubmissionsBySchoolAndStudent(schoolId, studentId);
        return ResponseEntity.ok(new ResponseMessage("Submissions retrieved successfully", submissions));
    }

    @GetMapping(value={"/submissions/{id}"})
    public ResponseEntity<ResponseMessage<AssignmentSubmission>> getSubmissionById(@PathVariable String id) {
        Optional submission = this.submissionService.getSubmissionById(id);
        if (submission.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("Submission retrieved successfully", ((AssignmentSubmission)submission.get())));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Submission not found", null));
    }

    @PutMapping(value={"/submissions/{id}/grade"})
    public ResponseEntity<ResponseMessage<AssignmentSubmission>> gradeSubmission(@PathVariable String id, @RequestParam String grade, @RequestParam String feedback) {
        AssignmentSubmission submission = this.submissionService.gradeSubmission(id, grade, feedback);
        if (submission != null) {
            return ResponseEntity.ok(new ResponseMessage("Submission graded successfully", submission));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Submission not found", null));
    }

    @DeleteMapping(value={"/submissions/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteSubmission(@PathVariable String id) {
        boolean deleted = this.submissionService.deleteSubmission(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Submission deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Submission not found", null));
    }
}

