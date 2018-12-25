package com.example.employer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Dell on 18-04-2018.
 */

public class EmployeeList extends AppCompatActivity {
    String[] categoryArray = {"Harsh Jain","Salony Khare","Kushal Agarwal"};
    ListView listView;
    private FirebaseDatabase fd;
    private DatabaseReference db;
    //Button avail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.activity_employee_list,R.id.label, categoryArray);

       // final ListView listView;
        fd = FirebaseDatabase.getInstance();
        listView = findViewById(R.id.category_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text =(String) listView.getItemAtPosition(i);
                Toast.makeText(
                        EmployeeList.this,
                        "Request alloted to " + text ,
                        Toast.LENGTH_SHORT
                ).show();
                DatabaseReference myref = fd.getReference("New");
                myref.setValue(1);
                Intent intent = new Intent(EmployeeList.this, Middle.class);
                startActivity(intent);
                finish();

            }
        });
    }

}

