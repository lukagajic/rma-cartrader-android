package com.example.cartrader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartrader.adapters.CarsListRecyclerViewAdapter;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.VehicleModel;
import com.example.cartrader.state.AppState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MyCarsFragment extends Fragment {
    private List<VehicleModel> myCars;
    private RecyclerView myCarsRecyclerView;
    private CarsListRecyclerViewAdapter adapter;

    @SuppressLint("HandlerLeak")
    private void initMyCars(View v) {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "secured/users/" + AppState.userId + "/vehicles", true, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();
                try {
                    myCars = VehicleModel.parseJSONArray(new JSONArray(response));
                    myCarsRecyclerView = v.findViewById(R.id.myCarsRecyclerView);

                    adapter = new CarsListRecyclerViewAdapter(getContext(), myCars);
                    myCarsRecyclerView.setAdapter(adapter);

                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(myCarsRecyclerView);

                    myCarsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                } catch (Exception e) {
                }

            }
        });
    }

    public MyCarsFragment() {
    }

    public static MyCarsFragment newInstance() {
        MyCarsFragment fragment = new MyCarsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_cars, container, false);

        initMyCars(v);

        return v;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addActionIcon(R.drawable.ic_delete)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_200))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Da li ste sigurni da želite da obrišete oglas?")
                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDeleteCar(viewHolder);
                        }
                    })
                    .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    }).create().show();
        }
    };

    @SuppressLint("HandlerLeak")
    private void doDeleteCar(RecyclerView.ViewHolder viewHolder) {
        CarTraderApi.deleteDataJSON(AppState.PREFIX_API_URL + "secured/vehicles/" + viewHolder.itemView.getTag(),  new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String response = getJson();

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.has("successful") && object.getBoolean("successful")) {
                        myCars.remove(viewHolder.getAdapterPosition());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

}