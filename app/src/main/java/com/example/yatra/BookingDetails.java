package com.example.yatra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yatra.Adapter.BookingListAdapter;
import com.example.yatra.Model.BookingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDetails extends AppCompatActivity {

    ListView bookingListView;
    BookingListAdapter bookingListAdapter;
    ImageView backArrow;
    FirebaseAuth mAuth;
    RatingBar rating_Bar;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        rating_Bar = findViewById(R.id.rating_Bar);
        bookingListView = findViewById(R.id.bookingListView);
        backArrow = findViewById(R.id.backArrow);

        List<BookingModel> bookingModelList = new ArrayList<>();
        bookingListAdapter = new BookingListAdapter(this, bookingModelList);
        bookingListView.setAdapter(bookingListAdapter);

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (rating_Bar != null) {
            rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    // Get the booking ID from the intent
                    String bookingId = getIntent().getStringExtra("booking_id");

                    // Create a map to update the rating field in Firestore
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("rating", rating);

                    db.collection("bookings")
                            .document(bookingId)
                            .update(updateData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Rating updated successfully
                                    Toast.makeText(BookingDetails.this, "Rating updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to update rating
                                    Toast.makeText(BookingDetails.this, "Failed to update rating", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }else{
            Toast.makeText(BookingDetails.this,"Not Found",Toast.LENGTH_SHORT).show();
        }

        // Get the currently logged-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Create a query to fetch bookings for the current user
            Query bookingsQuery = db.collection("bookings").whereEqualTo("UserId", currentUserId);

            // Fetch the bookings
            bookingsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        Toast.makeText(BookingDetails.this, "Failed to fetch bookings.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingDetails.this, DashboardActivity.class));
                finish(); // Simply finish this activity to return to the previous one
            }
        });
    }
}
