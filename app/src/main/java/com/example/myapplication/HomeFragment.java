package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.meta.When;

public class   HomeFragment extends Fragment {
    private RecyclerView recyclerUpcomingEvents, recyclerEventsForYou;
    private EventAdapter upcomingEventAdapter, eventsForYouAdapter;
    private List<Event> upcomingEventList, eventsForYouList;
    private DatabaseReference upcomingEventsRef, eventsForYouRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        ImageView menuIcon = view.findViewById(R.id.profile_picture);
        menuIcon.setOnClickListener(v -> ((MainActivity) getActivity()).openDrawer());

        TextView viewAllText = view.findViewById(R.id.view_all_upEv);
        TextView viewAllText1 = view.findViewById(R.id.view_all_evfy);

        // Set OnClickListener for View All Text
        viewAllText.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllUpcomingEvents_Activity.class);
            startActivity(intent);
        });

        viewAllText1.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllEventsForYou.class);
            startActivity(intent);
        });

        recyclerUpcomingEvents = view.findViewById(R.id.upcoming_events_recyclerview);
        recyclerEventsForYou = view.findViewById(R.id.events_for_you_recyclerview);
        upcomingEventsRef = FirebaseDatabase.getInstance().getReference("allEvents/upcomingEvents");
        eventsForYouRef = FirebaseDatabase.getInstance().getReference("allEvents/eventsForYou");

        // Setup Layout Managers
        recyclerUpcomingEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerEventsForYou.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Fetch event data
        upcomingEventList = new ArrayList<>();
        eventsForYouList = new ArrayList<>();

        // Initialize Adapters
        upcomingEventAdapter = new EventAdapter(getContext(), upcomingEventList);
        eventsForYouAdapter = new EventAdapter(getContext(), eventsForYouList);

        // Set Adapters to RecyclerViews
        recyclerUpcomingEvents.setAdapter(upcomingEventAdapter);
        recyclerEventsForYou.setAdapter(eventsForYouAdapter);

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

        // Fetch data from Firebase for events for you
        eventsForYouRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsForYouList.clear();  // Clear the list to avoid duplications
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    eventsForYouList.add(event);
                }
                eventsForYouAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView date = view.findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE, dd LLLL");
        String currentDate = dateFormat.format(new Date());
        date.setText(currentDate);

        Button musicButton = view.findViewById(R.id.music);
        Button sportsButton = view.findViewById(R.id.sports);
        Button concertsButton = view.findViewById(R.id.concerts);
        Button fitnessButton = view.findViewById(R.id.fitness);
        Button kidsButton = view.findViewById(R.id.kids);

        // Set up button click listeners for categories
        setupCategoryButton(musicButton, "Music");
        setupCategoryButton(sportsButton, "Sports");
        setupCategoryButton(concertsButton, "Concerts");
        setupCategoryButton(fitnessButton, "Fitness");
        setupCategoryButton(kidsButton, "Kids");

        TextView search = view.findViewById(R.id.search_editText);
        ImageView filter = view.findViewById(R.id.filter_icon);

        // Click listeners for search and filter
        search.setOnClickListener(v -> loadSearchFragment());
        filter.setOnClickListener(v -> loadSearchFragment());
    }

    // Method to load the SearchFragment
    private void loadSearchFragment() {
        Fragment searchFragment = new SearchFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, searchFragment);  // Replace with the search fragment
        transaction.addToBackStack(null);  // Optionally add to back stack
        transaction.commit();
    }

    // Utility method to handle category button clicks
    private void setupCategoryButton(Button button, String category) {
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EventsUnderCategory_Activity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
    }

    public void setEventReminder(Context context, long eventStartTimeInMillis) {
        long reminderTimeInMillis = eventStartTimeInMillis - 5 * 60 * 1000;

        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
        }
    }

    public void getEventStartTimeFromFirebase(String eventId) {
        // Get the reference to the event in Firebase Realtime Database
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("allEvents/eventsForYou").child(eventId);

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the event start time as a string
                    String eventStartTimeStr = dataSnapshot.child("time").getValue(String.class);

                    // Parse the eventStartTime string into a Date object
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    try {
                        Date eventStartTime = sdf.parse(eventStartTimeStr);

                        if (eventStartTime != null) {
                            // Convert the event start time to milliseconds
                            long eventStartTimeMillis = eventStartTime.getTime();

                            // Set the reminder 5 minutes before the event
                            setEventReminder(getContext(), eventStartTimeMillis - (5 * 60 * 1000));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("Firebase", "No such event found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read event data: " + databaseError.getMessage());
            }
        });
    }







}
