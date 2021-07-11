package com.example.cartrader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cartrader.adapters.CarsListRecyclerViewAdapter;
import com.example.cartrader.models.VehicleModel;

import org.json.JSONArray;

import java.util.List;

public class SearchResultFragment extends Fragment {
    private List<VehicleModel> vehicles;
    private RecyclerView searchResultRecyclerView;
    private CarsListRecyclerViewAdapter adapter;

    private static final String ARG_JSON = "json";

    private String json;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(String json) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JSON, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString(ARG_JSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);

        try {
            initComponents(v);
        } catch (Exception e) {
            // ...
        }

        return v;
    }

    private void initComponents(View v) throws Exception {
        vehicles = VehicleModel.parseJSONArray(new JSONArray(json));

        searchResultRecyclerView = v.findViewById(R.id.searchResultRecyclerView);
        adapter = new CarsListRecyclerViewAdapter(getContext(), vehicles);
        searchResultRecyclerView.setAdapter(adapter);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}