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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class IssueActivity extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    DatabaseReference mReference;
    EditText et, et2;
    Button b1;
    int count;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        et = findViewById(R.id.acno);
        et2 = findViewById(R.id.editText);
        b1 = findViewById(R.id.button);
        qrScan = new IntentIntegrator(this);
        getSupportActionBar().setTitle("IssueBook");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mdatabase = FirebaseDatabase.getInstance();
        mReference = mdatabase.getReference();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(et.getText().toString())) && !(TextUtils.isEmpty(et2.getText().toString()))) {
                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long num = dataSnapshot.getChildrenCount();
                            int i;
                            for (i = 0; i < num; i++) {
                                String str = String.valueOf(i);
                                String acno = dataSnapshot.child(str).child("AccNo").getValue().toString();

                                String issue = dataSnapshot.child(str).child("Issued").getValue().toString();
                                if ((et.getText().toString()).equals(acno)) {
                                    count=0;
                                    if (!(issue.equals(""))) {
                                        Toast.makeText(IssueActivity.this, "Already Issued to " + issue, Toast.LENGTH_SHORT).show();
                                        et2.getText().clear();
                                    } else {
                                        for (int j=0;j<num;j++){
                                            String string = String.valueOf(j);
                                            String issued = dataSnapshot.child(string).child("Issued").getValue().toString();
                                            if (issued.equals(et2.getText().toString())){
                                                count++;
                                            }
                                        }
                                        if (count<5) {

                                            mReference.child(str).child("Issued").setValue(et2.getText().toString());


                                            Toast.makeText(IssueActivity.this, "Issued to " + et2.getText().toString(), Toast.LENGTH_SHORT).show();
                                            et2.getText().clear();
                                            et.getText().clear();
                                        }else {
                                            Toast.makeText(IssueActivity.this, "Issue book limit exceeded to "+et2.getText().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    break;
                                }
                            }
                            if (i==num){
                                Toast.makeText(IssueActivity.this, "Invalid Book Number", Toast.LENGTH_SHORT).show();
                                et2.getText().clear();
                                et.getText().clear();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(IssueActivity.this, "null", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(IssueActivity.this, "Please enter Book No.& Emp ID", Toast.LENGTH_SHORT).show();
                }
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
                    et.setText(result.getContents(), TextView.BufferType.EDITABLE);
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
