package com.kelvin.ms_app.controller;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.service.ExpenseService;
import com.kelvin.ms_app.service.GraphService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    @Autowired
    GraphService graphService;
    @GetMapping("/getTotalData")
    public ResponseEntity<?> getTotalDataByUsername(@RequestParam String username) {
        ObjectResponse<JsonObject> res = graphService.getTotalData(username);
        return ResponseEntity.ok(res.getData().toString());
    }
}
