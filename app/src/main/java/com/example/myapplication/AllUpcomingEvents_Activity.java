package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllUpcomingEvents_Activity extends AppCompatActivity {

    private RecyclerView recyclerUpcomingEvents;
    private AllEventAdapter upcomingEventAdapter;
    private List<Event> upcomingEventList;
    private DatabaseReference upcomingEventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_upcoming_events);

        recyclerUpcomingEvents = findViewById(R.id.all_upcoming_events_recyclerview);
        upcomingEventsRef = FirebaseDatabase.getInstance().getReference("allEvents/upcomingEvents");

        // Setup Layout Managers
        recyclerUpcomingEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fetch event data
        upcomingEventList = new ArrayList<>();
        // Initialize Adapters
        upcomingEventAdapter = new AllEventAdapter(this, upcomingEventList);

        // Set Adapters to RecyclerViews
        recyclerUpcomingEvents.setAdapter(upcomingEventAdapter);

        upcomingEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingEventList.clear();  // Clear the list to avoid duplications
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    upcomingEventList.add(event);
                }
                upcomingEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        // Find the back button
        ImageView backButton = findViewById(R.id.back_allUpEv);

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