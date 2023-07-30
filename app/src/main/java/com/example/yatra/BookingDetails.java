package com.example.yatra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yatra.Adapter.BookingListAdapter;
import com.example.yatra.Model.BookingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingDetails extends AppCompatActivity {

    ListView bookingListView;
    BookingListAdapter bookingListAdapter;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        bookingListView = findViewById(R.id.bookingListView);
        backArrow = findViewById(R.id.backArrow);
        List<BookingModel> bookingModelList = new ArrayList<>();
        bookingListAdapter = new BookingListAdapter(this, bookingModelList);
        bookingListView.setAdapter(bookingListAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookingModelList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        BookingModel bookingModel = document.toObject(BookingModel.class);
                        bookingModelList.add(bookingModel);
                    }
                    bookingListAdapter.notifyDataSetChanged();
                } else {
                    // Handle the error case if needed
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingDetails.this, DashboardActivity.class));
                finish();
            }
        });
    }
}
