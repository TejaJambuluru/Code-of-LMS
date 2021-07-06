package com.example.komalareddy.libraryaudit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class IssueAndReportActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mReference;
    FirebaseDatabase mdatabase;


    Workbook wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_and_report);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mReference = mdatabase.getReference();

    }

    public void getreportt(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IssueAndReportActivity.this);
        builder.setMessage("Do you want to download the report?");
        builder.setTitle("Download Report");
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                wb = new HSSFWorkbook();

                final Sheet sheet = wb.createSheet("Name of sheet");

                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long num = dataSnapshot.getChildrenCount();
                        int b = 0, j;
                        for (int i = 0; i < num; i++) {
                            String str1 = String.valueOf(i);
                            String ac = dataSnapshot.child(str1).child("AccNo").getValue().toString();
                            String title = Objects.requireNonNull(dataSnapshot.child(str1).child("Title").getValue()).toString();
                            if ((dataSnapshot.child(str1).child("Status").getValue().toString()).isEmpty() &&
                                    (dataSnapshot.child(str1).child("Issued").getValue().toString()).isEmpty()) {
                                j = 0;
                                Row row = sheet.createRow(b);
                                Cell cell = row.createCell(j);
                                cell.setCellValue(ac);
                                j++;
                                cell = row.createCell(j);
                                cell.setCellValue(title);
                                b++;
                            }
                        }
                        File file = new File(getExternalFilesDir(null), "book.xls");
                        FileOutputStream outputStream = null;

                        try {
                            outputStream = new FileOutputStream(file);
                            wb.write(outputStream);
                            Toast.makeText(getApplicationContext(), "Downloaded in path Device storage/Android/data/com.example.komalareddy.libraryaudit/files/book.xls", Toast.LENGTH_LONG).show();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_LONG).show();
                            try {
                                outputStream.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(IssueAndReportActivity.this, "null", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void issueandreturnBook(View view) {
        Intent inten=new Intent(IssueAndReportActivity.this,IssueAndReturnActivity.class);
        startActivity(inten);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.signout == item.getItemId())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(IssueAndReportActivity.this);
            builder.setTitle("Are you ready to exit the application?");
            builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    signout();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();
        }

        else if (R.id.reset == item.getItemId()){
            Intent intent=new Intent(this,ResetActivity.class);
            startActivity(intent);
        }
        else if(R.id.generateQRCode==item.getItemId()){
            Intent intent=new Intent(this,GenerateQRCodeActivity.class);
            startActivity(intent);

        }
        else if(R.id.KeepNotes==item.getItemId()){
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        }



        else if(R.id.KeepNotes==item.getItemId()){
            Intent intent = new Intent(this, NoteEditorActivity.class);
            startActivity(intent);
            AlertDialog.Builder builder = new AlertDialog.Builder(IssueAndReportActivity.this);
            builder.setTitle("Are you ready to exit the application?");
            builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    signout();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();
        }


        else if (R.id.bookno == item.getItemId()){
            Intent intent=new Intent(this,CheckIssuedBooks.class);
            startActivity(intent);
        }
        else if (R.id.empid == item.getItemId()){
            Intent inte=new Intent(this,CheckIssuedEmployee.class);
            startActivity(inte);
        }
        return super.onOptionsItemSelected(item);
    }
    private void signout() {
        mAuth.signOut();
        Intent i=new Intent(IssueAndReportActivity.this,DepartmentActivity.class);
        startActivity(i);
        finish();
    }
}
