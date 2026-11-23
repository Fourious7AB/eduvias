/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AssignmentSubmission
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Teacher.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="assignment_submissions")
public class AssignmentSubmission {
    @Id
    private String id;
    private String assignmentId;
    private String schoolId;
    private String studentId;
    private String studentName;
    private String submissionContent;
    private String attachmentPath;
    private LocalDate submissionDate;
    private String status;
    private String grade;
    private String teacherFeedback;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getAssignmentId() {
        return this.assignmentId;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getStudentId() {
        return this.studentId;
    }

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public String getSubmissionContent() {
        return this.submissionContent;
    }

    @Generated
    public String getAttachmentPath() {
        return this.attachmentPath;
    }

    @Generated
    public LocalDate getSubmissionDate() {
        return this.submissionDate;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getGrade() {
        return this.grade;
    }

    @Generated
    public String getTeacherFeedback() {
        return this.teacherFeedback;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setSubmissionContent(String submissionContent) {
        this.submissionContent = submissionContent;
    }

    @Generated
    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    @Generated
    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Generated
    public void setTeacherFeedback(String teacherFeedback) {
        this.teacherFeedback = teacherFeedback;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssignmentSubmission)) {
            return false;
        }
        AssignmentSubmission other = (AssignmentSubmission)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$assignmentId = this.getAssignmentId();
        String other$assignmentId = other.getAssignmentId();
        if (this$assignmentId == null ? other$assignmentId != null : !this$assignmentId.equals(other$assignmentId)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$studentId = this.getStudentId();
        String other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !this$studentId.equals(other$studentId)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$submissionContent = this.getSubmissionContent();
        String other$submissionContent = other.getSubmissionContent();
        if (this$submissionContent == null ? other$submissionContent != null : !this$submissionContent.equals(other$submissionContent)) {
            return false;
        }
        String this$attachmentPath = this.getAttachmentPath();
        String other$attachmentPath = other.getAttachmentPath();
        if (this$attachmentPath == null ? other$attachmentPath != null : !this$attachmentPath.equals(other$attachmentPath)) {
            return false;
        }
        LocalDate this$submissionDate = this.getSubmissionDate();
        LocalDate other$submissionDate = other.getSubmissionDate();
        if (this$submissionDate == null ? other$submissionDate != null : !((Object)this$submissionDate).equals(other$submissionDate)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$grade = this.getGrade();
        String other$grade = other.getGrade();
        if (this$grade == null ? other$grade != null : !this$grade.equals(other$grade)) {
            return false;
        }
        String this$teacherFeedback = this.getTeacherFeedback();
        String other$teacherFeedback = other.getTeacherFeedback();
        return !(this$teacherFeedback == null ? other$teacherFeedback != null : !this$teacherFeedback.equals(other$teacherFeedback));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AssignmentSubmission;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $assignmentId = this.getAssignmentId();
        result = result * 59 + ($assignmentId == null ? 43 : $assignmentId.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : $studentId.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $submissionContent = this.getSubmissionContent();
        result = result * 59 + ($submissionContent == null ? 43 : $submissionContent.hashCode());
        String $attachmentPath = this.getAttachmentPath();
        result = result * 59 + ($attachmentPath == null ? 43 : $attachmentPath.hashCode());
        LocalDate $submissionDate = this.getSubmissionDate();
        result = result * 59 + ($submissionDate == null ? 43 : ((Object)$submissionDate).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $grade = this.getGrade();
        result = result * 59 + ($grade == null ? 43 : $grade.hashCode());
        String $teacherFeedback = this.getTeacherFeedback();
        result = result * 59 + ($teacherFeedback == null ? 43 : $teacherFeedback.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AssignmentSubmission(id=" + this.getId() + ", assignmentId=" + this.getAssignmentId() + ", schoolId=" + this.getSchoolId() + ", studentId=" + this.getStudentId() + ", studentName=" + this.getStudentName() + ", submissionContent=" + this.getSubmissionContent() + ", attachmentPath=" + this.getAttachmentPath() + ", submissionDate=" + this.getSubmissionDate() + ", status=" + this.getStatus() + ", grade=" + this.getGrade() + ", teacherFeedback=" + this.getTeacherFeedback() + ")";
    }

    @Generated
    public AssignmentSubmission() {
    }

    @Generated
    public AssignmentSubmission(String id, String assignmentId, String schoolId, String studentId, String studentName, String submissionContent, String attachmentPath, LocalDate submissionDate, String status, String grade, String teacherFeedback) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.schoolId = schoolId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.submissionContent = submissionContent;
        this.attachmentPath = attachmentPath;
        this.submissionDate = submissionDate;
        this.status = status;
        this.grade = grade;
        this.teacherFeedback = teacherFeedback;
    }
}

