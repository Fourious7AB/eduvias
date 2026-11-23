/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AdmissionHistory
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admission_history")
public class AdmissionHistory {
    @Id
    private String id;
    private String admissionNumber;
    private String schoolId;
    private String studentName;
    private String rollNo;
    private String className;
    private String section;
    private String academicYear;
    private String status;
    private String promotedDate;
    private String previousClassName;
    private String previousSection;
    private String previousAcademicYear;
    private String promotionType;

    @Generated
    public AdmissionHistory() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getAdmissionNumber() {
        return this.admissionNumber;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public String getRollNo() {
        return this.rollNo;
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
    public String getAcademicYear() {
        return this.academicYear;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getPromotedDate() {
        return this.promotedDate;
    }

    @Generated
    public String getPreviousClassName() {
        return this.previousClassName;
    }

    @Generated
    public String getPreviousSection() {
        return this.previousSection;
    }

    @Generated
    public String getPreviousAcademicYear() {
        return this.previousAcademicYear;
    }

    @Generated
    public String getPromotionType() {
        return this.promotionType;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setPromotedDate(String promotedDate) {
        this.promotedDate = promotedDate;
    }

    @Generated
    public void setPreviousClassName(String previousClassName) {
        this.previousClassName = previousClassName;
    }

    @Generated
    public void setPreviousSection(String previousSection) {
        this.previousSection = previousSection;
    }

    @Generated
    public void setPreviousAcademicYear(String previousAcademicYear) {
        this.previousAcademicYear = previousAcademicYear;
    }

    @Generated
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdmissionHistory)) {
            return false;
        }
        AdmissionHistory other = (AdmissionHistory)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$admissionNumber = this.getAdmissionNumber();
        String other$admissionNumber = other.getAdmissionNumber();
        if (this$admissionNumber == null ? other$admissionNumber != null : !this$admissionNumber.equals(other$admissionNumber)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$rollNo = this.getRollNo();
        String other$rollNo = other.getRollNo();
        if (this$rollNo == null ? other$rollNo != null : !this$rollNo.equals(other$rollNo)) {
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
        String this$academicYear = this.getAcademicYear();
        String other$academicYear = other.getAcademicYear();
        if (this$academicYear == null ? other$academicYear != null : !this$academicYear.equals(other$academicYear)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$promotedDate = this.getPromotedDate();
        String other$promotedDate = other.getPromotedDate();
        if (this$promotedDate == null ? other$promotedDate != null : !this$promotedDate.equals(other$promotedDate)) {
            return false;
        }
        String this$previousClassName = this.getPreviousClassName();
        String other$previousClassName = other.getPreviousClassName();
        if (this$previousClassName == null ? other$previousClassName != null : !this$previousClassName.equals(other$previousClassName)) {
            return false;
        }
        String this$previousSection = this.getPreviousSection();
        String other$previousSection = other.getPreviousSection();
        if (this$previousSection == null ? other$previousSection != null : !this$previousSection.equals(other$previousSection)) {
            return false;
        }
        String this$previousAcademicYear = this.getPreviousAcademicYear();
        String other$previousAcademicYear = other.getPreviousAcademicYear();
        if (this$previousAcademicYear == null ? other$previousAcademicYear != null : !this$previousAcademicYear.equals(other$previousAcademicYear)) {
            return false;
        }
        String this$promotionType = this.getPromotionType();
        String other$promotionType = other.getPromotionType();
        return !(this$promotionType == null ? other$promotionType != null : !this$promotionType.equals(other$promotionType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AdmissionHistory;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $rollNo = this.getRollNo();
        result = result * 59 + ($rollNo == null ? 43 : $rollNo.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $academicYear = this.getAcademicYear();
        result = result * 59 + ($academicYear == null ? 43 : $academicYear.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $promotedDate = this.getPromotedDate();
        result = result * 59 + ($promotedDate == null ? 43 : $promotedDate.hashCode());
        String $previousClassName = this.getPreviousClassName();
        result = result * 59 + ($previousClassName == null ? 43 : $previousClassName.hashCode());
        String $previousSection = this.getPreviousSection();
        result = result * 59 + ($previousSection == null ? 43 : $previousSection.hashCode());
        String $previousAcademicYear = this.getPreviousAcademicYear();
        result = result * 59 + ($previousAcademicYear == null ? 43 : $previousAcademicYear.hashCode());
        String $promotionType = this.getPromotionType();
        result = result * 59 + ($promotionType == null ? 43 : $promotionType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AdmissionHistory(id=" + this.getId() + ", admissionNumber=" + this.getAdmissionNumber() + ", schoolId=" + this.getSchoolId() + ", studentName=" + this.getStudentName() + ", rollNo=" + this.getRollNo() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", academicYear=" + this.getAcademicYear() + ", status=" + this.getStatus() + ", promotedDate=" + this.getPromotedDate() + ", previousClassName=" + this.getPreviousClassName() + ", previousSection=" + this.getPreviousSection() + ", previousAcademicYear=" + this.getPreviousAcademicYear() + ", promotionType=" + this.getPromotionType() + ")";
    }
}

