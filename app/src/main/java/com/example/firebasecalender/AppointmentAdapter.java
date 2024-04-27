package com.example.firebasecalender;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Booking> bookings;

    public AppointmentAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.dateTimeTextView.setText(booking.getDate() + " " + booking.getTime());
        holder.reasonTextView.setText(booking.getReason());
        holder.statusTextView.setText(booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTextView, reasonTextView, statusTextView;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}
