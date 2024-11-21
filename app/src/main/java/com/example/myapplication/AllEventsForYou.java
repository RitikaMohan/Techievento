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

public class AllEventsForYou extends AppCompatActivity {

    private RecyclerView recyclerEventsForYou;
    private AllEventAdapter EventsForYouAdapter;
    private List<Event> EventsForYouList;
    private DatabaseReference EventsForYouRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_events_for_you);

        recyclerEventsForYou = findViewById(R.id.all_events_for_you_recyclerview);
        EventsForYouRef = FirebaseDatabase.getInstance().getReference("allEvents/eventsForYou");

        // Setup Layout Managers
        recyclerEventsForYou.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fetch event data
        EventsForYouList = new ArrayList<>();
        // Initialize Adapters
        EventsForYouAdapter = new AllEventAdapter(this, EventsForYouList);

        // Set Adapters to RecyclerViews
        recyclerEventsForYou.setAdapter(EventsForYouAdapter);

        EventsForYouRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventsForYouList.clear();  // Clear the list to avoid duplications
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    EventsForYouList.add(event);
                }
                EventsForYouAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        // Find the back button
        ImageView backButton = findViewById(R.id.back_allEfy);

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