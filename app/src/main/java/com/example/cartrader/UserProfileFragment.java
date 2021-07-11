package com.example.cartrader;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.UserModel;
import com.example.cartrader.state.AppState;

import org.json.JSONObject;

public class UserProfileFragment extends Fragment {

    private TextView labelFirstNameLastNameProfile;
    private TextView labelEmailProfile;
    private TextView labelPhoneNumberProfile;
    private TextView labelCityProfile;
    private TextView labelAddressProfile;
    private TextView labelPostalCodeProfile;

    public UserProfileFragment() {
    }

    private void initComponents(View v) {
        labelFirstNameLastNameProfile = v.findViewById(R.id.labelFirstNameLastNameProfile);
        labelEmailProfile = v.findViewById(R.id.labelEmailProfile);
        labelPhoneNumberProfile = v.findViewById(R.id.labelPhoneNumberProfile);
        labelCityProfile = v.findViewById(R.id.labelCityProfile);
        labelAddressProfile = v.findViewById(R.id.labelAddressProfile);
        labelPostalCodeProfile = v.findViewById(R.id.labelPostalCodeProfile);
    }

    @SuppressLint("HandlerLeak")
    private void initUserProfileData() {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "secured/users/profile", true, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    UserModel user = UserModel.parseJSONObject(new JSONObject(response));

                    labelFirstNameLastNameProfile.setText(user.getFirstName() + " " + user.getLastName());
                    labelEmailProfile.setText(user.getEmail());
                    labelPhoneNumberProfile.setText(user.getPhoneNumber());
                    labelCityProfile.setText(user.getCity());
                    labelAddressProfile.setText(user.getAddress());
                    labelPostalCodeProfile.setText(String.valueOf(user.getPostalCode()));

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initComponents(v);
        initUserProfileData();

        return v;
    }
}