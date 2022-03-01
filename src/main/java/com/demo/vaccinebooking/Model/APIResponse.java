package com.demo.vaccinebooking.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
public class APIResponse {
    @Getter
    @Setter
    private Boolean success;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private List<Object> data;
}
