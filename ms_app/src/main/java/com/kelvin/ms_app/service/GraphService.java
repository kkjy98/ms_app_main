package com.kelvin.ms_app.service;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ObjectResponse;
import com.nimbusds.jose.shaded.gson.JsonObject;

import java.util.List;

public interface GraphService {
    ObjectResponse<JsonObject> getTotalData(String username);
}
