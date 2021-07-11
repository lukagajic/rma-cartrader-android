package com.example.cartrader;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.cartrader.adapters.CarsListRecyclerViewAdapter;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.VehicleModel;
import com.example.cartrader.state.AppState;

import org.json.JSONArray;

import java.util.List;

public class CarsListFragment extends Fragment implements View.OnClickListener {

    private List<VehicleModel> vehicles;
    private RecyclerView carsListRecyclerView;
    private CarsListRecyclerViewAdapter adapter;

    private Button buttonSwitchToSearch;

    private void initComponents(View v) {
        SearchView searchView = v.findViewById(R.id.inputFastSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initCarsList() {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "public/vehicles", false, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String odgovor = getJson();
                try {
                    JSONArray array = new JSONArray(odgovor);

                    CarsListFragment.this.vehicles = VehicleModel.parseJSONArray(array);

                    carsListRecyclerView = getView().findViewById(R.id.favoritesRecyclerView);
                    adapter = new CarsListRecyclerViewAdapter(getContext(), CarsListFragment.this.vehicles);

                    carsListRecyclerView.setAdapter(adapter);
                    carsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    ((ProgressBar) getView().findViewById(R.id.carsListProgressBar)).setVisibility(View.GONE);

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }


    public CarsListFragment() {

    }

    public static CarsListFragment newInstance() {
        CarsListFragment fragment = new CarsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cars_list, container, false);

        initCarsList();

        initComponents(v);
        buttonSwitchToSearch = v.findViewById(R.id.buttonSwitchToSearch);
        buttonSwitchToSearch.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSwitchToSearch:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
                break;
        }
    }
}