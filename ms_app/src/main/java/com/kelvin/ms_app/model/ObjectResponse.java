package com.kelvin.ms_app.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ObjectResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success = false;
    private String subCode;
    private String message;
    private T data;
}
