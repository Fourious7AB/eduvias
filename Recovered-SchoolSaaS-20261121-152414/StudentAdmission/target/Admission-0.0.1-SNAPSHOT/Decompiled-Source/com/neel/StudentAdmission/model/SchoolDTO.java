/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.SchoolDTO
 *  lombok.Generated
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;
import org.springframework.web.multipart.MultipartFile;

public class SchoolDTO {
    private String schoolName;
    private String schoolCode;
    private MultipartFile schoolLogo;
    private MultipartFile schoolImage;
    private String schoolRegistrationNumber;
    private String schoolAddress;
    private String schoolEmail;
    private String schoolPhone;
    private String schoolDescription;
    private String schoolPrincipalName;
    private String schoolPrincipalEmail;
    private String schoolPrincipalPhone;

    @Generated
    public SchoolDTO() {
    }

    @Generated
    public String getSchoolName() {
        return this.schoolName;
    }

    @Generated
    public String getSchoolCode() {
        return this.schoolCode;
    }

    @Generated
    public MultipartFile getSchoolLogo() {
        return this.schoolLogo;
    }

    @Generated
    public MultipartFile getSchoolImage() {
        return this.schoolImage;
    }

    @Generated
    public String getSchoolRegistrationNumber() {
        return this.schoolRegistrationNumber;
    }

    @Generated
    public String getSchoolAddress() {
        return this.schoolAddress;
    }

    @Generated
    public String getSchoolEmail() {
        return this.schoolEmail;
    }

    @Generated
    public String getSchoolPhone() {
        return this.schoolPhone;
    }

    @Generated
    public String getSchoolDescription() {
        return this.schoolDescription;
    }

    @Generated
    public String getSchoolPrincipalName() {
        return this.schoolPrincipalName;
    }

    @Generated
    public String getSchoolPrincipalEmail() {
        return this.schoolPrincipalEmail;
    }

    @Generated
    public String getSchoolPrincipalPhone() {
        return this.schoolPrincipalPhone;
    }

    @Generated
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Generated
    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    @Generated
    public void setSchoolLogo(MultipartFile schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    @Generated
    public void setSchoolImage(MultipartFile schoolImage) {
        this.schoolImage = schoolImage;
    }

    @Generated
    public void setSchoolRegistrationNumber(String schoolRegistrationNumber) {
        this.schoolRegistrationNumber = schoolRegistrationNumber;
    }

    @Generated
    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    @Generated
    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    @Generated
    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    @Generated
    public void setSchoolDescription(String schoolDescription) {
        this.schoolDescription = schoolDescription;
    }

    @Generated
    public void setSchoolPrincipalName(String schoolPrincipalName) {
        this.schoolPrincipalName = schoolPrincipalName;
    }

    @Generated
    public void setSchoolPrincipalEmail(String schoolPrincipalEmail) {
        this.schoolPrincipalEmail = schoolPrincipalEmail;
    }

    @Generated
    public void setSchoolPrincipalPhone(String schoolPrincipalPhone) {
        this.schoolPrincipalPhone = schoolPrincipalPhone;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SchoolDTO)) {
            return false;
        }
        SchoolDTO other = (SchoolDTO)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
            return false;
        }
        String this$schoolCode = this.getSchoolCode();
        String other$schoolCode = other.getSchoolCode();
        if (this$schoolCode == null ? other$schoolCode != null : !this$schoolCode.equals(other$schoolCode)) {
            return false;
        }
        MultipartFile this$schoolLogo = this.getSchoolLogo();
        MultipartFile other$schoolLogo = other.getSchoolLogo();
        if (this$schoolLogo == null ? other$schoolLogo != null : !this$schoolLogo.equals(other$schoolLogo)) {
            return false;
        }
        MultipartFile this$schoolImage = this.getSchoolImage();
        MultipartFile other$schoolImage = other.getSchoolImage();
        if (this$schoolImage == null ? other$schoolImage != null : !this$schoolImage.equals(other$schoolImage)) {
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
        return !(this$schoolPrincipalPhone == null ? other$schoolPrincipalPhone != null : !this$schoolPrincipalPhone.equals(other$schoolPrincipalPhone));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SchoolDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        String $schoolCode = this.getSchoolCode();
        result = result * 59 + ($schoolCode == null ? 43 : $schoolCode.hashCode());
        MultipartFile $schoolLogo = this.getSchoolLogo();
        result = result * 59 + ($schoolLogo == null ? 43 : $schoolLogo.hashCode());
        MultipartFile $schoolImage = this.getSchoolImage();
        result = result * 59 + ($schoolImage == null ? 43 : $schoolImage.hashCode());
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
        String $schoolPrincipalName = this.getSchoolPrincipalName();
        result = result * 59 + ($schoolPrincipalName == null ? 43 : $schoolPrincipalName.hashCode());
        String $schoolPrincipalEmail = this.getSchoolPrincipalEmail();
        result = result * 59 + ($schoolPrincipalEmail == null ? 43 : $schoolPrincipalEmail.hashCode());
        String $schoolPrincipalPhone = this.getSchoolPrincipalPhone();
        result = result * 59 + ($schoolPrincipalPhone == null ? 43 : $schoolPrincipalPhone.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SchoolDTO(schoolName=" + this.getSchoolName() + ", schoolCode=" + this.getSchoolCode() + ", schoolLogo=" + this.getSchoolLogo() + ", schoolImage=" + this.getSchoolImage() + ", schoolRegistrationNumber=" + this.getSchoolRegistrationNumber() + ", schoolAddress=" + this.getSchoolAddress() + ", schoolEmail=" + this.getSchoolEmail() + ", schoolPhone=" + this.getSchoolPhone() + ", schoolDescription=" + this.getSchoolDescription() + ", schoolPrincipalName=" + this.getSchoolPrincipalName() + ", schoolPrincipalEmail=" + this.getSchoolPrincipalEmail() + ", schoolPrincipalPhone=" + this.getSchoolPrincipalPhone() + ")";
    }
}

