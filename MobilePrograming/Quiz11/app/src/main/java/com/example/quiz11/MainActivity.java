package com.example.quiz11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    Button btn_prev, btn_next, btn_end;
    CalendarView calendarView;
    TimePicker timePicker;
    ViewFlipper viewFlipper;
    TextView tv_end;
    String year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_prev = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);
        btn_end = findViewById(R.id.btn_end);
        viewFlipper = findViewById(R.id.view_flipper);
        calendarView = findViewById(R.id.calendar);
        timePicker = findViewById(R.id.timepicker);
        tv_end = findViewById(R.id.tv_end);

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                year = String.valueOf(i);
                month = String.valueOf(i1+1);
                day = String.valueOf(i2);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = String.valueOf(i);
                minute = String.valueOf(i1);
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_end.setText(
                        year + "년" + month + "월" + day + "일 " + hour + "시" + minute + "분"
                );
            }
        });

    }
}