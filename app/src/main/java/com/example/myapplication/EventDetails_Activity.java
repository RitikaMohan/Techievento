package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class EventDetails_Activity extends AppCompatActivity {

    private ImageView eventImage;
    private TextView eventTitle, eventHost, eventDateText, eventTimeText, eventLocationText, winner1Text, winner2Text, winner3Text, activitiesText;
    private Button bookNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize views
        eventImage = findViewById(R.id.event_image);
        eventTitle = findViewById(R.id.event_title);
        eventHost = findViewById(R.id.event_host);
        eventDateText = findViewById(R.id.event_date);
        eventTimeText = findViewById(R.id.event_time);
        eventLocationText = findViewById(R.id.event_location);
        winner1Text = findViewById(R.id.winner1);
        winner2Text = findViewById(R.id.winner2);
        winner3Text = findViewById(R.id.winner3);
        bookNowButton = findViewById(R.id.book_now_button);
        activitiesText = findViewById(R.id.activities);

        // Get the passed data from the intent
        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        String eventHostName = intent.getStringExtra("eventHost");
        String eventImageURL = intent.getStringExtra("eventImage");
        String eventDate = intent.getStringExtra("eventDate");
        String eventTime = intent.getStringExtra("eventTiming");
        String eventLocation = intent.getStringExtra("eventLocation");
        String winner1 = intent.getStringExtra("winner1");
        String winner2 = intent.getStringExtra("winner2");
        String winner3 = intent.getStringExtra("winner3");
        String[] activitiesArray = intent.getStringArrayExtra("activities");

        if (activitiesArray != null) {
            for (String activity : activitiesArray) {
                Log.d("EventDetails", "Activity: " + activity);  // This will log the activities
                activitiesText.append(activity + "\n");
            }
        } else {
            Log.d("EventDetails", "No activities found");  // Log when no activities are passed
        }

        // Set the data in views
        eventTitle.setText(eventName);
        eventHost.setText("Hosted by " + eventHostName);
        eventDateText.setText("Date: " + eventDate);
        eventTimeText.setText("Time: " + eventTime);
        eventLocationText.setText("Location: " + eventLocation);
        winner1Text.setText("1st Winner : " + winner1);
        winner2Text.setText("2nd Winner : " + winner2);
        winner3Text.setText("3rd Winner : " + winner3);

        // Load image using Glide (or any image loading library)
        Glide.with(this).load(eventImageURL).into(eventImage);

        // Book Now Button action (e.g., redirect to booking page)
        bookNowButton.setOnClickListener(v -> {
            String formUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfFyQNVfvAxTnA5YJnd-edZ0uC4fEgYxXW2FJnf3PYIV7tasg/viewform?usp=sf_link"; // Replace with your Google Form URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(formUrl));
            startActivity(browserIntent);
        });

        ImageView backButton = findViewById(R.id.back_details);

        // Set the click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Correct method call
            }
        });

        // Handling physical back button press as well
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                finish(); // Ends the activity and goes back
            }
        });


    }
}