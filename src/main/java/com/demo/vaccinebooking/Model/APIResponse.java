package com.demo.vaccinebooking.Model;

import java.util.List;
public class APIResponse {
    private Boolean success;
   private String message;
   private List<Object> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;

    }
}
