package com.kelvin.ms_app.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiObjectResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Object data;

    public ApiObjectResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiObjectResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
