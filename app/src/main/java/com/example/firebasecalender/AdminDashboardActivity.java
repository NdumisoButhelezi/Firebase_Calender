package com.example.firebasecalender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private FirebaseFirestore db;
    private TextView titleTextView;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        titleTextView = findViewById(R.id.titleTextView);
        recyclerView = findViewById(R.id.bookingsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        loadBookings();
    }

    private void loadBookings() {
        db.collection("bookings")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> bookings = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking booking = document.toObject(Booking.class);
                        booking.setId(document.getId());
                        bookings.add(booking);
                    }
                    adapter = new BookingAdapter(bookings, this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // Show/hide empty state
                    if (bookings.isEmpty()) {
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }


    public void handleBookingAction(Booking booking, boolean isAccepted) {
        String newStatus = isAccepted ? "accepted" : "declined";

        // Update the booking status in Firestore
        db.collection("bookings").document(booking.getId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Remove the booking from the adapter
                    adapter.removeBooking(booking);

                    // Fetch email and send confirmation
                    fetchUserEmailAndSendConfirmation(booking.getPatientId(), isAccepted);

                    Toast.makeText(this, "Booking " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchUserEmailAndSendConfirmation(String userId, boolean isAccepted) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String email = documentSnapshot.getString("email");
                    if (email != null) {
                        sendBookingConfirmation(email, isAccepted);
                    } else {
                        Toast.makeText(this, "Email not found for user", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch user email", Toast.LENGTH_SHORT).show();
                });
    }

    public void sendBookingConfirmation(String email, boolean isAccepted) {
        String subject = isAccepted ? "Booking Confirmation" : "Booking Declined";
        String body = isAccepted ? "Your booking has been confirmed." : "Unfortunately, we cannot accommodate your booking at this time.";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }
    public void fetchUserEmail(String userId, BookingAdapter.BookingViewHolder holder) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String email = documentSnapshot.getString("email");
                    if (email != null) {
                        holder.emailTextView.setText(email);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch user email", Toast.LENGTH_SHORT).show();
                });
    }
}
