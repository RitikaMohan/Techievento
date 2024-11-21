package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<com.example.myapplication.CommunityAdapter.CommunityViewHolder> {

        private Context context;
        private List<Community> communityList;

        public CommunityAdapter(Context context, List<Community> communityList) {
            this.context = context;
            this.communityList = communityList;
        }

        @NonNull
        @Override
        public CommunityAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.community_recycler, parent, false);

            return new com.example.myapplication.CommunityAdapter.CommunityViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.communityTitle.setText(community.getTitle());
        holder.people.setText(community.getPeople());

        Glide.with(holder.communityImage.getContext()).load(community.getImage_url()).into(holder.communityImage);

        // Handle submit button click
        holder.joinButton.setOnClickListener(v -> {
            Toast.makeText(context, "Joined the community!", Toast.LENGTH_SHORT).show();
            // Perform any other operations here, if necessary
        });
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {
        ImageView communityImage;
        TextView communityTitle, people, joinButton;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            communityImage = itemView.findViewById(R.id.community_image);
            communityTitle = itemView.findViewById(R.id.community_title);
            people = itemView.findViewById(R.id.people);
            joinButton = itemView.findViewById(R.id.join_now);

        }
    }
}
