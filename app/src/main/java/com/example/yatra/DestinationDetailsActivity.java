package com.example.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DestinationDetailsActivity extends AppCompatActivity {
    ImageView backArrowDest, destinationImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_dest_view_all);

        backArrowDest = findViewById(R.id.backArrowDest);
        destinationImageView = findViewById(R.id.destinationImage);

        backArrowDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DestinationDetailsActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Retrieve data from the intent
        Intent intent = getIntent();
        String destinationImage = intent.getStringExtra("destination_image");
        String destinationName = intent.getStringExtra("destination_name");
        String destinationDescription = intent.getStringExtra("destination_description");

        //Use the data to populate your UI elements

        TextView nameTextView = findViewById(R.id.dest_name);
        TextView descriptionTextView = findViewById(R.id.dest_description);

        Glide.with(this)
                .load(destinationImage)
                .placeholder(R.drawable.kalinchowk) // Error image or fallback
                .into(destinationImageView);
        nameTextView.setText(destinationName);
        descriptionTextView.setText(destinationDescription);
    }
}