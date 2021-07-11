package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ColorTypeModel {
    private long id;
    private String name;

    public static ColorTypeModel parseJSONObject(JSONObject object) {
        ColorTypeModel colorType = new ColorTypeModel();

        try {
            if (object.has("id")) {
                colorType.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                colorType.setName(object.getString("name"));
            }
        } catch (Exception e) {
            // ...
        }

        return colorType;
    }

    public static List<ColorTypeModel> parseJSONArray(JSONArray array) {
        List<ColorTypeModel> colorTypes = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                ColorTypeModel colorType = parseJSONObject(array.getJSONObject(i));
                colorTypes.add(colorType);
            }
        } catch (Exception e) {
            // ...
        }

        return colorTypes;
    }

    public ColorTypeModel() {
    }

    public ColorTypeModel(long id, String name) {
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
