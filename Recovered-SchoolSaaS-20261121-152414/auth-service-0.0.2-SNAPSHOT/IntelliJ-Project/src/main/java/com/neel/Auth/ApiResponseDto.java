/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.ApiResponseDto
 */
package com.neel.Auth;

public class ApiResponseDto {
    private boolean success;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

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

    protected boolean canEqual(Object other) {
        return other instanceof ApiResponseDto;
    }

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

    public String toString() {
        return "ApiResponseDto(success=" + this.isSuccess() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    public ApiResponseDto(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}

