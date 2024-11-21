package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BreadCrumbAdapter extends RecyclerView.Adapter<BreadCrumbAdapter.PostViewHolder> {

private Context context;
private List<BreadCrumb> postList;

public BreadCrumbAdapter(Context context, List<BreadCrumb> postList) {
    this.context = context;
    this.postList = postList;
}

    @NonNull
    @Override
    public BreadCrumbAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.breadcrumb_recycler, parent, false);
        return new BreadCrumbAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreadCrumbAdapter.PostViewHolder holder, int position) {
        BreadCrumb post = postList.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postDescription.setText(post.getDescription());

        Glide.with(holder.postImage.getContext()).load(post.getImage_url()).into(holder.postImage);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle, postDescription;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            postTitle = itemView.findViewById(R.id.post_title);
            postDescription = itemView.findViewById(R.id.post_description);
        }
    }
}
