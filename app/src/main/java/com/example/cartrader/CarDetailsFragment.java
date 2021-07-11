package com.example.cartrader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartrader.adapters.SliderAdapter;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.helpers.AppDataHelper;
import com.example.cartrader.models.VehicleModel;
import com.example.cartrader.state.AppState;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class CarDetailsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String ARG_VEHICLE_ID = "vehicleId";

    private String vehicleId;

    private VehicleModel selectedCar;


    private void initComponents(View v) {

        TextView labelCarDetails = v.findViewById(R.id.labelCarDetails);
        TextView labelManufacturerValueDetails = v.findViewById(R.id.labelManufacturerValueDetails);
        TextView labelModelValueDetails = v.findViewById(R.id.labelModelValueDetails);
        TextView labelPriceDetails = v.findViewById(R.id.labelPriceDetails);
        TextView labelYearValueDetails = v.findViewById(R.id.labelYearValueDetails);
        TextView labelEngineCapacityValueDetails = v.findViewById(R.id.labelEngineCapacityValueDetails);
        TextView labelEnginePowerValueDetails = v.findViewById(R.id.labelEnginePowerValueDetails);
        TextView labelMileageValueDetails = v.findViewById(R.id.labelMileageValueDetails);

        TextView labelDoorCountValueDetails = v.findViewById(R.id.labelDoorCountValueDetails);
        TextView labelColorValueDetails = v.findViewById(R.id.labelColorValueDetails);
        TextView labelGearboxValueDetails = v.findViewById(R.id.labelGearboxValueDetails);
        TextView labelRegistrationExpiresAtValueDetails = v.findViewById(R.id.labelRegistrationExpiresAtValueDetails);
        TextView labelFromOwnerValueDetails = v.findViewById(R.id.labelFromOwnerValueDetails);
        TextView labelBodyValueDetails = v.findViewById(R.id.labelBodyValueDetails);
        TextView labelSecurityFeaturesValueDetails = v.findViewById(R.id.labelSecurityFeaturesValueDetails);
        TextView labelOwnerValueDetails = v.findViewById(R.id.labelOwnerValueDetails);
        TextView labelEquipmentFeaturesValueDetails = v.findViewById(R.id.labelEquipmentFeaturesValueDetails);
        TextView labelDescriptionDetails = v.findViewById(R.id.labelDescriptionDetails);


        labelCarDetails.setText(selectedCar.getFullModelName() + " " + selectedCar.getExcerpt());
        labelManufacturerValueDetails.setText(selectedCar.getManufacturer());
        labelModelValueDetails.setText(selectedCar.getModel());
        labelPriceDetails.setText("Cena: " + String.valueOf(selectedCar.getPrice()) + " eur");
        labelYearValueDetails.setText(String.valueOf(selectedCar.getYear()));
        labelEngineCapacityValueDetails.setText(String.valueOf(selectedCar.getEngineCapacity()));
        labelEnginePowerValueDetails.setText(String.valueOf(selectedCar.getEnginePower()) + " / " + String.valueOf(AppDataHelper.convertFromKWtoHP(selectedCar.getEnginePower())));
        labelMileageValueDetails.setText(String.valueOf(selectedCar.getMileage()) + " km");
        labelDescriptionDetails.setText(selectedCar.getDescription());

        labelDoorCountValueDetails.setText(selectedCar.getDoorCountType());
        labelColorValueDetails.setText(selectedCar.getColorType());
        labelGearboxValueDetails.setText(selectedCar.getGearboxType());
        labelRegistrationExpiresAtValueDetails.setText(selectedCar.getRegistrationExpiresAt() != null ? AppDataHelper.convertDateFormat(selectedCar.getRegistrationExpiresAt()) : "Nije registrovan");

        String userDetails = String.format("%s %s\n%s\n%s %s\n%s", selectedCar.getUser().getFirstName(),
                                                                        selectedCar.getUser().getLastName(),
                                                                        selectedCar.getUser().getAddress(),
                                                                        String.valueOf(selectedCar.getUser().getPostalCode()),
                                                                        selectedCar.getUser().getCity(),
                                                                        selectedCar.getUser().getPhoneNumber());
        labelOwnerValueDetails.setText(userDetails);
        labelFromOwnerValueDetails.setText(selectedCar.isFromOwner() ? "Da": "Ne");

        labelBodyValueDetails.setText(selectedCar.getCategory());

        BottomNavigationView bottomActions = v.findViewById(R.id.bottomActions);
        bottomActions.setOnNavigationItemSelectedListener(this);

        ViewPager2 imageSourceDetails = v.findViewById(R.id.imageSourceDetails);
        imageSourceDetails.setAdapter(new SliderAdapter(getContext(), selectedCar.getPhotos(), imageSourceDetails));

        String securityFeatures = "";
        for (String s : selectedCar.getSecurityFeatures()) {
            securityFeatures += s +", ";
        }
        securityFeatures = securityFeatures.substring(0, securityFeatures.length() - 2);

        labelSecurityFeaturesValueDetails.setText(securityFeatures);

        String equipmentFeatures = "";

        for (String s : selectedCar.getEquipmentFeatures()) {
            equipmentFeatures += s +", ";
        }

        equipmentFeatures = equipmentFeatures.substring(0, equipmentFeatures.length() - 2);
        labelEquipmentFeaturesValueDetails.setText(equipmentFeatures);

    }



    @SuppressLint("HandlerLeak")
    private void initCarDetails(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/vehicles/" + vehicleId, false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String odgovor = getJson();
                try {
                    JSONObject object = new JSONObject(odgovor);
                    CarDetailsFragment.this.selectedCar = VehicleModel.parseJSONObject(object);

                    initComponents(v);

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    private void handleSendEmail() {
        String sendTo = this.selectedCar.getUser().getEmail();
        String subject = "E-mail vezan za oglas: " + this.selectedCar.getFullModelName();
        String message = "Ovde unesite željeni tekst poruke...";

        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sendTo});
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, message);

        sendEmailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendEmailIntent, "Izaberite e-mail klijent :"));

    }

    @SuppressLint("HandlerLeak")
    private void handleAddToFavorites() {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "secured/users/" + AppState.userId + "/favorites/" + selectedCar.getId(), true, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String odgovor = getJson();

                try {
                    JSONObject object = new JSONObject(odgovor);

                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //...
                }
            }
        });
    }

    private void handleCallOwner() {
        String numberToCall = this.selectedCar.getUser().getPhoneNumber();
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setData(Uri.parse("tel:" + numberToCall));

        if (ActivityCompat.
                checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 4001);
        }

        startActivity(phoneCallIntent);
    }

    public CarDetailsFragment() {

    }

    public static CarDetailsFragment newInstance(String vehicleId) {
        CarDetailsFragment fragment = new CarDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VEHICLE_ID, vehicleId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vehicleId = getArguments().getString(ARG_VEHICLE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_details, container, false);

        initCarDetails(v);

        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_call:
                handleCallOwner();
                break;
            case R.id.act_favorite:
                handleAddToFavorites();
                break;
            case R.id.act_email:
                handleSendEmail();
                break;
        }

        return true;
    }
}