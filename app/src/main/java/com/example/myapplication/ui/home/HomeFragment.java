package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AboutFragment;
import com.example.myapplication.CredentialsFragment;
import com.example.myapplication.Event;
import com.example.myapplication.EventAdapter;
import com.example.myapplication.FeedbackFragment;
import com.example.myapplication.Login_Activity;
import com.example.myapplication.LogoutFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Nav_Menu;
import com.example.myapplication.PrivacyPolicyFragment;
import com.example.myapplication.R;
import com.example.myapplication.SettingsFragment;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Toolbar toolbar;

    ActionBarDrawerToggle toggle;
    ImageView mImageview;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    DatabaseReference dbReference;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
////        final TextView textView = binding.date;
////        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;

        recyclerView = getView().findViewById(R.id.upcoming_events_recyclerview);
        dbReference = FirebaseDatabase.getInstance().getReference("Upcoming Events");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        // Initialize event list and add sample events
        eventList = new ArrayList<>();
        // Initialize adapter and set it to the RecyclerView
        eventAdapter = new EventAdapter(this.getActivity(), eventList);
        recyclerView.setAdapter(eventAdapter);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Event event =snapshot.getValue(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        eventList.add(new Event("https://thumbs.dreamstime.com/z/eyes-heavy-metal-band-live-music-show-razzmatazz-stage-barcelona-feb-february-barcelona-spain-51895792.jpg?ct=jpeg", "Riana's music show", "7:30PM", "City centre, road 24/2, Banani", 33));
//        eventList.add(new Event("https://images.pexels.com/photos/358042/pexels-photo-358042.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Women's Basketball", "5:30PM", "Indoor Stadium, road 01, Mirpur", 335));

        return inflater.inflate(R.layout.activity_nav_menu, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
////        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final DrawerLayout drawerLayout = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) getView().findViewById(R.id.nav_view);
//        mImageview = (ImageView) getView().findViewById(R.id.profile_picture);

//        toggle = new ActionBarDrawerToggle(this.getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        mImageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.END);
//
//            }
//        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feedback:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new FeedbackFragment()).commit();
                        break;
                    case R.id.nav_about:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new AboutFragment()).commit();
                        break;
                    case R.id.nav_settings:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new SettingsFragment()).commit();
                        break;
                    case R.id.nav_credentials:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new CredentialsFragment()).commit();
                        break;
                    case R.id.nav_privacy_policy:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new PrivacyPolicyFragment()).commit();
                        break;
                    case R.id.nav_logout:
                        getParentFragmentManager() .beginTransaction().replace(R.id.Fragment_layout, new LogoutFragment()).commit();
                        break;
                }
                DrawerLayout drawer = Objects.requireNonNull(getView()).findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}