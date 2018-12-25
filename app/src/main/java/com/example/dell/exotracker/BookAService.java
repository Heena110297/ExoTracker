package com.example.dell.exotracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Dell on 05-03-2018.
 */

public class BookAService extends AppCompatActivity {

    private static final String TAG ="mytag" ;
    Button btnSave;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private FirebaseDatabase fd;
    private DatabaseReference db;
    private String reqid;
    private DatePickerDialog datePickerDialog;
    private EditText date;
    private EditText probdes;
    private EditText inputAddress;
    private Spinner spinner;
    private Spinner spinner2;
    private FirebaseAuth auth;
    private List<Address> addresses;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Geocoder geocoder;
    private Button btnShowAddress;

    private String id;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookservice);
        mGeoDataClient = Places.getGeoDataClient(this,null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this,null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        inputAddress = findViewById(R.id.editText);
        date = (EditText)findViewById(R.id.date);
        probdes=(EditText)findViewById(R.id.editText2);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        btnShowAddress = (Button) findViewById(R.id.inputAddress);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //date = calendar.get(Calendar.DATE);
auth = FirebaseAuth.getInstance();
        btnShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        btnSave = (Button)findViewById(R.id.button3);
        spinner = (Spinner) findViewById(R.id.ServicesSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String service = parent.getItemAtPosition(i).toString();
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("message");

                //myRef.setValue(service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Services_Array, android.R.layout.simple_spinner_item);
/* Specify the layout to use when the list of choices appears */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner2 = (Spinner) findViewById(R.id.TimeSpinner);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String time = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.Time_Array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        fd = FirebaseDatabase.getInstance();
        db = fd.getReference("requests");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String pd=probdes.getText().toString();
                String dt=date.getText().toString();
                String serv = spinner.getSelectedItem().toString();
                String time = spinner2.getSelectedItem().toString();
                String address = inputAddress.getText().toString();
                DatabaseReference myref = fd.getReference("Status");
                myref.setValue(1);
                createUser(pd,serv,dt,time,address);
                Intent myIntent = new Intent(view.getContext(), confirmedActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    private void createUser(String pd,String serv,String dt,String time,String address)
    {
        id = auth.getCurrentUser().getUid();
        if(isEmpty(id))
        {
            id=db.push().getKey();
        }

        Requests requests =new Requests(pd,serv,dt,time,address);
        db.child(id).setValue(requests);
    }

    public void setDate(View view) {
        datePickerDialog = new DatePickerDialog(BookAService.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            getLocationPermission();
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng coord = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());

                            try {
                                addresses=geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            // Write a message to the database

                            Toast.makeText(BookAService.this, address ,
                                    Toast.LENGTH_SHORT).show();
                            inputAddress.setText(address);
                        } else {

                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());

                        }

                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}



