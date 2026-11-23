/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.BookIssue
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.NewLibrary.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="book_issues")
public class BookIssue {
    @Id
    private String id;
    private String bookName;
    private String bookNumber;
    private int quantity;
    private String studentName;
    private String studentId;
    private String classAssigned;
    private String section;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status;
    private String schoolId;

    @Generated
    public BookIssue() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getBookName() {
        return this.bookName;
    }

    @Generated
    public String getBookNumber() {
        return this.bookNumber;
    }

    @Generated
    public int getQuantity() {
        return this.quantity;
    }

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public String getStudentId() {
        return this.studentId;
    }

    @Generated
    public String getClassAssigned() {
        return this.classAssigned;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    @Generated
    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    @Generated
    public String getStatus() {
        return this.status;
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
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Generated
    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    @Generated
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Generated
    public void setClassAssigned(String classAssigned) {
        this.classAssigned = classAssigned;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Generated
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
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
        if (!(o instanceof BookIssue)) {
            return false;
        }
        BookIssue other = (BookIssue)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getQuantity() != other.getQuantity()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$bookName = this.getBookName();
        String other$bookName = other.getBookName();
        if (this$bookName == null ? other$bookName != null : !this$bookName.equals(other$bookName)) {
            return false;
        }
        String this$bookNumber = this.getBookNumber();
        String other$bookNumber = other.getBookNumber();
        if (this$bookNumber == null ? other$bookNumber != null : !this$bookNumber.equals(other$bookNumber)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$studentId = this.getStudentId();
        String other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !this$studentId.equals(other$studentId)) {
            return false;
        }
        String this$classAssigned = this.getClassAssigned();
        String other$classAssigned = other.getClassAssigned();
        if (this$classAssigned == null ? other$classAssigned != null : !this$classAssigned.equals(other$classAssigned)) {
            return false;
        }
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        LocalDate this$issueDate = this.getIssueDate();
        LocalDate other$issueDate = other.getIssueDate();
        if (this$issueDate == null ? other$issueDate != null : !((Object)this$issueDate).equals(other$issueDate)) {
            return false;
        }
        LocalDate this$returnDate = this.getReturnDate();
        LocalDate other$returnDate = other.getReturnDate();
        if (this$returnDate == null ? other$returnDate != null : !((Object)this$returnDate).equals(other$returnDate)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BookIssue;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getQuantity();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $bookName = this.getBookName();
        result = result * 59 + ($bookName == null ? 43 : $bookName.hashCode());
        String $bookNumber = this.getBookNumber();
        result = result * 59 + ($bookNumber == null ? 43 : $bookNumber.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : $studentId.hashCode());
        String $classAssigned = this.getClassAssigned();
        result = result * 59 + ($classAssigned == null ? 43 : $classAssigned.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        LocalDate $issueDate = this.getIssueDate();
        result = result * 59 + ($issueDate == null ? 43 : ((Object)$issueDate).hashCode());
        LocalDate $returnDate = this.getReturnDate();
        result = result * 59 + ($returnDate == null ? 43 : ((Object)$returnDate).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "BookIssue(id=" + this.getId() + ", bookName=" + this.getBookName() + ", bookNumber=" + this.getBookNumber() + ", quantity=" + this.getQuantity() + ", studentName=" + this.getStudentName() + ", studentId=" + this.getStudentId() + ", classAssigned=" + this.getClassAssigned() + ", section=" + this.getSection() + ", issueDate=" + this.getIssueDate() + ", returnDate=" + this.getReturnDate() + ", status=" + this.getStatus() + ", schoolId=" + this.getSchoolId() + ")";
    }
}

