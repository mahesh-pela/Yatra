package com.example.yatra.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.yatra.BookingDetails;
import com.example.yatra.Model.BookingModel;
import com.example.yatra.R;
import com.example.yatra.RatingDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookingListAdapter extends ArrayAdapter<BookingModel> {

    private Context context;
    private List<BookingModel> bookingModelList;
    private FirebaseFirestore firestore;

    public BookingListAdapter(Context context, List<BookingModel> bookingModelList) {
        super(context, R.layout.booking_list_item, bookingModelList);
        this.context = context;
        this.bookingModelList = bookingModelList;
//        this.firestore = FirebaseFirestore.getInstance();
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.booking_list_item, parent, false);
        }

        TextView txtRateUs = convertView.findViewById(R.id.txtRateUs);
        // Get the booking ID for this specific item
        String bookingsId = bookingModelList.get(position).getBooking_id();

        BookingModel bookingModel = bookingModelList.get(position);

        // Check if the hotel has been rated
        // Check if the hotel has been rated
        if (bookingModel.hasRated()) {
            txtRateUs.setText("Rated");
            txtRateUs.setOnClickListener(null); // Disable click listener
        } else {
            txtRateUs.setText("Rate Us");
            txtRateUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create a new RatingDialog with the correct bookingId
                    RatingDialog ratingDialog = new RatingDialog(context, bookingsId);
                    ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
                    ratingDialog.setCancelable(false);
                    ratingDialog.show();

                    // Update the rated status after the user rates the hotel
                    bookingModel.setRated(true);
                    notifyDataSetChanged(); // Notify the adapter to refresh the view
                }
            });
        }


        // Rest of your code...



    TextView textViewRoomType = convertView.findViewById(R.id.textViewRoomType);
        TextView textViewHotelLocation = convertView.findViewById(R.id.textViewHotelLocation);
//        TextView textViewHotelPrice = convertView.findViewById(R.id.textViewHotelPrice);
        TextView textViewTotalPrice = convertView.findViewById(R.id.textViewTotalPrice);
        TextView textViewHotelName = convertView.findViewById(R.id.textViewHotelName);
        TextView textViewCheckInDate = convertView.findViewById(R.id.textViewCheckInDate);
        TextView textViewCheckOutDate = convertView.findViewById(R.id.textViewCheckOutDate);
        TextView textViewRoomCount = convertView.findViewById(R.id.textViewRoomCount);
        TextView textStatus = convertView.findViewById(R.id.textStatus);
//        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        textViewRoomType.setText("Room Type: " + bookingModel.getRoom_type());
        textViewHotelLocation.setText("Location: " + bookingModel.getLocation());
//        textViewHotelPrice.setText("Price : " + bookingModel.getRoom_price());
        textViewTotalPrice.setText("Total Price: " + bookingModel.getTotal_price());
        textViewCheckInDate.setText("Check-in Date: " + bookingModel.getCheck_in_date());
        textViewCheckOutDate.setText("Check-out Date: " + bookingModel.getCheck_out_date());
        textViewRoomCount.setText("Room Count: " + bookingModel.getRoom_count());
        textViewHotelName.setText("Hotel Name: " + bookingModel.getHotel_name());
        textStatus.setText("Status: " + bookingModel.getStatus());
//        ratingBar.setRating(Float.parseFloat("Rating: " + bookingModel.getRating()));

        return convertView;
    }
}
