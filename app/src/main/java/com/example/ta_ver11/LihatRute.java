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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LihatRute extends AppCompatActivity {

    private TextView textViewTujuan;
    private TextView textViewJarak;

    private String terdekat;
    private String nama;
    private String coor = "-6.1217, 106.7324";
    private double latTujuan = -6.119267764452542 ;
    private double lonTujuan = 106.72941738153077;

    //var dari ujicoba
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private EditText editTextTags;
    private TextView textViewData;
    private TextView textViewNodeTerbaik;
    private TextView textViewIdNodeTerbaik;
    private TextView textViewNstate;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private CollectionReference notebookRef = db.collection("Notebook");
    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("node");

    private Point Goal = Point.fromLngLat(106.767897, -6.158282); // B40 106.73563,-6.137062 Rs 106.7849847,-6.166142055 B168 106.767897,-6.158282
    private Point coorNstate;
    private Point tujuan;
    private Point origin; //Point.fromLngLat(106.74460242519883, -6.1387230632241065);


    private Double lat;
    private Double lon;
    private Double jarak; //jarak dari gps ke semua node
    private Double[] jarakState; //jarak daro goal ke next state
    private Double jarakgoal;
    private Double terbaikjarak = 100.0;
    private Double jarakTerkecil = 100.0;
    private Double[] NormJarak;
    private Double[] NormMacet;
    private Double NMacet;
    private Double NJarak;
    private Double[] NilaiSAW;
    private Double Nsaw;
    private Double sawTerpilih = 10.0;

    private String InitialstateID;
    private String[] Nextstate;
    private String jamfirebase = "";
    private String hari;
    private String sawTerpilihID;
    private String[] ruteID;

    private Integer[] kemacetan;
    private Integer macet;
    private Integer indexDO = 0;
    private Double macetTerkecil = 5.0;

    private String testing;

    private boolean status = false;

    private LocationManager locationManager;
    double lonGPS;
    double latGPS;

    private Double[] actualCoast;
    private Double aCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_rute);

        textViewTujuan = findViewById(R.id.textView5);
        textViewJarak = findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            coor = extras.getString("coordinat");
            latTujuan = Double.parseDouble(extras.getString("lat"));
            lonTujuan = Double.parseDouble(extras.getString("lon"));
            terdekat = extras.getString("terdekat");
            nama = extras.getString("nama");

            textViewTujuan.setText(nama);
            Log.d("tesTerdekat", String.valueOf(terdekat));
            Log.d("LihatRute", nama);
            Log.d("LihatRute", String.valueOf(lonTujuan)+latTujuan);


            //ambil gps
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
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

            Log.d("lokasiUser", origin +" origin ");

            //Pengambilan waktu dari android user
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH"); //format 24 jam
            String jam = simpleDateFormat2.format(calendar.getTime());
            hari = simpleDateFormat.format(calendar.getTime());

            //penyesuaian hari dan jam antara firebase dan android
            if (jam.equals("06")){
                jamfirebase = "0";
            }else if (jam.equals("07")){
                jamfirebase = "1";
            }else if (jam.equals("08")){
                jamfirebase = "2";
            }else if (jam.equals("09")){
                jamfirebase = "3";
            }else if (jam.equals("10")){
                jamfirebase = "4";
            }else if (jam.equals("11")){
                jamfirebase = "5";
            }else if (jam.equals("12")){
                jamfirebase = "6";
            }else if (jam.equals("13")){
                jamfirebase = "7";
            }else if (jam.equals("14")){
                jamfirebase = "8";
            }else if (jam.equals("15")){
                jamfirebase = "9";
            }else if (jam.equals("16")){
                jamfirebase = "10";
            }else if (jam.equals("17")){
                jamfirebase = "11";
            }else if (jam.equals("18")){
                jamfirebase = "12";
            }else if (jam.equals("19")){
                jamfirebase = "13";
            }else if (jam.equals("20")){
                jamfirebase = "14";
            }else if (jam.equals("02")){
                jamfirebase = "15";
            }

        }

    }

    public void loadNotes(View v){

        reff.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String data3 = ""; // tambahan
                db.getReference("rute").setValue(null);
                String data = "";

                String c = dataSnapshot.child(terdekat).child("lat").getValue().toString();
                String d = dataSnapshot.child(terdekat).child("lon").getValue().toString();

                Double latTerdekat = Double.valueOf(c);
                Double lonTerdekat = Double.valueOf(d);

                Goal = Point.fromLngLat(lonTerdekat,latTerdekat);
                Log.d("cekGOAL", String.valueOf(Goal));

                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    Node node = keyNode.getValue(Node.class);
                    node.setDocumentId(keyNode.getKey());
                    String documentId= node.getDocumentId();


                    data += "Node: " + documentId;
                    data += "\nLatitude: " + node.getLat();
                    data += "\nLongitude: " + node.getLon();
                    data += "\nJarak: " + node.getJarak();

                    for (String state : node.getState()) {
                        data += "\n-" + state;
                    }
                    for (Double jarakAsli : node.getJarakAsli()) {
                        data += "\n-" + jarakAsli;
                       // Log.d("jarakAsli", String.valueOf(jarakAsli));
                    }
                    data += "\n\n";


                    //store latitude&longitude dari next state di firebase
                    lat = Double.valueOf(node.getLat());
                    lon = Double.valueOf(node.getLon());

                    //deklarasi origin dan tujuan
                    //106.74460242519883,-6.1387230632241065 /monas/106.82714206866876,-6.1753870622395635
                    tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                    //menghitung jarak dari gps user ke semua node
                    jarak = TurfMeasurement.distance(origin,tujuan); //luminor 106.8231926, -6.148533433
                    reff.child(documentId).child("jarak").setValue(jarak); //store jarak ke firebase

                    Log.d("logJarak", String.valueOf(jarak)+ " ID " +documentId);
                    //mennetukan jarak terbaik untuk titik awal
                    if (Double.compare(terbaikjarak, node.getJarak()) == 0){
                        //System.out.println("d1=d2"+ " iterasike-" );

                    } else if(Double.compare(terbaikjarak, node.getJarak()) < 0) {
                        //System.out.println("d1<d2" + " iterasike-" );

                    }
                    else {
                        terbaikjarak = node.getJarak();
                        InitialstateID = documentId;

                        //System.out.println("d1>d2"+ " iterasike-"+": " +InitialstateID);
                    }

                }
                Log.d("Keluaran initial state",InitialstateID);
                //InitialstateID = "B170";

                ruteID = new  String[23];// pajang 23- isi 0-22
                ruteID[0] =InitialstateID;
                db.getReference("rute").child(String.valueOf(0)).setValue(ruteID[0]); //store firebase index 0

                do{
                    Log.d("cek indeks sblm rute", String.valueOf(indexDO));
                    Log.d("SAWterpilih", String.valueOf(sawTerpilih));
                    if (indexDO>0){
                        InitialstateID = sawTerpilihID;
                        macetTerkecil = 5.0;
                        jarakTerkecil = 500.0;
                        sawTerpilih = 10.0;
                        // algo dendi sawTerpilih=10 mati karena perbandingan curent state harus lebih kecil dari next state
                        //algo raka sawTerpilih=10 aktif karena perbandingan hanya antar next state

                    }

                    Integer jmlstate = (int) dataSnapshot.child(InitialstateID).child("state").getChildrenCount();
                    Log.d("Keluaran jumlah state", String.valueOf(jmlstate));

                    Nextstate = new String[jmlstate];
                    actualCoast = new Double[jmlstate];
                    String nestate;
                    //store nexstate ke array Nstate
                    for (int i = 0; i < jmlstate; i++) {
                        nestate = dataSnapshot.child(InitialstateID).child("state").child(String.valueOf(i)).getValue().toString();
                        aCost = (Double) dataSnapshot.child(InitialstateID).child("jarakAsli").child(String.valueOf(i)).getValue();
                        Nextstate[i] = nestate;
                        actualCoast[i] = aCost;
                        Log.d("Nextstate" + i, String.valueOf(Nextstate[i]));
                        Log.d("ActualCoast" + i, String.valueOf(actualCoast[i]));
                    }// end for nexstate

                    //mengambil data kemacetan dari firebase, simpan di array kemacetan
                    kemacetan = new Integer[jmlstate];
                    for (int j = 0; j < jmlstate; j++) {

                        macet = Integer.valueOf(dataSnapshot.child(Nextstate[j]).child(hari).child(jamfirebase).getValue().toString()); //kemacetan sudah integer
                        kemacetan[j] = macet;

                        Log.d("Kemacetan" + j,Nextstate[j] +" macet : "+ String.valueOf(kemacetan[j]));
                    }//end for kemacetan

                    //menghitung jarak dari state ke goal, ambil data lat-lon dari firebase
                    //dataSnapshot.child(terdekat).child("lat").getValue().toString();
                    //dataSnapshot.child(terdekat).child("lon").getValue().toString();

                    jarakState = new Double[jmlstate];
                    for (int k = 0; k < jmlstate; k++) {
                        //store lat&lon Nstate dari firebase
                        String a = dataSnapshot.child(Nextstate[k]).child("lat").getValue().toString();
                        String b = dataSnapshot.child(Nextstate[k]).child("lon").getValue().toString();

                        //convert lat&lon Nsate string to Double
                        Double latNstate = Double.valueOf(a);
                        Double lonNstate = Double.valueOf(b);
                        coorNstate = Point.fromLngLat(lonNstate, latNstate);

                        //hitung jarak
                        jarakgoal = TurfMeasurement.distance(Goal, coorNstate);  // jarak Goal ke node(Nstate)
                        jarakState[k]=jarakgoal;
                        Log.d("jarakkegoal",Nextstate[k]+" jarak : "+jarakState[k]);
                        if (jarakState[k]==0.0){

                            status = true;
                            Log.d("tesif",Nextstate[k]+" jarak : "+jarakState[k]);
                            Log.d("tesif", String.valueOf(status));
                        }
                    }//end for jarak

                    // mengubah nilai dari rute sebelumnya agar tidak mengulang

                    if (indexDO>0){

                        for (int z = 0; z < jmlstate; z++) {
                            for (int y =0;y<indexDO;y++) {
                                if (Nextstate[z].equals(ruteID[y])) {

                                    jarakState[z] += 10.0;

                                } else {
                                    //jarakState[z]+= 0.0;

                                }
                            }
                        }
                    }//end if

                    /**SAW******************************************/
                    //hitung kemacetan terkecil
//                    for (int l = 0; l < jmlstate; l++) {
//                        if (Double.compare(macetTerkecil, kemacetan[l]) == 0) {
//
//                            System.out.println("d1=d2" + " iterasike-"+l+": " + macetTerkecil);
//
//                        } else if (Double.compare(macetTerkecil, kemacetan[l]) < 0) {
//
//                            System.out.println("d1<d2" + " iterasike-"+ l +": " + macetTerkecil);
//
//                        } else {
//                            macetTerkecil = Double.valueOf(kemacetan[l]);
//                            System.out.println("d1>d2" + " iterasike-"+l + ": " + macetTerkecil);
//                        }
//                    }

                    //Hitung jarak terkecil
//                    for (int m = 0; m < jmlstate; m++) {
//                        if (Double.compare(jarakTerkecil, jarakState[m]) == 0) {
//
//                            //System.out.println("d1=d2" + " iterasike-"+m+": " + jarakTerkecil);
//
//                        } else if (Double.compare(jarakTerkecil, jarakState[m]) < 0) {
//
//                            //System.out.println("d1<d2" + " iterasike-"+ m +": " + jarakTerkecil);
//
//                        } else {
//                            jarakTerkecil = jarakState[m];
//                            //System.out.println("d1>d2" + " iterasike-"+m + ": " + jarakTerkecil);
//                        }
//                    }
//                    Log.d("terkecil jarak", String.valueOf(jarakTerkecil));
//                    Log.d("terkecil macet", String.valueOf(macetTerkecil));

                    //normalisis dan pembobotan
                    NormMacet = new Double[jmlstate];
                    NormJarak = new Double[jmlstate];
                    NilaiSAW = new  Double[jmlstate];
                    //Log.d("cekSAW terbaik",sawTerpilihID);
                    for (int n = 0; n < jmlstate; n++){
//                        NJarak = jarakTerkecil / jarakState[n]; //masuk normalisasi
//                        NMacet = macetTerkecil / kemacetan[n];
                        NJarak = jarakState[n]; //tanpa normalisasi
                        NMacet = Double.valueOf(kemacetan[n]);



                        NormJarak[n] = NJarak;
                        NormMacet[n] = NMacet;

                        //Nsaw=((0.574 * NormJarak[n]) + (0.426 * NormMacet[n])); //masuk normalisasi
                        //Nsaw=((0.9 * NormJarak[n]) + (0.1 * NormMacet[n])); //tanpa normalisasi dendi
                        Nsaw=((0.9 * NormJarak[n]) + (0.1 * NormMacet[n])+actualCoast[n]); //tanpa normalisasi raka
                        NilaiSAW[n] = Nsaw;
                        Log.d("Normalisasi jarak"+n, String.valueOf(NormJarak[n]));
                        Log.d("Normalisasi macet"+n, String.valueOf(NormMacet[n]));
                        Log.d("BOBOT SAW"+n,Nextstate[n] +" - "+ String.valueOf(NilaiSAW[n]));


                    }//end for normalisasi dan pembobotan

                    /**end SAW*******************/

                    //perbandingan antar nilai SAW

                    for (int o = 0; o < jmlstate; o++){

                        if (Double.compare(sawTerpilih, NilaiSAW[o]) == 0) {

                            //System.out.println("d1=d2" + " iterasike-"+o+": " + sawTerpilih);

                        } else if (Double.compare(sawTerpilih, NilaiSAW[o]) < 0) {
//                            sawTerpilih = NilaiSAW[o]; //masuk normalisasi
//                            sawTerpilihID = Nextstate[o];

                            System.out.println("d1<d2" + " iterasike-"+ o +": " + sawTerpilih);

                        } else {
                            sawTerpilih = NilaiSAW[o];//tanpa normalisasi
                            sawTerpilihID = Nextstate[o];

                            System.out.println("d1>d2" + " iterasike-"+ o + ": " + sawTerpilih);
                        }

                    }//end for perbandingan SAW
                    data3 += "\nRute: " +indexDO+". "+ sawTerpilihID +" SAW:"+sawTerpilih;

                    ruteID[indexDO+1] =sawTerpilihID;
                    //rutePoint[indexDO+1] =
                    Log.d("cekruteID"+indexDO, ruteID[indexDO]);

                    db.getReference("rute").child(String.valueOf(indexDO+1)).setValue(ruteID[indexDO+1]); //store rute ke firebase
                    indexDO++;
                    Log.d("StatusTRUE", String.valueOf(status));
                }


                //while (indexDO < 9);
                while (status==false);

                Log.d("cekrute9"+indexDO, String.valueOf(ruteID.length));
