package com.example.ta_ver11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.CoordinateContainer;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.utils.DistanceFormatter;
import com.mapbox.turf.TurfMeasurement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.text.DateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.geojson.Point.fromLngLat;

public class map extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private static final String DESTINATION_SYMBOL_LAYER_ID = "destination-symbol-layer-id";
    private static final String DESTINATION_ICON_ID = "destination-icon-id";
    private static final String DESTINATION_SOURCE_ID = "destination-source-id";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Point origin, destination;
    private Button BtnStart;
    private NavigationMapRoute navigationMapRoute;
    private DirectionsRoute currentRoute;
    private Marker destinationMarker;
    private static final String TAG = "opsiIsoman";
    private int status = 0;
    private String coor = "-6.1217, 106.7324";
    private double lat = -6.119267764452542 ;
    private double lon = 106.72941738153077;
    private static final Point TOWER_BRIDGE = Point.fromLngLat(106.82680731347287,-6.173546319206281);
    private static final Point LONDON_EYE = Point.fromLngLat(106.72941738153077,-6.119267764452542);
    public double jarak;
    private double jarak2;
    private double distance;

    private CoordinateContainer from;
    private CoordinateContainer to;

    private Point tujuan;
    private Point awal;

    private String namadb;
    private String terdekat;

    private String[] ruteID;
    private Integer index=0;
    private Point[] waypoint;
    private Double[] latRute;
    private Double[] lonRute;
    private Point waypoint1=Point.fromLngLat(106.8271849841787,-6.175307062883577);
    //private Point waypoint  =Point.fromLngLat(106.8271849841787,-6.175307062883577);

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private CollectionReference notebookRef = db.collection("Notebook");
   // private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("node");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mapbox
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //coor = extras.getString("coordinat");
            lat = Double.parseDouble(extras.getString("lat"));
            lon = Double.parseDouble(extras.getString("lon"));
            terdekat = extras.getString("terdekat");

        }


        tujuan = Point.fromLngLat(lon,lat);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        BtnStart = findViewById(R.id.btnStart);

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status != 1) {
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(true)
                            .build();
                    NavigationLauncher.startNavigation(map.this, options);
                }else if(status == 1){
                    status = 0;
                    getNavigation(origin,destination);

                }
            }
        });

    }

    //permission
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            );

            locationComponent.setLocationComponentEnabled(true);

            //locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
            this.origin = fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude());
        }else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //new override
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Grant Location Permission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) { enableLocationComponent(style); }
            });
        }else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }
