package com.kelvin.ms_app.controller;

import com.kelvin.ms_app.service.AccountServiceImpl;
import com.kelvin.ms_app.service.HttpService;
import com.kelvin.ms_app.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    @Autowired
    private HttpService httpService;

    @Autowired
    @Qualifier("apiLogInterceptor")
    private RestTemplate restTemplate;


    @GetMapping
    @PreAuthorize("hasRole('ms_user')")
    public String hello() {
        return "Hello from Spring boot & Keycloak";
    }

    @GetMapping("/hello-2")
    @PreAuthorize("hasRole('ms_admin')")
    public String hello2() {
        return "Hello from Spring boot & Keycloak - ADMIN";
    }

    @GetMapping("/hello-3")
    public String hello3() {
        logger.info("Testing claims");
        logger.info(SecurityUtil.getCurrentUserUsername());
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}
