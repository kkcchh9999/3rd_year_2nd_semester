package com.example.quiz4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText firstInput;
    EditText secondInput;
    ImageButton addButton;
    Button devButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstInput = findViewById(R.id.first_input);
        secondInput = findViewById(R.id.second_input);
        addButton = findViewById(R.id.button_add);
        devButton = findViewById(R.id.button_dev);

        addButton.setOnClickListener(view -> {
            double result = 0;

            try {
                result = Double.parseDouble(String.valueOf(firstInput.getText()))
                        + Double.parseDouble(String.valueOf(secondInput.getText()));

                Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "올바른 정수를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        devButton.setOnClickListener(view -> {
            double result = 0;

            try {
                result = Double.parseDouble(String.valueOf(firstInput.getText()))
                        / Double.parseDouble(String.valueOf(secondInput.getText()));

                Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "올바른 정수를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}