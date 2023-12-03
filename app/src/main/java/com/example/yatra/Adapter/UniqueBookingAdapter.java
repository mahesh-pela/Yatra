package com.example.yatra.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yatra.Model.ProductModel;
import com.example.yatra.R;

import java.util.List;

public class UniqueBookingAdapter extends RecyclerView.Adapter<UniqueBookingAdapter.ViewHolder> {

    private List<ProductModel> uniqueBookingList;

    public UniqueBookingAdapter(Context applicationContext, List<ProductModel> uniqueBookingList) {
        this.uniqueBookingList = uniqueBookingList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameTextView;
        TextView averageRatingTextView;
        TextView ratingCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotel_name);
            averageRatingTextView = itemView.findViewById(R.id.userRating);
//            ratingCountTextView = itemView.findViewById(R.id.ratingCountTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recommended_hotels, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel uniqueBooking = uniqueBookingList.get(position);

        // Set data to views
        holder.hotelNameTextView.setText(uniqueBooking.getHotel_name());
        holder.averageRatingTextView.setText(String.valueOf(uniqueBooking.getAverageRating()));
//        holder.ratingCountTextView.setText(String.valueOf(uniqueBooking.getRatingCount()));

    }

    @Override
    public int getItemCount() {
        return uniqueBookingList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUniqueBookingList(List<ProductModel> uniqueBookingList) {
        this.uniqueBookingList = uniqueBookingList;
        notifyDataSetChanged();
    }
}

