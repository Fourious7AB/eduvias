/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  lombok.Generated
 */
package com.neel.AcademicMain.dto;

import lombok.Generated;

public class ApiResponseDto {
    Boolean success;
    private String message;
    private Object data;

    @Generated
    public Boolean getSuccess() {
        return this.success;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public Object getData() {
        return this.data;
    }

    @Generated
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(Object data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiResponseDto)) {
            return false;
        }
        ApiResponseDto other = (ApiResponseDto)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Boolean this$success = this.getSuccess();
        Boolean other$success = other.getSuccess();
        if (this$success == null ? other$success != null : !((Object)this$success).equals(other$success)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        Object this$data = this.getData();
        Object other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ApiResponseDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Boolean $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : ((Object)$success).hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ApiResponseDto(success=" + this.getSuccess() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    @Generated
    public ApiResponseDto(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    @Generated
    public ApiResponseDto() {
    }
}

