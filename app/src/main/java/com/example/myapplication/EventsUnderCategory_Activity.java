package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class EventsUnderCategory_Activity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private AllEventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_under_category);

        eventsRecyclerView = findViewById(R.id.events_recyclerview);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the selected category from the intent
        String selectedCategory = getIntent().getStringExtra("category");

        // Initialize list and adapter (single list and adapter for both queries)
        eventList = new ArrayList<>();
        eventAdapter = new AllEventAdapter(this, eventList);
        eventsRecyclerView.setAdapter(eventAdapter);

        // Fetch data from both upcomingEvents and eventsForYou
        fetchEventsByCategory("allEvents/upcomingEvents", selectedCategory);
        fetchEventsByCategory("allEvents/eventsForYou", selectedCategory);

        ImageView backButton = findViewById(R.id.back_eventscat);

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

    private void fetchEventsByCategory(String path, String category) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        Query query = databaseReference.orderByChild("category").equalTo(category);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    eventList.add(event);  // Append event to the list
                }
                // Notify the adapter after adding data from each query
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}

