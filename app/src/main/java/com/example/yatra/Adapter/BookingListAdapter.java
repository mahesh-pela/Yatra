package com.example.yatra.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yatra.Model.BookingModel;
import com.example.yatra.R;
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
        this.firestore = FirebaseFirestore.getInstance();
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.booking_list_item, parent, false);
        }

        BookingModel bookingModel = bookingModelList.get(position);

        TextView textViewRoomType = convertView.findViewById(R.id.textViewRoomType);
        TextView textViewHotelLocation = convertView.findViewById(R.id.textViewHotelLocation);
//        TextView textViewHotelPrice = convertView.findViewById(R.id.textViewHotelPrice);
        TextView textViewTotalPrice = convertView.findViewById(R.id.textViewTotalPrice);
        TextView textViewHotelName = convertView.findViewById(R.id.textViewHotelName);
        TextView textViewCheckInDate = convertView.findViewById(R.id.textViewCheckInDate);
        TextView textViewCheckOutDate = convertView.findViewById(R.id.textViewCheckOutDate);
        TextView textViewRoomCount = convertView.findViewById(R.id.textViewRoomCount);

        textViewRoomType.setText("Room Type: " + bookingModel.getRoom_type());
        textViewHotelLocation.setText("Location: " + bookingModel.getLocation());
//        textViewHotelPrice.setText("Price : " + bookingModel.getRoom_price());
        textViewTotalPrice.setText("Total Price: " + bookingModel.getTotal_price());
        textViewCheckInDate.setText("Check-in Date: " + bookingModel.getCheck_in_date());
        textViewCheckOutDate.setText("Check-out Date: " + bookingModel.getCheck_out_date());
        textViewRoomCount.setText("Room Count: " + bookingModel.getRoom_count());
        textViewHotelName.setText("Hotel Name: " + bookingModel.getHotel_name());

        return convertView;
    }
}
