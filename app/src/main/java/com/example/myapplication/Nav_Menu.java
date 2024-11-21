package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Nav_Menu extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ImageView mImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

//        mImageview = (ImageView) findViewById(R.id.profile_picture);

        if (findViewById(R.id.Fragment_layout) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.Fragment_layout, new HomeFragment())
                    .commit();
        }
//        mImageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.START);
//
//            }
//        });


//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_feedback:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new FeedbackFragment()).commit();
//                        break;
//                    case R.id.nav_about:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new AboutFragment()).commit();
//                        break;
//                    case R.id.nav_settings:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new SettingsFragment()).commit();
//                        break;
//                    case R.id.nav_credentials:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new CredentialsFragment()).commit();
//                        break;
//                    case R.id.nav_privacy_policy:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new PrivacyPolicyFragment()).commit();
//                        break;
//                    case R.id.nav_logout:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_layout, new LogoutFragment()).commit();
//                        break;
//                }
//                DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.END);
//                return true;
//            }
//        });
//
//    }
//    public void openDrawer() {
//        drawerLayout.openDrawer(GravityCompat.END);
//    }
    }
}