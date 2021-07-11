package com.example.cartrader;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cartrader.adapters.CarsListRecyclerViewAdapter;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.VehicleModel;
import com.example.cartrader.state.AppState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FavoritesFragment extends Fragment {

    private List<VehicleModel> favorites;
    private RecyclerView favoritesRecyclerView;
    private CarsListRecyclerViewAdapter adapter;

    @SuppressLint("HandlerLeak")
    private void initFavorites() {
        CarTraderApi.getJSON(AppState.PREFIX_API_URL + "secured/users/" + AppState.userId + "/favorites", true, new ReadDataHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String odgovor = getJson();
                try {
                    JSONArray array = new JSONArray(odgovor);

                    favorites = VehicleModel.parseJSONArray(array);

                    favoritesRecyclerView = getView().findViewById(R.id.favoritesRecyclerView);
                    adapter = new CarsListRecyclerViewAdapter(getContext(), favorites);

                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(favoritesRecyclerView);

                    favoritesRecyclerView.setAdapter(adapter);
                    favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    ((ProgressBar) getView().findViewById(R.id.carsListProgressBar)).setVisibility(View.GONE);

                } catch (Exception e) {
                    // ...
                }
            }
        });
    }

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        initFavorites();

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

        @SuppressLint("HandlerLeak")
        private void doRemoveFromFavorites(RecyclerView.ViewHolder viewHolder) {
            CarTraderApi.deleteDataJSON(AppState.PREFIX_API_URL + "secured/users/" + AppState.userId +"/favorites/" + viewHolder.itemView.getTag(),  new ReadDataHandler() {
                @Override
                public void handleMessage(@NonNull Message msg) {

                    String response = getJson();

                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.has("successful") && object.getBoolean("successful")) {
                            favorites.remove(viewHolder.getAdapterPosition());
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // ...
                    }
                }
            });
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            favorites.remove(viewHolder.getAdapterPosition());
            doRemoveFromFavorites(viewHolder);
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Izbrisali ste oglas iz liste omiljenih", Toast.LENGTH_SHORT).show();
        }
    };
}