package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.Manifest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class ProfileFragment extends Fragment {

    private TextView profileName, profileSapId, profileEmail, profilePhone;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ImageView profileImage;
    private String userId;
    private static final int PICK_IMAGE_REQUEST = 22;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileName = view.findViewById(R.id.name);
        profileSapId = view.findViewById(R.id.id);
        profileEmail = view.findViewById(R.id.email);
        profilePhone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.profile_image);


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if (currentUser != null) {
            userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if user data exists
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            // Display the data in the text views
                            profileName.setText(user.name);
                            profileSapId.setText(user.sapId);
                            profileEmail.setText(user.email);
                            profilePhone.setText(user.phone);

                            // Check if the user has a profile picture
                            if (user.getProfile() != null) {
                                // Load the profile picture into the ImageView
                                Glide.with(getContext()).load(user.getProfile()).circleCrop().into(profileImage);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    public void updateProfile(User user) {
        profileName.setText(user.name);
        profileSapId.setText(user.sapId);
        profileEmail.setText(user.email);
        profilePhone.setText(user.phone);

        // Load profile image into profileImage using Glide
        if (user.getProfile() != null) {
            Glide.with(getContext())
                    .load(user.getProfile()).circleCrop()
                    .into(profileImage);
        }
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button
        ImageView backButton = view.findViewById(R.id.back_profile);

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

        profileImage = view.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileFragment", "Profile image clicked");
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("ProfileFragment", "Requesting permission");
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
                } else {
                    Log.d("ProfileFragment", "Permission already granted, opening image picker");
                    openImagePicker();
                }
            }
        });

    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);  // This needs to be set before calling the chooser
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);
        } else {
            Log.d("ProfileFragment", "Image selection failed or cancelled");
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (userId != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Users/" + userId + ".jpg");

            // Upload file
            storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Retrieve download URL
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Save the image URL to Firebase Database
                            saveImageUrlToFirebase(downloadUri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void saveImageUrlToFirebase(String downloadUrl) {
        // Reference to the user's node in the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Update the profile_pic field with the new image URL
        userRef.child("profile_pic").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Profile picture updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image picker
                openImagePicker();
            } else {
                // Permission denied
                Toast.makeText(getActivity(), "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }



}