package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yatra.Adapter.AllPlacesAdapter;
import com.example.yatra.Adapter.RecommendedAllAdapter;
import com.example.yatra.Adapter.RecommendedHotelsAdapter;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.ProductModel;
import com.example.yatra.Model.TopDestinationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendedActivity extends AppCompatActivity {
    FirebaseFirestore db;
    // top destinations
    RecyclerView recommend_rec;
    ImageView backArrowRecommended;
    List<ProductModel> productModelList;
    RecommendedAllAdapter recommendedAllAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_recyclerview);

        recommend_rec = findViewById(R.id.recommend_rec);
        backArrowRecommended = findViewById(R.id.backArrowRecommended);

        // Initializing database
        db = FirebaseFirestore.getInstance();

        recommend_rec.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        productModelList = new ArrayList<>();
        recommendedAllAdapter = new RecommendedAllAdapter(getApplicationContext(), productModelList);
        recommend_rec.setAdapter(recommendedAllAdapter);

        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ProductModel> allProducts = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = document.toObject(ProductModel.class);
                                allProducts.add(productModel);
                            }

                            // Process the data to get unique hotels with average ratings
                            List<ProductModel> uniqueHotelsList = processUniqueHotels(allProducts);

                            // Set the processed data in the adapter
                            productModelList.clear();
                            productModelList.addAll(uniqueHotelsList);
                            recommendedAllAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //recycler event

        recommend_rec.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recommend_rec, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Handle click event here
                ProductModel recommend = productModelList.get(position);
                Intent intent = new Intent(RecommendedActivity.this, BookingActivity.class);
                intent.putExtra("hotel_name", recommend.getHotel_name()); // Assuming you have a unique ID for each hotel
                intent.putExtra("price", recommend.getRoom_price());
                intent.putExtra("location", recommend.getLocation());
                intent.putExtra("description", recommend.getDescription());
//                intent.putExtra("discount", recommend.getDiscount());
                intent.putExtra("img_url", recommend.getImg_url());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                // Handle long press event here (if needed)
            }
        }));

        backArrowRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecommendedActivity.this, DashboardActivity.class));
                finish();
            }
        });


    }
    private List<ProductModel> processUniqueHotels(List<ProductModel> inputList) {
        Map<String, List<ProductModel>> hotelMap = new HashMap<>();

        // Group the data by hotel name
        for (ProductModel productModel : inputList) {
            String hotelName = productModel.getHotel_name();
            if (hotelMap.containsKey(hotelName)) {
                hotelMap.get(hotelName).add(productModel);
            } else {
                List<ProductModel> newList = new ArrayList<>();
                newList.add(productModel);
                hotelMap.put(hotelName, newList);
            }
        }

        // Calculate average rating for each hotel
        List<ProductModel> resultList = new ArrayList<>();
        for (Map.Entry<String, List<ProductModel>> entry : hotelMap.entrySet()) {
            String hotelName = entry.getKey();
            List<ProductModel> hotelList = entry.getValue();

            // Calculate average rating
            double totalRating = 0;
            int validRatingsCount = 0; // Count of valid ratings (non-null)
            for (ProductModel hotel : hotelList) {
                Double rating = hotel.getRating();
                if (rating != null) {
                    totalRating += rating;
                    validRatingsCount++;
                }
            }

            // Avoid division by zero
            if (validRatingsCount > 0) {
                double averageRating = totalRating / validRatingsCount;

                // Round off the rating value to the nearest half
                double roundedRating = (Math.round(averageRating * 2) / 2.0);

                // Create a new ProductModel with the calculated average rating
                ProductModel uniqueHotel = new ProductModel();
                uniqueHotel.setHotel_name(hotelName);
                uniqueHotel.setRating((double) roundedRating);
                uniqueHotel.setLocation(hotelList.get(0).getLocation()); // Assuming location is the same for all entries of the same hotel
                uniqueHotel.setRoom_price(hotelList.get(0).getRoom_price()); // Assuming price is the same for all entries of the same hotel
                uniqueHotel.setImg_url(hotelList.get(0).getImg_url());
                uniqueHotel.setDescription((hotelList.get(0).getDescription()));

                // Add the unique hotel to the result list
                resultList.add(uniqueHotel);
            }
        }

        // Sort the result list by average rating in descending order
        Collections.sort(resultList, (hotel1, hotel2) -> Double.compare(hotel2.getRating(), hotel1.getRating()));

        return resultList;
    }

    private interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }}