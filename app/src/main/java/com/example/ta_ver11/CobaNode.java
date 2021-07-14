package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CobaNode extends AppCompatActivity {
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

    private Point tujuan;
    private Point origin;
    private Point coorIdNode;
    private Point coorNstate;
    private Point Goal;
    private Double lat;
    private Double lon;
    private Double jarak;
    private Double jarakState;
    private Integer kemacetan;

    private Double terbaikjarak=100.0;
    private String IdNode;
    private String[] Nstate;
    private String nestate;

    private Integer cekarray = 0;
    private Integer jmlstate = 0;

    private Double TerbaikSAW=0.0;
    private Double NilaiSAW=0.0;
    private String IdSAW;

    private Double jarakTerkecil = 100.0;
    private Integer macetTerkecil = 5;

    private Double macetterkecil;

    int statusGoal;
    String goalID;
    Point goalCoor;

    String TerbaikSAWID;
    Point TerbaikSAWCoor;

    String jamfirebase;

    //Integer i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_node);
//        editTextTitle = findViewById(R.id.edit_text_title);
//        editTextDescription = findViewById(R.id.edit_text_description);
//        editTextPriority = findViewById(R.id.edit_text_priority);
//        editTextTags = findViewById(R.id.edit_text_tags);
        textViewData = findViewById(R.id.text_view_data);
        textViewNodeTerbaik = findViewById(R.id.text_view_nodeterbaik);
        textViewIdNodeTerbaik = findViewById(R.id.text_view_idnodeternaik);
        textViewNstate = findViewById(R.id.text_view_nstate);
        //Latitude longitude goal
        Goal = Point.fromLngLat(106.7849847,-6.166142055);
//        updateArray();
    }
    //    public void addNote(View v) {
