package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yatra.Model.HelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private TextView txtName, txtEmail;
    private Button btnLogout;
    ImageView backArrow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogout = findViewById(R.id.btnLogout);
        backArrow = findViewById(R.id.back_Arrow);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("users").child("Info").child(mAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass helperClass = dataSnapshot.getValue(HelperClass.class);
                    if (helperClass != null) {
                        txtName.setText("Full Name: " + helperClass.getFullname());
                        txtEmail.setText("Email: " + helperClass.getEmail());
                    }
                } else {
                    Log.d(TAG, "Data snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error retrieving data from Firebase: " + databaseError.getMessage());
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(ProfileActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
