package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.Adapter.AllHotelsAdapter;
import com.example.yatra.Adapter.PopularAdapter;
import com.example.yatra.Adapter.RecommendedAllAdapter;
import com.example.yatra.Adapter.RecommendedHotelsAdapter;
import com.example.yatra.Adapter.TopDestAdapter;
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

public class DashboardActivity extends AppCompatActivity {

    FirebaseFirestore db;
    SearchView searchView;
    RecyclerView topDestRec, recommendedHotelsRecyclerView;
    List<TopDestinationModel> topDestinationModelList;
    List<PopularModel> popularModelList;
    //    List<BookingModel> bookingModelList;
    List<ProductModel> productModelList;

    //it holds the original data from the FireStore database
    List<PopularModel> originalPopularModelList;
    TopDestAdapter topDestAdapter;
    PopularAdapter popularAdapter;
    RecommendedHotelsAdapter recommendedHotelsAdapter;


    AllHotelsAdapter allHotelsAdapter;
    ImageView imgProfile, imgPlaces, imgHotels, imgBookings;
    TextView viewAll_dest, viewAll_hotels;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        searchView = findViewById(R.id.searchView);
        viewAll_dest = findViewById(R.id.viewAll_dest);
        viewAll_hotels = findViewById(R.id.viewAll_hotels);
        topDestRec = findViewById(R.id.top_destinations);
        recommendedHotelsRecyclerView = findViewById(R.id.recommended_hotels);
        imgPlaces = findViewById(R.id.imgPlaces);
        imgHotels = findViewById(R.id.imgHotels);
        imgBookings = findViewById(R.id.imgBookings);
        imgProfile = findViewById(R.id.img_Profile);

        popularModelList = new ArrayList<>();

        //initializing FirebaseFireStore
        db = FirebaseFirestore.getInstance();

        topDestRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        topDestinationModelList = new ArrayList<>();
        topDestAdapter = new TopDestAdapter(getApplicationContext(), topDestinationModelList);
        topDestRec.setAdapter(topDestAdapter);

        //implementing the clicklistener on destination
        topDestAdapter = new TopDestAdapter(getApplicationContext(), topDestinationModelList);
        topDestRec.setAdapter(topDestAdapter);

        topDestAdapter.setOnItemClickListener(new TopDestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TopDestinationModel destinationModel) {
                // Handle item click here, start DestinationDetailsActivity
                Intent intent = new Intent(DashboardActivity.this, DestinationDetailsActivity.class);
                intent.putExtra("destination_image", destinationModel.getImg_url());
                intent.putExtra("destination_name", destinationModel.getName());
                intent.putExtra("destination_description", destinationModel.getDescription());
                // Add more data if needed
                startActivity(intent);
            }
        });

// clicklistener ends here

        productModelList = new ArrayList<>();
        recommendedHotelsAdapter = new RecommendedHotelsAdapter(getApplicationContext(), productModelList);
        recommendedHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recommendedHotelsRecyclerView.setAdapter(recommendedHotelsAdapter);


        // adding item touch in recyclerview
//        SearchView implementation starts here
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the list based on the complete input
                filterListHotel(query);
                filterListDestination(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // No action needed when text is being changed
                return false;
            }
        });

        //SearchView Section ends here

        //retrieves data from the FireStore database from 'TopDestination' collection
        db.collection("TopDestination")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    //Task<QuerySnapshot> will be called when the task is complete
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // QueryDocumentSnapshot iterates through each document in the collection
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TopDestinationModel topDestinationModel = document.toObject(TopDestinationModel.class);
                                topDestinationModelList.add(topDestinationModel);
                                topDestAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//
//        db.collection("bookings")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                ProductModel productModel = document.toObject(ProductModel.class);
//                                productModelList.add(productModel);
//                                recommendedHotelsAdapter.notifyDataSetChanged();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewAll_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, Places.class));
                finish();
            }
        });

        viewAll_hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, RecommendedActivity.class));
                finish();
            }
        });

        imgPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, Places.class));
                finish();
            }
        });

        imgHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, HotelsActivity.class));
                finish();
            }
        });

        imgBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, BookingDetails.class));
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                finish();
            }
        });

        //code for recommendation that retrieves the data from the firebase
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
                            recommendedHotelsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //implementing the click listener in recommended hotel recyclerview
        recommendedHotelsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recommendedHotelsRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Handle click event here
                ProductModel recommend = productModelList.get(position);
                Intent intent = new Intent(DashboardActivity.this, BookingActivity.class);
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


    }

    private void filterListHotel(String query) {
        List<ProductModel> filteredListHotel = new ArrayList<>();
        for (ProductModel hotelItem : productModelList) {
            if (hotelItem.getHotel_name().toLowerCase().equals(query.toLowerCase())) {
                filteredListHotel.add(hotelItem);
            }
        }

        // Check if any results found
        if (filteredListHotel.isEmpty()) {
            Toast.makeText(this, "Hotel not found", Toast.LENGTH_SHORT).show();
        } else {
            recommendedHotelsAdapter.setFilteredList(filteredListHotel);
        }
    }

    private void filterListDestination(String query) {
        List<TopDestinationModel> filteredListDestination = new ArrayList<>();
        for (TopDestinationModel destItem : topDestinationModelList) {
            if (destItem.getName().toLowerCase().equals(query.toLowerCase())) {
                filteredListDestination.add(destItem);
            }
        }

        // Check if any results found
        if (filteredListDestination.isEmpty()) {
            Toast.makeText(this, "Destination not found", Toast.LENGTH_SHORT).show();
        } else {
            topDestAdapter.setFilteredList(filteredListDestination);
        }
    }

    // recommendation code
    // recommendation code
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

                //round off the rating value to the nearest half
                double roundedRating = (Math.round(averageRating * 2)/2.0);

                // Create a new ProductModel with the calculated average rating
                ProductModel uniqueHotel = new ProductModel();
                uniqueHotel.setHotel_name(hotelName);
                uniqueHotel.setRating((double) roundedRating);
                uniqueHotel.setLocation(hotelList.get(0).getLocation()); // Assuming location is the same for all entries of the same hotel
                uniqueHotel.setRoom_price(hotelList.get(0).getRoom_price()); // Assuming price is the same for all entries of the same hotel
                uniqueHotel.setImg_url(hotelList.get(0).getImg_url());


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
    }



}