package com.example.cartrader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.CategoryModel;
import com.example.cartrader.models.ColorTypeModel;
import com.example.cartrader.models.DoorCountTypeModel;
import com.example.cartrader.models.FuelTypeModel;
import com.example.cartrader.models.GearboxTypeModel;
import com.example.cartrader.models.ManufacturerModel;
import com.example.cartrader.models.ModelModel;
import com.example.cartrader.state.AppState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCarFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public interface OnFragmentInteractionListener {
        public void onAddCarSuccess();
    }

    private OnFragmentInteractionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCarFragment.OnFragmentInteractionListener) {
            listener = (AddCarFragment.OnFragmentInteractionListener) context;
        } else {
            // ...
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.inputManufacturerAddCar:

                String manufacturerId = "";

                for (ManufacturerModel m : manufacturers) {
                    if (m.getName().equals(((ManufacturerModel) inputManufacturerAddCar.getSelectedItem()).getName())) {
                        manufacturerId = String.valueOf(m.getId());
                    }
                }

                initModels(getView(), manufacturerId);
                break;

            case R.id.inputCategoryAddCar:
                long categoryId = ((CategoryModel) inputCategoryAddCar.getSelectedItem()).getId();
                getSecurityFeaturesForCategory(categoryId);
                getEquipmentFeaturesForCategory(categoryId);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private void getSecurityFeaturesForCategory(long categoryId) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/categories/" + categoryId + "/securityFeatures", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();
                try {
                    JSONArray array = new JSONArray(response);
                    securityFeaturesArray = new String[array.length()];

                    for (int i = 0; i < array.length(); i++) {
                        securityFeaturesArray[i] = array.getJSONObject(i).getString("name");
                    }

                    seletedSecurityFeaturesArray = new boolean[securityFeaturesArray.length];

                    for (int i = 0; i < seletedSecurityFeaturesArray.length; i++) {
                        seletedSecurityFeaturesArray[i] = false;
                    }

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void getEquipmentFeaturesForCategory(long categoryId) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/categories/" + categoryId + "/equipmentFeatures", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    JSONArray array = new JSONArray(response);
                    equipmentFeaturesArray = new String[array.length()];

                    for (int i = 0; i < array.length(); i++) {
                        equipmentFeaturesArray[i] = array.getJSONObject(i).getString("name");
                    }

                    selectedEquipmentFeaturesArray = new boolean[equipmentFeaturesArray.length];

                    for (int i = 0; i < selectedEquipmentFeaturesArray.length; i++) {
                        selectedEquipmentFeaturesArray[i] = false;
                    }

                } catch (Exception e) {

                }
            }
        });
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Button buttonSelectSecurityFeaturesAddCar;
    private Button buttonSelectEquipmentFeaturesAddCar;

    private String[] securityFeaturesArray;
    private boolean[] seletedSecurityFeaturesArray;

    private String[] equipmentFeaturesArray;
    private boolean[] selectedEquipmentFeaturesArray;

    private List<Integer> securityFeaturesItems = new ArrayList<>();
    private List<Integer> equipmentFeaturesItems = new ArrayList<>();


    private List<ManufacturerModel> manufacturers;
    private List<ModelModel> models;
    private List<FuelTypeModel> fuelTypes;
    private List<CategoryModel> categories;
    private List<DoorCountTypeModel> doorCountTypes;
    private List<ColorTypeModel> colorTypes;
    private List<GearboxTypeModel> gearboxTypes;

    private Spinner inputManufacturerAddCar;
    private Spinner inputModelAddCar;
    private Spinner inputFuelTypeAddCar;
    private Spinner inputCategoryAddCar;
    private Spinner inputDoorCountAddCar;
    private Spinner inputColorAddCar;
    private Spinner inputGearboxAddCar;

    private EditText inputExcerptAddCar;
    private EditText inputDescriptionAddCar;

    // private LinearLayout linearLayoutRegistrationAddCar;

    private CheckBox inputNotRegisteredAddCar;

    private TextView labelRegistrationExpiresAtAddCar;
    private DatePicker inputRegistrationExpiresAtAddCar;

    private EditText inputEngineCapacityAddCar;
    private EditText inputEnginePowerAddCar;
    private EditText inputYearAddCar;
    private EditText inputMileageAddCar;

    private EditText inputPriceAddCar;
    private CheckBox inputIsFromOwnerAddCar;
    private Button buttonSelectCarImages;
    private List<Bitmap> bitmaps;

    private Button buttonAddCar;


    // Sve api metode

    @SuppressLint("HandlerLeak")
    private void initManufacturers(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/manufacturers", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    manufacturers = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    manufacturers.addAll(ManufacturerModel.parseJSONArray(array));

                    inputManufacturerAddCar = v.findViewById(R.id.inputManufacturerAddCar);
                    ArrayAdapter<ManufacturerModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, manufacturers);

                    inputManufacturerAddCar.setAdapter(adapter);

                    inputManufacturerAddCar.setOnItemSelectedListener(AddCarFragment.this);
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initColorTypes(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/colorTypes", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    colorTypes = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    colorTypes.addAll(ColorTypeModel.parseJSONArray(array));

                    inputColorAddCar = v.findViewById(R.id.inputColorAddCar);
                    ArrayAdapter<ColorTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, colorTypes);

                    inputColorAddCar.setAdapter(adapter);

                    inputColorAddCar.setOnItemSelectedListener(AddCarFragment.this);
                } catch (Exception e) {
                    // ...
                }
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private void initGearboxTypes(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/gearboxTypes", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    gearboxTypes = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    gearboxTypes.addAll(GearboxTypeModel.parseJSONArray(array));

                    inputGearboxAddCar = v.findViewById(R.id.inputGearboxAddCar);
                    ArrayAdapter<GearboxTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gearboxTypes);

                    inputGearboxAddCar.setAdapter(adapter);

                    inputGearboxAddCar.setOnItemSelectedListener(AddCarFragment.this);
                } catch (Exception e) {
                    // ...
                }
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private void initDoorCountOptions(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/doorCountTypes", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    doorCountTypes = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    doorCountTypes.addAll(DoorCountTypeModel.parseJSONArray(array));
                    inputDoorCountAddCar = v.findViewById(R.id.inputDoorCountAddCar);
                    ArrayAdapter<DoorCountTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doorCountTypes);
                    inputDoorCountAddCar.setAdapter(adapter);
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initCategories(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/categories", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    categories = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    categories.addAll(CategoryModel.parseJSONArray(array));

                    for (CategoryModel cm : categories) {
                        if (cm.getParentCategoryId() == 0) {
                            categories.remove(cm);
                        }
                    }

                    inputCategoryAddCar = v.findViewById(R.id.inputCategoryAddCar);
                    ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
                    inputCategoryAddCar.setAdapter(adapter);

                    inputCategoryAddCar.setOnItemSelectedListener(AddCarFragment.this);
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initFuelTypes(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/fuelTypes", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    fuelTypes = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    fuelTypes.addAll(FuelTypeModel.parseJSONArray(array));
                    inputFuelTypeAddCar = v.findViewById(R.id.inputFuelTypeAddCar);
                    ArrayAdapter<FuelTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fuelTypes);
                    inputFuelTypeAddCar.setAdapter(adapter);
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initModels(View v, String manufacturerId) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/manufacturers/" + manufacturerId + "/models", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    models = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    models.addAll(ModelModel.parseJSONArray(array));
                    inputModelAddCar = v.findViewById(R.id.inputModelAddCar);
                    ArrayAdapter<ModelModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, models);
                    inputModelAddCar.setAdapter(adapter);
                    if (models.size() == 0) {
                        inputModelAddCar.setAdapter(null);
                    }
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }


    private void initComponents(View v) {
        buttonSelectCarImages = v.findViewById(R.id.buttonSelectCarImages);

        inputIsFromOwnerAddCar = v.findViewById(R.id.inputIsFromOwnerAddCar);

        buttonSelectCarImages.setOnClickListener(this);

        labelRegistrationExpiresAtAddCar = v.findViewById(R.id.labelRegistrationExpiresAtAddCar);
        inputRegistrationExpiresAtAddCar = v.findViewById(R.id.inputRegistrationExpiresAtAddCar);

        inputNotRegisteredAddCar = v.findViewById(R.id.inputNotRegisteredAddCar);

        inputNotRegisteredAddCar.setOnClickListener(this);

        inputEngineCapacityAddCar = v.findViewById(R.id.inputEngineCapacityAddCar);
        inputEnginePowerAddCar = v.findViewById(R.id.inputEnginePowerAddCar);
        inputYearAddCar = v.findViewById(R.id.inputYearAddCar);
        inputMileageAddCar = v.findViewById(R.id.inputMileageAddCar);

        buttonAddCar = v.findViewById(R.id.buttonAddCar);
        buttonAddCar.setOnClickListener(this);

        inputPriceAddCar = v.findViewById(R.id.inputPriceAddCar);


        inputExcerptAddCar = v.findViewById(R.id.inputExcerptAddCar);
        inputDescriptionAddCar = v.findViewById(R.id.inputDescriptionAddCar);

        buttonSelectSecurityFeaturesAddCar = v.findViewById(R.id.buttonSelectSecurityFeaturesAddCar);
        buttonSelectEquipmentFeaturesAddCar = v.findViewById(R.id.buttonSelectEquipmentFeaturesAddCar);

        buttonSelectSecurityFeaturesAddCar.setOnClickListener(this);
        buttonSelectEquipmentFeaturesAddCar.setOnClickListener(this);

        ArrayList<Integer> items = new ArrayList<>();
    }

    public AddCarFragment() {

    }

    public static AddCarFragment newInstance() {
        AddCarFragment fragment = new AddCarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_car, container, false);

        initComponents(v);
        initManufacturers(v);
        initFuelTypes(v);
        initCategories(v);
        initDoorCountOptions(v);
        initGearboxTypes(v);
        initColorTypes(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputNotRegisteredAddCar:
                onNotRegisteredCheckChange();
                break;
            case R.id.buttonSelectSecurityFeaturesAddCar:
                showSelectSecurityFeaturesDialog();
                break;
            case R.id.buttonSelectEquipmentFeaturesAddCar:
                showSelectEquipmentFeaturesAddCar();
                break;
            case R.id.buttonSelectCarImages:
                selectCarImages();
                break;
            case R.id.buttonAddCar:
                doAddCar();
                break;
        }
    }

    private void selectCarImages() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity().getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setType("image/*");
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("FRAGMENT", "CALLED ONACTIVITYRESULT");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            bitmaps = new ArrayList<>();

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                    }
                }
            } else {
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                }
            }

            Log.d("FRAGMENTADDCAR", "KOLIKO SLIKA: " + bitmaps.size());
        }
    }

    private void showSelectSecurityFeaturesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMultiChoiceItems(securityFeaturesArray, seletedSecurityFeaturesArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    if (!securityFeaturesItems.contains(which)) {
                        securityFeaturesItems.add(which);
                    } else {
                        securityFeaturesItems.remove(which);
                    }
                }
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Izabrali ste sigurnosnu opremu!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Poništi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Obriši sve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < seletedSecurityFeaturesArray.length; i++) {
                    seletedSecurityFeaturesArray[i] = false;
                }
                Toast.makeText(getContext(), "Poništili ste odabranu sigurnosnu opremu!", Toast.LENGTH_SHORT).show();
                securityFeaturesItems.clear();
            }
        });

        builder.create().show();
    }

    private void showSelectEquipmentFeaturesAddCar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMultiChoiceItems(equipmentFeaturesArray, selectedEquipmentFeaturesArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    if (!equipmentFeaturesItems.contains(which)) {
                        equipmentFeaturesItems.add(which);
                    } else {
                        equipmentFeaturesItems.remove(which);
                    }
                }
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Izabrali ste opremu!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Poništi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Obriši sve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedEquipmentFeaturesArray.length; i++) {
                    selectedEquipmentFeaturesArray[i] = false;
                }
                Toast.makeText(getContext(), "Poništili ste odabranu opremu!", Toast.LENGTH_SHORT).show();
                equipmentFeaturesItems.clear();
            }
        });

        builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    private void doAddCar() {
        Map<String, Object> data = new HashMap<>();

        data.put("modelId", ((ModelModel) inputModelAddCar.getSelectedItem()).getId());
        data.put("fuelTypeId", ((FuelTypeModel) inputFuelTypeAddCar.getSelectedItem()).getId());
        data.put("categoryId", ((CategoryModel) inputCategoryAddCar.getSelectedItem()).getId());
        data.put("doorCountTypeId", ((DoorCountTypeModel) inputDoorCountAddCar.getSelectedItem()).getId());
        data.put("colorTypeId", ((ColorTypeModel) inputColorAddCar.getSelectedItem()).getId());
        data.put("gearboxTypeId", ((GearboxTypeModel) inputGearboxAddCar.getSelectedItem()).getId());
        data.put("engineCapacity", Integer.parseInt(inputEngineCapacityAddCar.getText().toString()));
        data.put("enginePower", Integer.parseInt(inputEnginePowerAddCar.getText().toString()));
        data.put("year", Integer.parseInt(inputYearAddCar.getText().toString()));
        data.put("mileage", Integer.parseInt(inputMileageAddCar.getText().toString()));

        data.put("excerpt", inputExcerptAddCar.getText().toString());
        data.put("description", inputDescriptionAddCar.getText().toString());
        data.put("userId", Integer.valueOf(AppState.userId));
        data.put("price", Integer.parseInt(inputPriceAddCar.getText().toString()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(inputRegistrationExpiresAtAddCar.getYear(), inputRegistrationExpiresAtAddCar.getMonth(), inputRegistrationExpiresAtAddCar.getDayOfMonth());


        if (!inputNotRegisteredAddCar.isChecked()) {
            Date registrationExpires = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String registrationExpiresString = sdf.format(registrationExpires);

            data.put("registrationExpiresAt", registrationExpiresString);
        } else {
            data.put("registrationExpiresAt", null);
        }

        List securityFeaturesList = new ArrayList();
        List equipmentFeaturesList = new ArrayList();

        for (int i = 0; i < securityFeaturesArray.length; i++) {
            if (seletedSecurityFeaturesArray[i] == true) {
                securityFeaturesList.add(securityFeaturesArray[i]);
            }
        }

        for (int i = 0; i < equipmentFeaturesArray.length; i++) {
            if (selectedEquipmentFeaturesArray[i] == true) {
                equipmentFeaturesList.add(equipmentFeaturesArray[i]);
            }
        }

        data.put("securityFeatures", securityFeaturesList);
        data.put("equipmentFeatures", equipmentFeaturesList);


        if (inputIsFromOwnerAddCar.isChecked()) {
            data.put("fromOwner", true);
        } else {
            data.put("fromOwner", false);
        }

        try {
            JSONObject object = new JSONObject(data);
            CarTraderApi.postDataJSON(AppState.PREFIX_API_URL + "secured/vehicles", object, true, new ReadDataHandler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    String response = getJson();

                    try {
                        JSONObject backObject = new JSONObject(response);

                        listener.onAddCarSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
        }

    }

    private void uploadImage(String url, Bitmap bitmap) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Slika dodata!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imageString = getStringForBitmap(bitmap);
                Map<String, String> params = new HashMap<>();
                params.put("imageFile", imageString);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private String getStringForBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageInBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageInBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void onNotRegisteredCheckChange() {
        if (inputNotRegisteredAddCar.isChecked()) {
            labelRegistrationExpiresAtAddCar.setVisibility(
                    View.GONE);
            inputRegistrationExpiresAtAddCar.setVisibility(View.GONE);
        } else {
            labelRegistrationExpiresAtAddCar.setVisibility(View.VISIBLE);
            inputRegistrationExpiresAtAddCar.setVisibility(View.VISIBLE);
        }
    }
}