package com.example.komalareddy.libraryaudit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class IssueAndReturnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_and_return);
        getSupportActionBar().setTitle("IssueAndReturn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void issuebook(View view) {
        Intent i=new Intent(IssueAndReturnActivity.this,IssueActivity.class);
        startActivity(i);
    }

    public void returnbook(View view) {
        Intent in=new Intent(IssueAndReturnActivity.this,ReturnActivity.class);
        startActivity(in);
    }
}
