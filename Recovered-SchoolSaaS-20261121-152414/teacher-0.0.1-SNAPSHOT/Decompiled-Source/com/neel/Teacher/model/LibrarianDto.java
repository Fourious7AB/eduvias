/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.LibrarianDto
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.model;

import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class LibrarianDto {
    private String schoolId;
    private String librarianName;
    private MultipartFile librarianImage;
    private String email;
    private String gender;
    private String bloodGroup;
    private String religion;
    private String phone;
    private String maritialStatus;
    private String joiningDate;
    private String pinCode;
    private String state;
    private String city;
    private String district;
    private String country;
    private String policeStation;
    private String villPost;

    @Generated
    public LibrarianDto() {
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getLibrarianName() {
        return this.librarianName;
    }

    @Generated
    public MultipartFile getLibrarianImage() {
        return this.librarianImage;
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
    public String getMaritialStatus() {
        return this.maritialStatus;
    }

    @Generated
    public String getJoiningDate() {
        return this.joiningDate;
    }

    @Generated
    public String getPinCode() {
        return this.pinCode;
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
    public String getVillPost() {
        return this.villPost;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }

    @Generated
    public void setLibrarianImage(MultipartFile librarianImage) {
        this.librarianImage = librarianImage;
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
    public void setMaritialStatus(String maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    @Generated
    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Generated
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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
    public void setVillPost(String villPost) {
        this.villPost = villPost;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LibrarianDto)) {
            return false;
        }
        LibrarianDto other = (LibrarianDto)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$librarianName = this.getLibrarianName();
        String other$librarianName = other.getLibrarianName();
        if (this$librarianName == null ? other$librarianName != null : !this$librarianName.equals(other$librarianName)) {
            return false;
        }
        MultipartFile this$librarianImage = this.getLibrarianImage();
        MultipartFile other$librarianImage = other.getLibrarianImage();
        if (this$librarianImage == null ? other$librarianImage != null : !this$librarianImage.equals(other$librarianImage)) {
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
        String this$maritialStatus = this.getMaritialStatus();
        String other$maritialStatus = other.getMaritialStatus();
        if (this$maritialStatus == null ? other$maritialStatus != null : !this$maritialStatus.equals(other$maritialStatus)) {
            return false;
        }
        String this$joiningDate = this.getJoiningDate();
        String other$joiningDate = other.getJoiningDate();
        if (this$joiningDate == null ? other$joiningDate != null : !this$joiningDate.equals(other$joiningDate)) {
            return false;
        }
        String this$pinCode = this.getPinCode();
        String other$pinCode = other.getPinCode();
        if (this$pinCode == null ? other$pinCode != null : !this$pinCode.equals(other$pinCode)) {
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
        String this$villPost = this.getVillPost();
        String other$villPost = other.getVillPost();
        return !(this$villPost == null ? other$villPost != null : !this$villPost.equals(other$villPost));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof LibrarianDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $librarianName = this.getLibrarianName();
        result = result * 59 + ($librarianName == null ? 43 : $librarianName.hashCode());
        MultipartFile $librarianImage = this.getLibrarianImage();
        result = result * 59 + ($librarianImage == null ? 43 : $librarianImage.hashCode());
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
        String $maritialStatus = this.getMaritialStatus();
        result = result * 59 + ($maritialStatus == null ? 43 : $maritialStatus.hashCode());
        String $joiningDate = this.getJoiningDate();
        result = result * 59 + ($joiningDate == null ? 43 : $joiningDate.hashCode());
        String $pinCode = this.getPinCode();
        result = result * 59 + ($pinCode == null ? 43 : $pinCode.hashCode());
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
        String $villPost = this.getVillPost();
        result = result * 59 + ($villPost == null ? 43 : $villPost.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "LibrarianDto(schoolId=" + this.getSchoolId() + ", librarianName=" + this.getLibrarianName() + ", librarianImage=" + this.getLibrarianImage() + ", email=" + this.getEmail() + ", gender=" + this.getGender() + ", bloodGroup=" + this.getBloodGroup() + ", religion=" + this.getReligion() + ", phone=" + this.getPhone() + ", maritialStatus=" + this.getMaritialStatus() + ", joiningDate=" + this.getJoiningDate() + ", pinCode=" + this.getPinCode() + ", state=" + this.getState() + ", city=" + this.getCity() + ", district=" + this.getDistrict() + ", country=" + this.getCountry() + ", policeStation=" + this.getPoliceStation() + ", villPost=" + this.getVillPost() + ")";
    }
}

