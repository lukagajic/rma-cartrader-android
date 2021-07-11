package com.example.cartrader.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VehicleModel {
    private long id;
    private String manufacturer;
    private String model;
    private String fullModelName;
    private String gearboxType;
    private String category;
    private String fuelType;
    private String colorType;
    private String doorCountType;
    private String excerpt;
    private String description;
    private int engineCapacity;
    private int enginePower;
    private int mileage;
    private int year;
    private int price;
    private boolean isFromOwner;
    private String registrationExpiresAt;
    private String createdAt;
    private List<String> photos;
    private UserModel user;

    private List<String> securityFeatures;
    private List<String> equipmentFeatures;

    public static List<VehicleModel> parseJSONArray(JSONArray array) {
        List<VehicleModel> vehicles = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                VehicleModel vehicle = parseJSONObject(array.getJSONObject(i));
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            // ...
        }

        return vehicles;
    }

    public static VehicleModel parseJSONObject(JSONObject object) {
        VehicleModel vehicle = new VehicleModel();
        try {
            if (object.has("manufacturer")) {
                vehicle.setManufacturer(object.getString("manufacturer"));
            }
            if (object.has("model")) {
                vehicle.setModel(object.getString("model"));
            }
            if (object.has("fullModelName")) {
                vehicle.setFullModelName(object.getString("fullModelName"));
            }
            if (object.has("id")) {
                vehicle.setId(object.getLong("id"));
            }
            if (object.has("excerpt")) {
                vehicle.setExcerpt(object.getString("excerpt"));
            }

            // ovde parsirati nove objekte
            if (object.has("fuelType")) {
                vehicle.setFuelType(object.getString("fuelType"));
            }

            if (object.has("colorType")) {
                vehicle.setColorType(object.getString("colorType"));
            }

            if (object.has("category")) {
                vehicle.setCategory(object.getString("category"));
            }

            if (object.has("doorCountType")) {
                vehicle.setDoorCountType(object.getString("doorCountType"));
            }

            if (object.has("gearboxType")) {
                vehicle.setGearboxType(object.getString("gearboxType"));
            }

            if (object.has("photos")) {
                List<String> photos = new ArrayList<>();
                JSONArray array = object.getJSONArray("photos");
                for (int i = 0; i < array.length(); i++) {
                    photos.add(array.getString(i));
                }
                vehicle.setPhotos(photos);
            }

            if (object.has("securityFeatures")) {
                List<String> securityFeatures = new ArrayList<>();
                JSONArray array = object.getJSONArray("securityFeatures");
                for (int i = 0; i < array.length(); i++) {
                    securityFeatures.add(array.getString(i));
                }
                vehicle.setSecurityFeatures(securityFeatures);
            }

            if (object.has("equipmentFeatures")) {
                List<String> equipmentFeatures = new ArrayList<>();
                JSONArray array = object.getJSONArray("equipmentFeatures");
                for (int i = 0; i < array.length(); i++) {
                    equipmentFeatures.add(array.getString(i));
                }
                vehicle.setEquipmentFeatures(equipmentFeatures);
            }

            if (object.has("engineCapacity")) {
                vehicle.setEngineCapacity(object.getInt("engineCapacity"));
            }
            if (object.has("mileage")) {
                vehicle.setMileage(object.getInt("mileage"));
            }
            if (object.has("price")) {
                vehicle.setPrice(object.getInt("price"));
            }
            if (object.has("year")) {
                vehicle.setYear(object.getInt("year"));
            }
            if (object.has("registrationExpiresAt")) {
                vehicle.setRegistrationExpiresAt(object.getString("registrationExpiresAt"));
            }
            if (object.has("createdAt")) {
                vehicle.setCreatedAt(object.getString("createdAt"));
            }
            if (object.has("fromOwner")) {
                vehicle.setFromOwner(object.getBoolean("fromOwner"));
            }
            if (object.has("description")) {
                vehicle.setDescription(object.getString("description"));
            }
            if (object.has("enginePower")) {
                vehicle.setEnginePower(object.getInt("enginePower"));
            }
            if (object.has("user")) {
                vehicle.setUser(UserModel.parseJSONObject(object.getJSONObject("user")));
            }
        } catch (Exception e) {
            // ...
        }
        return vehicle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullModelName() {
        return fullModelName;
    }

    public void setFullModelName(String fullModelName) {
        this.fullModelName = fullModelName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getColorType() {
        return colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public String getDoorCountType() {
        return doorCountType;
    }

    public void setDoorCountType(String doorCountType) {
        this.doorCountType = doorCountType;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isFromOwner() {
        return isFromOwner;
    }

    public void setFromOwner(boolean fromOwner) {
        isFromOwner = fromOwner;
    }

    public String getRegistrationExpiresAt() {
        return registrationExpiresAt;
    }

    public void setRegistrationExpiresAt(String registrationExpiresAt) {
        this.registrationExpiresAt = registrationExpiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<String> getSecurityFeatures() {
        return securityFeatures;
    }

    public void setSecurityFeatures(List<String> securityFeatures) {
        this.securityFeatures = securityFeatures;
    }

    public List<String> getEquipmentFeatures() {
        return equipmentFeatures;
    }

    public void setEquipmentFeatures(List<String> equipmentFeatures) {
        this.equipmentFeatures = equipmentFeatures;
    }
}
