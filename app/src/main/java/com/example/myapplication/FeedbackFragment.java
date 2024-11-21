package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;


public class FeedbackFragment extends Fragment {

    private RatingBar ratingBar;
    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Initialize UI elements
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.btnSubmit);

        // Handle submit button click
        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();

            if (rating > 0) {
                    Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    // Clear the fields after submission
                    ratingBar.setRating(0); // Reset to default rating
                } else {
                    Toast.makeText(getActivity(), "Please provide your feedback.", Toast.LENGTH_SHORT).show();
                }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button
        ImageView backButton = view.findViewById(R.id.back_feedback);

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