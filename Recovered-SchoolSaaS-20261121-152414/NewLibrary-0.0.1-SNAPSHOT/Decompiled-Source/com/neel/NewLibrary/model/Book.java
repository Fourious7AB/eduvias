/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Book
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.NewLibrary.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="books")
public class Book {
    @Id
    private String id;
    private String bookName;
    private String author;
    private String bookNumber;
    private int bookQuantity;
    private String bookCategory;
    private String classAssigned;
    private String status;
    private String schoolId;

    public void updateStatus() {
        this.status = this.bookQuantity > 0 ? "Available " + this.bookQuantity : "Unavailable";
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
    public String getAuthor() {
        return this.author;
    }

    @Generated
    public String getBookNumber() {
        return this.bookNumber;
    }

    @Generated
    public int getBookQuantity() {
        return this.bookQuantity;
    }

    @Generated
    public String getBookCategory() {
        return this.bookCategory;
    }

    @Generated
    public String getClassAssigned() {
        return this.classAssigned;
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
    public void setAuthor(String author) {
        this.author = author;
    }

    @Generated
    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    @Generated
    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    @Generated
    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Generated
    public void setClassAssigned(String classAssigned) {
        this.classAssigned = classAssigned;
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
        if (!(o instanceof Book)) {
            return false;
        }
        Book other = (Book)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getBookQuantity() != other.getBookQuantity()) {
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
        String this$author = this.getAuthor();
        String other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) {
            return false;
        }
        String this$bookNumber = this.getBookNumber();
        String other$bookNumber = other.getBookNumber();
        if (this$bookNumber == null ? other$bookNumber != null : !this$bookNumber.equals(other$bookNumber)) {
            return false;
        }
        String this$bookCategory = this.getBookCategory();
        String other$bookCategory = other.getBookCategory();
        if (this$bookCategory == null ? other$bookCategory != null : !this$bookCategory.equals(other$bookCategory)) {
            return false;
        }
        String this$classAssigned = this.getClassAssigned();
        String other$classAssigned = other.getClassAssigned();
        if (this$classAssigned == null ? other$classAssigned != null : !this$classAssigned.equals(other$classAssigned)) {
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
        return other instanceof Book;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getBookQuantity();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $bookName = this.getBookName();
        result = result * 59 + ($bookName == null ? 43 : $bookName.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        String $bookNumber = this.getBookNumber();
        result = result * 59 + ($bookNumber == null ? 43 : $bookNumber.hashCode());
        String $bookCategory = this.getBookCategory();
        result = result * 59 + ($bookCategory == null ? 43 : $bookCategory.hashCode());
        String $classAssigned = this.getClassAssigned();
        result = result * 59 + ($classAssigned == null ? 43 : $classAssigned.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Book(id=" + this.getId() + ", bookName=" + this.getBookName() + ", author=" + this.getAuthor() + ", bookNumber=" + this.getBookNumber() + ", bookQuantity=" + this.getBookQuantity() + ", bookCategory=" + this.getBookCategory() + ", classAssigned=" + this.getClassAssigned() + ", status=" + this.getStatus() + ", schoolId=" + this.getSchoolId() + ")";
    }

    @Generated
    public Book() {
    }

    @Generated
    public Book(String id, String bookName, String author, String bookNumber, int bookQuantity, String bookCategory, String classAssigned, String status, String schoolId) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.bookNumber = bookNumber;
        this.bookQuantity = bookQuantity;
        this.bookCategory = bookCategory;
        this.classAssigned = classAssigned;
        this.status = status;
        this.schoolId = schoolId;
    }
}

