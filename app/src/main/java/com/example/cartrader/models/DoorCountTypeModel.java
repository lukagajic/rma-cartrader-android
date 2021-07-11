package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoorCountTypeModel {
    private long id;
    private String name;

    public DoorCountTypeModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<DoorCountTypeModel> parseJSONArray(JSONArray array) {
        List<DoorCountTypeModel> doorCountTypeModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                DoorCountTypeModel doorCountTypeModel = parseJSONObject(array.getJSONObject(i));
                doorCountTypeModels.add(doorCountTypeModel);
            }
        } catch (Exception e) {
            // ...
        }

        return doorCountTypeModels;
    }

    public static DoorCountTypeModel parseJSONObject(JSONObject object) {
        DoorCountTypeModel doorCountTypeModel = new DoorCountTypeModel();

        try {
            if (object.has("id")) {
                doorCountTypeModel.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                doorCountTypeModel.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return doorCountTypeModel;
    }

    public DoorCountTypeModel() {
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
