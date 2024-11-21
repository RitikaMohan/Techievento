package com.example.myapplication;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private ImageView applyFilter;
    private RecyclerView searchResultsRecyclerView;
    private List<Event> filteredEventsList;
    private AllEventAdapter eventAdapter;

    // Assume you have a list of events to search/filter from
    private List<Event> allEventsList = new ArrayList<>();
    Bundle bundle = getArguments();
    String selectedCategory = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.search_edit_text);
        applyFilter = view.findViewById(R.id.apply_filter);
        searchResultsRecyclerView = view.findViewById(R.id.recyclerViewSearchResults);

        if (bundle != null) {
            selectedCategory = bundle.getString("category");
        }
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredEventsList = new ArrayList<>();
        eventAdapter = new AllEventAdapter(getContext(), filteredEventsList);
        searchResultsRecyclerView.setAdapter(eventAdapter);


        // Handle the search logic
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list as the user types
                String query = charSequence.toString().toLowerCase();
                filterEvents(query, new ArrayList<>(), null, null);  // Call filter logic
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle filter button click
        applyFilter.setOnClickListener(v -> {
            // Define your filter options
            String[] filterOptions = {"Music", "Sports", "Concerts","Fitness", "Kids"}; // Add more as needed
            boolean[] checkedItems = new boolean[filterOptions.length];  // Array to keep track of selected items

            // Build the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Filter");

            // Set up multi-choice dialog
            builder.setMultiChoiceItems(filterOptions, checkedItems, (dialog, which, isChecked) -> {
                // Update the checkedItems array when a user selects or deselects an option
                checkedItems[which] = isChecked;
            });

            builder.setNeutralButton("Select Date Range", (dialog, which) -> {
                // Open a DatePickerDialog for selecting the date range
                showDateRangePicker(); // Implement the method to show date picker
            });


            // Set up OK button
            // Define variables to store the selected date range
            final String[] startDate = {null};
            final String[] endDate = {null};

            builder.setPositiveButton("Apply", (dialog, which) -> {
                // Create a list for selected categories
                List<String> selectedCategories = new ArrayList<>();

                // Collect selected filters from checkedItems
                for (int i = 0; i < filterOptions.length; i++) {
                    if (checkedItems[i]) {
                        selectedCategories.add(filterOptions[i]);
                    }
                }

                // Check if date range is selected, otherwise use default
                String finalStartDate = startDate[0];
                String finalEndDate = endDate[0];

                if (finalStartDate == null || finalEndDate == null) {
                    // If no date range is selected, you can either set a default or ignore the date filter
                    finalStartDate = "2000-01-01";  // Example: set a default start date
                    finalEndDate = "2100-12-31";    // Example: set a default end date
                }

                // Apply both category and date filters
                applySelectedFilters(selectedCategories.toArray(new String[0]), checkedItems, finalStartDate, finalEndDate);
            });


            // Set up Cancel button
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();  // Dismiss the dialog without applying any filters
            });

            // Show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        fetchEventsByCategory("allEvents/upcomingEvents", selectedCategory);
        fetchEventsByCategory("allEvents/eventsForYou", selectedCategory);
        // Inflate the layout for this fragment
        return view;
    }

    private void fetchEventsByCategory(String path, String category) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allEventsList.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (category == null || event.getCategory().equals(category)) {  // Use the passed category instead of selectedCategory
                        allEventsList.add(event);  // Only add the event if it matches the category
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button
        ImageView backButton = view.findViewById(R.id.back_search);

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
    private void filterEvents(String query, List<String> selectedCategories, String startDate, String endDate) {
        filteredEventsList.clear();

        for (Event event : allEventsList) {
            // Filter by title query
            boolean matchesQuery = event.getTitle().toLowerCase().contains(query.toLowerCase());

            // Filter by selected categories
            boolean matchesCategory = selectedCategories == null || selectedCategories.isEmpty() || selectedCategories.contains(event.getCategory());

            // Filter by date range (assuming event has a valid date format)
            boolean matchesDate = true;
            if (startDate != null && endDate != null) {
                String eventDate = event.getDate();  // Assuming Event has a getDate() method
                matchesDate = eventDate.compareTo(startDate) >= 0 && eventDate.compareTo(endDate) <= 0;
            }

            // Add the event to the filtered list if all conditions are met
            if (matchesQuery && matchesCategory && matchesDate) {
                filteredEventsList.add(event);
            }
        }

        eventAdapter.notifyDataSetChanged();
    }



    private void applyDateFilter(String startDate, String endDate) {
        // Reapply filters with the selected date range
        String query = searchEditText.getText().toString();

        // Create arrays for filterOptions and checkedItems (e.g., categories like Music, Sports)
        String[] filterOptions = {"Music", "Sports", "Concerts", "Fitness", "Kids"};
        boolean[] checkedItems = new boolean[filterOptions.length];

        // Example: Iterate and mark all categories as checked (based on logic or user input)
        for (int i = 0; i < filterOptions.length; i++) {
            checkedItems[i] = true;  // In a real case, set this based on actual user selection
        }

        // Now apply the selected filters and pass in the date range
        applySelectedFilters(filterOptions, checkedItems, startDate, endDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detach Firebase listeners here
    }

    private void applySelectedFilters(String[] filterOptions, boolean[] checkedItems, String startDate, String endDate) {
        List<String> selectedCategories = new ArrayList<>();

        // Collect selected filters based on checkedItems
        for (int i = 0; i < filterOptions.length; i++) {
            if (checkedItems[i]) {
                selectedCategories.add(filterOptions[i]);
            }
        }

        // Apply filtering logic to the event list with the selected categories and date range
        String query = searchEditText.getText().toString().toLowerCase();
        filterEvents(query, selectedCategories, startDate, endDate);  // Apply the date filtering as well
    }


    private void filterEventsBasedOnSelection(List<String> selectedFilters) {
        filteredEventsList.clear();

        for (Event event : allEventsList) {
            // Filter based on selected categories (or whatever criteria you're using)
            if (selectedFilters.contains(event.getCategory())) {
                filteredEventsList.add(event);
            }
        }

        eventAdapter.notifyDataSetChanged();  // Update the list with filtered results
    }

    private void showDateRangePicker() {
        // Show DatePicker for selecting the start date
        DatePickerDialog startDatePicker = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            // Use Calendar to handle date formatting
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String startDate = sdf.format(startCalendar.getTime());

            // Show DatePicker for selecting the end date
            DatePickerDialog endDatePicker = new DatePickerDialog(getContext(), (endView, endYear, endMonth, endDayOfMonth) -> {
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endYear, endMonth, endDayOfMonth);
                String endDate = sdf.format(endCalendar.getTime());

                // Apply the date range filter
                applyDateFilter(startDate, endDate);

            }, year, month, dayOfMonth);
            endDatePicker.setTitle("Select End Date");
            endDatePicker.show();

        }, 2023, 0, 1);  // Default date
        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }


}