package com.example.cartrader.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoModel {
    private long id;
    private String path;

    public static List<PhotoModel> parseJSONArray(JSONArray array) {
        List<PhotoModel> photoModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                PhotoModel photoModel = parseJSONObject(array.getJSONObject(i));
                photoModels.add(photoModel);
            }
        } catch (Exception e) {
            // ...
        }

        return photoModels;
    }

    public static PhotoModel parseJSONObject(JSONObject object) {
        PhotoModel photoModel = new PhotoModel();

        try {
            if (object.has("id")) {
                photoModel.setId(object.getLong("id"));
            }
            if (object.has("path")) {
                photoModel.setPath(object.getString("path"));
            }
        } catch(Exception e) {
            // ...
        }

        return photoModel;
    }

    public PhotoModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
