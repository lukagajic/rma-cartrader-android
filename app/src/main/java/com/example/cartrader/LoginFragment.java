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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.state.AppState;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button buttonLogin;
    private EditText inputEmailLogin;
    private EditText inputPasswordLogin;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onLoginSuccess();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            listener = (LoginFragment.OnFragmentInteractionListener) context;
        } else {
            // ...
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initComponents(View v) {
        buttonLogin = v.findViewById(R.id.buttonLogin);
        inputEmailLogin = v.findViewById(R.id.inputEmailLogin);
        inputPasswordLogin = v.findViewById(R.id.inputPasswordLogin);

        buttonLogin.setOnClickListener(this);
    }

    public LoginFragment() {
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        initComponents(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                doLogin();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private void doLogin() {
        Map<String, String> loginData = new HashMap<>();

        loginData.put("email", inputEmailLogin.getText().toString());
        loginData.put("password", inputPasswordLogin.getText().toString());

        JSONObject payload = new JSONObject(loginData);

        CarTraderApi.postDataJSON(AppState.PREFIX_API_URL + "auth/login", payload, false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                if(response.equals("[]")) {
                    Toast.makeText(getContext(), "Došlo je do greške! Pokušajte ponovo!", Toast.LENGTH_LONG);
                } else {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("token")) {
                            AppState.token = object.getString("token");
                            AppState.email = object.getString("email");
                            AppState.userId = object.getInt("userId");
                            Toast.makeText(getContext(), "Uspešno ste se prijavili!", Toast.LENGTH_LONG).show();
                            listener.onLoginSuccess();
                        }

                    } catch (Exception e) {
                        // ...
                    }
                }
            }
        });
    }
}