/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeeSelection
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="fee_selection")
public class FeeSelection {
    @Id
    private String id;
    private String schoolId;
    private String feeType;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getFeeType() {
        return this.feeType;
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
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    @Generated
    public FeeSelection() {
    }

    @Generated
    public FeeSelection(String id, String schoolId, String feeType) {
        this.id = id;
        this.schoolId = schoolId;
        this.feeType = feeType;
    }
}

