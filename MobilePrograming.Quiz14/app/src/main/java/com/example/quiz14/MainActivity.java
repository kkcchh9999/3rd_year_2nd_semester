package com.example.quiz14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView dog1, dog2, dog3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dog1 = findViewById(R.id.dog1);
        dog2 = findViewById(R.id.dog2);
        dog3 = findViewById(R.id.dog3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                dog1.setVisibility(View.VISIBLE);
                dog2.setVisibility(View.INVISIBLE);
                dog3.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu2:
                dog1.setVisibility(View.INVISIBLE);
                dog2.setVisibility(View.VISIBLE);
                dog3.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu3:
                dog1.setVisibility(View.INVISIBLE);
                dog2.setVisibility(View.INVISIBLE);
                dog3.setVisibility(View.VISIBLE);
                return true;
        }
        return false;
    }
}