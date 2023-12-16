package com.example.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    ImageView backArrow;
    EditText roomCount_edtText, checkInDateEditText, checkOutDateEditText ;
    Button btnIncrement, btnDecrement, book_button, dashboardbtn;
    int roomCount = 1;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Spinner room_type_spinner;
    Calendar checkInCalendar, checkOutCalendar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        backArrow = findViewById(R.id.backArrow);
        book_button = findViewById(R.id.book_button);
        room_type_spinner = findViewById(R.id.room_type_spinner);
        roomCount_edtText = findViewById(R.id.roomCount_edtText);
        btnIncrement = findViewById(R.id.btnIncrement);
        btnDecrement = findViewById(R.id.btnDecrement);
        dashboardbtn = findViewById(R.id.dashboardbtn);

        mAuth = FirebaseAuth.getInstance();
        checkInDateEditText = findViewById(R.id.check_in_date_edit_text);
        checkOutDateEditText = findViewById(R.id.check_out_date_edit_text);
        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();

        // Set initial room count in the edit Text
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
                // Checks that room count does not become negative
                if (roomCount > 1) {
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

        dashboardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingActivity.this, DashboardActivity.class));
                finish();
            }
        });

        // Set click listeners for the Check-in Date and Check-out Date EditText fields
        checkInDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkInDateEditText, checkInCalendar);
            }
        });

        checkOutDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkOutDateEditText, checkOutCalendar);
            }
        });

        // Drop Down Room Types
        Spinner roomTypeSpinner = findViewById(R.id.room_type_spinner);
        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        roomTypeAdapter.add("Single Room");
        roomTypeAdapter.add("Double Room");
        roomTypeAdapter.add("Suite");
        roomTypeSpinner.setAdapter(roomTypeAdapter);


        // Handle book_button click
        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = mAuth.getUid();
                String bookingId = generateBookingId(currentUserId);
//                String hotelId = getIntent().getStringExtra("hotel_id");
                String hotelName = getIntent().getStringExtra("hotel_name");
                String roomPrice = getIntent().getStringExtra("price");
                String description = getIntent().getStringExtra("description");
//                String discount = getIntent().getStringExtra("discount");
                String img_url = getIntent().getStringExtra("img_url");
                String hotelLocation = getIntent().getStringExtra("location");
                String checkInDate = checkInDateEditText.getText().toString();
                String roomType = room_type_spinner.getSelectedItem().toString();
                String checkOutDate = checkOutDateEditText.getText().toString();
                String status = "pending";

                // Check if any of the fields are empty
                if (checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(BookingActivity.this);
                progressDialog.setMessage("Booking in progress..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                int totalRoomPrice = Integer.parseInt(roomPrice);
                int totalPrice = totalRoomPrice * roomCount;


                storeBookingDataToFirestore(currentUserId, bookingId, hotelName, roomPrice, description, img_url, hotelLocation, checkInDate, roomType, checkOutDate, roomCount, totalPrice, status);
            }
        });
    }

    private void showDatePickerDialog(final EditText editText, final Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                editText.setText(sdf.format(calendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Optional: Set minimum date to today's date to disallow past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    //sending booking details to the firebase
    private void storeBookingDataToFirestore(String currentUserId, String bookingId, String hotelName, String roomPrice, String description, String img_url, String hotelLocation, String checkInDate, String roomType, String checkOutDate, int roomCount, int totalPrice, String status) {
        // Get the Firebase Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Create a new document with a random ID and set the booking data
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("UserId", currentUserId);
        bookingData.put("booking_id", bookingId);
//        bookingData.put("hotel_id", hotelId);
        bookingData.put("hotel_name", hotelName);
        bookingData.put("description", description);
//        bookingData.put("discount", discount);
        bookingData.put("img_url", img_url);
        bookingData.put("room_price", roomPrice);
        bookingData.put("total_price", totalPrice);
        bookingData.put("location", hotelLocation);
        bookingData.put("check_in_date", checkInDate);
        bookingData.put("room_type", roomType);
        bookingData.put("check_out_date", checkOutDate);
        bookingData.put("room_count", roomCount);
        bookingData.put("status", status);

        // Add the booking data to the "bookings" collection
        db.collection("bookings")
                .add(bookingData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        // Handle the success case if needed (e.g., show a success message)
                        Toast.makeText(BookingActivity.this, "Booking Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BookingActivity.this, DashboardActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the failure case if needed (e.g., show an error message)
                        progressDialog.dismiss();
                    }
                });
    }

    public static String generateBookingId(String userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US);
        String timestamp = dateFormat.format(new Date());

        // Concatenate the timestamp and user ID to create a unique booking ID
        return timestamp + userId;
    }

}
