/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.TransferCertificateRequest
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="transfer_certificate_requests")
public class TransferCertificateRequest {
    @Id
    private String id;
    private String admissionNumber;
    private String schoolId;
    private String studentName;
    private String className;
    private String section;
    private LocalDate requestDate;
    private String reasonForLeaving;
    private List<String> requiredDocumentPaths;
    private String status;
    private String adminRemarks;
    private LocalDate approvalDate;
    private String tcId;

    @Generated
    public TransferCertificateRequest() {
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
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public LocalDate getRequestDate() {
        return this.requestDate;
    }

    @Generated
    public String getReasonForLeaving() {
        return this.reasonForLeaving;
    }

    @Generated
    public List<String> getRequiredDocumentPaths() {
        return this.requiredDocumentPaths;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getAdminRemarks() {
        return this.adminRemarks;
    }

    @Generated
    public LocalDate getApprovalDate() {
        return this.approvalDate;
    }

    @Generated
    public String getTcId() {
        return this.tcId;
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
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    @Generated
    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    @Generated
    public void setRequiredDocumentPaths(List<String> requiredDocumentPaths) {
        this.requiredDocumentPaths = requiredDocumentPaths;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    @Generated
    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    @Generated
    public void setTcId(String tcId) {
        this.tcId = tcId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TransferCertificateRequest)) {
            return false;
        }
        TransferCertificateRequest other = (TransferCertificateRequest)o;
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
        LocalDate this$requestDate = this.getRequestDate();
        LocalDate other$requestDate = other.getRequestDate();
        if (this$requestDate == null ? other$requestDate != null : !((Object)this$requestDate).equals(other$requestDate)) {
            return false;
        }
        String this$reasonForLeaving = this.getReasonForLeaving();
        String other$reasonForLeaving = other.getReasonForLeaving();
        if (this$reasonForLeaving == null ? other$reasonForLeaving != null : !this$reasonForLeaving.equals(other$reasonForLeaving)) {
            return false;
        }
        List this$requiredDocumentPaths = this.getRequiredDocumentPaths();
        List other$requiredDocumentPaths = other.getRequiredDocumentPaths();
        if (this$requiredDocumentPaths == null ? other$requiredDocumentPaths != null : !((Object)this$requiredDocumentPaths).equals(other$requiredDocumentPaths)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$adminRemarks = this.getAdminRemarks();
        String other$adminRemarks = other.getAdminRemarks();
        if (this$adminRemarks == null ? other$adminRemarks != null : !this$adminRemarks.equals(other$adminRemarks)) {
            return false;
        }
        LocalDate this$approvalDate = this.getApprovalDate();
        LocalDate other$approvalDate = other.getApprovalDate();
        if (this$approvalDate == null ? other$approvalDate != null : !((Object)this$approvalDate).equals(other$approvalDate)) {
            return false;
        }
        String this$tcId = this.getTcId();
        String other$tcId = other.getTcId();
        return !(this$tcId == null ? other$tcId != null : !this$tcId.equals(other$tcId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TransferCertificateRequest;
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
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        LocalDate $requestDate = this.getRequestDate();
        result = result * 59 + ($requestDate == null ? 43 : ((Object)$requestDate).hashCode());
        String $reasonForLeaving = this.getReasonForLeaving();
        result = result * 59 + ($reasonForLeaving == null ? 43 : $reasonForLeaving.hashCode());
        List $requiredDocumentPaths = this.getRequiredDocumentPaths();
        result = result * 59 + ($requiredDocumentPaths == null ? 43 : ((Object)$requiredDocumentPaths).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $adminRemarks = this.getAdminRemarks();
        result = result * 59 + ($adminRemarks == null ? 43 : $adminRemarks.hashCode());
        LocalDate $approvalDate = this.getApprovalDate();
        result = result * 59 + ($approvalDate == null ? 43 : ((Object)$approvalDate).hashCode());
        String $tcId = this.getTcId();
        result = result * 59 + ($tcId == null ? 43 : $tcId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "TransferCertificateRequest(id=" + this.getId() + ", admissionNumber=" + this.getAdmissionNumber() + ", schoolId=" + this.getSchoolId() + ", studentName=" + this.getStudentName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", requestDate=" + this.getRequestDate() + ", reasonForLeaving=" + this.getReasonForLeaving() + ", requiredDocumentPaths=" + this.getRequiredDocumentPaths() + ", status=" + this.getStatus() + ", adminRemarks=" + this.getAdminRemarks() + ", approvalDate=" + this.getApprovalDate() + ", tcId=" + this.getTcId() + ")";
    }
}

