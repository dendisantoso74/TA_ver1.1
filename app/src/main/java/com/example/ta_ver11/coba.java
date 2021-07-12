package com.example.ta_ver11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class coba extends AppCompatActivity {

    DatabaseReference reff;
    String jamfirebase;
    String nstate;

    //Integer[] statearray = new Integer[]{ 0,1,2 };;
    //Integer[] kemacetan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);

        reff = FirebaseDatabase.getInstance().getReference("node");


        TextView textViewDate = findViewById(R.id.textjam);
        TextView textViewMacet = findViewById(R.id.textViewmacet);
        TextView textViewJarak = findViewById(R.id.textViewjarak);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH"); //format 24 jam
        String jam = simpleDateFormat2.format(calendar.getTime());
        String hari = simpleDateFormat.format(calendar.getTime());
        reff.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

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


                //for (Integer i = 0; i <= 2; i++) {
                String nstste = snapshot.child("B1").child("state").child("0").getValue().toString(); //B3
                Integer kemacetan = Integer.valueOf(snapshot.child(nstste).child(hari).child(jamfirebase).getValue().toString()); //kemacetan sudah integer
                    //kemacetan[i] = Integer.valueOf(snapshot.child(nstste).child(hari).child(jamfirebase).getValue().toString()); //kemacetan sudah integer
                    textViewMacet.setText(Integer.toString(kemacetan)); // testing textview tengah
                //textview atas
                textViewDate.setText(jamfirebase);
               //}
                //textViewMacet.setText(Integer.toString(kemacetan[0])); // testing textview tengah


                //end penyesuaian hari dan jam antara firebase dan android


                //String kemacetan=snapshot.child("B1").child("Minggu").child(jamfirebase).getValue().toString();
                String jarak=snapshot.child("B1").child("jarak").getValue().toString();
                String jarak2=snapshot.child("B100").child("jarak").getValue().toString();
                String jarak3=snapshot.child("B10").child("jarak").getValue().toString();
                String jarak4=snapshot.child("B101").child("jarak").getValue().toString();

                //perbandingan jarak
                Integer a = Integer.parseInt(jarak); //100 //initial state
                Integer b = Integer.parseInt(jarak2); //1
                Integer c = Integer.parseInt(jarak3); //101
                Integer d = Integer.parseInt(jarak4); //10

                //9 = 0;
                //10 = 1;

                Integer jml= a + b;

                Integer nextState=0;

                //text view bawah
                textViewJarak.setText(Integer.toString(jml));

                reff.child("B10").child("jarak").setValue(jml);

                //pilih nilai paling kecil
                if(a>c){
                    nextState = c;

                    //textViewMacet.setText("betul");
                }else {nextState = a;}

                if (nextState>b){
                    nextState = b;
                    //
                } else {nextState = nextState;}

                if (nextState>d){
                    nextState = d;
                } else {nextState=nextState;}
                //end perbandingan jarak
                //text view tengah
                //textViewMacet.setText(Integer.toString(nextState));




            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });











    }


}