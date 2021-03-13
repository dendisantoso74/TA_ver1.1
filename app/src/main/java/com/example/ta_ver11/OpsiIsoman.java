package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpsiIsoman extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opsi_isoman);

        //define button
        Button buttonhotel = findViewById(R.id.buttonHotel);
        Button buttonrs = findViewById(R.id.buttonRS);


        //set click listener button
        buttonhotel.setOnClickListener(this);
        buttonrs.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.buttonHotel:
                i = new Intent(this,Isoman.class);
                startActivity(i);
                break;
            case R.id.buttonRS:
                i = new Intent(this,Isoman.class);
                startActivity(i);
                break;
        }

    }
}