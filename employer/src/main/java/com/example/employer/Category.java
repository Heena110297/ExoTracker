package com.example.employer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Category extends AppCompatActivity {
    String[] categoryArray = {"Carpentry Services","Plumbing Services","Electrician","Pest Control","Baby Sitting"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final String info = getIntent().getStringExtra("info");
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview,R.id.label, categoryArray);

        ListView listView;
        listView = findViewById(R.id.category_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(Category.this, EmployeeList.class);
                intent.putExtra("Info", info);
                startActivity(intent);
                finish();
            }
        });
    }

}
