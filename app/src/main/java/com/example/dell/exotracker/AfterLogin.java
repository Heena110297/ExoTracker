package com.example.dell.exotracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Dell on 20-03-2018.
 */

public class AfterLogin extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);
        Log.d("msg","abhi yaha hu  mai");
       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            Log.d("msg",name);
            Log.d("My Tag","yahi hu mai");
        } else {
            Log.d("My Tag","nahi hu mai");
            // No user is signed in
        }
*/
        final String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
       mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight

                        menuItem.setChecked(true);
                        int itemid = menuItem.getItemId();
                        if(itemid==R.id.nav_service)
                        {
                            Intent intent = new Intent(AfterLogin.this, BookAService.class);
                            startActivity(intent);
                        }
                        else if(itemid==R.id.nav_viewOrder)
                        {
                            Intent intent = new Intent(AfterLogin.this, ViewOrders.class);
                            intent.putExtra("EXTRA_SESSION_ID", s);
                            startActivity(intent);
                        }
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