//                for (int q=indexDO+1;q<23;q++){
//
//                    ruteID[q] = ruteID[indexDO];
//                    //iterasi 1 = ruteID[10] = ruteID[9]
//                    //iterasi 2 = ruteID[11] = ruteID[9]
//                    //iterasi 3 = ruteID[12] = ruteID[9]
//                    //iterasi 4 = ruteID[13] = ruteID[9]
//                    Log.d("logRuteIDArray",ruteID[q]);
//                    Log.d("logRuteIDArray", String.valueOf(q));
//                }
                //textViewData.setText(data3);
            }

        });
        //db.getReference("rute2").setValue("coorNstate");
        Log.d("tescoornstate", String.valueOf(coorNstate));
        //hitung actual cost
        Point tujuanAkhir=Point.fromLngLat(lonTujuan, latTujuan);
        Double ActualCost = TurfMeasurement.distance(origin,tujuanAkhir);
       // Double jarak2 = currentRoute.distance()/1000;
        //parsing data ke map
       Intent intent = new Intent(this,map.class);
       intent.putExtra("lon",String.valueOf(lonTujuan));
       intent.putExtra("lat",String.valueOf(latTujuan));
       intent.putExtra("terdekat",terdekat);
       //intent.putExtra("ruteID",ruteID);
       startActivity(intent);

       Log.d("actualCost", String.valueOf(ActualCost));


        //Toast.makeText(mContext,"Tujuan: " + mNama.getText(), Toast.LENGTH_SHORT).show();

    }

    private void onLocationChanged(Location location) {
        lonGPS=location.getLongitude();
        latGPS=location.getLatitude();
    }
}