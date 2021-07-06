package com.example.komalareddy.libraryaudit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckIssuedBooks extends AppCompatActivity {

    EditText editText;
    TextView textView;
    String string;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_issued_books);
        getSupportActionBar().setTitle("By BookNo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = findViewById(R.id.booknumber);
        textView = findViewById(R.id.result);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

    }

    public void submit(View view) {
        string = editText.getText().toString();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (string.isEmpty()) {
                    Toast.makeText(CheckIssuedBooks.this, "Please Enter Book Number", Toast.LENGTH_SHORT).show();
                } else {
                    long num = dataSnapshot.getChildrenCount();
                    int i;
                    for (i = 0; i < num; i++) {
                        String str = String.valueOf(i);
                        String acno = dataSnapshot.child(str).child("AccNo").getValue().toString();
                        String issue = dataSnapshot.child(str).child("Issued").getValue().toString();
                        if (string.equals(acno)) {
                            if (issue.isEmpty()) {
                                textView.setText("This book is not Issued Before");
                                break;
                            } else {
                                textView.setText(acno + " is issued to the employee with Emp ID : " + issue);
                                break;
                            }
                        }
                    }
                    if (i==num){
                        Toast.makeText(CheckIssuedBooks.this, "Invalid Book Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CheckIssuedBooks.this, "null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
