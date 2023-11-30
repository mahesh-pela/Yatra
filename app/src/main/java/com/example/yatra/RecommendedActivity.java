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
import java.util.List;

public class RecommendedActivity extends AppCompatActivity {
    FirebaseFirestore db;
    // top destinations
    RecyclerView recommend_rec;
    ImageView backArrow;
    List<ProductModel> productModelList;
    RecommendedAllAdapter recommendedAllAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_recyclerview);

        recommend_rec = findViewById(R.id.recommend_rec);
        backArrow = findViewById(R.id.backArrow);

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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = document.toObject(ProductModel.class);
                                productModelList.add(productModel);
                                recommendedAllAdapter.notifyDataSetChanged();
                            }
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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecommendedActivity.this, DashboardActivity.class));
                finish();
            }
        });


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