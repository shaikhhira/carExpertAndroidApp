package com.example.carexpertsystem.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.carexpertsystem.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MechanicMap extends AppCompatActivity {

    private Spinner sp_type;
    private Button find_btn;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_map);

        sp_type = findViewById(R.id.sp_type);
        find_btn = findViewById(R.id.find_btn);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        final String[] placeTypeList = {"car_dealer", "car_rental", "bank", "car_repair"};
        String[] placeNameList = {"Car Dealer", "Car Rental", "Bank", "Mechanic"};

        sp_type.setAdapter(new ArrayAdapter<>(MechanicMap.this,
                android.R.layout.simple_spinner_dropdown_item, placeNameList));


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(MechanicMap.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentlocation();
        } else {
            ActivityCompat.requestPermissions(MechanicMap.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = sp_type.getSelectedItemPosition();
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong +
                        "&radius=5000" +
                        "&type=" + placeTypeList[i] +
                        "&sensor=true" + "&key=" + getResources().getString(R.string.google_map_key);

                new PlaceTask().execute(url);
            }
        });
    }

    private void getCurrentlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        currentLat=location.getLatitude();
                        currentLong=location.getLongitude();

                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map=googleMap;

                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(currentLat,currentLong),10
                                ));
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getCurrentlocation();
        }
    }


    private class PlaceTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String data=null;
            try {
                //initialize data
                 data=downloadUrl(strings[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //execute parser task
            new Parsertask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //Initialize url
        URL url=new URL(string);
        //initialize connection
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        //connect connection
        connection.connect();
        //initialize input stream
        InputStream stream=connection.getInputStream();
        //initailize buffer reader
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        //initailize string builder
        StringBuilder builder=new StringBuilder();
        //initialize string variable
        String line="";
        //use while loop
        while ((line=reader.readLine())!=null){
            //append line
            builder.append(line);
        }
        //get append data
        String data=builder.toString();
        //close reader
        reader.close();
        //return data
        return data;
    }

    private class Parsertask extends AsyncTask<String,Integer, List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create json parser class
            JsonParser jsonParser=new JsonParser();
            //initialize hash map list
            List<HashMap<String,String>> mapList=null;
            JSONObject object;
            try {
                //initialize json object
                 object=new JSONObject(strings[0]);
                 //parse json object
                mapList=jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return map list
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //clear map
            map.clear();
            //use for loop
            for(int i=0;i<hashMaps.size();i++){
                //initialize hash map
                HashMap<String,String> hashMapList=hashMaps.get(i);
                //get latitude
                double lat= Double.parseDouble(hashMapList.get("lat"));
                //get longitude
                double lng= Double.parseDouble(hashMapList.get("lng"));
                //get name
                String name=hashMapList.get("name");
                //concat latitude and longitude
                LatLng latLng=new LatLng(lat,lng);
                //initialize marker options
                MarkerOptions options=new MarkerOptions();
                //set options
                options.position(latLng);
                //set title
                options.title(name);
                //add marker on map
                map.addMarker(options);
            }
        }
    }
}