package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.ui.CalendarAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView eventList;
    private AllEventAdapter calendarAdapter;
    private List<Event> events;
    private DatabaseReference eventsDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);


        calendarView = view.findViewById(R.id.calendarView);
        eventList = view.findViewById(R.id.eventList);

        events = new ArrayList<>();  // Replace with real event data
        calendarAdapter = new AllEventAdapter(getContext(),events);
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList.setAdapter(calendarAdapter);


        // Initialize Firebase database reference
        eventsDatabase = FirebaseDatabase.getInstance().getReference("allEvents/upcomingEvents");

        // Set listener for date selection
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                loadEventsForDate(year, month, dayOfMonth);
            }
        });

        return view;
    }

    private void loadEventsForDate(int year, int month, int dayOfMonth) {
        // Format the selected date to match Firebase date format (assuming it's stored as YYYY-MM-DD)
        String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

        // Fetch events for the selected date from Firebase
        eventsDatabase.orderByChild("date").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();  // Clear the existing events

                // Loop through all events on that date and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    events.add(event);
                }

                // Notify adapter to refresh the RecyclerView
                calendarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CalendarFragment", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    private List<Event> getEventsForDate(int year, int month, int dayOfMonth) {
        // Logic to fetch events based on the date (Mock data for now)
        List<Event> eventList = new ArrayList<>();
        return eventList;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button
        ImageView backButton = view.findViewById(R.id.back_calendar);

        // Set the click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Handling physical back button press as well
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Go back when back is pressed
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
        );


    }
}
