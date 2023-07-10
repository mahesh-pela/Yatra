package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yatra.Adapter.AllPlacesAdapter;
import com.example.yatra.Adapter.TopDestAdapter;
import com.example.yatra.Model.TopDestinationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Places extends AppCompatActivity {
    FirebaseFirestore db;
    // top destinations
    RecyclerView place_rec;
    List<TopDestinationModel> topDestinationModelList;
    AllPlacesAdapter allPlacesAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        place_rec = findViewById(R.id.place_rec);

        // Initializing database
        db = FirebaseFirestore.getInstance();

        place_rec.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        topDestinationModelList = new ArrayList<>();
        allPlacesAdapter = new AllPlacesAdapter(getApplicationContext(), topDestinationModelList);
        place_rec.setAdapter(allPlacesAdapter);

        // Inflating the layout that contains the backArrow ImageView
        View anotherLayout = getLayoutInflater().inflate(R.layout.all_places, null);
        ImageView backArrow = anotherLayout.findViewById(R.id.backArrow);

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
                                allPlacesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Places.this, DashboardActivity.class));
                finish();
            }
        });
    }
}
