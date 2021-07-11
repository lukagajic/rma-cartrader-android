package com.example.cartrader.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String city;
    private int postalCode;

    // TODO: Implementirati parsiranje...

    public static UserModel parseJSONObject(JSONObject object) {
        UserModel user = new UserModel();

        try {

            if (object.has("id")) {
                user.setId(object.getLong("id"));
            }
            if (object.has("firstName")) {
                user.setFirstName(object.getString("firstName"));
            }
            if (object.has("lastName")) {
                user.setLastName(object.getString("lastName"));
            }
            if (object.has("email")) {
                user.setEmail(object.getString("email"));
            }
            if (object.has("address")) {
                user.setAddress(object.getString("address"));
            }
            if (object.has("city")) {
                user.setCity(object.getString("city"));
            }
            if (object.has("phoneNumber")) {
                user.setPhoneNumber(object.getString("phoneNumber"));
            }
            if (object.has("postalCode")) {
                user.setPostalCode(object.getInt("postalCode"));
            }

        } catch (Exception e) {
            // ...
        }


        return user;
    }

    public static List<UserModel> parseJSONArray(JSONArray array) {
        List<UserModel> users = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                UserModel user = parseJSONObject(array.getJSONObject(i));
                users.add(user);
            }
        } catch (Exception e) {
            // ...
        }

        return users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
