/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.Services
 *  com.neel.StudentAdmission.model.Subjects
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import com.neel.StudentAdmission.model.Services;
import com.neel.StudentAdmission.model.Subjects;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admissions")
public class Admission {
    @Id
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
    private String status = "pending";
    private String academicYear;
    private boolean tcRequestNotificationSent = false;
    private boolean tcApprovalNotificationSent = false;
    private List<Services> services = new ArrayList();
    private List<Subjects> subject = new ArrayList();

    @Generated
    public Admission() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getAdmissionNumber() {
        return this.admissionNumber;
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
    public String getAdmissionDate() {
        return this.admissionDate;
    }

    @Generated
    public String getStudentImage() {
        return this.studentImage;
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
    public String getGender() {
        return this.gender;
    }

    @Generated
    public String getBloodGroup() {
        return this.bloodGroup;
    }

    @Generated
    public String getDob() {
        return this.dob;
    }

    @Generated
    public String getReligion() {
        return this.religion;
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
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getAlternativePhone() {
        return this.alternativePhone;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getJointImage() {
        return this.jointImage;
    }

    @Generated
    public String getPincode() {
        return this.pincode;
    }

    @Generated
    public String getState() {
        return this.state;
    }

    @Generated
    public String getCity() {
        return this.city;
    }

    @Generated
    public String getDistrict() {
        return this.district;
    }

    @Generated
    public String getCountry() {
        return this.country;
    }

    @Generated
    public String getPoliceStation() {
        return this.policeStation;
    }

    @Generated
    public String getVillagePost() {
        return this.villagePost;
    }

    @Generated
    public String getIdCardPath() {
        return this.idCardPath;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getAcademicYear() {
        return this.academicYear;
    }

    @Generated
    public boolean isTcRequestNotificationSent() {
        return this.tcRequestNotificationSent;
    }

    @Generated
    public boolean isTcApprovalNotificationSent() {
        return this.tcApprovalNotificationSent;
    }

    @Generated
    public List<Services> getServices() {
        return this.services;
    }

    @Generated
    public List<Subjects> getSubject() {
        return this.subject;
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
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
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
    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Generated
    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
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
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Generated
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Generated
    public void setDob(String dob) {
        this.dob = dob;
    }

    @Generated
    public void setReligion(String religion) {
        this.religion = religion;
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
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setJointImage(String jointImage) {
        this.jointImage = jointImage;
    }

    @Generated
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Generated
    public void setState(String state) {
        this.state = state;
    }

    @Generated
    public void setCity(String city) {
        this.city = city;
    }

    @Generated
    public void setDistrict(String district) {
        this.district = district;
    }

    @Generated
    public void setCountry(String country) {
        this.country = country;
    }

    @Generated
    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    @Generated
    public void setVillagePost(String villagePost) {
        this.villagePost = villagePost;
    }

    @Generated
    public void setIdCardPath(String idCardPath) {
        this.idCardPath = idCardPath;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    @Generated
    public void setTcRequestNotificationSent(boolean tcRequestNotificationSent) {
        this.tcRequestNotificationSent = tcRequestNotificationSent;
    }

    @Generated
    public void setTcApprovalNotificationSent(boolean tcApprovalNotificationSent) {
        this.tcApprovalNotificationSent = tcApprovalNotificationSent;
    }

    @Generated
    public void setServices(List<Services> services) {
        this.services = services;
    }

    @Generated
    public void setSubject(List<Subjects> subject) {
        this.subject = subject;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Admission)) {
            return false;
        }
        Admission other = (Admission)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isTcRequestNotificationSent() != other.isTcRequestNotificationSent()) {
            return false;
        }
        if (this.isTcApprovalNotificationSent() != other.isTcApprovalNotificationSent()) {
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
        String this$admissionNumber = this.getAdmissionNumber();
        String other$admissionNumber = other.getAdmissionNumber();
        if (this$admissionNumber == null ? other$admissionNumber != null : !this$admissionNumber.equals(other$admissionNumber)) {
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
        String this$admissionDate = this.getAdmissionDate();
        String other$admissionDate = other.getAdmissionDate();
        if (this$admissionDate == null ? other$admissionDate != null : !this$admissionDate.equals(other$admissionDate)) {
            return false;
        }
        String this$studentImage = this.getStudentImage();
        String other$studentImage = other.getStudentImage();
        if (this$studentImage == null ? other$studentImage != null : !this$studentImage.equals(other$studentImage)) {
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
        String this$gender = this.getGender();
        String other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !this$gender.equals(other$gender)) {
            return false;
        }
        String this$bloodGroup = this.getBloodGroup();
        String other$bloodGroup = other.getBloodGroup();
        if (this$bloodGroup == null ? other$bloodGroup != null : !this$bloodGroup.equals(other$bloodGroup)) {
            return false;
        }
        String this$dob = this.getDob();
        String other$dob = other.getDob();
        if (this$dob == null ? other$dob != null : !this$dob.equals(other$dob)) {
            return false;
        }
        String this$religion = this.getReligion();
        String other$religion = other.getReligion();
        if (this$religion == null ? other$religion != null : !this$religion.equals(other$religion)) {
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
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$alternativePhone = this.getAlternativePhone();
        String other$alternativePhone = other.getAlternativePhone();
        if (this$alternativePhone == null ? other$alternativePhone != null : !this$alternativePhone.equals(other$alternativePhone)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$jointImage = this.getJointImage();
        String other$jointImage = other.getJointImage();
        if (this$jointImage == null ? other$jointImage != null : !this$jointImage.equals(other$jointImage)) {
            return false;
        }
        String this$pincode = this.getPincode();
        String other$pincode = other.getPincode();
        if (this$pincode == null ? other$pincode != null : !this$pincode.equals(other$pincode)) {
            return false;
        }
        String this$state = this.getState();
        String other$state = other.getState();
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) {
            return false;
        }
        String this$city = this.getCity();
        String other$city = other.getCity();
        if (this$city == null ? other$city != null : !this$city.equals(other$city)) {
            return false;
        }
        String this$district = this.getDistrict();
        String other$district = other.getDistrict();
        if (this$district == null ? other$district != null : !this$district.equals(other$district)) {
            return false;
        }
        String this$country = this.getCountry();
        String other$country = other.getCountry();
        if (this$country == null ? other$country != null : !this$country.equals(other$country)) {
            return false;
        }
        String this$policeStation = this.getPoliceStation();
        String other$policeStation = other.getPoliceStation();
        if (this$policeStation == null ? other$policeStation != null : !this$policeStation.equals(other$policeStation)) {
            return false;
        }
        String this$villagePost = this.getVillagePost();
        String other$villagePost = other.getVillagePost();
        if (this$villagePost == null ? other$villagePost != null : !this$villagePost.equals(other$villagePost)) {
            return false;
        }
        String this$idCardPath = this.getIdCardPath();
        String other$idCardPath = other.getIdCardPath();
        if (this$idCardPath == null ? other$idCardPath != null : !this$idCardPath.equals(other$idCardPath)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$academicYear = this.getAcademicYear();
        String other$academicYear = other.getAcademicYear();
        if (this$academicYear == null ? other$academicYear != null : !this$academicYear.equals(other$academicYear)) {
            return false;
        }
        List this$services = this.getServices();
        List other$services = other.getServices();
        if (this$services == null ? other$services != null : !((Object)this$services).equals(other$services)) {
            return false;
        }
        List this$subject = this.getSubject();
        List other$subject = other.getSubject();
        return !(this$subject == null ? other$subject != null : !((Object)this$subject).equals(other$subject));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Admission;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isTcRequestNotificationSent() ? 79 : 97);
        result = result * 59 + (this.isTcApprovalNotificationSent() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $rollNo = this.getRollNo();
        result = result * 59 + ($rollNo == null ? 43 : $rollNo.hashCode());
        String $admissionDate = this.getAdmissionDate();
        result = result * 59 + ($admissionDate == null ? 43 : $admissionDate.hashCode());
        String $studentImage = this.getStudentImage();
        result = result * 59 + ($studentImage == null ? 43 : $studentImage.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $gender = this.getGender();
        result = result * 59 + ($gender == null ? 43 : $gender.hashCode());
        String $bloodGroup = this.getBloodGroup();
        result = result * 59 + ($bloodGroup == null ? 43 : $bloodGroup.hashCode());
        String $dob = this.getDob();
        result = result * 59 + ($dob == null ? 43 : $dob.hashCode());
        String $religion = this.getReligion();
        result = result * 59 + ($religion == null ? 43 : $religion.hashCode());
        String $fatherName = this.getFatherName();
        result = result * 59 + ($fatherName == null ? 43 : $fatherName.hashCode());
        String $motherName = this.getMotherName();
        result = result * 59 + ($motherName == null ? 43 : $motherName.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $alternativePhone = this.getAlternativePhone();
        result = result * 59 + ($alternativePhone == null ? 43 : $alternativePhone.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $jointImage = this.getJointImage();
        result = result * 59 + ($jointImage == null ? 43 : $jointImage.hashCode());
        String $pincode = this.getPincode();
        result = result * 59 + ($pincode == null ? 43 : $pincode.hashCode());
        String $state = this.getState();
        result = result * 59 + ($state == null ? 43 : $state.hashCode());
        String $city = this.getCity();
        result = result * 59 + ($city == null ? 43 : $city.hashCode());
        String $district = this.getDistrict();
        result = result * 59 + ($district == null ? 43 : $district.hashCode());
        String $country = this.getCountry();
        result = result * 59 + ($country == null ? 43 : $country.hashCode());
        String $policeStation = this.getPoliceStation();
        result = result * 59 + ($policeStation == null ? 43 : $policeStation.hashCode());
        String $villagePost = this.getVillagePost();
        result = result * 59 + ($villagePost == null ? 43 : $villagePost.hashCode());
        String $idCardPath = this.getIdCardPath();
        result = result * 59 + ($idCardPath == null ? 43 : $idCardPath.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $academicYear = this.getAcademicYear();
        result = result * 59 + ($academicYear == null ? 43 : $academicYear.hashCode());
        List $services = this.getServices();
        result = result * 59 + ($services == null ? 43 : ((Object)$services).hashCode());
        List $subject = this.getSubject();
        result = result * 59 + ($subject == null ? 43 : ((Object)$subject).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Admission(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", admissionNumber=" + this.getAdmissionNumber() + ", studentName=" + this.getStudentName() + ", rollNo=" + this.getRollNo() + ", admissionDate=" + this.getAdmissionDate() + ", studentImage=" + this.getStudentImage() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", gender=" + this.getGender() + ", bloodGroup=" + this.getBloodGroup() + ", dob=" + this.getDob() + ", religion=" + this.getReligion() + ", fatherName=" + this.getFatherName() + ", motherName=" + this.getMotherName() + ", phone=" + this.getPhone() + ", alternativePhone=" + this.getAlternativePhone() + ", email=" + this.getEmail() + ", jointImage=" + this.getJointImage() + ", pincode=" + this.getPincode() + ", state=" + this.getState() + ", city=" + this.getCity() + ", district=" + this.getDistrict() + ", country=" + this.getCountry() + ", policeStation=" + this.getPoliceStation() + ", villagePost=" + this.getVillagePost() + ", idCardPath=" + this.getIdCardPath() + ", status=" + this.getStatus() + ", academicYear=" + this.getAcademicYear() + ", tcRequestNotificationSent=" + this.isTcRequestNotificationSent() + ", tcApprovalNotificationSent=" + this.isTcApprovalNotificationSent() + ", services=" + this.getServices() + ", subject=" + this.getSubject() + ")";
    }
}

