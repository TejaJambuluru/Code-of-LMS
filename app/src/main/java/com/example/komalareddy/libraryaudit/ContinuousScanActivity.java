package com.example.komalareddy.libraryaudit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;

import java.util.List;


public class ContinuousScanActivity extends AppCompatActivity {

    TextView tvCardText;
    DecoratedBarcodeView dbvScanner;

    FirebaseDatabase mdatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_scan);

        tvCardText = findViewById(R.id.data);//RUN APP chudam nduko
        dbvScanner = findViewById(R.id.dbv_barcode);

        mdatabase = FirebaseDatabase.getInstance();
        mReference = mdatabase.getReference();

        dbvScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(final BarcodeResult result) {
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long num = dataSnapshot.getChildrenCount();
                        for (int i = 0; i < num; i++) {
                            String str = String.valueOf(i);
                            String acno = dataSnapshot.child(str).child("AccNo").getValue().toString();
                            if ((result.getText()).equals(acno)) {
                                if ((dataSnapshot.child(str).child("Status").getValue().toString()).equals("1"))
                                {
                                    updateText(result.getText());
                                    Toast.makeText(ContinuousScanActivity.this, "Already Scanned", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                else {
                                    beepSound();
                                    updateText(result.getText());
                                    Toast.makeText(ContinuousScanActivity.this, "Scanned", Toast.LENGTH_SHORT).show();
                                    mReference.child(str).child("Status").setValue("1");
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ContinuousScanActivity.this, "null", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }

        });
        requestPermission();
    }

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    protected void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeScanner();
    }

    protected void resumeScanner() {
        if (!dbvScanner.isActivated())
            dbvScanner.resume();
        Log.d("scanner-pause", "paused: false");
    }

    protected void pauseScanner() {
        dbvScanner.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length < 1) {
            requestPermission();
        } else {
            dbvScanner.resume();
        }
    }
    public void finishScan(View view) {
        Intent intent=new Intent(ContinuousScanActivity.this,ScanActivity.class);
        startActivity(intent);
        finish();
    }
}
