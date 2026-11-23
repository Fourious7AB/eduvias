/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AttendanceDetails
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import java.util.Date;
import java.util.Map;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="attendance_details")
public class AttendanceDetails {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private Date attendanceDate;
    private String studentName;
    private Map<String, String> attendance;
    private Map<String, String> studentIds;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
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
    public Date getAttendanceDate() {
        return this.attendanceDate;
    }

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public Map<String, String> getAttendance() {
        return this.attendance;
    }

    @Generated
    public Map<String, String> getStudentIds() {
        return this.studentIds;
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
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setAttendance(Map<String, String> attendance) {
        this.attendance = attendance;
    }

    @Generated
    public void setStudentIds(Map<String, String> studentIds) {
        this.studentIds = studentIds;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceDetails)) {
            return false;
        }
        AttendanceDetails other = (AttendanceDetails)o;
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
        Date this$attendanceDate = this.getAttendanceDate();
        Date other$attendanceDate = other.getAttendanceDate();
        if (this$attendanceDate == null ? other$attendanceDate != null : !((Object)this$attendanceDate).equals(other$attendanceDate)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        Map this$attendance = this.getAttendance();
        Map other$attendance = other.getAttendance();
        if (this$attendance == null ? other$attendance != null : !((Object)this$attendance).equals(other$attendance)) {
            return false;
        }
        Map this$studentIds = this.getStudentIds();
        Map other$studentIds = other.getStudentIds();
        return !(this$studentIds == null ? other$studentIds != null : !((Object)this$studentIds).equals(other$studentIds));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AttendanceDetails;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        Date $attendanceDate = this.getAttendanceDate();
        result = result * 59 + ($attendanceDate == null ? 43 : ((Object)$attendanceDate).hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        Map $attendance = this.getAttendance();
        result = result * 59 + ($attendance == null ? 43 : ((Object)$attendance).hashCode());
        Map $studentIds = this.getStudentIds();
        result = result * 59 + ($studentIds == null ? 43 : ((Object)$studentIds).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AttendanceDetails(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", attendanceDate=" + this.getAttendanceDate() + ", studentName=" + this.getStudentName() + ", attendance=" + this.getAttendance() + ", studentIds=" + this.getStudentIds() + ")";
    }

    @Generated
    public AttendanceDetails() {
    }

    @Generated
    public AttendanceDetails(String id, String schoolId, String className, String section, Date attendanceDate, String studentName, Map<String, String> attendance, Map<String, String> studentIds) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.section = section;
        this.attendanceDate = attendanceDate;
        this.studentName = studentName;
        this.attendance = attendance;
        this.studentIds = studentIds;
    }
}

