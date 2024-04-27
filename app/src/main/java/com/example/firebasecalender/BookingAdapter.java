package com.example.firebasecalender;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private final List<Booking> bookings;
    private final AdminDashboardActivity context;

    public BookingAdapter(List<Booking> bookings, AdminDashboardActivity context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.dateTextView.setText(booking.getDate());
        holder.timeTextView.setText(booking.getTime());
        holder.reasonTextView.setText(booking.getReason());

        context.fetchUserEmail(booking.getPatientId(), holder);

        holder.acceptButton.setOnClickListener(v -> handleBookingAction(booking, true));
        holder.declineButton.setOnClickListener(v -> handleBookingAction(booking, false));
    }

    private void handleBookingAction(Booking booking, boolean isAccepted) {
        context.handleBookingAction(booking, isAccepted);
        removeBooking(booking);
    }

    public void removeBooking(Booking booking) {
        int position = bookings.indexOf(booking);
        if (position != -1) {
            bookings.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, reasonTextView, emailTextView;
        MaterialButton acceptButton, declineButton;

        BookingViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
