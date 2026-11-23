/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Assignment
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Teacher.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="assignments")
public class Assignment {
    @Id
    private String id;
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
    private String status;
    private LocalDate createdAt;
    private String filePath;

    @Generated
    public String getId() {
        return this.id;
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
    public String getStatus() {
        return this.status;
    }

    @Generated
    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
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
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        Assignment other = (Assignment)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
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
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDate this$createdAt = this.getCreatedAt();
        LocalDate other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        String this$filePath = this.getFilePath();
        String other$filePath = other.getFilePath();
        return !(this$filePath == null ? other$filePath != null : !this$filePath.equals(other$filePath));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Assignment;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
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
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDate $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        String $filePath = this.getFilePath();
        result = result * 59 + ($filePath == null ? 43 : $filePath.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Assignment(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", teacherId=" + this.getTeacherId() + ", teacherName=" + this.getTeacherName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", subject=" + this.getSubject() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", assignmentDate=" + this.getAssignmentDate() + ", dueDate=" + this.getDueDate() + ", status=" + this.getStatus() + ", createdAt=" + this.getCreatedAt() + ", filePath=" + this.getFilePath() + ")";
    }

    @Generated
    public Assignment() {
    }

    @Generated
    public Assignment(String id, String schoolId, String teacherId, String teacherName, String className, String section, String subject, String title, String description, LocalDate assignmentDate, LocalDate dueDate, String status, LocalDate createdAt, String filePath) {
        this.id = id;
        this.schoolId = schoolId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.className = className;
        this.section = section;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.assignmentDate = assignmentDate;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
        this.filePath = filePath;
    }
}

