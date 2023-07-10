package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    //top destinations
    RecyclerView topDestRec, pop_hotels;
    List<TopDestinationModel> topDestinationModelList;
    List<PopularModel> popularModelList;
    TopDestAdapter topDestAdapter;
    PopularAdapter popularAdapter;
    ImageView imgProfile, imgPlaces;
    TextView viewAll_dest;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        imgProfile = findViewById(R.id.imgProfile);
        viewAll_dest = findViewById(R.id.viewAll_dest);
        topDestRec = findViewById(R.id.top_destinations);
        pop_hotels = findViewById(R.id.pop_hotels);
        imgPlaces = findViewById(R.id.imgPlaces);

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

        db.collection("TopDestination")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

        imgPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, Places.class));
                finish();
            }
        });


    }


}