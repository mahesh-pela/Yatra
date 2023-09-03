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
import com.example.yatra.Adapter.TopDestAdapter;
import com.example.yatra.Model.PopularModel;
import com.example.yatra.Model.TopDestinationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    FirebaseFirestore db;
    SearchView searchView;
    RecyclerView topDestRec, pop_hotels;
    List<TopDestinationModel> topDestinationModelList;
    List<PopularModel> popularModelList;

    //it holds the original data from the FireStore database
//    List<PopularModel> originalPopularModelList;
    TopDestAdapter topDestAdapter;
    PopularAdapter popularAdapter;
    AllHotelsAdapter allHotelsAdapter;
    ImageView imgProfile, imgPlaces, imgHotels, imgBookings;
    TextView viewAll_dest, viewAll_hotels;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        searchView  = findViewById(R.id.searchView);
        imgProfile = findViewById(R.id.imgProfile);
        viewAll_dest = findViewById(R.id.viewAll_dest);
        viewAll_hotels = findViewById(R.id.viewAll_hotels);
        topDestRec = findViewById(R.id.top_destinations);
        pop_hotels = findViewById(R.id.pop_hotels);
        imgPlaces = findViewById(R.id.imgPlaces);
        imgHotels = findViewById(R.id.imgHotels);
        imgBookings = findViewById(R.id.imgBookings);

        popularModelList = new ArrayList<>();

        //initializing FirebaseFireStore
        db = FirebaseFirestore.getInstance();

        topDestRec.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        topDestinationModelList = new ArrayList<>();
        topDestAdapter = new TopDestAdapter(getApplicationContext(), topDestinationModelList);
        topDestRec.setAdapter(topDestAdapter);

        pop_hotels.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        popularModelList = new ArrayList<>();
        popularAdapter = new PopularAdapter(getApplicationContext(), popularModelList);
        pop_hotels.setAdapter(popularAdapter);

        // adding item touch in recyclerview
        //SearchView implementation starts here
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
                            Toast.makeText(getApplicationContext(),"Error"+ task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //retrieves data from FireStore database from 'PopularHotels' collection
        db.collection("PopularHotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Error"+ task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,ProfileActivity.class);
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
                startActivity(new Intent(DashboardActivity.this, HotelsActivity.class));
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
    }

    private void filterListHotel(String newText) {
        List<PopularModel> filteredListHotel = new ArrayList<>();
        for(PopularModel HotelItem: popularModelList){
            if(HotelItem.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredListHotel.add(HotelItem);
            }
        }
        if(filteredListHotel.isEmpty()){
        }else{
            popularAdapter.setFilteredList(filteredListHotel);
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

}