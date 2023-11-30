package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.Adapter.AllHotelsAdapter;
import com.example.yatra.Adapter.PopularAdapter;
import com.example.yatra.Adapter.RecommendedHotelsAdapter;
import com.example.yatra.Adapter.TopDestAdapter;
import com.example.yatra.Model.BookingModel;
import com.example.yatra.Model.HelperClass;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.ProductModel;
import com.example.yatra.Model.TopDestinationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        productModelList = new ArrayList<>();
        recommendedHotelsAdapter = new RecommendedHotelsAdapter(getApplicationContext(), productModelList);
        recommendedHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recommendedHotelsRecyclerView.setAdapter(recommendedHotelsAdapter);

//        recommended_hotels.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
//        productModelList = new ArrayList<>();
//        recommendedHotelsAdapter = new RecommendedHotelsAdapter(getApplicationContext(), productModelList);
//        recommended_hotels.setAdapter(recommendedHotelsAdapter);

        //recommendation code
        // Fetch hotels from Firebase Firestore
        fetchUniqueBookingDetails();

        // adding item touch in recyclerview
//        SearchView implementation starts here
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterListHotel(newText);
//                filterListDestination(newText);
//                return true;
//            }
//        });

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

        //retrieves data from FireStore database from 'PopularHotels' collection
        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = document.toObject(ProductModel.class);
                                productModelList.add(productModel);
                                recommendedHotelsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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

    }

//    private void filterListHotel(String newText) {
//        List<PopularModel> filteredListHotel = new ArrayList<>();
//        for(PopularModel HotelItem: popularModelList){
//            if(HotelItem.getName().toLowerCase().contains(newText.toLowerCase())){
//                filteredListHotel.add(HotelItem);
//            }
//        }
//        if(filteredListHotel.isEmpty()){
//        }else{
//            popularAdapter.setFilteredList(filteredListHotel);
//        }
//    }
//
//    private void filterListDestination(String newText) {
//        List<TopDestinationModel> filteredListDestination = new ArrayList<>();
//        for(TopDestinationModel DestItem: topDestinationModelList){
//            if(DestItem.getName().toLowerCase().contains(newText.toLowerCase())){
//                filteredListDestination.add(DestItem);
//            }
//        }
//        if(filteredListDestination.isEmpty()){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        }else{
//            topDestAdapter.setFilteredList(filteredListDestination);
//        }
//    }

// recommendation code
private void fetchUniqueBookingDetails() {
    db.collection("bookings")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Map<String, List<Double>> ratingsMap = new HashMap<>();
                        Map<String, Integer> ratingCountMap = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ProductModel productModel = document.toObject(ProductModel.class);
                            String hotelName = productModel.getHotel_name();
                            Double rating = productModel.getRating();

                            // Check if the rating is not null before using it
                            if (rating != null) {
                                if (ratingsMap.containsKey(hotelName)) {
                                    ratingsMap.get(hotelName).add(rating);
                                } else {
                                    List<Double> ratings = new ArrayList<>();
                                    ratings.add(rating);
                                    ratingsMap.put(hotelName, ratings);
                                }

                                // Increment the rating count for the hotel
                                ratingCountMap.put(hotelName, ratingCountMap.getOrDefault(hotelName, 0) + 1);
                            }
                        }

                        List<ProductModel> uniqueBookingList = new ArrayList<>();

                        for (Map.Entry<String, List<Double>> entry : ratingsMap.entrySet()) {
                            String hotelName = entry.getKey();
                            List<Double> ratings = entry.getValue();

                            double sum = 0;
                            for (Double rating : ratings) {
                                sum += rating;
                            }

                            double averageRating = sum / ratings.size();
                            int ratingCount = ratingCountMap.get(hotelName);

                            // Set the average rating to the corresponding hotel
                            ProductModel uniqueBooking = new ProductModel();
                            uniqueBooking.setHotel_name(hotelName);
                            uniqueBooking.setAverageRating(averageRating);
                            uniqueBooking.setRatingCount(ratingCount);

                            uniqueBookingList.add(uniqueBooking);
                        }

                        // Remove duplicates by converting the list to a set and back to a list
                        Set<String> uniqueHotelNames = new HashSet<>();
                        List<ProductModel> distinctBookingList = new ArrayList<>();
                        for (ProductModel uniqueBooking : uniqueBookingList) {
                            if (uniqueHotelNames.add(uniqueBooking.getHotel_name())) {
                                distinctBookingList.add(uniqueBooking);
                            }
                        }

                        // Sort the list based on average rating in descending order
                        Collections.sort(distinctBookingList, (p1, p2) -> Double.compare(p2.getAverageRating(), p1.getAverageRating()));

                        // Update your RecyclerView adapter with the unique booking details
                        // (use the appropriate adapter instance and method)
                        recommendedHotelsAdapter.setProductList(distinctBookingList);
                        recommendedHotelsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
}

}



