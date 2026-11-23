/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.SignUp
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sign_up")
public class SignUp {
    @Id
    private String id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String schoolName;
    private String address;
    private String instituteType;
    private String password;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Generated
    public String getSchoolName() {
        return this.schoolName;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getInstituteType() {
        return this.instituteType;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Generated
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setInstituteType(String instituteType) {
        this.instituteType = instituteType;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SignUp)) {
            return false;
        }
        SignUp other = (SignUp)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$phoneNumber = this.getPhoneNumber();
        String other$phoneNumber = other.getPhoneNumber();
        if (this$phoneNumber == null ? other$phoneNumber != null : !this$phoneNumber.equals(other$phoneNumber)) {
            return false;
        }
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
            return false;
        }
        String this$address = this.getAddress();
        String other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) {
            return false;
        }
        String this$instituteType = this.getInstituteType();
        String other$instituteType = other.getInstituteType();
        if (this$instituteType == null ? other$instituteType != null : !this$instituteType.equals(other$instituteType)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        return !(this$password == null ? other$password != null : !this$password.equals(other$password));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SignUp;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $phoneNumber = this.getPhoneNumber();
        result = result * 59 + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        String $address = this.getAddress();
        result = result * 59 + ($address == null ? 43 : $address.hashCode());
        String $instituteType = this.getInstituteType();
        result = result * 59 + ($instituteType == null ? 43 : $instituteType.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SignUp(id=" + this.getId() + ", userName=" + this.getUserName() + ", email=" + this.getEmail() + ", phoneNumber=" + this.getPhoneNumber() + ", schoolName=" + this.getSchoolName() + ", address=" + this.getAddress() + ", instituteType=" + this.getInstituteType() + ", password=" + this.getPassword() + ")";
    }

    @Generated
    public SignUp() {
    }

    @Generated
    public SignUp(String id, String userName, String email, String phoneNumber, String schoolName, String address, String instituteType, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.address = address;
        this.instituteType = instituteType;
        this.password = password;
    }
}

