/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.neel.Auth.model.School
 *  org.springframework.data.annotation.Id
 */
package com.neel.Auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown=true)
public class School {
    @Id
    private String id;
    private String schoolId;
    private String schoolName;
    private String schoolRegistrationNumber;
    private String schoolAddress;
    private String schoolEmail;
    private String schoolPhone;
    private String schoolDescription;
    private String schoolLogo;
    private String schoolImage;
    private String schoolCode;
    private String schoolPrincipalName;
    private String schoolPrincipalEmail;
    private String schoolPrincipalPhone;
    private String status;
    private Boolean active;

    public String getId() {
        return this.id;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public String getSchoolRegistrationNumber() {
        return this.schoolRegistrationNumber;
    }

    public String getSchoolAddress() {
        return this.schoolAddress;
    }

    public String getSchoolEmail() {
        return this.schoolEmail;
    }

    public String getSchoolPhone() {
        return this.schoolPhone;
    }

    public String getSchoolDescription() {
        return this.schoolDescription;
    }

    public String getSchoolLogo() {
        return this.schoolLogo;
    }

    public String getSchoolImage() {
        return this.schoolImage;
    }

    public String getSchoolCode() {
        return this.schoolCode;
    }

    public String getSchoolPrincipalName() {
        return this.schoolPrincipalName;
    }

    public String getSchoolPrincipalEmail() {
        return this.schoolPrincipalEmail;
    }

    public String getSchoolPrincipalPhone() {
        return this.schoolPrincipalPhone;
    }

    public String getStatus() {
        return this.status;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolRegistrationNumber(String schoolRegistrationNumber) {
        this.schoolRegistrationNumber = schoolRegistrationNumber;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public void setSchoolDescription(String schoolDescription) {
        this.schoolDescription = schoolDescription;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public void setSchoolImage(String schoolImage) {
        this.schoolImage = schoolImage;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public void setSchoolPrincipalName(String schoolPrincipalName) {
        this.schoolPrincipalName = schoolPrincipalName;
    }

    public void setSchoolPrincipalEmail(String schoolPrincipalEmail) {
        this.schoolPrincipalEmail = schoolPrincipalEmail;
    }

    public void setSchoolPrincipalPhone(String schoolPrincipalPhone) {
        this.schoolPrincipalPhone = schoolPrincipalPhone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof School)) {
            return false;
        }
        School other = (School)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Boolean this$active = this.getActive();
        Boolean other$active = other.getActive();
        if (this$active == null ? other$active != null : !((Object)this$active).equals(other$active)) {
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
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
            return false;
        }
        String this$schoolRegistrationNumber = this.getSchoolRegistrationNumber();
        String other$schoolRegistrationNumber = other.getSchoolRegistrationNumber();
        if (this$schoolRegistrationNumber == null ? other$schoolRegistrationNumber != null : !this$schoolRegistrationNumber.equals(other$schoolRegistrationNumber)) {
            return false;
        }
        String this$schoolAddress = this.getSchoolAddress();
        String other$schoolAddress = other.getSchoolAddress();
        if (this$schoolAddress == null ? other$schoolAddress != null : !this$schoolAddress.equals(other$schoolAddress)) {
            return false;
        }
        String this$schoolEmail = this.getSchoolEmail();
        String other$schoolEmail = other.getSchoolEmail();
        if (this$schoolEmail == null ? other$schoolEmail != null : !this$schoolEmail.equals(other$schoolEmail)) {
            return false;
        }
        String this$schoolPhone = this.getSchoolPhone();
        String other$schoolPhone = other.getSchoolPhone();
        if (this$schoolPhone == null ? other$schoolPhone != null : !this$schoolPhone.equals(other$schoolPhone)) {
            return false;
        }
        String this$schoolDescription = this.getSchoolDescription();
        String other$schoolDescription = other.getSchoolDescription();
        if (this$schoolDescription == null ? other$schoolDescription != null : !this$schoolDescription.equals(other$schoolDescription)) {
            return false;
        }
        String this$schoolLogo = this.getSchoolLogo();
        String other$schoolLogo = other.getSchoolLogo();
        if (this$schoolLogo == null ? other$schoolLogo != null : !this$schoolLogo.equals(other$schoolLogo)) {
            return false;
        }
        String this$schoolImage = this.getSchoolImage();
        String other$schoolImage = other.getSchoolImage();
        if (this$schoolImage == null ? other$schoolImage != null : !this$schoolImage.equals(other$schoolImage)) {
            return false;
        }
        String this$schoolCode = this.getSchoolCode();
        String other$schoolCode = other.getSchoolCode();
        if (this$schoolCode == null ? other$schoolCode != null : !this$schoolCode.equals(other$schoolCode)) {
            return false;
        }
        String this$schoolPrincipalName = this.getSchoolPrincipalName();
        String other$schoolPrincipalName = other.getSchoolPrincipalName();
        if (this$schoolPrincipalName == null ? other$schoolPrincipalName != null : !this$schoolPrincipalName.equals(other$schoolPrincipalName)) {
            return false;
        }
        String this$schoolPrincipalEmail = this.getSchoolPrincipalEmail();
        String other$schoolPrincipalEmail = other.getSchoolPrincipalEmail();
        if (this$schoolPrincipalEmail == null ? other$schoolPrincipalEmail != null : !this$schoolPrincipalEmail.equals(other$schoolPrincipalEmail)) {
            return false;
        }
        String this$schoolPrincipalPhone = this.getSchoolPrincipalPhone();
        String other$schoolPrincipalPhone = other.getSchoolPrincipalPhone();
        if (this$schoolPrincipalPhone == null ? other$schoolPrincipalPhone != null : !this$schoolPrincipalPhone.equals(other$schoolPrincipalPhone)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        return !(this$status == null ? other$status != null : !this$status.equals(other$status));
    }

    protected boolean canEqual(Object other) {
        return other instanceof School;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Boolean $active = this.getActive();
        result = result * 59 + ($active == null ? 43 : ((Object)$active).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        String $schoolRegistrationNumber = this.getSchoolRegistrationNumber();
        result = result * 59 + ($schoolRegistrationNumber == null ? 43 : $schoolRegistrationNumber.hashCode());
        String $schoolAddress = this.getSchoolAddress();
        result = result * 59 + ($schoolAddress == null ? 43 : $schoolAddress.hashCode());
        String $schoolEmail = this.getSchoolEmail();
        result = result * 59 + ($schoolEmail == null ? 43 : $schoolEmail.hashCode());
        String $schoolPhone = this.getSchoolPhone();
        result = result * 59 + ($schoolPhone == null ? 43 : $schoolPhone.hashCode());
        String $schoolDescription = this.getSchoolDescription();
        result = result * 59 + ($schoolDescription == null ? 43 : $schoolDescription.hashCode());
        String $schoolLogo = this.getSchoolLogo();
        result = result * 59 + ($schoolLogo == null ? 43 : $schoolLogo.hashCode());
        String $schoolImage = this.getSchoolImage();
        result = result * 59 + ($schoolImage == null ? 43 : $schoolImage.hashCode());
        String $schoolCode = this.getSchoolCode();
        result = result * 59 + ($schoolCode == null ? 43 : $schoolCode.hashCode());
        String $schoolPrincipalName = this.getSchoolPrincipalName();
        result = result * 59 + ($schoolPrincipalName == null ? 43 : $schoolPrincipalName.hashCode());
        String $schoolPrincipalEmail = this.getSchoolPrincipalEmail();
        result = result * 59 + ($schoolPrincipalEmail == null ? 43 : $schoolPrincipalEmail.hashCode());
        String $schoolPrincipalPhone = this.getSchoolPrincipalPhone();
        result = result * 59 + ($schoolPrincipalPhone == null ? 43 : $schoolPrincipalPhone.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "School(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", schoolRegistrationNumber=" + this.getSchoolRegistrationNumber() + ", schoolAddress=" + this.getSchoolAddress() + ", schoolEmail=" + this.getSchoolEmail() + ", schoolPhone=" + this.getSchoolPhone() + ", schoolDescription=" + this.getSchoolDescription() + ", schoolLogo=" + this.getSchoolLogo() + ", schoolImage=" + this.getSchoolImage() + ", schoolCode=" + this.getSchoolCode() + ", schoolPrincipalName=" + this.getSchoolPrincipalName() + ", schoolPrincipalEmail=" + this.getSchoolPrincipalEmail() + ", schoolPrincipalPhone=" + this.getSchoolPrincipalPhone() + ", status=" + this.getStatus() + ", active=" + this.getActive() + ")";
    }
}

