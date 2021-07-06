package com.example.komalareddy.libraryaudit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DepartmentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        sp=findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dept=sp.getItemAtPosition(position).toString();
                if (!dept.equals("CSE")){
                    Toast.makeText(DepartmentActivity.this, "Please select CSE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void proceed(View view) {
        if (isOnline()) {
            Intent inte = new Intent(DepartmentActivity.this, LoginActivity.class);
            startActivity(inte);
        }else {
            noOnline();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            String useremail=currentUser.getEmail();
            if (useremail.equals("admin@gmail.com")) {
                Intent intent = new Intent(this, IssueAndReportActivity.class);
                startActivity(intent);
            }else {
                Intent inten = new Intent(this, ScanActivity.class);
                startActivity(inten);
            }
        }
    }
    @SuppressLint("MissingPermission")
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo()!= null;

    }

    private void noOnline() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on the internet to login");
        builder.setTitle("Internet Not Available!");
        builder.setPositiveButton("Go TO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).show();
    }
}
