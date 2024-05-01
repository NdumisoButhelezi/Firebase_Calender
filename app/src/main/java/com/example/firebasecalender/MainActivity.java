package com.example.firebasecalender;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatePicker datePicker;


    private Spinner timeSlot;

    private Button btn;

    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        datePicker =findViewById(R.id.datePicker);
        timeSlot = findViewById(R.id.spinnerTimeSlots);

        setupDatePicker();
        buttonSaveEvent();

        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");


    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);  // Start from the next day

        // Set the minimum date to tomorrow
        datePicker.setMinDate(calendar.getTimeInMillis());

        // Disable weekends in the DatePicker dialog
        datePicker.setCalendarViewShown(false); // Deprecate the calendar view
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datePicker.getMinDate());
        while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            datePicker.setMinDate(cal.getTimeInMillis());
        }

        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (newDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || newDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    if (newDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        newDate.add(Calendar.DAY_OF_MONTH, 2); // Skip to Monday
                    } else {
                        newDate.add(Calendar.DAY_OF_MONTH, 1); // Skip to Monday
                    }
                    datePicker.updateDate(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));
                }
            }
        });
    }

    public void buttonSaveEvent(){
        btn = findViewById(R.id.btnBook);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(String.valueOf(datePicker)).setValue(timeSlot.getSelectedItem().toString());
            }
        });

    }
}