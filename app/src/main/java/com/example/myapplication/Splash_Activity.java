package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class Splash_Activity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splash_Activity.this, MainActivity.class);
                Splash_Activity.this.startActivity(i);
                Splash_Activity.this.finish();

            }
        }, SPLASH_DISPLAY_TIME);
    }
}