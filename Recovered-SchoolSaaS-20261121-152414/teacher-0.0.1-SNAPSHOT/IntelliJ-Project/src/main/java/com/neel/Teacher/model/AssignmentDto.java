/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AssignmentDto
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class AssignmentDto {
    private String schoolId;
    private String teacherId;
    private String teacherName;
    private String className;
    private String section;
    private String subject;
    private String title;
    private String description;
    private LocalDate assignmentDate;
    private LocalDate dueDate;
    private MultipartFile attachment;

    @Generated
    public AssignmentDto() {
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getTeacherId() {
        return this.teacherId;
    }

    @Generated
    public String getTeacherName() {
        return this.teacherName;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public String getSubject() {
        return this.subject;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public LocalDate getAssignmentDate() {
        return this.assignmentDate;
    }

    @Generated
    public LocalDate getDueDate() {
        return this.dueDate;
    }

    @Generated
    public MultipartFile getAttachment() {
        return this.attachment;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Generated
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Generated
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    @Generated
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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
        if (!(o instanceof AssignmentDto)) {
            return false;
        }
        AssignmentDto other = (AssignmentDto)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$teacherId = this.getTeacherId();
        String other$teacherId = other.getTeacherId();
        if (this$teacherId == null ? other$teacherId != null : !this$teacherId.equals(other$teacherId)) {
            return false;
        }
        String this$teacherName = this.getTeacherName();
        String other$teacherName = other.getTeacherName();
        if (this$teacherName == null ? other$teacherName != null : !this$teacherName.equals(other$teacherName)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        String this$subject = this.getSubject();
        String other$subject = other.getSubject();
        if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        LocalDate this$assignmentDate = this.getAssignmentDate();
        LocalDate other$assignmentDate = other.getAssignmentDate();
        if (this$assignmentDate == null ? other$assignmentDate != null : !((Object)this$assignmentDate).equals(other$assignmentDate)) {
            return false;
        }
        LocalDate this$dueDate = this.getDueDate();
        LocalDate other$dueDate = other.getDueDate();
        if (this$dueDate == null ? other$dueDate != null : !((Object)this$dueDate).equals(other$dueDate)) {
            return false;
        }
        MultipartFile this$attachment = this.getAttachment();
        MultipartFile other$attachment = other.getAttachment();
        return !(this$attachment == null ? other$attachment != null : !this$attachment.equals(other$attachment));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AssignmentDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $teacherId = this.getTeacherId();
        result = result * 59 + ($teacherId == null ? 43 : $teacherId.hashCode());
        String $teacherName = this.getTeacherName();
        result = result * 59 + ($teacherName == null ? 43 : $teacherName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $subject = this.getSubject();
        result = result * 59 + ($subject == null ? 43 : $subject.hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        LocalDate $assignmentDate = this.getAssignmentDate();
        result = result * 59 + ($assignmentDate == null ? 43 : ((Object)$assignmentDate).hashCode());
        LocalDate $dueDate = this.getDueDate();
        result = result * 59 + ($dueDate == null ? 43 : ((Object)$dueDate).hashCode());
        MultipartFile $attachment = this.getAttachment();
        result = result * 59 + ($attachment == null ? 43 : $attachment.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AssignmentDto(schoolId=" + this.getSchoolId() + ", teacherId=" + this.getTeacherId() + ", teacherName=" + this.getTeacherName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", subject=" + this.getSubject() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", assignmentDate=" + this.getAssignmentDate() + ", dueDate=" + this.getDueDate() + ", attachment=" + this.getAttachment() + ")";
    }
}

