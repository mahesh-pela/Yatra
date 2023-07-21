package com.example.yatra.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.TopDestinationModel;
import com.example.yatra.R;

import java.util.ArrayList;
import java.util.List;

public class AllPlacesAdapter extends RecyclerView.Adapter<AllPlacesAdapter.ViewHolder> {
    private Context context;
    private List<TopDestinationModel> topDestinationModelList;
//    private List<TopDestinationModel> filteredList;

    public AllPlacesAdapter(Context context, List<TopDestinationModel> topDestinationModelList) {
        this.context = context;
        this.topDestinationModelList = topDestinationModelList;
//        this.filteredList = new ArrayList<>(topDestinationModelList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_places, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(topDestinationModelList.get(position).getImg_url()).into(holder.top_img);
        holder.dest_name.setText(topDestinationModelList.get(position).getName());
        holder.dest_description.setText(topDestinationModelList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return topDestinationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView top_img;
        TextView dest_name;
        TextView dest_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            top_img = itemView.findViewById(R.id.top_img);
            dest_name = itemView.findViewById(R.id.dest_name);
            dest_description =itemView.findViewById(R.id.dest_description);
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
//                    filteredList.addAll(topDestinationModelList);
//                } else {
//                    List<TopDestinationModel> tempList = new ArrayList<>();
//                    for (TopDestinationModel place : topDestinationModelList) {
//                        if (place.getName().toLowerCase().contains(filterPattern)) {
//                            tempList.add(place);
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
//                filteredList = (List<TopDestinationModel>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//}
}
