package com.example.quiz6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit1;
    EditText edit2;
    TextView textResult;
    Button btnAdd;
    Button btnSub;
    Button btnDiv;
    Button btnMul;
    Button btnRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit1 = (EditText) findViewById(R.id.Edit1);
        edit2 = (EditText) findViewById(R.id.Edit2);
        textResult = (TextView) findViewById(R.id.TextResult);
        btnAdd = (Button)findViewById(R.id.BtnAdd);
        btnSub = (Button)findViewById(R.id.BtnSub);
        btnDiv = (Button)findViewById(R.id.BtnDiv);
        btnMul = (Button)findViewById(R.id.BtnMul);
        btnRest = (Button)findViewById(R.id.BtnRest);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double result;
                try {
                    result = Double.parseDouble(String.valueOf(edit1.getText()))
                            + Double.parseDouble(String.valueOf(edit2.getText()));

                    textResult.setText("계산 결과: " + String.valueOf(result));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "올바른 수를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double result;
                try {
                    result = Double.parseDouble(String.valueOf(edit1.getText()))
                            - Double.parseDouble(String.valueOf(edit2.getText()));

                    textResult.setText("계산 결과: " + String.valueOf(result));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "올바른 수를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double result;
                try {
                    result = Double.parseDouble(String.valueOf(edit1.getText()))
                            * Double.parseDouble(String.valueOf(edit2.getText()));

                    textResult.setText("계산 결과: " + String.valueOf(result));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "올바른 수를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double result;
                try {
                    result = Double.parseDouble(String.valueOf(edit1.getText()))
                            / Double.parseDouble(String.valueOf(edit2.getText()));

                    textResult.setText("계산 결과: " + String.valueOf(result));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "올바른 수를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double result;
                try {
                    result = Double.parseDouble(String.valueOf(edit1.getText()))
                            % Double.parseDouble(String.valueOf(edit2.getText()));

                    textResult.setText("계산 결과: " + String.valueOf(result));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "올바른 수를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}