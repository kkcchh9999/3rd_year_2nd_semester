package com.example.practice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout.LayoutParams match_wrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //---LinearLayout---
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        //---TextView---
        TextView textView = new TextView(this);
        textView.setLayoutParams(match_wrap);
        textView.setTextColor(Color.BLACK);
        textView.setText("결과");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);


        //---editText---
        EditText editText1 = new EditText(this);
        editText1.setHint("숫자 1 입력");
        editText1.setLayoutParams(match_wrap);
        parentLayout.addView(editText1);
        //---editText---
        EditText editText2 = new EditText(this);

        editText2.setHint("숫자 2 입력");
        editText2.setLayoutParams(match_wrap);
        parentLayout.addView(editText2);

        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setLayoutParams(match_wrap);

        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setLayoutParams(match_wrap);

        Button button[] = new Button[14];
        for (int i = 0; i < 10; i ++) {
            button[i] = new Button(this);
            button[i].setText(String.valueOf(i));
            button[i].setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    ,1
            ));

            int finalI = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText1.isFocused()) {
                        String tmp = editText1.getText().toString() + button[finalI].getText().toString();
                        editText1.setText(tmp);
                    } else if (editText2.isFocused()) {
                        String tmp = editText2.getText().toString() + button[finalI].getText().toString();
                        editText2.setText(tmp);
                    } else {
                        Toast.makeText(getApplicationContext(), "에디트 텍스트 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (i < 5) {
                linearLayout1.addView(button[i]);
            } else {
                linearLayout2.addView(button[i]);
            }
        }
        parentLayout.addView(linearLayout1);
        parentLayout.addView(linearLayout2
        );

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        for (int i = 10; i<14; i ++) {
            button[i] = new Button(this);
            button[i].setLayoutParams(match_wrap);
            if (i == 10) {
                button[i].setText("+");
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
                        textView.setText(String.valueOf(Integer.parseInt(editText1.getText().toString()) + Integer.parseInt(editText2.getText().toString())));
                    }
                });
            } else if (i == 11) {
                button[i].setText("-");
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
                        textView.setText(String.valueOf(Integer.parseInt(editText1.getText().toString()) - Integer.parseInt(editText2.getText().toString())));
                    }
                });
            } else if (i == 12) {
                button[i].setText("X");
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
                        textView.setText(String.valueOf(Integer.parseInt(editText1.getText().toString()) * Integer.parseInt(editText2.getText().toString())));
                    }
                });
            } else {
                button[i].setText("/");
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
                        textView.setText(String.valueOf(Double.parseDouble(editText1.getText().toString()) / Double.parseDouble(editText2.getText().toString())));
                    }
                });
            }
            parentLayout.addView(button[i]);
        }
        parentLayout.addView(textView);
        setContentView(parentLayout, matchParent);
    }
}