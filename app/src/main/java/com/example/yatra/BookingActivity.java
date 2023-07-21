package com.example.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.yatra.R;

import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    ImageView backArrow;
    EditText roomCount_edtText;
    Button btnIncrement, btnDecrement;
    int roomCount =1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        backArrow = findViewById(R.id.backArrow);
        roomCount_edtText = findViewById(R.id.roomCount_edtText);
        btnIncrement = findViewById(R.id.btnIncrement);
        btnDecrement = findViewById(R.id.btnDecrement);

        //set initial room count in the edit Text
        roomCount_edtText.setText(String.valueOf(roomCount));
        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomCount++;
                roomCount_edtText.setText(String.valueOf(roomCount));
            }
        });

        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checks that room count does not become negative
                if(roomCount > 1){
                    roomCount--;
                    roomCount_edtText.setText(String.valueOf(roomCount));
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingActivity.this, HotelsActivity.class));
            }
        });


        //DatePicker

        //Drop Down Room Types
        Spinner roomTypeSpinner = findViewById(R.id.room_type_spinner);
        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        roomTypeAdapter.add("Single Room");
        roomTypeAdapter.add("Double Room");
        roomTypeAdapter.add("Suite");
        roomTypeSpinner.setAdapter(roomTypeAdapter);
    }
}