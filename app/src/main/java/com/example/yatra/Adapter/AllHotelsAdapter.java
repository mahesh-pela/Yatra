package com.example.yatra.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yatra.BookingActivity;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.TopDestinationModel;
import com.example.yatra.R;

import java.util.ArrayList;
import java.util.List;

public class AllHotelsAdapter extends RecyclerView.Adapter<AllHotelsAdapter.ViewHolder> {
    private Context context;
    private List<PopularModel> popularModelList;
//    Button bookbtn;
//    private List<PopularModel> filteredList;

    public AllHotelsAdapter(Context context, List<PopularModel> popularModelList) {
        this.context = context;
        this.popularModelList = popularModelList;
//        this.filteredList = new ArrayList<>(popularModelList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_hotels, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bookbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, BookingActivity.class);
//                        context.startActivity(intent);
//                    }
//                });
            }
        });
        Glide.with(context).load(popularModelList.get(position).getImg_url()).into(holder.pop_img);
        holder.hotel_name.setText(popularModelList.get(position).getName());
        holder.price.setText(popularModelList.get(position).getPrice());
        holder.discount.setText(popularModelList.get(position).getDiscount());
        holder.pop_description.setText(popularModelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return popularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pop_img;
        TextView hotel_name, price, discount, pop_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pop_img = itemView.findViewById(R.id.pop_img);
            hotel_name = itemView.findViewById(R.id.hotel_name);
            price =itemView.findViewById(R.id.price);
            discount =itemView.findViewById(R.id.discount);
            pop_description =itemView.findViewById(R.id.pop_description);
//            bookbtn = itemView.findViewById(R.id.bookbtn);

        }
    }



//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                if (filterPattern.isEmpty()) {
//                    filteredList.clear();
//                    filteredList.addAll(popularModelList);
//                } else {
//                    List<PopularModel> tempList = new ArrayList<>();
//                    for (PopularModel hotel : popularModelList) {
//                        if (hotel.getName().toLowerCase().contains(filterPattern)) {
//                            tempList.add(hotel);
//                        }
//                    }
//                    filteredList.clear();
//                    filteredList.addAll(tempList);
//                }
//
//                FilterResults results = new FilterResults();
//                results.values = filteredList;
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filteredList = (List<PopularModel>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//    }


}
