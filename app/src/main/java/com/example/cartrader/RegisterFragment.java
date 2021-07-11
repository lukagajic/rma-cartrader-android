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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.CityModel;
import com.example.cartrader.state.AppState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText inputFirstNameRegister;
    private EditText inputLastNameRegister;
    private EditText inputEmailRegister;
    private EditText inputPasswordRegister;
    private EditText inputPasswordRepeatRegister;
    private EditText inputPhoneNumberRegister;
    private Spinner inputCityRegister;
    private EditText inputAddressRegister;
    private EditText inputPostalCodeRegister;
    private Button buttonRegister;

    private TextView labelErrorsMessageRegister;

    private List<CityModel> cities;

    public interface OnFragmentInteractionListener {
        void onRegisterSuccess();
    }

    private OnFragmentInteractionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
            listener = (RegisterFragment.OnFragmentInteractionListener) context;
        } else {
            // ...
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @SuppressLint("HandlerLeak")
    private void initComponents(View v) {
        inputFirstNameRegister = v.findViewById(R.id.inputFirstNameRegister);
        inputLastNameRegister = v.findViewById(R.id.inputLastNameRegister);
        inputEmailRegister = v.findViewById(R.id.inputEmailRegister);
        inputPasswordRegister = v.findViewById(R.id.inputPasswordRegister);
        inputPasswordRepeatRegister = v.findViewById(R.id.inputPasswordRepeatRegister);
        inputPhoneNumberRegister = v.findViewById(R.id.inputPhoneNumberRegister);
        inputAddressRegister = v.findViewById(R.id.inputAddressRegister);
        inputCityRegister = v.findViewById(R.id.inputCityRegister);
        inputPostalCodeRegister = v.findViewById(R.id.inputPostalCodeRegister);
        labelErrorsMessageRegister = v.findViewById(R.id.labelErrorsMessageRegister);

        buttonRegister = v.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/cities", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    JSONArray array = new JSONArray(response);
                    cities = CityModel.parseJSONArray(array);
                    ArrayAdapter<CityModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
                    inputCityRegister.setAdapter(adapter);

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }


    public RegisterFragment() {
    }


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        initComponents(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                doRegister();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private void doRegister()  {
        String firstName = inputFirstNameRegister.getText().toString();
        String lastName = inputLastNameRegister.getText().toString();
        String email = inputEmailRegister.getText().toString();
        String password = inputPasswordRegister.getText().toString();
        String passwordRepeat = inputPasswordRepeatRegister.getText().toString();

        String phoneNumber = inputPhoneNumberRegister.getText().toString();
        String address = inputAddressRegister.getText().toString();
        int postalCode = Integer.parseInt(inputPostalCodeRegister.getText().toString());
        long cityId = ((CityModel) inputCityRegister.getSelectedItem()).getId();

        labelErrorsMessageRegister.setText("");

        if (
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() ||
            phoneNumber.isEmpty() || postalCode == 0 || address.isEmpty()
        ) {
            labelErrorsMessageRegister.append("Niste popunili sve podatke! \n");
            return;
        }

        if (!firstName.matches("[A-Z][a-z]+")) {
            labelErrorsMessageRegister.append("Ime nije u odgovarajućem formatu!\\n");
            return;
        }

        if (!lastName.matches("[A-Z][a-z]+")) {
            labelErrorsMessageRegister.append("Prezime nije u odgovarajućem formatu!\\n");
            return;
        }

        if (!email.matches("[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}")) {
            labelErrorsMessageRegister.append("Email nije u odgovarajućem formatu\n");
            return;
        }

        if (password.length() < 7 || passwordRepeat.length() < 7) {
            labelErrorsMessageRegister.append("Lozinka mora da ima najmanje 7 karaktera\n");
            return;
        }

        if (!password.equals(passwordRepeat)) {
            labelErrorsMessageRegister.append("Lozinke se ne poklapaju!\n");
            return;
        }

        if (!address.matches("[A-Z][A-z0-9\\ ]+")) {
            labelErrorsMessageRegister.append("Adresa nije u odgovarajućem formatu!\n");
            return;
        }

        if (!phoneNumber.matches("\\+[0-9]{11,12}")) {
            labelErrorsMessageRegister.append("Broj telefona nije u odgovarajućem formatu!\n");
            return;
        }

        JSONObject payload = new JSONObject();

        try {
            payload.accumulate("firstName", firstName);
            payload.accumulate("lastName", lastName);
            payload.accumulate("email", email);
            payload.accumulate("password", password);
            payload.accumulate("phoneNumber", phoneNumber);
            payload.accumulate("address", address);
            payload.accumulate("postalCode", Integer.valueOf(postalCode));
            payload.accumulate("cityId", Long.valueOf(cityId));

        } catch (Exception e) {
            // ...
        }

        CarTraderApi.postDataJSON(AppState.PREFIX_API_URL + "auth/register", payload, false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("successful") && object.getBoolean("successful")) {
                        Toast.makeText(getContext(), "Uspešna registracija!", Toast.LENGTH_LONG);
                        listener.onRegisterSuccess();
                    }
                } catch (Exception e) {
                    // ...
                }
            }
        });

    }
}