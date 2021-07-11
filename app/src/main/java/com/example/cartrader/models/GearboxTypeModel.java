package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GearboxTypeModel {
    private long id;
    private String name;

    public static GearboxTypeModel parseJSONObject(JSONObject object) {
        GearboxTypeModel gearboxType = new GearboxTypeModel();

        try {
            if (object.has("id")) {
                gearboxType.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                gearboxType.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return gearboxType;
    }

    public static List<GearboxTypeModel> parseJSONArray(JSONArray array) {
        List<GearboxTypeModel> gearboxTypeModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                GearboxTypeModel gearboxType = parseJSONObject(array.getJSONObject(i));
                gearboxTypeModels.add(gearboxType);
            }
        } catch (Exception e) {
            // ...
        }

        return gearboxTypeModels;
    }

    public GearboxTypeModel() {
    }

    public GearboxTypeModel(long id, String name) {
        this.id = id;
        this.name = name;
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
