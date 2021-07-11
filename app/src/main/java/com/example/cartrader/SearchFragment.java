package com.example.cartrader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button buttonSearch;

    public interface OnFragmentInteractionListener {
        void onSearchButtonClick(String json);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            // ...
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private OnFragmentInteractionListener listener;

    private List<ManufacturerModel> manufacturers;
    private List<ModelModel> models;
    private List<FuelTypeModel> fuelTypes;
    private List<CategoryModel> categories;
    private List<DoorCountTypeModel> doorCountTypes;
    private List<ColorTypeModel> colorTypes;
    private List<GearboxTypeModel> gearboxTypes;

    private Spinner inputManufacturer;
    private Spinner inputModelSearch;
    private Spinner inputFuelTypeSearch;
    private Spinner inputCategorySearch;
    private Spinner inputNumberOfDoorsSearch;
    private Spinner inputColorTypeSearch;
    private Spinner inputGearboxTypeSearch;

    private EditText inputStartCcmSearch;
    private EditText inputEndCcmSearch;
    private EditText inputStartKWSearch;
    private EditText inputEndKWSearch;
    private EditText inputStartYearSearch;
    private EditText inputEndYearSearch;
    private EditText inputStartMileageSearch;
    private EditText inputEndMileageSearch;


    @SuppressLint("HandlerLeak")
    private void initColorTypes(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/colorTypes", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    colorTypes = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    colorTypes.add(new ColorTypeModel(0, "Izaberite boju"));
                    colorTypes.addAll(ColorTypeModel.parseJSONArray(array));

                    inputColorTypeSearch = v.findViewById(R.id.inputColorTypeSearch);
                    ArrayAdapter<ColorTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, colorTypes);

                    inputColorTypeSearch.setAdapter(adapter);

                    inputColorTypeSearch.setOnItemSelectedListener(SearchFragment.this);
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
                    gearboxTypes.add(new GearboxTypeModel(0, "Izaberite vrstu menjača"));
                    gearboxTypes.addAll(GearboxTypeModel.parseJSONArray(array));

                    inputGearboxTypeSearch = v.findViewById(R.id.inputGearboxTypeSearch);
                    ArrayAdapter<GearboxTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gearboxTypes);

                    inputGearboxTypeSearch.setAdapter(adapter);

                    inputGearboxTypeSearch.setOnItemSelectedListener(SearchFragment.this);
                } catch (Exception e) {
                    // ...
                }
            }
        });

    }


    @SuppressLint("HandlerLeak")
    private void doSearch() {
        Map<String, String> searchParameters = new HashMap<>();

        final String EMPTY_PREFIX = "Izaberite ";

        if (!((ManufacturerModel) inputManufacturer.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("manufacturer",  ((ManufacturerModel) inputManufacturer.getSelectedItem()).getName());
        }
        if (!((ModelModel) inputModelSearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("model", ((ModelModel) inputModelSearch.getSelectedItem()).getName());
        }
        if (!((FuelTypeModel) inputFuelTypeSearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("fuelType", ((FuelTypeModel) inputFuelTypeSearch.getSelectedItem()).getName());
        }
        if (!((CategoryModel) inputCategorySearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("category", ((CategoryModel) inputCategorySearch.getSelectedItem()).getName());
        }
        if (!((DoorCountTypeModel) inputNumberOfDoorsSearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("doorCount", ((DoorCountTypeModel) inputNumberOfDoorsSearch.getSelectedItem()).getName());
        }

        // Ovde dodati za color i gearbox
        if (!((ColorTypeModel) inputColorTypeSearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("colorType", ((ColorTypeModel) inputColorTypeSearch.getSelectedItem()).getName());
        }

        if (!((GearboxTypeModel)  inputGearboxTypeSearch.getSelectedItem()).getName().startsWith(EMPTY_PREFIX)) {
            searchParameters.put("gearboxType", ((GearboxTypeModel) inputGearboxTypeSearch.getSelectedItem()).getName());
        }

        if ( !inputStartCcmSearch.getText().toString().isEmpty() ) {
            searchParameters.put("startingCcm", String.valueOf(inputStartCcmSearch.getText().toString()));
        }

        if ( !inputEndCcmSearch.getText().toString().isEmpty() ) {
            searchParameters.put("endingCcm", String.valueOf(inputEndCcmSearch.getText().toString()));
        }

        if ( !inputStartKWSearch.getText().toString().isEmpty() ) {
            searchParameters.put("startingKw", String.valueOf(inputStartKWSearch.getText().toString()));
        }

        if ( !inputEndKWSearch.getText().toString().isEmpty() ) {
            searchParameters.put("endingKw", String.valueOf(inputEndKWSearch.getText().toString()));
        }

        if ( !inputStartYearSearch.getText().toString().isEmpty() ) {
            searchParameters.put("startingYear", String.valueOf(inputStartYearSearch.getText().toString()));
        }

        if ( !inputEndYearSearch.getText().toString().isEmpty() ) {
            searchParameters.put("endingYear", String.valueOf(inputEndYearSearch.getText().toString()));
        }

        if ( !inputStartMileageSearch.getText().toString().isEmpty() ) {
            searchParameters.put("startingMileage", String.valueOf(inputStartMileageSearch.getText().toString()));
        }

        if ( !inputEndMileageSearch.getText().toString().isEmpty() ) {
            searchParameters.put("endingMileage", String.valueOf(inputEndMileageSearch.getText().toString()));
        }

        JSONObject payload = new JSONObject(searchParameters);

        CarTraderApi.postDataJSON(AppState.PREFIX_API_URL + "public/vehicles/search", payload,false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();
                listener.onSearchButtonClick(response);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initManufacturers(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/manufacturers", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    manufacturers = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    manufacturers.add(new ManufacturerModel(0, "Izaberite proizvođača"));
                    manufacturers.addAll(ManufacturerModel.parseJSONArray(array));

                    inputManufacturer = v.findViewById(R.id.inputManufacturer);
                    ArrayAdapter<ManufacturerModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, manufacturers);

                    inputManufacturer.setAdapter(adapter);

                    inputManufacturer.setOnItemSelectedListener(SearchFragment.this);
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
                    doorCountTypes.add(new DoorCountTypeModel(0, "Izaberite broj vrata"));
                    JSONArray array = new JSONArray(response);
                    doorCountTypes.addAll(DoorCountTypeModel.parseJSONArray(array));
                    inputNumberOfDoorsSearch = v.findViewById(R.id.inputNumberOfDoorsSearch);
                    ArrayAdapter<DoorCountTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doorCountTypes);
                    SearchFragment.this.inputNumberOfDoorsSearch.setAdapter(adapter);
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
                    categories.add(new CategoryModel(0, "Izaberite tip karoserije", -1));
                    JSONArray array = new JSONArray(response);
                    categories.addAll(CategoryModel.parseJSONArray(array));

                    for (CategoryModel cm : categories) {
                        if (cm.getParentCategoryId() == 0) {
                            categories.remove(cm);
                        }
                    }

                    inputCategorySearch = v.findViewById(R.id.inputCategorySearch);
                    ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
                    inputCategorySearch.setAdapter(adapter);
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
                    fuelTypes.add(new FuelTypeModel(0, "Izaberite vrstu goriva"));
                    JSONArray array = new JSONArray(response);
                    fuelTypes.addAll(FuelTypeModel.parseJSONArray(array));
                    inputFuelTypeSearch = v.findViewById(R.id.inputFuelTypeSearch);
                    ArrayAdapter<FuelTypeModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fuelTypes);
                    inputFuelTypeSearch.setAdapter(adapter);
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
                    models.add(new ModelModel(0, "Izaberite model", null));
                    JSONArray array = new JSONArray(response);
                    models.addAll(ModelModel.parseJSONArray(array));
                    inputModelSearch = v.findViewById(R.id.inputModelSearch);
                    ArrayAdapter<ModelModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, models);
                    inputModelSearch.setAdapter(adapter);
                    if (models.size() == 0) {
                        inputModelSearch.setAdapter(null);
                    }
                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    private void initComponents(View v) {
        buttonSearch = v.findViewById(R.id.buttonSearch);

        inputStartCcmSearch = v.findViewById(R.id.inputStartCcmSearch);
        inputEndCcmSearch = v.findViewById(R.id.inputEndCcmSearch);
        inputStartKWSearch = v.findViewById(R.id.inputStartKWSearch);
        inputEndKWSearch = v.findViewById(R.id.inputEndKWSearch);
        inputStartYearSearch = v.findViewById(R.id.inputStartYearSearch);
        inputEndYearSearch = v.findViewById(R.id.inputEndYearSearch);
        inputStartMileageSearch = v.findViewById(R.id.inputStartMileageSearch);
        inputEndMileageSearch = v.findViewById(R.id.inputEndMileageSearch);

        buttonSearch.setOnClickListener(this);
    }

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

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
            case R.id.buttonSearch:
                doSearch();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.inputManufacturer:
                String manufacturerId = "";

                for (ManufacturerModel m: manufacturers) {
                    if (m.getName().equals(((ManufacturerModel) inputManufacturer.getSelectedItem()).getName())) {
                        manufacturerId = String.valueOf(m.getId());
                    }
                }

                initModels(getView(), manufacturerId);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
