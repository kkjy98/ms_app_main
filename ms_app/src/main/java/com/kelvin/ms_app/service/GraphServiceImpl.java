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
        Map<String, Map<String, Double>> resultMap = mathUtil.addition(expenseList);

        // Create the response JSON object
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Map<String, Double>> entry : resultMap.entrySet()) {
            String month = entry.getKey();
            Map<String, Double> totals = entry.getValue();

            JsonObject monthJsonObject = new JsonObject();
            monthJsonObject.addProperty("fb", totals.get("fb"));
            monthJsonObject.addProperty("et", totals.get("et"));
            monthJsonObject.addProperty("hl", totals.get("hl"));
            monthJsonObject.addProperty("tp", totals.get("tp"));

            jsonObject.add(month, monthJsonObject);
        }

        // Create the response object
        ObjectResponse<JsonObject> res = new ObjectResponse<>();
        res.setData(jsonObject);

        return res;
    }
}
