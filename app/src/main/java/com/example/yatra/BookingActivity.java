package com.example.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.yatra.R;

import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //DatePicker
        EditText checkInDateEditText = findViewById(R.id.check_in_date_edit_text);
        EditText checkOutDateEditText = findViewById(R.id.check_out_date_edit_text);

        checkInDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(checkInDateEditText);
            }

            private void showDatePickerDialog(EditText checkInDateEditText) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Create the DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update the EditText with the selected date
                                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                        dayOfMonth, monthOfYear + 1, year);
                                checkInDateEditText.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        checkOutDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(checkOutDateEditText);
            }

            private void showDatePickerDialog(EditText checkOutDateEditText) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Create the DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update the EditText with the selected date
                                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                        dayOfMonth, monthOfYear + 1, year);
                                checkOutDateEditText.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                // Show the DatePickerDialog
                datePickerDialog.show();

            }
        });


        //Drop Down Room Types
        Spinner roomTypeSpinner = findViewById(R.id.room_type_spinner);
        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        roomTypeAdapter.add("Single Room");
        roomTypeAdapter.add("Double Room");
        roomTypeAdapter.add("Suite");
        roomTypeSpinner.setAdapter(roomTypeAdapter);
    }
}