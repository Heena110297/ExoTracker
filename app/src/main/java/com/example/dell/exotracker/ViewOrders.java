package com.example.dell.exotracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 20-03-2018.
 */

public class ViewOrders extends AppCompatActivity {
    private FirebaseDatabase database;
    DatabaseReference myRef ;
    TextView label;
    private Button emp;
    private Button edit;
    private Button cancel;

    Requests request;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieworders);

        emp = findViewById(R.id.emp);
        edit = findViewById(R.id.edit);
        cancel = findViewById(R.id.cancel);

        label = findViewById(R.id.label);
        final String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requests");

        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrders.this, ViewWorker.class);
                intent.putExtra("EXTRA_SESSION_ID", s);
                startActivity(intent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //int c =1
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String num = ds.getKey();
                        if (num.equals(s)){
                        request = ds.getValue(Requests.class);
                        String prob = request.getProbdes();
                        String category = request.category();
                        String date = request.date();
                        String time = request.time();
                        if (prob != null)
                            label.setText("Problem Description: " + prob + "\nService Type: " + category + "\nDate :" + date + "\ntime: " + time);
                       break;
                        }
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });




    }
}
