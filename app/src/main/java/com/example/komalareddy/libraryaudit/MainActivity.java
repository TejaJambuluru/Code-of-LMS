package com.example.komalareddy.libraryaudit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class MainActivity extends AppCompatActivity {



    private static int SPLASH_TIMEOUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,DepartmentActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIMEOUT);
    }

}
