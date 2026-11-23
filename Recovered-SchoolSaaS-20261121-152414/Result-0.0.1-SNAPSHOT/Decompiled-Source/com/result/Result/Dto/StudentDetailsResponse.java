/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.Dto.StudentDetailsResponse
 *  com.result.Result.Dto.StudentDetailsResponse$StudentData
 *  lombok.Generated
 */
package com.result.Result.Dto;

import com.result.Result.Dto.StudentDetailsResponse;
import lombok.Generated;

public class StudentDetailsResponse {
    private String message;
    private StudentData data;

    @Generated
    public StudentDetailsResponse() {
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public StudentData getData() {
        return this.data;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(StudentData data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StudentDetailsResponse)) {
            return false;
        }
        StudentDetailsResponse other = (StudentDetailsResponse)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        StudentData this$data = this.getData();
        StudentData other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof StudentDetailsResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        StudentData $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StudentDetailsResponse(message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }
}

