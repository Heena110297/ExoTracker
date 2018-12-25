package com.example.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 18-04-2018.
 */

public class ViewOrder extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView label;


    Requests request;
  String address;
Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieworders);

            label = findViewById(R.id.label);
            final String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("requests");
            btn = findViewById(R.id.button2);


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //int c =1
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String num = ds.getKey();

                            request = ds.getValue(Requests.class);
                            String r = request.getEmp();
                            if(r!=null){
                            if (r.equals("harsh")) {
                                String prob = request.getProbdes();
                                String category = request.category();
                                String date = request.date();
                                String time = request.time();
                                address = request.address();
                                if (prob != null)
                                    label.setText("Problem Description: " + prob + "\nService Type: " + category + "\nDate :" + date + "\ntime: " + time);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });



        }

       public void click(View view)
       {

                   Intent intent = new Intent(ViewOrder.this, MapsActivity.class);
                   intent.putExtra("Address",address);
                   startActivity(intent);
                   finish();
       }
    }

