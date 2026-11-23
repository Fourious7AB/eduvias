/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.TeacherDTO
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.model;

import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class TeacherDTO {
    private String schoolId;
    private String teachersName;
    private MultipartFile teachersImage;
    private String email;
    private String gender;
    private String bloodGroup;
    private String religion;
    private String phone;
    private String maritalStatus;
    private String subject;
    private String joiningDate;
    private String pincode;
    private String state;
    private String city;
    private String district;
    private String country;
    private String policeStation;
    private String villagePost;

    @Generated
    public TeacherDTO() {
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getTeachersName() {
        return this.teachersName;
    }

    @Generated
    public MultipartFile getTeachersImage() {
        return this.teachersImage;
    }

    @Generated
    public String getEmail() {
        return this.email;
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
    public String getReligion() {
        return this.religion;
    }

    @Generated
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    @Generated
    public String getSubject() {
        return this.subject;
    }

    @Generated
    public String getJoiningDate() {
        return this.joiningDate;
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
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }

    @Generated
    public void setTeachersImage(MultipartFile teachersImage) {
        this.teachersImage = teachersImage;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
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
    public void setReligion(String religion) {
        this.religion = religion;
    }

    @Generated
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Generated
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Generated
    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
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
        if (!(o instanceof TeacherDTO)) {
            return false;
        }
        TeacherDTO other = (TeacherDTO)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$teachersName = this.getTeachersName();
        String other$teachersName = other.getTeachersName();
        if (this$teachersName == null ? other$teachersName != null : !this$teachersName.equals(other$teachersName)) {
            return false;
        }
        MultipartFile this$teachersImage = this.getTeachersImage();
        MultipartFile other$teachersImage = other.getTeachersImage();
        if (this$teachersImage == null ? other$teachersImage != null : !this$teachersImage.equals(other$teachersImage)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
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
        String this$religion = this.getReligion();
        String other$religion = other.getReligion();
        if (this$religion == null ? other$religion != null : !this$religion.equals(other$religion)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$maritalStatus = this.getMaritalStatus();
        String other$maritalStatus = other.getMaritalStatus();
        if (this$maritalStatus == null ? other$maritalStatus != null : !this$maritalStatus.equals(other$maritalStatus)) {
            return false;
        }
        String this$subject = this.getSubject();
        String other$subject = other.getSubject();
        if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject)) {
            return false;
        }
        String this$joiningDate = this.getJoiningDate();
        String other$joiningDate = other.getJoiningDate();
        if (this$joiningDate == null ? other$joiningDate != null : !this$joiningDate.equals(other$joiningDate)) {
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
        return other instanceof TeacherDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $teachersName = this.getTeachersName();
        result = result * 59 + ($teachersName == null ? 43 : $teachersName.hashCode());
        MultipartFile $teachersImage = this.getTeachersImage();
        result = result * 59 + ($teachersImage == null ? 43 : $teachersImage.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $gender = this.getGender();
        result = result * 59 + ($gender == null ? 43 : $gender.hashCode());
        String $bloodGroup = this.getBloodGroup();
        result = result * 59 + ($bloodGroup == null ? 43 : $bloodGroup.hashCode());
        String $religion = this.getReligion();
        result = result * 59 + ($religion == null ? 43 : $religion.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $maritalStatus = this.getMaritalStatus();
        result = result * 59 + ($maritalStatus == null ? 43 : $maritalStatus.hashCode());
        String $subject = this.getSubject();
        result = result * 59 + ($subject == null ? 43 : $subject.hashCode());
        String $joiningDate = this.getJoiningDate();
        result = result * 59 + ($joiningDate == null ? 43 : $joiningDate.hashCode());
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
        return "TeacherDTO(schoolId=" + this.getSchoolId() + ", teachersName=" + this.getTeachersName() + ", teachersImage=" + this.getTeachersImage() + ", email=" + this.getEmail() + ", gender=" + this.getGender() + ", bloodGroup=" + this.getBloodGroup() + ", religion=" + this.getReligion() + ", phone=" + this.getPhone() + ", maritalStatus=" + this.getMaritalStatus() + ", subject=" + this.getSubject() + ", joiningDate=" + this.getJoiningDate() + ", pincode=" + this.getPincode() + ", state=" + this.getState() + ", city=" + this.getCity() + ", district=" + this.getDistrict() + ", country=" + this.getCountry() + ", policeStation=" + this.getPoliceStation() + ", villagePost=" + this.getVillagePost() + ")";
    }
}

