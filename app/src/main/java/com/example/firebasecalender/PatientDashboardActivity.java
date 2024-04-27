package com.example.firebasecalender;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PatientDashboardActivity extends AppCompatActivity {
    private EditText dateEditText, timeEditText, reasonEditText;
    private Button selectDateButton, selectTimeButton, bookButton;
    private CardView mainCardView, dateCardView, timeCardView, reasonCardView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        reasonEditText = findViewById(R.id.reasonEditText);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        bookButton = findViewById(R.id.bookButton);

        // Initialize CardViews
        mainCardView = findViewById(R.id.mainCardView);
        dateCardView = mainCardView.findViewById(R.id.dateCardView);
        timeCardView = mainCardView.findViewById(R.id.timeCardView);
        reasonCardView = mainCardView.findViewById(R.id.reasonCardView);

        // Set click listeners
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());
        bookButton.setOnClickListener(v -> bookAppointment());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year),
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute)),
                mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void bookAppointment() {
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String reason = reasonEditText.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> booking = new HashMap<>();
        booking.put("date", date);
        booking.put("time", time);
        booking.put("reason", reason);
        booking.put("patientId", mAuth.getCurrentUser().getUid());
        booking.put("status", "pending"); // Add the status field

        db.collection("bookings").add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private void clearForm() {
        dateEditText.setText("");
        timeEditText.setText("");
        reasonEditText.setText("");
    }
}
