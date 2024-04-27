package com.example.firebasecalender;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.firebasecalender.Booking;


public class AppointmentStatusActivity extends AppCompatActivity {

    private RecyclerView appointmentRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_status);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appointmentRecyclerView = findViewById(R.id.appointmentRecyclerView);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        loadAppointments();
    }

    private void loadAppointments() {
        db.collection("bookings")
                .whereIn("status", Arrays.asList("accepted", "declined"))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> bookings = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking booking = document.toObject(Booking.class);
                        booking.setId(document.getId());
                        bookings.add(booking);
                    }
                    appointmentAdapter = new AppointmentAdapter(bookings);
                    appointmentRecyclerView.setAdapter(appointmentAdapter);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

}

