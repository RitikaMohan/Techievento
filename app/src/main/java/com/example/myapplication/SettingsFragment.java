package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "prefs";
    private static final String DARK_MODE_KEY = "dark_mode";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize UI components
        TextView setStatus = view.findViewById(R.id.set_status);
        TextView status = view.findViewById(R.id.status);
        Switch notificationSwitch = view.findViewById(R.id.switch_notifications);
        TextView languageOption = view.findViewById(R.id.set_language);
        TextView language = view.findViewById(R.id.language);
        ImageButton shareApp = view.findViewById(R.id.button_share_app);

        // Set Status functionality
        setStatus.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a Status");
            String[] statuses = {"Available", "In Event", "Participated"};
            builder.setItems(statuses, (dialog, which) -> {
                // Handle status selection
                String selectedStatus = statuses[which];
                status.setText(selectedStatus);
                // You can save this status to SharedPreferences or Firebase if needed
                Toast.makeText(getContext(), "Status Set to: " + selectedStatus, Toast.LENGTH_SHORT).show();
            });
            builder.show();
        });


        // Notification Toggle functionality
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable notifications
                Toast.makeText(getContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Disable notifications
                Toast.makeText(getContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Language selection functionality
        languageOption.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose Language");
            String[] languages = {"English", "Spanish", "French"};
            builder.setItems(languages, (dialog, which) -> {
                String selectedLanguage = languages[which];
                language.setText(selectedLanguage);
                // You can save this selection and reload activity with the new language
                Toast.makeText(getContext(), "Language Set to: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            });
            builder.show();
        });

        // Share App functionality
        shareApp.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this app: [App Link]";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share App via"));
        });


        return view;
    }

    // Method to save dark mode preference in SharedPreferences
    private void saveDarkModePreference(boolean isDarkMode) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DARK_MODE_KEY, isDarkMode);
        editor.apply();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button
        ImageView backButton = view.findViewById(R.id.back_settings);

        // Set the click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                Log.d("SettingsFragment", "Back button clicked");
            }
        });

        // Handling physical back button press as well
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Go back when back is pressed
                        Log.d("SettingsFragment", "Physical back button pressed");
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
        );
    }

}