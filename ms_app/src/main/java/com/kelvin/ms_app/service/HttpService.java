package com.kelvin.ms_app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;




@Service
public class HttpService {
    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);
   // @Autowired
    //private MessageProperties messageProperties;
//    public ResponseEntity<?> returnApiObjectResponse(Object obj){
//        return this.returnApiObjectResponse("1000", obj);
//    }

//    public ResponseEntity<?> returnApiObjectResponse(String code, Object obj){
//        ApiObjectResponse apiObjectResponse = new ApiObjectResponse(code, messageProperties.getMessageByCode(code), obj);
//        return new ResponseEntity<Object>(apiObjectResponse, HttpStatus.OK);
//    }

}
