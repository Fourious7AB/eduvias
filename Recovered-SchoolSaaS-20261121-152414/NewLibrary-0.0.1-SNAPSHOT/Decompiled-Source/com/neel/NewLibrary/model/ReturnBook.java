/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.ReturnBook
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.NewLibrary.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="returnBooks")
public class ReturnBook {
    @Id
    private String id;
    private String bookNumber;
    private String studentId;
    private String message;
    private String schoolId;

    @Generated
    public ReturnBook() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getBookNumber() {
        return this.bookNumber;
    }

    @Generated
    public String getStudentId() {
        return this.studentId;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    @Generated
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReturnBook)) {
            return false;
        }
        ReturnBook other = (ReturnBook)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$bookNumber = this.getBookNumber();
        String other$bookNumber = other.getBookNumber();
        if (this$bookNumber == null ? other$bookNumber != null : !this$bookNumber.equals(other$bookNumber)) {
            return false;
        }
        String this$studentId = this.getStudentId();
        String other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !this$studentId.equals(other$studentId)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ReturnBook;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $bookNumber = this.getBookNumber();
        result = result * 59 + ($bookNumber == null ? 43 : $bookNumber.hashCode());
        String $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : $studentId.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ReturnBook(id=" + this.getId() + ", bookNumber=" + this.getBookNumber() + ", studentId=" + this.getStudentId() + ", message=" + this.getMessage() + ", schoolId=" + this.getSchoolId() + ")";
    }
}

