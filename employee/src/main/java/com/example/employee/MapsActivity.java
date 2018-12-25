package com.example.employee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.pm.PackageManager.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAGPBIHyF1ORMM5ZKQdLqSABuVfI0VtL7I";
     Double mylat = 30.415828;
    Double mylang = 77.966321;
    static final int re = 1;
    LocationManager manager;
    Double mylat2 = null;
    Double mylang2 = null;
    LatLng dest,dun;
    ArrayList<LatLng> listPoints;
    float results[]=new float[10];
    EditText e1;
    GPS gps;
    String tim ="30";
    FirebaseDatabase fd;
    DatabaseReference mDatabaseLocationDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fd = FirebaseDatabase.getInstance();
        mDatabaseLocationDetails = fd.getReference("location");
        e1 = findViewById(R.id.editText);

        final String s = getIntent().getStringExtra("Address");
        e1.setText(s);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
        gps = new GPS(MapsActivity.this,tim);
        startService(new Intent(MapsActivity.this,GPS.class));

        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            storeInDatabase(latitude,longitude);
            //mText.setText(latitude+" ::: "+longitude);
            Toast.makeText(MapsActivity.this, latitude+" ::: "+ longitude, Toast.LENGTH_SHORT).show();
        }else{
            gps.showSettingsAlert();
        }


        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }

    private boolean runtime_permission() {
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(MapsActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(MapsActivity.this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},123);
            return true;
        }
        return false;
    }

    private void enable_button() {

        gps = new GPS(MapsActivity.this,tim);
                startService(new Intent(MapsActivity.this,GPS.class));

                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    storeInDatabase(latitude,longitude);
                    //mText.setText(latitude+" ::: "+longitude);
                    //Toast.makeText(MapsActivity.this, latitude+" ::: "+ longitude, Toast.LENGTH_SHORT).show();
                }else{
                    gps.showSettingsAlert();
                }
            }


    private void storeInDatabase(double latitude, double longitude) {

        //DatabaseReference mDatabaseLocationDetails = null;
        mDatabaseLocationDetails.child("longitude").setValue(longitude);
        mDatabaseLocationDetails.child("latitude").setValue(latitude);
    }


    ////////////////////////////////////////////////

    public void findroute(View v) throws IOException{
        mMap.clear();
       // EditText e1=(EditText)findViewById(R.id.editText);
        String des=e1.getText().toString();
        List<Address> list = null;
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        list = geocoder.getFromLocationName(des,1);
        Address add = list.get(0);
        String local=add.getLocality();
        mylat2=add.getLatitude();
        mylang2=add.getLongitude();
        dest = new LatLng(mylat2, mylang2);
        Location.distanceBetween(mylat,mylang,mylat2,mylang2,results);
        mMap.addMarker(new MarkerOptions().position(dest).title(local).snippet("Distance="+results[0]));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        String url = getRequestUrl(dest,dun);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);

    }

    /////////////////////////////////////////////////////////////////////

    void getLocation() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, re);
            } else {
                Location loc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    mylang = loc.getLongitude();
                    mylat = loc.getLatitude();
                }
        }
    }

    ////////////////////////////////////////////////////////////




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        dun = new LatLng(mylat, mylang);
        mMap.addMarker(new MarkerOptions().position(dun).title("Dehradun"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dun));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},re);
            }

        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
       String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
      // String url = DIRECTION_URL_API + "origin=" + str_org + "&destination=" + str_dest + "&key=" + GOOGLE_API_KEY;
        return url;
    }

////////////////////////////////////////////////////////////////////////////////////////
    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }



//////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case re:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED){
                        mMap.setMyLocationEnabled(true);
                    }

                } else
                {
                    Toast.makeText(getApplicationContext(),"need permission",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////
    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