//end permision

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {



        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded(){
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                enableLocationComponent(style);
                //TextView coordinattxt = findViewById(R.id.jaraktxt);

//                ////////////////////baca firebase////////////////////////////
//                FirebaseDatabase.getInstance().getReference("rute").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        String data = "";
//                        Integer jmlrute = (int) dataSnapshot.getChildrenCount();
//                        ruteID = new String[jmlrute];
//                        Log.d("LogjmlRute", String.valueOf(jmlrute));
//                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
//
//                            ruteID[index] = dataSnapshot.child(String.valueOf(index)).getValue().toString();//B199
//                            //Log.d("logDB", ruteID[index]);
//                            index= index+1;
//                            //String c = dataSnapshot.child("node").child(ruteID[index]).child("lat").getValue().toString();
//                        }
//
//                        Log.d("logini","masuk");
//                    }
//                });
//
//
////                FirebaseDatabase.getInstance().getReference("node").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
////                    @Override
////                    public void onSuccess(DataSnapshot dataSnapshot) {
////                        //Log.d("logDB", ruteID[index]);
////                        //waypoint = new Point[10];
////                        //latRute = new Double[10];
////                        //lonRute = new Double[10];
////                        for (int j=0;j<10;j++){
////                            String c = dataSnapshot.child(ruteID[j]).child("lat").getValue().toString();
////                            String d = dataSnapshot.child(ruteID[j]).child("lon").getValue().toString();
////
////                            //latRute[j] = Double.valueOf(c);
////                            //lonRute[j] = Double.valueOf(d);
////                            Double x = Double.valueOf(c);
////                            Double y = Double.valueOf(d);
////
////                            waypoint1 = Point.fromLngLat(y,x);
////                            //Log.d("LogDB", String.valueOf(waypoint[j]));
////
////                            //private Point monas =Point.fromLngLat(106.8271849841787,-6.175307062883577);
////                        }
////                        //Log.d("ArrayLogDB", String.valueOf(waypoint1));
////                    }
////                });
//
//                ///////////////////////end baca firebase/////////////////////

                origin = fromLngLat(origin.longitude(),origin.latitude());
                destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                destination = fromLngLat(lon, lat);
                BtnStart.setEnabled(true);
                //Log.d("ArrayLogDB", String.valueOf(waypoint1));
                //Log.d("logDB", ruteID[1]);
                //Log.d("logDB", ruteID[1]);
                getRoute(origin,destination);

            }
        });

    }

    //on start
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //navigation
    private void getNavigation(Point originL, Point destination) {

        NavigationRoute.builder(getApplicationContext())
                .accessToken(getString(R.string.mapbox_access_token))
                .origin(originL)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        //TextView coordinattxt = findViewById(R.id.jaraktxt);
                        Timber.d("Response code: " + response.code());
                        if (response.body() == null) {
                            Toast.makeText(map.this, "No routes found, make sure you set the right user and access token.", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Toast.makeText(map.this, "No routes found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentRoute = response.body().routes().get(0);
                        //coordinattxt.setText(Html.fromHtml("<font color='#6200EE'><b>Jarak :</b><br></font>" + String.format("%.2f",jarak)+ "km"));
                        //jarak = currentRoute.distance();

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(true)
                                .build();
                        NavigationLauncher.startNavigation(map.this, options);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Toast.makeText(map.this, "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //routing
    private void getRoute(Point origin, Point destination) {
        //Log.d("waypoint", String.valueOf(waypoint[0]));
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                //.addWaypoint(Point.fromLngLat(106.8271849841787,-6.175307062883577)) //private Point monas =Point.fromLngLat(106.8271849841787,-6.175307062883577);
//                .addWaypoint(waypoint1)//1
                .addWaypoint(Point.fromLngLat( 106.804166,-6.156796))//2
                .addWaypoint(Point.fromLngLat( 106.807969,-6.156489 ))//3
                .addWaypoint(Point.fromLngLat( 106.807772,-6.152952))//4
                .addWaypoint(Point.fromLngLat(106.811788,-6.152182 ))//5
                .addWaypoint(Point.fromLngLat(  106.811842, -6.151462))//6
                .addWaypoint(Point.fromLngLat( 106.817043, -6.151411))//7
                .addWaypoint(Point.fromLngLat(106.817154,-6.149971 ))//8 209
//                .addWaypoint(Point.fromLngLat(   106.816415,-6.147441)) //b214
//                .addWaypoint(Point.fromLngLat(106.81837,-6.146877))  //b213
                .addWaypoint(Point.fromLngLat(106.818675,-6.149644))//9 211
                .addWaypoint(Point.fromLngLat(106.820422,-6.14949))//10
                .addWaypoint(Point.fromLngLat( 106.824278,-6.148876))//11
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//12
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//13
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//14
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//15
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//16
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//17
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//18
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//19
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//20
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//21
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//22
//                .addWaypoint(Point.fromLngLat(106.816415, -6.147441))//23
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        TextView coordinattxt = findViewById(R.id.jarakactualtxt);

                        // You can get the generic HTTP info about the response
                        Timber.d("Response code: " + response.code());
                        if (response.body() == null) {
                            Timber.e("No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Timber.e("No routes found");
                            return;
                        }

                        // Get the directions route
                        currentRoute = response.body().routes().get(0);
                        if(navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView,mapboxMap );
                        }
                        navigationMapRoute.addRoute(currentRoute);
                        jarak2 = currentRoute.distance()/1000;
                        coordinattxt.setText(Html.fromHtml("<font color='#6200EE'><b>Jarak actual :</b><br></font>" + String.format("%.2f",jarak2)+ "km"));
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Timber.e("Error: " + throwable.getMessage());
                        Toast.makeText(map.this, "Error: " + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}