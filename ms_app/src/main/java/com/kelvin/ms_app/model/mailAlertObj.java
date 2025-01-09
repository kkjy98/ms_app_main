package com.kelvin.ms_app.model;

import lombok.Data;

@Data
public class mailAlertObj {

    private String mailsubject;
    private String mailfrom;
    private String mailto;
    private String mailcc;
    private String mailbcc;
    private String mailcontent;
    private String contentImg;
    private String username;
}
