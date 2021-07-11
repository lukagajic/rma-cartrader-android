package com.example.cartrader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cartrader.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private Context context;
    private List<String> urls;
    private ViewPager2 imageSource;

    public SliderAdapter(Context context, List<String> urls, ViewPager2 imageSource) {
        this.context = context;
        this.urls = urls;
        this.imageSource = imageSource;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
            LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.single_car_image, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(urls.get(position));
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView singleCarImage;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            singleCarImage = itemView.findViewById(R.id.singleCarImage);
        }

        public void setImage(String url) {
            Glide.with(context)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(singleCarImage);
        }
    }
}
