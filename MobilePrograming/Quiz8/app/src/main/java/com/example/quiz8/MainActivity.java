package com.example.quiz8;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      setContentView(R.layout.activity_main);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout, params);

        TextView textView = new TextView(this);
        textView.setText("권철현 입니다.");
        layout.addView(textView);

        CheckBox checkBox1 = new CheckBox(this);
        CheckBox checkBox2 = new CheckBox(this);
        checkBox1.setText("KOR");
        checkBox2.setText("ENG");
        layout.addView(checkBox1);
        layout.addView(checkBox2);

        Button btn = new Button(this);
        EditText editText = new EditText(this);
        editText.setEnabled(false);
        editText.setTextColor(Color.BLACK);
        btn.setText("Click");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked() && checkBox2.isChecked()) {
                    editText.setText("KOR & ENG");
                } else if (checkBox1.isChecked() && !checkBox2.isChecked()) {
                    editText.setText("KOR");
                } else if (!checkBox1.isChecked() && checkBox2.isChecked()) {
                    editText.setText("ENG");
                } else {
                    editText.setText("");
                }
            }
        });
        layout.addView(btn);
        layout.addView(editText);
    }
}