package com.example.employer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by Dell on 21-03-2018.
 */

public class Afterogin extends AppCompatActivity {
    private static final String TAG = "My Tag";
    ListView listview;
    ArrayList<String> list ;
    ArrayList<String> id;
    ArrayAdapter<String> adapter;
     Requests request ;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterogin);

        mAuth = FirebaseAuth.getInstance();
        String tokenfordb = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myR = database.getReference("msgnew");
        Log.d(TAG, "Refreshed token for Server: " + tokenfordb);
        myR.setValue(tokenfordb);

        listview=findViewById(R.id.listview);
        id = new ArrayList<>();
        list = new ArrayList<>();
        adapter= new ArrayAdapter(this,R.layout.activity_listview,R.id.label ,list);
        request = new Requests();
      //  c=1;

        DatabaseReference myRef = database.getReference("requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //int c =1;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                   request = ds.getValue(Requests.class);
                   String prob = request.getProbdes();
                   String category = request.category();
                   String date = request.date();
                   String time = request.time();
                   String address =request.address();
                   if(prob!=null)
                   list.add("Problem Description: "+ prob +"\nService Type: " + category +"\nDate :"+ date +"\ntime: " +time +"\nAddress: " +address);
                   id.add(ds.getKey());
                }
               listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Toast.makeText(
                         Afterogin.this,
                         "Your message here" + listview.getItemAtPosition(i) ,
                         Toast.LENGTH_SHORT
                 ).show();
                 String info = (String) listview.getItemAtPosition(i);
                 Intent intent = new Intent(Afterogin.this, Category.class);
                 intent.putExtra("Info", info);
                 startActivity(intent);
                 finish();
             }
         });
    }
}
