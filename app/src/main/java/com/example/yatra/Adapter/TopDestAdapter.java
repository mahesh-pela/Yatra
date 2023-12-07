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
import com.example.yatra.Model.TopDestinationModel;
import com.example.yatra.R;

import java.util.List;

public class TopDestAdapter extends RecyclerView.Adapter<TopDestAdapter.ViewHolder> {
    private Context context;
    private List<TopDestinationModel> topDestinationModelList;
    private OnItemClickListener onItemClickListener; // Define an interface member

    public TopDestAdapter(Context context, List<TopDestinationModel> topDestinationModelList) {
        this.context = context;
        this.topDestinationModelList = topDestinationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_destination, parent, false), onItemClickListener);
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

    //this method is called when the search query changes
    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<TopDestinationModel> filteredList) {
        this.topDestinationModelList = filteredList;
        notifyDataSetChanged();
    }

    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Interface to handle item click
    public interface OnItemClickListener {
        void onItemClick(TopDestinationModel destinationModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView top_img;
        TextView dest_name;
        TextView dest_description;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            top_img = itemView.findViewById(R.id.top_img);
            dest_name = itemView.findViewById(R.id.dest_name);
            dest_description = itemView.findViewById(R.id.dest_description);

            // Set click listener on the entire item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(topDestinationModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
