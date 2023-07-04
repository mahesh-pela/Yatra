package com.example.yatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private TextView haveAccounttxt;
    private EditText edtfullname, edtEmail, edtPassword, edtConPassword;
    String fullname, email, password, conpassword;
    private AppCompatButton btnRegister;
    //    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
//    // shows the location in the database
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViews();
    }

    private void findViews() {
        edtfullname = findViewById(R.id.edtfullname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConPassword = findViewById(R.id.edtConPassword);
        haveAccounttxt = findViewById(R.id.haveAccounttxt);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        haveAccounttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = edtfullname.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                conpassword = edtConPassword.getText().toString();

//                //validation for form empty
                if (fullname.isEmpty()) {
                    edtfullname.setError("Name cannot be empty");
                } else if (email.isEmpty()) {
                    edtEmail.setError("Email cannot be empty");
                } else if (password.isEmpty()) {
                    edtPassword.setError("Password cannot be empty");
                } else if (conpassword.isEmpty()) {
                    edtConPassword.setError("");
                } else if (!password.equals(conpassword)) {
                    Toast.makeText(SignupActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference("users");
                            if (task.isSuccessful()) {
                                HelperClass helperClass = new HelperClass(fullname, email, password, conpassword);
                                String id=task.getResult().getUser().getUid();
                                reference.child("Info").child(id).setValue(helperClass);
                                Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignupActivity.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }}