package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Comparator;
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



        // adding item touch in recyclerview
//        SearchView implementation starts here
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListHotel(newText);
                filterListDestination(newText);
                return true;
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


    }

    private void filterListHotel(String newText) {
        List<ProductModel> filteredListHotel = new ArrayList<>();
        for(ProductModel HotelItem: productModelList){
            if(HotelItem.getHotel_name().toLowerCase().contains(newText.toLowerCase())){
                filteredListHotel.add(HotelItem);
            }
        }
        if(filteredListHotel.isEmpty()){
        }else{
            recommendedHotelsAdapter.setFilteredList(filteredListHotel);
        }
    }

    private void filterListDestination(String newText) {
        List<TopDestinationModel> filteredListDestination = new ArrayList<>();
        for(TopDestinationModel DestItem: topDestinationModelList){
            if(DestItem.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredListDestination.add(DestItem);
            }
        }
        if(filteredListDestination.isEmpty()){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else{
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



}



