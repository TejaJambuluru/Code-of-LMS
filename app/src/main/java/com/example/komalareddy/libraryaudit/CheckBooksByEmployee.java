package com.example.komalareddy.libraryaudit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckBooksByEmployee extends AppCompatActivity {

    EditText editText;
    TextView textView;
    String string;
    ListView datas, booksName;
    ArrayList<String> strings, bookNames;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_books_by_employee);
        getSupportActionBar().setTitle("CheckBooks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = findViewById(R.id.employeeid1);
        textView = findViewById(R.id.result2);
        datas = findViewById(R.id.listView2);
        booksName = findViewById(R.id.listView3);
        strings = new ArrayList<>();
        bookNames = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    public void submit2(View view) {
        string = editText.getText().toString();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (string.isEmpty()) {
                    Toast.makeText(CheckBooksByEmployee.this, "Please Enter Emp ID To Search", Toast.LENGTH_SHORT).show();
                } else {
                    long num = dataSnapshot.getChildrenCount();
                    int i;
                    for (i = 0; i < num; i++) {
                        String str = String.valueOf(i);
                        String acno = dataSnapshot.child(str).child("AccNo").getValue().toString();
                        String issue = dataSnapshot.child(str).child("Issued").getValue().toString();
                        String name = dataSnapshot.child(str).child("Title").getValue().toString();
                        if (string.equals(issue)) {
                            if (issue.isEmpty()) {
                                textView.setText("Employee with Emp Id " + string + " didn't issued with any book");
                                break;
                            } else {
                                strings.add(acno);
                                bookNames.add(name);
                                textView.setText("Employee with ID "+issue + " is issued with book(s) :");
                            }
                        }
                    }
                    datas.setAdapter(new ArrayAdapter<String>(CheckBooksByEmployee.this, android.R.layout.simple_list_item_1, strings));
                    booksName.setAdapter(new ArrayAdapter<String>(CheckBooksByEmployee.this, android.R.layout.simple_list_item_1, bookNames));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CheckBooksByEmployee.this, "null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}