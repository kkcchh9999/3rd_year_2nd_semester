package com.example.quiz16;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    TextView tv_date, tv_time;
    Button btn_date, btn_time;
    DatePicker datePicker;
    TimePicker timePicker;
    View dialogView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        btn_date = findViewById(R.id.btn_date);
        btn_time = findViewById(R.id.btn_time);

        btn_date.setOnClickListener(v -> {
            dialogView = View.inflate(MainActivity.this,
                    R.layout.dialog1, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("날짜 입력");
            builder.setView(dialogView);
            builder.setPositiveButton("확인",
                    (dialog, which) -> {
                        datePicker = dialogView.findViewById(R.id.date_picker);

                        tv_date.setText(datePicker.getMonth()+1 + "월 " + datePicker.getDayOfMonth() + "일");
                    });
            builder.setNegativeButton("취소", null);
            builder.show();
        });


        btn_time.setOnClickListener(v -> {
            dialogView = View.inflate(MainActivity.this,
                    R.layout.dialog2, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("날짜 입력");
            builder.setView(dialogView);
            builder.setPositiveButton("확인",
                    (dialog, which) -> {
                        timePicker = dialogView.findViewById(R.id.time_picker);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tv_time.setText(timePicker.getHour() +"시 "+ timePicker.getMinute() + "분");
                        }
                    });
            builder.setNegativeButton("취소", null);
            builder.show();
        });



    }
}