package com.example.komalareddy.libraryaudit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ReturnActivity extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    DatabaseReference mReference;
    EditText editText;
    String str12;
    Button b2;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        editText = findViewById(R.id.bookacno);
        b2 = findViewById(R.id.returnbook);
        mdatabase = FirebaseDatabase.getInstance();
        mReference = mdatabase.getReference();
        qrScan = new IntentIntegrator(this);
        getSupportActionBar().setTitle("ReturnBook");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str12 = editText.getText().toString();
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (TextUtils.isEmpty(str12)) {
                            Toast.makeText(ReturnActivity.this, "Please Enter Book Number To Return", Toast.LENGTH_SHORT).show();
                        } else {
                            long num = dataSnapshot.getChildrenCount();
                            int i;
                            for (i = 0; i < num; i++) {
                                String str = String.valueOf(i);
                                String acno = dataSnapshot.child(str).child("AccNo").getValue().toString();
                                String emp = dataSnapshot.child(str).child("Issued").getValue().toString();
                                if (str12.equals(acno)) {
                                    if ((dataSnapshot.child(str).child("Issued").getValue().toString()).equals("")) {
                                        Toast.makeText(ReturnActivity.this, "This Book is not issued before", Toast.LENGTH_LONG).show();
                                        editText.getText().clear();
                                        break;
                                    }
                                    mReference.child(str).child("Issued").setValue("");
                                    Toast.makeText(ReturnActivity.this, str12+" has returned successfully from "+emp, Toast.LENGTH_LONG).show();
                                    editText.getText().clear();
                                    break;
                                }
                            }
                            if (i == num) {
                                Toast.makeText(ReturnActivity.this, "Invalid Book Number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ReturnActivity.this, "null", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrscan,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.codescan == item.getItemId()){
            qrScan.initiateScan();
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    editText.setText(result.getContents(), TextView.BufferType.EDITABLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
