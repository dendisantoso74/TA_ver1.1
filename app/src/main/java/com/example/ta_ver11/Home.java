package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationManager;
    private double lonGPS;
    private double latGPS;
    private Point origin;
    private Point tujuan;

    private Double lat;
    private Double lon;
    private Double jarak; //jarak dari gps ke semua node

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("isoman");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //define button
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        //set click listener button
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        onLocationChanged(location);

        Log.d("lokasiUsergps", String.valueOf(lonGPS) +", "+ String.valueOf(latGPS));

        origin = Point.fromLngLat(lonGPS, latGPS);

        Log.d("lokasiUserHome", origin +" origin ");

        //itung jarak

        //end itung jarak

    }

    private void onLocationChanged(Location location) {
        lonGPS=location.getLongitude();
        latGPS=location.getLatitude();
    }

//    public void loadNotes(View v){
//        reff.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                String data3 = ""; // tambahan
//                String data = "";
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
//                    FirebaseFasilitas node = keyNode.getValue(FirebaseFasilitas.class);
//                    node.setDocumentId(keyNode.getKey());
//                    String documentId= node.getDocumentId();
//
//                    data += "Node: " + documentId;
//                    data += "\nLatitude: " + node.getLat();
//                    data += "\nLongitude: " + node.getLon();
//                    data += "\nJarak: " + node.getJarak();
//
//                    data += "\n\n";
//
//                    //store latitude&longitude dari next state di firebase
//                    lat = Double.valueOf(node.getLat());
//                    lon = Double.valueOf(node.getLon());
//
//                    //deklarasi origin dan tujuan
//                    //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
//                    tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145
//
//                    //menghitung jarak dari gps user ke semua node
//                    jarak = TurfMeasurement.distance(origin,tujuan);
//                    reff.child(documentId).child("jarak").setValue(jarak); //store jarak ke firebase
//
//                    Log.d("logJarakHome", String.valueOf(jarak)+ " ID " +documentId);
//
//
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.button1:

                //hitung jarak
                FirebaseDatabase.getInstance().getReference("PCR").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String data3 = ""; // tambahan
                        String data = "";
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            FirebaseFasilitas node = keyNode.getValue(FirebaseFasilitas.class);
                            node.setDocumentId(keyNode.getKey());
                            String documentId= node.getDocumentId();

                            data += "Node: " + documentId;
                            data += "\nLatitude: " + node.getLat();
                            data += "\nLongitude: " + node.getLon();
                            data += "\nJarak: " + node.getJarak();

                            data += "\n\n";

                            //store latitude&longitude dari next state di firebase
                            lat = Double.valueOf(node.getLat());
                            lon = Double.valueOf(node.getLon());

                            //deklarasi origin dan tujuan
                            //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
                            tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                            //menghitung jarak dari gps user ke semua node
                            jarak = TurfMeasurement.distance(origin,tujuan);
                            FirebaseDatabase.getInstance().getReference("PCR").child(String.valueOf(documentId)).child("jarak").setValue(jarak); //store jarak ke firebase

                            //Log.d("logJarakHome", String.valueOf(jarak)+ " ID " +documentId);


                        }
                    }
                });
                //end hitung jarak

                //hitung jarak
                FirebaseDatabase.getInstance().getReference("tcm").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String data3 = ""; // tambahan
                        String data = "";
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            FirebaseFasilitas node = keyNode.getValue(FirebaseFasilitas.class);
                            node.setDocumentId(keyNode.getKey());
                            String documentId= node.getDocumentId();

                            data += "Node: " + documentId;
                            data += "\nLatitude: " + node.getLat();
                            data += "\nLongitude: " + node.getLon();
                            data += "\nJarak: " + node.getJarak();

                            data += "\n\n";

                            //store latitude&longitude dari next state di firebase
                            lat = Double.valueOf(node.getLat());
                            lon = Double.valueOf(node.getLon());

                            //deklarasi origin dan tujuan
                            //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
                            tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                            //menghitung jarak dari gps user ke semua node
                            jarak = TurfMeasurement.distance(origin,tujuan);
                            FirebaseDatabase.getInstance().getReference("tcm").child(String.valueOf(documentId)).child("jarak").setValue(jarak); //store jarak ke firebase

                            //Log.d("logJarakHome", String.valueOf(jarak)+ " ID " +documentId);


                        }
                    }
                });
                //end hitung jarak

                i = new Intent(this,OpsiTes.class);
                startActivity(i);
                break;
            case R.id.button2:

                //hitung jarak
                FirebaseDatabase.getInstance().getReference("isoman").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String data3 = ""; // tambahan
                        String data = "";
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            FirebaseFasilitas node = keyNode.getValue(FirebaseFasilitas.class);
                            node.setDocumentId(keyNode.getKey());
                            String documentId= node.getDocumentId();

                            data += "Node: " + documentId;
                            data += "\nLatitude: " + node.getLat();
                            data += "\nLongitude: " + node.getLon();
                            data += "\nJarak: " + node.getJarak();

                            data += "\n\n";

                            //store latitude&longitude dari next state di firebase
                            lat = Double.valueOf(node.getLat());
                            lon = Double.valueOf(node.getLon());

                            //deklarasi origin dan tujuan
                            //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
                            tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                            //menghitung jarak dari gps user ke semua node
                            jarak = TurfMeasurement.distance(origin,tujuan);
                            FirebaseDatabase.getInstance().getReference("isoman").child(String.valueOf(documentId)).child("jarak").setValue(jarak); //store jarak ke firebase

                            //Log.d("logJarakHome", String.valueOf(jarak)+ " ID " +documentId);


                        }
                    }
                });
                //end hitung jarak


                i = new Intent(this,Isoman.class);
                startActivity(i);
                break;
            case R.id.button3:

                //hitung jarak
                FirebaseDatabase.getInstance().getReference("rumah_sakit").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String data3 = ""; // tambahan
                        String data = "";
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            FirebaseFasilitas node = keyNode.getValue(FirebaseFasilitas.class);
                            node.setDocumentId(keyNode.getKey());
                            String documentId= node.getDocumentId();

                            data += "Node: " + documentId;
                            data += "\nLatitude: " + node.getLat();
                            data += "\nLongitude: " + node.getLon();
                            data += "\nJarak: " + node.getJarak();

                            data += "\n\n";

                            //store latitude&longitude dari next state di firebase
                            lat = Double.valueOf(node.getLat());
                            lon = Double.valueOf(node.getLon());

                            //deklarasi origin dan tujuan
                            //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
                            tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                            //menghitung jarak dari gps user ke semua node
                            jarak = TurfMeasurement.distance(origin,tujuan);
                            FirebaseDatabase.getInstance().getReference("rumah_sakit").child(String.valueOf(documentId)).child("jarak").setValue(jarak); //store jarak ke firebase

                            //Log.d("logJarakHome", String.valueOf(jarak)+ " ID " +documentId);


                        }
                    }
                });
                //end hitung jarak

                i = new Intent(this,Rsrujukan.class);
                startActivity(i);
                break;

        }

    }
}