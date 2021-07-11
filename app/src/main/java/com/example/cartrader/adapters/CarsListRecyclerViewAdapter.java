package com.example.cartrader.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import com.example.cartrader.CarDetailsFragment;
import com.example.cartrader.R;
import com.example.cartrader.api.CarTraderApi;
import com.example.cartrader.api.ReadDataHandler;
import com.example.cartrader.models.VehicleModel;
import com.example.cartrader.models.VehiclePhotoModel;

import org.json.JSONArray;

public class CarsListRecyclerViewAdapter extends RecyclerView.Adapter<CarsListRecyclerViewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<VehicleModel> vehicles;
    private List<VehicleModel> vehiclesFull;

    public CarsListRecyclerViewAdapter(Context context, List<VehicleModel> vehicles) {
        this.context = context;
        this.vehicles = vehicles;
        this.vehiclesFull = new ArrayList<>(vehicles);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_car, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.labelModel.setText(vehicles.get(position).getFullModelName());
        holder.labelPrice.setText(String.valueOf(vehicles.get(position).getPrice()) + " eur");
        holder.labelYear.setText(String.valueOf(vehicles.get(position).getYear()) + ". godište");
        holder.labelMileage.setText(String.valueOf(vehicles.get(position).getMileage()) + " km");

        holder.parentLayout.setTag(String.valueOf(vehicles.get(position).getId()));
        holder.parentLayout.setTag(String.valueOf(vehicles.get(position).getId()));

        //

        Glide.with(context)
                .load(vehicles.get(position).getPhotos().get(0))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageSource);


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment carDetailsFragment = new CarDetailsFragment();

                Bundle args = new Bundle();
                args.putString("vehicleId", String.valueOf(v.getTag()));

                carDetailsFragment.setArguments(args);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, carDetailsFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    private Filter vehiclesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VehicleModel> filteredVehicles = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredVehicles.addAll(vehiclesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (VehicleModel v : vehiclesFull) {
                    if (v.getFullModelName().toLowerCase().contains(filterPattern)) {
                        filteredVehicles.add(v);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredVehicles;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            vehicles.clear();
            vehicles.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return vehiclesFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSource;
        TextView labelModel;
        TextView labelYear;
        TextView labelPrice;
        TextView labelMileage;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSource = itemView.findViewById(R.id.imageSource);
            labelModel = itemView.findViewById(R.id.labelModel);
            labelYear = itemView.findViewById(R.id.labelYear);
            labelPrice = itemView.findViewById(R.id.labelPrice);
            labelMileage = itemView.findViewById(R.id.labelMileage);
            parentLayout = itemView.findViewById(R.id.singleCarItem);
        }
    }
}
