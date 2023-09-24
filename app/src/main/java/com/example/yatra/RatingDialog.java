package com.example.yatra;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RatingDialog extends Dialog {
    Button btnRateNow, btnRateLater;
    RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    //bookingsId - booking_detail Id, bookingId- document booking Id
    private String bookingsId, bookingName, bookingId; // The retrieved booking ID

    public RatingDialog(Context context, String bookingsId) {
        super(context);
        this.bookingsId = bookingsId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_dialog);

        btnRateNow = findViewById(R.id.btnRateNow);
        btnRateLater = findViewById(R.id.btnRateLater);
        ratingBar = findViewById(R.id.ratingBar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Fetch the booking ID based on the user's ID
        fetchBookingId();

        btnRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float userRating = ratingBar.getRating();
                // Handle user's rating
                if (userRating > 0) {
                    if (bookingId != null) {
                        // Creating a map to update the rating field in Firestore
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("rating", userRating);

                        // Update the rating in Firestore for the retrieved booking ID
                        db.collection("bookings").document(bookingId)
                                .update(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        showToast("Rating Successful!!");
                                        dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("RatingDialog", "Rating update failed: " + e.getMessage());
                                        showToast("Failed to update rating!!");
                                    }
                                });
                    } else {
                        showToast("Booking ID not found.");
                    }
                } else {
                    showToast("Please provide a rating!!");
                }
            }
        });


        btnRateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //fetching the booking Id from the firebase document
    private void fetchBookingId() {

        // Construct a query to retrieve the booking ID based on the booking_id
        Query query = db.collection("bookings")
                .whereEqualTo("booking_id",bookingsId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Access the booking ID from the document
                        bookingId = document.getId();

                    }
                } else {
                    showToast("Failed to fetch booking ID.");
                }
            }
        });
    }
}
