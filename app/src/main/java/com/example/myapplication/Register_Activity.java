package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.example.myapplication.User;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {

    EditText username;
    EditText passwordText, nameText, sapText, phoneText;
    Button RegisterButton;
    FirebaseAuth mAuth;
    TextView login;
    DatabaseReference databaseReference;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username= findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        nameText = findViewById(R.id.name);
        sapText = findViewById(R.id.sapid);
        phoneText = findViewById(R.id.phone);
        RegisterButton = findViewById(R.id.registerButton);
        mAuth= FirebaseAuth.getInstance();
        login= findViewById(R.id.loginText);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password, name, sapId, phone;
                email= username.getText().toString().trim();
                password= passwordText.getText().toString().trim();
                name = nameText.getText().toString().trim();
                sapId = sapText.getText().toString().trim();
                phone = phoneText.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register_Activity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register_Activity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getUid();

                                    // Save additional user details to Firebase Realtime Database
                                    User userDetails = new User(name, sapId, email, phone);
                                    databaseReference.child(userId).setValue(userDetails)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Register_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                                                    } else {
                                                        Toast.makeText(Register_Activity.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    // Registration failed
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}