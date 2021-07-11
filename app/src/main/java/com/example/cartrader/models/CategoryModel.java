package com.example.cartrader.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {
    private long id;
    private String name;
    private long parentCategoryId;



    public static List<CategoryModel> parseJSONArray(JSONArray array) {
        List<CategoryModel> categories = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                CategoryModel category =  parseJSONObject(array.getJSONObject(i));
                categories.add(category);
            }
        } catch (Exception e) {
            // ...
        }
        return categories;
    }

    public static CategoryModel parseJSONObject(JSONObject object) {
        CategoryModel category = new CategoryModel();

        try {
            if (object.has("id")) {
                category.setId(object.getLong("id"));
            }
            if (object.has("name")) {
                category.setName(object.getString("name"));
            }
            if (object.has("parentCategoryId")) {
                category.setParentCategoryId(object.getLong("parentCategoryId"));
            }
        } catch (Exception e) {
            // ...
        }

        return category;
    }

    public CategoryModel(long id, String name, int parentCategoryId) {
        this.id = id;
        this.name = name;
        this.parentCategoryId = parentCategoryId;
    }

    public CategoryModel() {
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

    public long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
