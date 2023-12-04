package com.example.yatra.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.ProductModel;
import com.example.yatra.R;

import java.util.List;

public class RecommendedHotelsAdapter extends RecyclerView.Adapter<RecommendedHotelsAdapter.ViewHolder> {
    private Context context;
    private List<ProductModel> productModelList;

    public RecommendedHotelsAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recommended_hotels, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImg_url()).into(holder.pop_img);
        holder.hotel_name.setText(productModelList.get(position).getHotel_name());
        holder.location.setText(productModelList.get(position).getLocation());
        holder.price.setText(productModelList.get(position).getRoom_price());
//        holder.discount.setText(productModelList.get(position).getDiscount());
        holder.userRating.setText(String.valueOf(productModelList.get(position).getRating()));
//        holder.pop_description.setText(productModelList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void setProductList(List<ProductModel> productModelList) {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<ProductModel> filteredList){
        this.productModelList = filteredList;
        notifyDataSetChanged();
    }


    //this method is called when the search query changes
//    @SuppressLint("NotifyDataSetChanged")
//    public void setFilteredList(List<ProductModel> filteredList){
//        this.productModelList = filteredList;
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pop_img;
        TextView hotel_name, price, discount,location, userRating, pop_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pop_img = itemView.findViewById(R.id.pop_img);
            hotel_name = itemView.findViewById(R.id.hotel_name);
            location = itemView.findViewById(R.id.location);
            price =itemView.findViewById(R.id.price);
            discount =itemView.findViewById(R.id.discount);
            userRating =itemView.findViewById(R.id.userRating);
            pop_description =itemView.findViewById(R.id.pop_description);
        }
    }}