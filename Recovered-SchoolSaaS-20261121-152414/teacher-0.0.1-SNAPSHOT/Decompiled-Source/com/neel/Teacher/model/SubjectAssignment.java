/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.SubjectAssignment
 *  lombok.Generated
 */
package com.neel.Teacher.model;

import java.time.LocalDate;
import lombok.Generated;

public class SubjectAssignment {
    private String subjectName;
    private String dayOfWeek;
    private String timeSlot;
    private String classGroup;
    private LocalDate assignedDate;
    private String status;

    @Generated
    public String getSubjectName() {
        return this.subjectName;
    }

    @Generated
    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    @Generated
    public String getTimeSlot() {
        return this.timeSlot;
    }

    @Generated
    public String getClassGroup() {
        return this.classGroup;
    }

    @Generated
    public LocalDate getAssignedDate() {
        return this.assignedDate;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Generated
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Generated
    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Generated
    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }

    @Generated
    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubjectAssignment)) {
            return false;
        }
        SubjectAssignment other = (SubjectAssignment)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$subjectName = this.getSubjectName();
        String other$subjectName = other.getSubjectName();
        if (this$subjectName == null ? other$subjectName != null : !this$subjectName.equals(other$subjectName)) {
            return false;
        }
        String this$dayOfWeek = this.getDayOfWeek();
        String other$dayOfWeek = other.getDayOfWeek();
        if (this$dayOfWeek == null ? other$dayOfWeek != null : !this$dayOfWeek.equals(other$dayOfWeek)) {
            return false;
        }
        String this$timeSlot = this.getTimeSlot();
        String other$timeSlot = other.getTimeSlot();
        if (this$timeSlot == null ? other$timeSlot != null : !this$timeSlot.equals(other$timeSlot)) {
            return false;
        }
        String this$classGroup = this.getClassGroup();
        String other$classGroup = other.getClassGroup();
        if (this$classGroup == null ? other$classGroup != null : !this$classGroup.equals(other$classGroup)) {
            return false;
        }
        LocalDate this$assignedDate = this.getAssignedDate();
        LocalDate other$assignedDate = other.getAssignedDate();
        if (this$assignedDate == null ? other$assignedDate != null : !((Object)this$assignedDate).equals(other$assignedDate)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        return !(this$status == null ? other$status != null : !this$status.equals(other$status));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SubjectAssignment;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $subjectName = this.getSubjectName();
        result = result * 59 + ($subjectName == null ? 43 : $subjectName.hashCode());
        String $dayOfWeek = this.getDayOfWeek();
        result = result * 59 + ($dayOfWeek == null ? 43 : $dayOfWeek.hashCode());
        String $timeSlot = this.getTimeSlot();
        result = result * 59 + ($timeSlot == null ? 43 : $timeSlot.hashCode());
        String $classGroup = this.getClassGroup();
        result = result * 59 + ($classGroup == null ? 43 : $classGroup.hashCode());
        LocalDate $assignedDate = this.getAssignedDate();
        result = result * 59 + ($assignedDate == null ? 43 : ((Object)$assignedDate).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SubjectAssignment(subjectName=" + this.getSubjectName() + ", dayOfWeek=" + this.getDayOfWeek() + ", timeSlot=" + this.getTimeSlot() + ", classGroup=" + this.getClassGroup() + ", assignedDate=" + this.getAssignedDate() + ", status=" + this.getStatus() + ")";
    }

    @Generated
    public SubjectAssignment() {
    }

    @Generated
    public SubjectAssignment(String subjectName, String dayOfWeek, String timeSlot, String classGroup, LocalDate assignedDate, String status) {
        this.subjectName = subjectName;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.classGroup = classGroup;
        this.assignedDate = assignedDate;
        this.status = status;
    }
}

