/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AdmissionDto
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class AdmissionDto {
    private String id;
    private String schoolId;
    private String studentName;
    private String rollNo;
    private String admissionDate;
    private MultipartFile studentImage;
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
    private MultipartFile jointImage;
    private String pincode;
    private String state;
    private String city;
    private String district;
    private String country;
    private String policeStation;
    private String villagePost;

    @Generated
    public AdmissionDto() {
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
    public MultipartFile getStudentImage() {
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
    public MultipartFile getJointImage() {
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
    public void setId(String id) {
        this.id = id;
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
    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Generated
    public void setStudentImage(MultipartFile studentImage) {
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
    public void setJointImage(MultipartFile jointImage) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdmissionDto)) {
            return false;
        }
        AdmissionDto other = (AdmissionDto)o;
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
        MultipartFile this$studentImage = this.getStudentImage();
        MultipartFile other$studentImage = other.getStudentImage();
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
        MultipartFile this$jointImage = this.getJointImage();
        MultipartFile other$jointImage = other.getJointImage();
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
        return !(this$villagePost == null ? other$villagePost != null : !this$villagePost.equals(other$villagePost));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AdmissionDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $rollNo = this.getRollNo();
        result = result * 59 + ($rollNo == null ? 43 : $rollNo.hashCode());
        String $admissionDate = this.getAdmissionDate();
        result = result * 59 + ($admissionDate == null ? 43 : $admissionDate.hashCode());
        MultipartFile $studentImage = this.getStudentImage();
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
        MultipartFile $jointImage = this.getJointImage();
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
        return result;
    }

    @Generated
    public String toString() {
        return "AdmissionDto(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", studentName=" + this.getStudentName() + ", rollNo=" + this.getRollNo() + ", admissionDate=" + this.getAdmissionDate() + ", studentImage=" + this.getStudentImage() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", gender=" + this.getGender() + ", bloodGroup=" + this.getBloodGroup() + ", dob=" + this.getDob() + ", religion=" + this.getReligion() + ", fatherName=" + this.getFatherName() + ", motherName=" + this.getMotherName() + ", phone=" + this.getPhone() + ", alternativePhone=" + this.getAlternativePhone() + ", email=" + this.getEmail() + ", jointImage=" + this.getJointImage() + ", pincode=" + this.getPincode() + ", state=" + this.getState() + ", city=" + this.getCity() + ", district=" + this.getDistrict() + ", country=" + this.getCountry() + ", policeStation=" + this.getPoliceStation() + ", villagePost=" + this.getVillagePost() + ")";
    }
}

