package com.example.yatra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView txtSignup, txtForgetPassword;
    private AppCompatButton btnLogin;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        findViews();
    }

    private void findViews(){
        edtEmail =findViewById(R.id.edtEmail);
        edtPassword =findViewById(R.id.edtPassword);
        txtSignup =findViewById(R.id.txtSignup);
        btnLogin =findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                //progressDialog section
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Login in progress");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!password.isEmpty()){
                        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        edtPassword.setError("Password cannot be empty");
                    }
                }else if(email.isEmpty()){
                    edtEmail.setError("Email cannot be empty");
                }else{
                    edtEmail.setError("Enter valid email");
                }
            }
        });

//        checks if the user is already login or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null){
            Intent intent= new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(intent);
        }

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,SignupActivity.class );
                startActivity(intent);
                finish();
            }
        });
    }
}
