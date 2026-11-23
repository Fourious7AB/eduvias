/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.dto.ApiResponse
 *  lombok.Generated
 */
package com.neel.Teacher.dto;

import lombok.Generated;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse(true, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse(false, message, null);
    }

    @Generated
    public boolean isSuccess() {
        return this.success;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public T getData() {
        return (T)this.data;
    }

    @Generated
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(T data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiResponse)) {
            return false;
        }
        ApiResponse other = (ApiResponse)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isSuccess() != other.isSuccess()) {
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
        return other instanceof ApiResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ApiResponse(success=" + this.isSuccess() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }
}

