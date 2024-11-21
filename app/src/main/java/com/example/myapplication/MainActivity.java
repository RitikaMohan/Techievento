package com.example.myapplication;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private CommunityFragment communityFragment;
    private CalendarFragment calendarFragment;
    private FeedbackFragment feedbackFragment;
    private SettingsFragment settingsFragment;
    private AboutFragment aboutFragment;
    private CredentialsFragment credentialsFragment;
    private PrivacyPolicyFragment privacyPolicyFragment;
    private LogoutFragment logoutFragment;
    private TextView date;
    private Fragment Nav_Menu;
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    private String userId;

    private TextView navProfileName;
    private ImageView navProfileImage;


    private static final String PREFS_NAME = "prefs";
    private static final String DARK_MODE_KEY = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EventReminderChannel";
            String description = "Channel for Event Reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("eventReminder", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();
        communityFragment = new CommunityFragment();
        calendarFragment = new CalendarFragment();
        feedbackFragment = new FeedbackFragment();
        settingsFragment = new SettingsFragment();
        aboutFragment = new AboutFragment();
        credentialsFragment = new CredentialsFragment();
        privacyPolicyFragment = new PrivacyPolicyFragment();
        logoutFragment = new LogoutFragment();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // NavigationView setup
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navProfileName = headerView.findViewById(R.id.profile_name_nav);
        navProfileImage = headerView.findViewById(R.id.profile_image_nav);


        if (user == null) {
            Intent intent = new Intent(MainActivity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        } else {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            loadFragment(new HomeFragment());
            userId = user.getUid();
            loadUserProfile();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            try {
                switch (item.getItemId()) {
                    case R.id.calendar:
                        loadFragment(new CalendarFragment());
                        return true;

                    case R.id.community:
                        loadFragment(new CommunityFragment());
                        return true;

                    case R.id.search:
                        loadFragment(new SearchFragment());
                        return true;

                    case R.id.profile:
                        loadFragment(new ProfileFragment());
                        return true;

                    case R.id.home:
                        loadFragment(new HomeFragment());
                        return true;

                    default:
                        return false;
                }
            } catch (Exception e) {
                throw e;
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feedback:
                        loadFragment(new FeedbackFragment());
                        break;
                    case R.id.nav_about:
                        loadFragment(new AboutFragment());
                        break;
                    case R.id.nav_settings:
                        loadFragment(new SettingsFragment());
                        break;
                    case R.id.nav_credentials:
                        loadFragment(new CredentialsFragment());
                        break;
                    case R.id.nav_privacy_policy:
                        loadFragment(new PrivacyPolicyFragment());
                        break;
                    case R.id.nav_logout:
                        loadFragment(new LogoutFragment());
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }


        public void openDrawer() {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        private void loadFragment(Fragment fragment) {
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Fragment_layout, fragment).addToBackStack(null)
                        .commit();
            }
        }

    private void updateProfileFragment(User user) {
        // Assuming you are using a ProfileFragment, pass the user data to it
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profile);
        if (profileFragment != null) {
            profileFragment.updateProfile(user);
        }
    }

    private void loadUserProfile() {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Set user data to Navigation Drawer Header
                        navProfileName.setText(user.getName());

                        // Load profile image using Glide into navigation drawer header
                        if (user.getProfile() != null) {
                            Glide.with(MainActivity.this)
                                    .load(user.getProfile()).circleCrop()
                                    .into(navProfileImage);
                        }

                        // Update ProfileFragment with the same data
                        updateProfileFragment(user);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}






