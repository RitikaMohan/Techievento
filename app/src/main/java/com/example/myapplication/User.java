package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    public String name;
    public String sapId;
    public String email;
    public String phone;
    public String profile_pic;

    // Default constructor for Firebase
    public User() {
    }

    public User(String name, String sapId, String email, String phone) {
        this.name = name;
        this.sapId = sapId;
        this.email = email;
        this.phone = phone;
    }

    public User(String name, String sapId, String email, String phone, String profile_pic) {
        this.name = name;
        this.sapId = sapId;
        this.email = email;
        this.phone = phone;
        this.profile_pic = profile_pic;
    }

    public void setProfile(String profile_pic){
        this.profile_pic = profile_pic;
    }

    public String getProfile(){
        return profile_pic;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid(); // This returns the unique user ID
        } else {
            return null; // Return null if no user is logged in
        }
    }
}

