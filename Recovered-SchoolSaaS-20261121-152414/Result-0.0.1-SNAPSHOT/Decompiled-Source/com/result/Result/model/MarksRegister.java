/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.MarksRegister
 *  com.result.Result.model.MarksRegister$StudentMark
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.result.Result.model;

import com.result.Result.model.MarksRegister;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="marks_register")
public class MarksRegister {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private String admissionNo;
    private String contactNo;
    private String motherName;
    private String fatherName;
    private String address;
    private String email;
    private String dob;
    private String studentPhoto;
    private String session;
    private String subject;
    private String examType;
    private double fullMarks;
    private String projectFullMark;
    private String subjectFullMark;
    private String teacherName;
    private List<StudentMark> marks;
    private Object studentDetails;

    @Generated
    public MarksRegister() {
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
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public String getAdmissionNo() {
        return this.admissionNo;
    }

    @Generated
    public String getContactNo() {
        return this.contactNo;
    }

    @Generated
    public String getMotherName() {
        return this.motherName;
    }

    @Generated
    public String getFatherName() {
        return this.fatherName;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getDob() {
        return this.dob;
    }

    @Generated
    public String getStudentPhoto() {
        return this.studentPhoto;
    }

    @Generated
    public String getSession() {
        return this.session;
    }

    @Generated
    public String getSubject() {
        return this.subject;
    }

    @Generated
    public String getExamType() {
        return this.examType;
    }

    @Generated
    public double getFullMarks() {
        return this.fullMarks;
    }

    @Generated
    public String getProjectFullMark() {
        return this.projectFullMark;
    }

    @Generated
    public String getSubjectFullMark() {
        return this.subjectFullMark;
    }

    @Generated
    public String getTeacherName() {
        return this.teacherName;
    }

    @Generated
    public List<StudentMark> getMarks() {
        return this.marks;
    }

    @Generated
    public Object getStudentDetails() {
        return this.studentDetails;
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
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    @Generated
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Generated
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    @Generated
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setDob(String dob) {
        this.dob = dob;
    }

    @Generated
    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    @Generated
    public void setSession(String session) {
        this.session = session;
    }

    @Generated
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Generated
    public void setExamType(String examType) {
        this.examType = examType;
    }

    @Generated
    public void setFullMarks(double fullMarks) {
        this.fullMarks = fullMarks;
    }

    @Generated
    public void setProjectFullMark(String projectFullMark) {
        this.projectFullMark = projectFullMark;
    }

    @Generated
    public void setSubjectFullMark(String subjectFullMark) {
        this.subjectFullMark = subjectFullMark;
    }

    @Generated
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Generated
    public void setMarks(List<StudentMark> marks) {
        this.marks = marks;
    }

    @Generated
    public void setStudentDetails(Object studentDetails) {
        this.studentDetails = studentDetails;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MarksRegister)) {
            return false;
        }
        MarksRegister other = (MarksRegister)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getFullMarks(), other.getFullMarks()) != 0) {
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
        String this$admissionNo = this.getAdmissionNo();
        String other$admissionNo = other.getAdmissionNo();
        if (this$admissionNo == null ? other$admissionNo != null : !this$admissionNo.equals(other$admissionNo)) {
            return false;
        }
        String this$contactNo = this.getContactNo();
        String other$contactNo = other.getContactNo();
        if (this$contactNo == null ? other$contactNo != null : !this$contactNo.equals(other$contactNo)) {
            return false;
        }
        String this$motherName = this.getMotherName();
        String other$motherName = other.getMotherName();
        if (this$motherName == null ? other$motherName != null : !this$motherName.equals(other$motherName)) {
            return false;
        }
        String this$fatherName = this.getFatherName();
        String other$fatherName = other.getFatherName();
        if (this$fatherName == null ? other$fatherName != null : !this$fatherName.equals(other$fatherName)) {
            return false;
        }
        String this$address = this.getAddress();
        String other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$dob = this.getDob();
        String other$dob = other.getDob();
        if (this$dob == null ? other$dob != null : !this$dob.equals(other$dob)) {
            return false;
        }
        String this$studentPhoto = this.getStudentPhoto();
        String other$studentPhoto = other.getStudentPhoto();
        if (this$studentPhoto == null ? other$studentPhoto != null : !this$studentPhoto.equals(other$studentPhoto)) {
            return false;
        }
        String this$session = this.getSession();
        String other$session = other.getSession();
        if (this$session == null ? other$session != null : !this$session.equals(other$session)) {
            return false;
        }
        String this$subject = this.getSubject();
        String other$subject = other.getSubject();
        if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject)) {
            return false;
        }
        String this$examType = this.getExamType();
        String other$examType = other.getExamType();
        if (this$examType == null ? other$examType != null : !this$examType.equals(other$examType)) {
            return false;
        }
        String this$projectFullMark = this.getProjectFullMark();
        String other$projectFullMark = other.getProjectFullMark();
        if (this$projectFullMark == null ? other$projectFullMark != null : !this$projectFullMark.equals(other$projectFullMark)) {
            return false;
        }
        String this$subjectFullMark = this.getSubjectFullMark();
        String other$subjectFullMark = other.getSubjectFullMark();
        if (this$subjectFullMark == null ? other$subjectFullMark != null : !this$subjectFullMark.equals(other$subjectFullMark)) {
            return false;
        }
        String this$teacherName = this.getTeacherName();
        String other$teacherName = other.getTeacherName();
        if (this$teacherName == null ? other$teacherName != null : !this$teacherName.equals(other$teacherName)) {
            return false;
        }
        List this$marks = this.getMarks();
        List other$marks = other.getMarks();
        if (this$marks == null ? other$marks != null : !((Object)this$marks).equals(other$marks)) {
            return false;
        }
        Object this$studentDetails = this.getStudentDetails();
        Object other$studentDetails = other.getStudentDetails();
        return !(this$studentDetails == null ? other$studentDetails != null : !this$studentDetails.equals(other$studentDetails));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof MarksRegister;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $fullMarks = Double.doubleToLongBits(this.getFullMarks());
        result = result * 59 + (int)($fullMarks >>> 32 ^ $fullMarks);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $admissionNo = this.getAdmissionNo();
        result = result * 59 + ($admissionNo == null ? 43 : $admissionNo.hashCode());
        String $contactNo = this.getContactNo();
        result = result * 59 + ($contactNo == null ? 43 : $contactNo.hashCode());
        String $motherName = this.getMotherName();
        result = result * 59 + ($motherName == null ? 43 : $motherName.hashCode());
        String $fatherName = this.getFatherName();
        result = result * 59 + ($fatherName == null ? 43 : $fatherName.hashCode());
        String $address = this.getAddress();
        result = result * 59 + ($address == null ? 43 : $address.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $dob = this.getDob();
        result = result * 59 + ($dob == null ? 43 : $dob.hashCode());
        String $studentPhoto = this.getStudentPhoto();
        result = result * 59 + ($studentPhoto == null ? 43 : $studentPhoto.hashCode());
        String $session = this.getSession();
        result = result * 59 + ($session == null ? 43 : $session.hashCode());
        String $subject = this.getSubject();
        result = result * 59 + ($subject == null ? 43 : $subject.hashCode());
        String $examType = this.getExamType();
        result = result * 59 + ($examType == null ? 43 : $examType.hashCode());
        String $projectFullMark = this.getProjectFullMark();
        result = result * 59 + ($projectFullMark == null ? 43 : $projectFullMark.hashCode());
        String $subjectFullMark = this.getSubjectFullMark();
        result = result * 59 + ($subjectFullMark == null ? 43 : $subjectFullMark.hashCode());
        String $teacherName = this.getTeacherName();
        result = result * 59 + ($teacherName == null ? 43 : $teacherName.hashCode());
        List $marks = this.getMarks();
        result = result * 59 + ($marks == null ? 43 : ((Object)$marks).hashCode());
        Object $studentDetails = this.getStudentDetails();
        result = result * 59 + ($studentDetails == null ? 43 : $studentDetails.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "MarksRegister(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", admissionNo=" + this.getAdmissionNo() + ", contactNo=" + this.getContactNo() + ", motherName=" + this.getMotherName() + ", fatherName=" + this.getFatherName() + ", address=" + this.getAddress() + ", email=" + this.getEmail() + ", dob=" + this.getDob() + ", studentPhoto=" + this.getStudentPhoto() + ", session=" + this.getSession() + ", subject=" + this.getSubject() + ", examType=" + this.getExamType() + ", fullMarks=" + this.getFullMarks() + ", projectFullMark=" + this.getProjectFullMark() + ", subjectFullMark=" + this.getSubjectFullMark() + ", teacherName=" + this.getTeacherName() + ", marks=" + this.getMarks() + ", studentDetails=" + this.getStudentDetails() + ")";
    }
}

