package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private Context context;
    private List<User> userList;

    public PeopleAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.people_recycler, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());

        // Load profile image with Glide or any image loading library
        Glide.with(context).load(user.getProfile()).into(holder.userImage);

        // Handle click to open chat
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Chat_Activity.class);
            intent.putExtra("userId", user.getUserId());
            intent.putExtra("userName", user.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.name);
        }
    }
}

