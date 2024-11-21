package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CommunityFragment extends Fragment {

    private GoogleMap mMap;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView recyclerPost, recyclerCommunity;
    private BreadCrumbAdapter postAdapter;
    private CommunityAdapter communityAdapter;
    private List<BreadCrumb> postList;
    private List<Community> communityList;
    private DatabaseReference postRef, communityRef;
    private TextView joinButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_community, container, false);

        // Find the bottom sheet view
        View bottomSheet = view.findViewById(R.id.bottomSheet);

        // Set up BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Optionally set the peek height (how much is visible when collapsed)
        bottomSheetBehavior.setPeekHeight(200);

        // Allow it to be dragged
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });


        recyclerPost = view.findViewById(R.id.breadcrumbsRecyclerView);
        postRef = FirebaseDatabase.getInstance().getReference("BreadCrumbs");

        // Setup Layout Managers
        recyclerPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Fetch event data
        postList = new ArrayList<>();
        // Initialize Adapters
        postAdapter = new BreadCrumbAdapter(getContext(), postList);

        // Set Adapters to RecyclerViews
        recyclerPost.setAdapter(postAdapter);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();  // Clear the list to avoid duplications
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BreadCrumb post = dataSnapshot.getValue(BreadCrumb.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        recyclerCommunity = view.findViewById(R.id.communitiesRecyclerView);
        communityRef = FirebaseDatabase.getInstance().getReference("Community");

        // Setup Layout Managers
        recyclerCommunity.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Fetch event data
        communityList = new ArrayList<>();
        // Initialize Adapters
        communityAdapter = new CommunityAdapter(getContext(), communityList);

        // Set Adapters to RecyclerViews
        recyclerCommunity.setAdapter(communityAdapter);

        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                communityList.clear();  // Clear the list to avoid duplications
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Community community = dataSnapshot.getValue(Community.class);
                    communityList.add(community);
                }
                communityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
        // Return view
        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at a sample location and move the camera
        LatLng location = new LatLng(-34, 151);  // Replace with your location coordinates
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));  // Adjust zoom as necessary
    }
}