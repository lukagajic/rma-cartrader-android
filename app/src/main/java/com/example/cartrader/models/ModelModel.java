package com.example.cartrader.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelModel {
    private long id;
    private String name;
    private ManufacturerModel manufacturer;

    public ModelModel(long id, String name, ManufacturerModel manufacturer) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public static List<ModelModel> parseJSONArray(JSONArray array) {
        List<ModelModel> models = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                ModelModel model = parseJSONObject(array.getJSONObject(i));
                models.add(model);
            }
        } catch (Exception e) {
            // ...
        }

        return models;
    }

    public static ModelModel parseJSONObject(JSONObject object) {
        ModelModel model = new ModelModel();

        try {
            if (object.has("id")) {
                model.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                model.setName(object.getString("name"));
            }
            if (object.has("manufacturer")) {
                ManufacturerModel manufacturer = ManufacturerModel.parseJSONObject(object.getJSONObject("manufacturer"));
                model.setManufacturer(manufacturer);
            }
        } catch (Exception e) {
            // ...
        }

        return model;
    }

    public ModelModel() {
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

    public ManufacturerModel getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerModel manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
