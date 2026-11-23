/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.Dto.StudentDetailsResponse
 *  com.result.Result.Dto.StudentDetailsResponse$StudentData
 *  lombok.Generated
 */
package com.result.Result.Dto;

import com.result.Result.Dto.StudentDetailsResponse;
import lombok.Generated;

public class StudentDetailsResponse {
    private String message;
    private StudentData data;

    @Generated
    public StudentDetailsResponse() {
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public StudentData getData() {
        return this.data;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(StudentData data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StudentDetailsResponse)) {
            return false;
        }
        StudentDetailsResponse other = (StudentDetailsResponse)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        StudentData this$data = this.getData();
        StudentData other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof StudentDetailsResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        StudentData $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StudentDetailsResponse(message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    public static class StudentData {
        private String id;
        private String schoolId;
        private String admissionNumber;
        private String studentName;
        private String rollNo;
        private String admissionDate;
        private String studentImage;
        private String className;
        private String section;
        private String gender;
        private String bloodGroup;
        private String dob;
        private String religion;
        private String fatherName;
        private String motherName;
        private String phone;
        private String alternativePhone;
        private String email;
        private String jointImage;
        private String pincode;
        private String state;
        private String city;
        private String district;
        private String country;
        private String policeStation;
        private String villagePost;
        private String idCardPath;
        private String status;
        private String academicYear;
        private boolean tcRequestNotificationSent;
        private boolean tcApprovalNotificationSent;
        private java.util.List<Object> services;
        private java.util.List<Object> subject;

        public StudentData() {}

        public String getId() { return this.id; }
        public String getSchoolId() { return this.schoolId; }
        public String getAdmissionNumber() { return this.admissionNumber; }
        public String getStudentName() { return this.studentName; }
        public String getRollNo() { return this.rollNo; }
        public String getAdmissionDate() { return this.admissionDate; }
        public String getStudentImage() { return this.studentImage; }
        public String getClassName() { return this.className; }
        public String getSection() { return this.section; }
        public String getGender() { return this.gender; }
        public String getBloodGroup() { return this.bloodGroup; }
        public String getDob() { return this.dob; }
        public String getReligion() { return this.religion; }
        public String getFatherName() { return this.fatherName; }
        public String getMotherName() { return this.motherName; }
        public String getPhone() { return this.phone; }
        public String getAlternativePhone() { return this.alternativePhone; }
        public String getEmail() { return this.email; }
        public String getJointImage() { return this.jointImage; }
        public String getPincode() { return this.pincode; }
        public String getState() { return this.state; }
        public String getCity() { return this.city; }
        public String getDistrict() { return this.district; }
        public String getCountry() { return this.country; }
        public String getPoliceStation() { return this.policeStation; }
        public String getVillagePost() { return this.villagePost; }
        public String getIdCardPath() { return this.idCardPath; }
        public String getStatus() { return this.status; }
        public String getAcademicYear() { return this.academicYear; }
        public boolean isTcRequestNotificationSent() { return this.tcRequestNotificationSent; }
        public boolean isTcApprovalNotificationSent() { return this.tcApprovalNotificationSent; }
        public java.util.List<Object> getServices() { return this.services; }
        public java.util.List<Object> getSubject() { return this.subject; }

        public void setId(String id) { this.id = id; }
        public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
        public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public void setAdmissionDate(String admissionDate) { this.admissionDate = admissionDate; }
        public void setStudentImage(String studentImage) { this.studentImage = studentImage; }
        public void setClassName(String className) { this.className = className; }
        public void setSection(String section) { this.section = section; }
        public void setGender(String gender) { this.gender = gender; }
        public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
        public void setDob(String dob) { this.dob = dob; }
        public void setReligion(String religion) { this.religion = religion; }
        public void setFatherName(String fatherName) { this.fatherName = fatherName; }
        public void setMotherName(String motherName) { this.motherName = motherName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAlternativePhone(String alternativePhone) { this.alternativePhone = alternativePhone; }
        public void setEmail(String email) { this.email = email; }
        public void setJointImage(String jointImage) { this.jointImage = jointImage; }
        public void setPincode(String pincode) { this.pincode = pincode; }
        public void setState(String state) { this.state = state; }
        public void setCity(String city) { this.city = city; }
        public void setDistrict(String district) { this.district = district; }
        public void setCountry(String country) { this.country = country; }
        public void setPoliceStation(String policeStation) { this.policeStation = policeStation; }
        public void setVillagePost(String villagePost) { this.villagePost = villagePost; }
        public void setIdCardPath(String idCardPath) { this.idCardPath = idCardPath; }
        public void setStatus(String status) { this.status = status; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
        public void setTcRequestNotificationSent(boolean tcRequestNotificationSent) { this.tcRequestNotificationSent = tcRequestNotificationSent; }
        public void setTcApprovalNotificationSent(boolean tcApprovalNotificationSent) { this.tcApprovalNotificationSent = tcApprovalNotificationSent; }
        public void setServices(java.util.List<Object> services) { this.services = services; }
        public void setSubject(java.util.List<Object> subject) { this.subject = subject; }
    }
}