//        String title = editTextTitle.getText().toString();
//        String description = editTextDescription.getText().toString();
//        if (editTextPriority.length() == 0) {
//            editTextPriority.setText("0");
//        }
//        int priority = Integer.parseInt(editTextPriority.getText().toString());
//        String tagInput = editTextTags.getText().toString();
//        String[] tagArray = tagInput.split("\\s*,\\s*");
//
//        List<String> tags = Arrays.asList(tagArray);
//        int bobot;
//        int jarak;
//        String lat;
//        String lon;
//        Node node = new Node(bobot, jarak, lat, lon, tags);
//        notebookRef.add(node);
//    }
    public void loadNotes(View v) {
        reff.get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String data = "";
                        String data2 = ""; // tambahan

                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            Node node = keyNode.getValue(Node.class);
                            node.setDocumentId(keyNode.getKey());

                            String documentId = node.getDocumentId();

                            data += "Node: " + documentId;
                            data += "\nLatitude: " + node.getLat();
                            data += "\nLongitude: " + node.getLon();
                            data += "\nJarak: " + node.getJarak();

                            //data2 += "\n-" + node.getState();

                            for (String state : node.getState()) {
                                data += "\n-" + state;
                            }

                            data += "\n\n";

                            //store latitude&longitude dari node di firebase
                            lat = Double.valueOf(node.getLat());
                            lon = Double.valueOf(node.getLon());

                            //deklarasi origin dan tujuan
                            origin = Point.fromLngLat(106.82714206866876,-6.1753870622395635);
                            tujuan = Point.fromLngLat(lon, lat); //-6.128317253066011, 106.83302015898145

                            //menghitung jarak dari gps user ke semua node
                            jarak = TurfMeasurement.distance(origin,tujuan);
                            reff.child(documentId).child("jarak").setValue(jarak); //store jarak ke firebase

                            //mennetukan jarak terbaik untuk titik awal
                            if (Double.compare(terbaikjarak, node.getJarak()) < 0){
                            } else {
                                terbaikjarak = node.getJarak();
                                IdNode = documentId;

                                for (String state : node.getState()) {
                                    data2 += "\n-" + state;
                                    cekarray = cekarray+1;
                                }
                            }

                        }

                        //itung panjang array state
                        jmlstate = (int) dataSnapshot.child(IdNode).child("state").getChildrenCount();

                        Nstate = new String[jmlstate];
                        //store nexstate ke array Nstate
                        for (int i = 0; i < jmlstate; i++) {
                            nestate = dataSnapshot.child(IdNode).child("state").child(String.valueOf(i)).getValue().toString();
                            Nstate[i] = nestate;
                        }

                        /***sudah ada node awal, next state sudah masuk ke array Nstate***/

                        //Pengambilan waktu dari android user
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH"); //format 24 jam
                        String jam = simpleDateFormat2.format(calendar.getTime());
                        String hari = simpleDateFormat.format(calendar.getTime());
                        Log.d("testing jam", jam);
                        Log.d("testing hari", hari);

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
                        }else if (jam.equals("22")){
                            jamfirebase = "15";
                        }

                        //store lat&lon IdNode dari firebase
                        String a = dataSnapshot.child(IdNode).child("lat").getValue().toString();
                        String b = dataSnapshot.child(IdNode).child("lon").getValue().toString();
                        //convert lat&lon string to Double
                        Double latIdNode = Double.valueOf(a);
                        Double lonIdNode = Double.valueOf(b);
                        coorIdNode = Point.fromLngLat(lonIdNode,latIdNode); //variabel point
                        Log.d("Testing lat: ", String.valueOf(latIdNode));
                        Log.d("Testing lon: ", String.valueOf(lonIdNode));

                        //menampilkan kemacetan dari tiap next state
                        for (int i = 0; i < jmlstate; i++) {
                            //nestate = dataSnapshot.child(IdNode).child("state").child(String.valueOf(i)).getValue().toString();
                            kemacetan = Integer.valueOf(dataSnapshot.child(Nstate[i]).child(hari).child(jamfirebase).getValue().toString()); //kemacetan sudah integer

                            //store lat&lon Nstate dari firebase
                            String c = dataSnapshot.child(Nstate[i]).child("lat").getValue().toString();
                            String d = dataSnapshot.child(Nstate[i]).child("lon").getValue().toString();
                            //convert lat&lon Nsate string to Double
                            Double latNstate = Double.valueOf(c);
                            Double lonNstate = Double.valueOf(d);
                            coorNstate = Point.fromLngLat(lonNstate,latNstate);
                            //Log.d("Testing lat Nstate" + i, String.valueOf(latNstate));
                            //Log.d("Testing lon Nstate" + i, String.valueOf(lonNstate));

                            //itung jarak dari IdNode ke tiap Nstate (longitude,latitude)
                            //jarakState = TurfMeasurement.distance(coorIdNode,coorNstate); //jarak state
                            jarakState = TurfMeasurement.distance(Goal,coorNstate);  // jarak Goal ke node(Nstate)
                            Log.d("Tesjarak" + i, String.valueOf(jarakState));
                            //reff.child(Nstate[i]).child("jarakDariGoal").setValue(jarakState);//store jarak ke firebase

//                            if (jarakState==0.0){
//                                Log.d("Tujuan", "sampai");
//                            }

                            /********PERHITUNGAN SAW (jarak 70% + kemacetan 30%)********/

                            //cari nilai terkecil dari jarak dan kemacetan
                            if (Double.compare(jarakState, jarakTerkecil) < 0){
                                jarakTerkecil = jarakState;

                                //Log.d("testIF" + i, "terpenuhi");
                            } else {
                                //Log.d("testIF" + i, "tidak terpenuhi");
                            }

                            if (jarakTerkecil==0.0){
                                statusGoal=1;
                                goalID=Nstate[i];
                                goalCoor=coorNstate;
//                                Log.d("tesIDgoal status", String.valueOf(statusGoal));
//                                Log.d("tesIDgoal ",goalID);
//                                Log.d("tesIDgoal coor", String.valueOf(goalCoor));

                            }else {
                                statusGoal=0;
                                Log.d("tesIDgoal", String.valueOf(statusGoal));
                            }


                            if (Double.compare(kemacetan, macetTerkecil) < 0){
                                macetTerkecil = kemacetan;
                                macetterkecil = new Double(kemacetan);
                                //Log.d("testIF" + i, String.valueOf(kemacetan));
                                //Log.d("testIF Double" + i, String.valueOf(macetterkecil));
                            } else {
                                //Log.d("testIF" + i, "tidak terpenuhi");

                            }
                        } //end for jmlstate

                        /** if else apabila jarak 0**/
                        //perbandingan apabila jarak == 0.0 , maka tujuan sampai
                        if (statusGoal==1){ // if status goal 1 ambil goalID,goalCoor
                            Log.d("tesBAgi0", "Terpenuhi");
                            //jika jarak == 0 maka perhitungan saw berhenti dan id - coor masuk ke rute -> tampil rute
                            //ambil id
                            //ambil coor

                            TerbaikSAWID = goalID;
                            TerbaikSAWCoor =goalCoor;

                            Log.d("tesIDgoal status", String.valueOf(statusGoal));
                            Log.d("tesIDgoal ",TerbaikSAWID);
                            Log.d("tesIDgoal coor", String.valueOf(TerbaikSAWCoor));

                        }
                        else {
                            Log.d("tesBAgi0", "tidak Terpenuhi");
                            //jika jarak tidak sama dengan 0 maka perhitungan saw lanjut ke normalisasi
                            /**Lanjutan SAW Setelah menetukan nilai minimal tiap kriteria**/
                            for (int j = 0; j < jmlstate; j++) {
                                kemacetan = Integer.valueOf(dataSnapshot.child(Nstate[j]).child(hari).child(jamfirebase).getValue().toString());

                                String c = dataSnapshot.child(Nstate[j]).child("lat").getValue().toString();
                                String d = dataSnapshot.child(Nstate[j]).child("lon").getValue().toString();
                                //convert lat&lon Nsate string to Double
                                Double latNstate = Double.valueOf(c);
                                Double lonNstate = Double.valueOf(d);
                                coorNstate = Point.fromLngLat(lonNstate,latNstate);

                                //itung jarak dari IdNode ke tiap Nstate (longitude,latitude)
                                jarakState = TurfMeasurement.distance(Goal,coorNstate); //jarak state
                                //Log.d("Tesjarak" + i, String.valueOf(jarakState));

                                Double NormJarak = jarakTerkecil / jarakState;
                                Double NormMacet = macetterkecil / kemacetan;
                                Log.d("normalisasi kemacetan"+j, String.valueOf(NormMacet));
                                Log.d("normalisasi jarak"+j, String.valueOf(NormJarak));

                                /**Nilai SAW**/
                                NilaiSAW = ((0.7 * NormJarak) + (0.3 * NormMacet)); //bisa array atau perulangan?
                                Log.d("testingsaw", String.valueOf(NilaiSAW));

                                /**perbandingan SAW**/
                                if (Double.compare(TerbaikSAW, NilaiSAW) == 0) {

                                    System.out.println("d1=d2");
                                }
                                else if (Double.compare(TerbaikSAW, NilaiSAW) < 0) {

                                    System.out.println("d1<d2");
                                    TerbaikSAW = NilaiSAW;
                                    TerbaikSAWID = Nstate[j];
                                    TerbaikSAWCoor =coorNstate;
                                }
                                else {

                                    System.out.println("d1>d2");

                                }
                                //Log.d("terbaikSAW", String.valueOf(TerbaikSAW));

                            } //Tutup for (END SAW)

                        } //tutup else
                        /** end if else apabila jarak 0**/

                        //array rute

                        Log.d("terbaikSAW", String.valueOf(TerbaikSAW));
                        Log.d("terbaikSAW ID", String.valueOf(TerbaikSAWID));
                        Log.d("terbaikSAW COOR", String.valueOf(TerbaikSAWCoor));
                        //text view
                        textViewData.setText(data);
                        textViewNodeTerbaik.setText(Double.toString(terbaikjarak));
                        textViewIdNodeTerbaik.setText(IdNode);
                        textViewNstate.setText(Integer.toString(jmlstate));


                    }
                });

    }

    private void classbaru() {
        Log.d("tesclass","berhasil");

    }

}