/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.TransferCertificate
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="transfer_certificates")
public class TransferCertificate {
    @Id
    private String id;
    private String tcNumber;
    private String admissionNumber;
    private String schoolId;
    private String studentName;
    private String fatherName;
    private String motherName;
    private String className;
    private String section;
    private String academicYear;
    private LocalDate issueDate;
    private LocalDate lastAttendanceDate;
    private String reasonForLeaving;
    private String conductAndBehavior;
    private String remarks;
    private boolean feesCleared;
    private String tcPath;

    @Generated
    public TransferCertificate() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getTcNumber() {
        return this.tcNumber;
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
    public String getFatherName() {
        return this.fatherName;
    }

    @Generated
    public String getMotherName() {
        return this.motherName;
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
    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    @Generated
    public LocalDate getLastAttendanceDate() {
        return this.lastAttendanceDate;
    }

    @Generated
    public String getReasonForLeaving() {
        return this.reasonForLeaving;
    }

    @Generated
    public String getConductAndBehavior() {
        return this.conductAndBehavior;
    }

    @Generated
    public String getRemarks() {
        return this.remarks;
    }

    @Generated
    public boolean isFeesCleared() {
        return this.feesCleared;
    }

    @Generated
    public String getTcPath() {
        return this.tcPath;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setTcNumber(String tcNumber) {
        this.tcNumber = tcNumber;
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
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Generated
    public void setMotherName(String motherName) {
        this.motherName = motherName;
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
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Generated
    public void setLastAttendanceDate(LocalDate lastAttendanceDate) {
        this.lastAttendanceDate = lastAttendanceDate;
    }

    @Generated
    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    @Generated
    public void setConductAndBehavior(String conductAndBehavior) {
        this.conductAndBehavior = conductAndBehavior;
    }

    @Generated
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Generated
    public void setFeesCleared(boolean feesCleared) {
        this.feesCleared = feesCleared;
    }

    @Generated
    public void setTcPath(String tcPath) {
        this.tcPath = tcPath;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TransferCertificate)) {
            return false;
        }
        TransferCertificate other = (TransferCertificate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isFeesCleared() != other.isFeesCleared()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$tcNumber = this.getTcNumber();
        String other$tcNumber = other.getTcNumber();
        if (this$tcNumber == null ? other$tcNumber != null : !this$tcNumber.equals(other$tcNumber)) {
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
        String this$fatherName = this.getFatherName();
        String other$fatherName = other.getFatherName();
        if (this$fatherName == null ? other$fatherName != null : !this$fatherName.equals(other$fatherName)) {
            return false;
        }
        String this$motherName = this.getMotherName();
        String other$motherName = other.getMotherName();
        if (this$motherName == null ? other$motherName != null : !this$motherName.equals(other$motherName)) {
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
        LocalDate this$issueDate = this.getIssueDate();
        LocalDate other$issueDate = other.getIssueDate();
        if (this$issueDate == null ? other$issueDate != null : !((Object)this$issueDate).equals(other$issueDate)) {
            return false;
        }
        LocalDate this$lastAttendanceDate = this.getLastAttendanceDate();
        LocalDate other$lastAttendanceDate = other.getLastAttendanceDate();
        if (this$lastAttendanceDate == null ? other$lastAttendanceDate != null : !((Object)this$lastAttendanceDate).equals(other$lastAttendanceDate)) {
            return false;
        }
        String this$reasonForLeaving = this.getReasonForLeaving();
        String other$reasonForLeaving = other.getReasonForLeaving();
        if (this$reasonForLeaving == null ? other$reasonForLeaving != null : !this$reasonForLeaving.equals(other$reasonForLeaving)) {
            return false;
        }
        String this$conductAndBehavior = this.getConductAndBehavior();
        String other$conductAndBehavior = other.getConductAndBehavior();
        if (this$conductAndBehavior == null ? other$conductAndBehavior != null : !this$conductAndBehavior.equals(other$conductAndBehavior)) {
            return false;
        }
        String this$remarks = this.getRemarks();
        String other$remarks = other.getRemarks();
        if (this$remarks == null ? other$remarks != null : !this$remarks.equals(other$remarks)) {
            return false;
        }
        String this$tcPath = this.getTcPath();
        String other$tcPath = other.getTcPath();
        return !(this$tcPath == null ? other$tcPath != null : !this$tcPath.equals(other$tcPath));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TransferCertificate;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isFeesCleared() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $tcNumber = this.getTcNumber();
        result = result * 59 + ($tcNumber == null ? 43 : $tcNumber.hashCode());
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $fatherName = this.getFatherName();
        result = result * 59 + ($fatherName == null ? 43 : $fatherName.hashCode());
        String $motherName = this.getMotherName();
        result = result * 59 + ($motherName == null ? 43 : $motherName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $academicYear = this.getAcademicYear();
        result = result * 59 + ($academicYear == null ? 43 : $academicYear.hashCode());
        LocalDate $issueDate = this.getIssueDate();
        result = result * 59 + ($issueDate == null ? 43 : ((Object)$issueDate).hashCode());
        LocalDate $lastAttendanceDate = this.getLastAttendanceDate();
        result = result * 59 + ($lastAttendanceDate == null ? 43 : ((Object)$lastAttendanceDate).hashCode());
        String $reasonForLeaving = this.getReasonForLeaving();
        result = result * 59 + ($reasonForLeaving == null ? 43 : $reasonForLeaving.hashCode());
        String $conductAndBehavior = this.getConductAndBehavior();
        result = result * 59 + ($conductAndBehavior == null ? 43 : $conductAndBehavior.hashCode());
        String $remarks = this.getRemarks();
        result = result * 59 + ($remarks == null ? 43 : $remarks.hashCode());
        String $tcPath = this.getTcPath();
        result = result * 59 + ($tcPath == null ? 43 : $tcPath.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "TransferCertificate(id=" + this.getId() + ", tcNumber=" + this.getTcNumber() + ", admissionNumber=" + this.getAdmissionNumber() + ", schoolId=" + this.getSchoolId() + ", studentName=" + this.getStudentName() + ", fatherName=" + this.getFatherName() + ", motherName=" + this.getMotherName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", academicYear=" + this.getAcademicYear() + ", issueDate=" + this.getIssueDate() + ", lastAttendanceDate=" + this.getLastAttendanceDate() + ", reasonForLeaving=" + this.getReasonForLeaving() + ", conductAndBehavior=" + this.getConductAndBehavior() + ", remarks=" + this.getRemarks() + ", feesCleared=" + this.isFeesCleared() + ", tcPath=" + this.getTcPath() + ")";
    }
}

