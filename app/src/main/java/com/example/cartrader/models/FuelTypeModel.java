package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FuelTypeModel {
    private long id;
    private String name;

    public FuelTypeModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<FuelTypeModel> parseJSONArray(JSONArray array) {
        List<FuelTypeModel> fuelTypeModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                FuelTypeModel fuelType = parseJSONObject(array.getJSONObject(i));
                fuelTypeModels.add(fuelType);
            }
        } catch (Exception e) {
            // ...
        }

        return fuelTypeModels;
    }

    public static FuelTypeModel parseJSONObject(JSONObject object) {
        FuelTypeModel fuelType = new FuelTypeModel();

        try {
            if (object.has("id")) {
                fuelType.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                fuelType.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return fuelType;
    }

    public FuelTypeModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
