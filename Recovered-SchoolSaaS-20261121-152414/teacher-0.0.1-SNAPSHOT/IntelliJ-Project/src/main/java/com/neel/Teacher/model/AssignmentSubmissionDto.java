/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AssignmentSubmissionDto
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.model;

import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class AssignmentSubmissionDto {
    private String assignmentId;
    private String schoolId;
    private String studentId;
    private String studentName;
    private String submissionContent;
    private MultipartFile attachment;

    @Generated
    public AssignmentSubmissionDto() {
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
    public MultipartFile getAttachment() {
        return this.attachment;
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
    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssignmentSubmissionDto)) {
            return false;
        }
        AssignmentSubmissionDto other = (AssignmentSubmissionDto)o;
        if (!other.canEqual((Object)this)) {
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
        MultipartFile this$attachment = this.getAttachment();
        MultipartFile other$attachment = other.getAttachment();
        return !(this$attachment == null ? other$attachment != null : !this$attachment.equals(other$attachment));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AssignmentSubmissionDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
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
        MultipartFile $attachment = this.getAttachment();
        result = result * 59 + ($attachment == null ? 43 : $attachment.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AssignmentSubmissionDto(assignmentId=" + this.getAssignmentId() + ", schoolId=" + this.getSchoolId() + ", studentId=" + this.getStudentId() + ", studentName=" + this.getStudentName() + ", submissionContent=" + this.getSubmissionContent() + ", attachment=" + this.getAttachment() + ")";
    }
}

