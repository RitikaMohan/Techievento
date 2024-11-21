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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_recycler, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDate.setText(event.getDate());
        holder.eventLocation.setText(event.getLocation());

        Glide.with(holder.eventImage.getContext()).load(event.getImage_url()).into(holder.eventImage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetails_Activity.class);
            intent.putExtra("eventName", event.getTitle());
            intent.putExtra("eventHost", event.getHost());
            intent.putExtra("eventImage", event.getImage_url());
            intent.putExtra("eventDate", event.getDate());
            intent.putExtra("eventTiming", event.getTime());
            intent.putExtra("eventLocation", event.getLocation());
            intent.putExtra("activities", event.getActivities().toArray(new String[0]));
            intent.putExtra("winner1", event.getWinner1());
            intent.putExtra("winner2", event.getWinner2());
            intent.putExtra("winner3", event.getWinner3());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        int limit = 5;
        return Math.min(eventList.size(), limit);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle, eventDate, eventLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDate = itemView.findViewById(R.id.event_time);
            eventLocation = itemView.findViewById(R.id.event_location);
        }
    }
}
