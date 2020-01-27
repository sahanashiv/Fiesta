package com.example.fiesta.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiesta.R;
import com.example.fiesta.models.College;
import com.example.fiesta.models.Event;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ProductViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventListAdapter(Context c, List<Event> p) {
        Log.d("COLLG_ADAP", p.toString());
        this.context = c;
        this.eventList = p;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventListAdapter.ProductViewHolder(LayoutInflater.from(this.context).inflate(R.layout.event_list_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ProductViewHolder holder, int position) {
        holder.eventName.setText("Event Name: "+eventList.get(position).getEventName());
        holder.eventCoordinator.setText("Event Coordinator: "+eventList.get(position).getEventCoordinator());
        holder.eventContact.setText("Event Contact: " +eventList.get(position).getEventContact());
        holder.eventDetail.setText("Event Description: "+eventList.get(position).getEventDescription());

    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView eventDetail,eventContact,eventName,eventCoordinator;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            eventContact = itemView.findViewById(R.id.eventContact);
            eventName = itemView.findViewById(R.id.eventName);
            eventCoordinator = itemView.findViewById(R.id.eventCoordinator);
            eventDetail = itemView.findViewById(R.id.eventDetailsView);

        }
    }
}
