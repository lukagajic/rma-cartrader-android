package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityModel {
    private long id;
    private String name;

    public static List<CityModel> parseJSONArray(JSONArray array) {
        List<CityModel> cityModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                CityModel cityModel = parseJSONObject(array.getJSONObject(i));
                cityModels.add(cityModel);
            }
        } catch (Exception e) {
            // ...
        }

        return cityModels;
    }

    public static CityModel parseJSONObject(JSONObject object) {
        CityModel cityModel = new CityModel();

        try {
            if (object.has("id")) {
                cityModel.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                cityModel.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return cityModel;
    }

    public CityModel() {
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
