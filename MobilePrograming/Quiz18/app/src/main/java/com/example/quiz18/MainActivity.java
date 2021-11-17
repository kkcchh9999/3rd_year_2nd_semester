package com.example.quiz18;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    MyPictureView myPictureView;
    Button btnNext, btnPrevious;
    int curNum = 0;
    String imageFname;
    File[] imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        myPictureView = findViewById(R.id.myPictureView);

        imageFiles = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/Pictures").listFiles();
        assert imageFiles != null;
        imageFname = imageFiles[0].toString();
        myPictureView.imagePath = imageFname;

        btnPrevious.setOnClickListener(v -> {
            if (curNum == 0) {
                curNum = imageFiles.length-2;
                imageFname = imageFiles[curNum].toString();
                myPictureView.imagePath = imageFname;
                myPictureView.invalidate();
            } else {
                curNum --;
                imageFname = imageFiles[curNum].toString();
                myPictureView.imagePath = imageFname;
                myPictureView.invalidate();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (curNum >= imageFiles.length-2) {
                curNum = 0;
                imageFname = imageFiles[curNum].toString();
                myPictureView.imagePath = imageFname;
                myPictureView.invalidate();
            } else {
                curNum ++;
                imageFname = imageFiles[curNum].toString();
                myPictureView.imagePath = imageFname;
                myPictureView.invalidate();
            }
        });
    }
}