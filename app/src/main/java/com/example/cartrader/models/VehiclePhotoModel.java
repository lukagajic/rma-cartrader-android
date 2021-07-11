package com.example.cartrader.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VehiclePhotoModel {
    private long id;
    private VehicleModel vehicle;
    private PhotoModel photo;

    public static VehiclePhotoModel parseJSONObject(JSONObject object) {
        VehiclePhotoModel vehiclePhotoModel = new VehiclePhotoModel();

        try {
            if (object.has("id")) {
                vehiclePhotoModel.setId(object.getLong("id"));
            }
            if (object.has("vehicle")) {
                VehicleModel vehicleModel = VehicleModel.parseJSONObject(object.getJSONObject("vehicle"));
                vehiclePhotoModel.setVehicle(vehicleModel);
            }
            if (object.has("photo")) {
                PhotoModel photoModel = PhotoModel.parseJSONObject(object.getJSONObject("photo"));
                vehiclePhotoModel.setPhoto(photoModel);
            }
        } catch (Exception e) {
            // ...
        }

        return vehiclePhotoModel;
    }

    public static List<VehiclePhotoModel> parseJSONArray(JSONArray array) {
        List<VehiclePhotoModel> vehiclePhotoModels = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                VehiclePhotoModel vehiclePhotoModel = parseJSONObject(array.getJSONObject(i));
                vehiclePhotoModels.add(vehiclePhotoModel);
            }
        } catch (Exception e) {
            // ...
        }

        return vehiclePhotoModels;
    }


    public VehiclePhotoModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VehicleModel getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleModel vehicle) {
        this.vehicle = vehicle;
    }

    public PhotoModel getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoModel photo) {
        this.photo = photo;
    }
}
