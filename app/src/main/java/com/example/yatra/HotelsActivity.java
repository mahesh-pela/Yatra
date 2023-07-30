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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.yatra.Adapter.AllHotelsAdapter;
import com.example.yatra.Model.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HotelsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView hotel_rec;
    ImageView backArrow;

    List<PopularModel> popularModelList;
    AllHotelsAdapter allHotelsAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        hotel_rec = findViewById(R.id.hotel_rec);
        backArrow = findViewById(R.id.backArrow);


        //initialize database
        db = FirebaseFirestore.getInstance();

        hotel_rec.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false));
        popularModelList = new ArrayList<>();
        allHotelsAdapter = new AllHotelsAdapter(getApplicationContext(), popularModelList);
        hotel_rec.setAdapter(allHotelsAdapter);

        // recycler itemEvent
        hotel_rec.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), hotel_rec, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Handle click event here
                PopularModel hotel = popularModelList.get(position);
                Intent intent = new Intent(HotelsActivity.this, BookingActivity.class);
                intent.putExtra("hotel_name", hotel.getName()); // Assuming you have a unique ID for each hotel
                intent.putExtra("price", hotel.getPrice());
                intent.putExtra("location", hotel.getLocation());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                // Handle long press event here (if needed)
            }
        }));



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HotelsActivity.this, DashboardActivity.class));
                finish();
            }
        });


        db.collection("PopularHotels").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        PopularModel popularModel = document.toObject(PopularModel.class);
                        popularModelList.add(popularModel);
                        allHotelsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

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
