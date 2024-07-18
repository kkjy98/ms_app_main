package com.kelvin.ms_app.service;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.repository.ExpenseRepository;
import com.kelvin.ms_app.util.MathUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class GraphServiceImpl implements GraphService{

    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    public ObjectResponse<JsonObject> getTotalData(String username) {
        List<Expense> expenseList = expenseRepository.getTotalDataSQL(username);

        MathUtil mathUtil = new MathUtil();
        JsonObject jsonObject = new JsonObject();
        Map<String,Double> resultMap = new HashMap<>();
        resultMap =  mathUtil.addition(expenseList);

        jsonObject.addProperty("fb",resultMap.get("fb"));
        jsonObject.addProperty("et",resultMap.get("et"));
        jsonObject.addProperty("hl",resultMap.get("hl"));
        jsonObject.addProperty("tp",resultMap.get("tp"));

        // Create the response object
        ObjectResponse<JsonObject> res = new ObjectResponse<>();
        res.setSuccess(true);
        res.setData(jsonObject);

        return res;
    }
}
