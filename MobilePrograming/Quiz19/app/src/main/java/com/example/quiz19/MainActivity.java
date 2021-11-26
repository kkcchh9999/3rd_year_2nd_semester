package com.example.quiz19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnInit, btnAdd, btnModify, btnDelete, btnShow;
    EditText etName, etNumber, etResult1, etResult2;
    MyDBHelper myDBHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Quiz19 권철현");

        etName = findViewById(R.id.et_name);
        etNumber = findViewById(R.id.et_number);
        etResult1 = findViewById(R.id.et_result1);
        etResult2 = findViewById(R.id.et_result2);

        btnInit = findViewById(R.id.btn_init);
        btnAdd = findViewById(R.id.btn_add);
        btnModify = findViewById(R.id.btn_modify);
        btnDelete = findViewById(R.id.btn_delete);
        btnShow = findViewById(R.id.btn_show);

        myDBHelper = new MyDBHelper(this);
        btnInit.setOnClickListener(v -> {
            sqlDB = myDBHelper.getWritableDatabase();
            myDBHelper.onUpgrade(sqlDB, 1, 2);
            sqlDB.close();
        });

        btnAdd.setOnClickListener(v -> {
            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO myTBL VALUES ('"
                    + etName.getText().toString() + "', "
                    + etNumber.getText().toString() + ");");
            sqlDB.close();
            Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
        });

        btnShow.setOnClickListener(v -> {
            sqlDB = myDBHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myTBL;", null);

            String strNames = "그룹이름" + "\r\n" + "----------" + "\r\n";
            String strNumbers = "인원" + "\r\n" + "----------" + "\r\n";

            while (cursor.moveToNext()) {
                strNames += cursor.getString(0) + "\r\n";
                strNumbers += cursor.getString(1) + "\r\n";
            }

            etResult1.setText(strNames);
            etResult2.setText(strNumbers);

            cursor.close();
            sqlDB.close();
        });

        btnModify.setOnClickListener(v -> {
            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("UPDATE myTBL SET gNumber = '"
                    + etNumber.getText().toString() + "'WHERE gName = '"
                    + etName.getText().toString() + "';");
            Toast.makeText(getApplicationContext(),"수정됨", Toast.LENGTH_SHORT).show();
            sqlDB.close();
        });

        btnDelete.setOnClickListener(v -> {
            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("DELETE FROM myTBL WHERE gName = '"
                    + etName.getText().toString() + "';");
            Toast.makeText(getApplicationContext(),"삭제됨", Toast.LENGTH_SHORT).show();
            sqlDB.close();
        });
    }

    public class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE myTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS myTBL");
            onCreate(db);
        }
    }
}