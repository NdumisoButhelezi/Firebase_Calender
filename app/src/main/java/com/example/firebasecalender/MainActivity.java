package com.example.firebasecalender;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private calendarView calenderView;
    private EditText editText;

    private String stringDateSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        calenderView =findViewById(R.id.calendarView);
        editText = findViewById(R.id.editText);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calenderView,int i,int i1, int i2){
                stringDateSelected = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                calenderClicked();
            }

        });


    }

    private void calenderClicked(){

    }

    public void buttonSaveEvent(View view){

    }
}