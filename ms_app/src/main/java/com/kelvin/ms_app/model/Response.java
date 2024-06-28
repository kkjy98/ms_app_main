package com.kelvin.ms_app.model;

import lombok.Data;

@Data
public class Response {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
