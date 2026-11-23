/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Category
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.NewLibrary.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="categories")
public class Category {
    @Id
    private String id;
    private String bookCategory;
    private String bookCategoryCode;
    private String schoolId;

    @Generated
    public Category() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getBookCategory() {
        return this.bookCategory;
    }

    @Generated
    public String getBookCategoryCode() {
        return this.bookCategoryCode;
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
    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Generated
    public void setBookCategoryCode(String bookCategoryCode) {
        this.bookCategoryCode = bookCategoryCode;
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
        if (!(o instanceof Category)) {
            return false;
        }
        Category other = (Category)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$bookCategory = this.getBookCategory();
        String other$bookCategory = other.getBookCategory();
        if (this$bookCategory == null ? other$bookCategory != null : !this$bookCategory.equals(other$bookCategory)) {
            return false;
        }
        String this$bookCategoryCode = this.getBookCategoryCode();
        String other$bookCategoryCode = other.getBookCategoryCode();
        if (this$bookCategoryCode == null ? other$bookCategoryCode != null : !this$bookCategoryCode.equals(other$bookCategoryCode)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Category;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $bookCategory = this.getBookCategory();
        result = result * 59 + ($bookCategory == null ? 43 : $bookCategory.hashCode());
        String $bookCategoryCode = this.getBookCategoryCode();
        result = result * 59 + ($bookCategoryCode == null ? 43 : $bookCategoryCode.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Category(id=" + this.getId() + ", bookCategory=" + this.getBookCategory() + ", bookCategoryCode=" + this.getBookCategoryCode() + ", schoolId=" + this.getSchoolId() + ")";
    }
}

