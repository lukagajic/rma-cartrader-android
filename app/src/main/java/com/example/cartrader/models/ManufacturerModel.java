package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerModel {
    private long id;
    private String name;

    public ManufacturerModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<ManufacturerModel> parseJSONArray(JSONArray array) {
        List<ManufacturerModel> manufacturers = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                ManufacturerModel manufacturer = parseJSONObject(array.getJSONObject(i));
                manufacturers.add(manufacturer);
            }
        } catch (Exception e) {
            // ...
        }

        return manufacturers;
    }

    public static ManufacturerModel parseJSONObject(JSONObject object) {
        ManufacturerModel manufacturer = new ManufacturerModel();

        try {
            if (object.has("id")) {
                manufacturer.setId(object.getInt("id"));
            }
            if (object.has("name")) {
                manufacturer.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return manufacturer;
    }

    public ManufacturerModel() {
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
        return this.name;
    }
}
