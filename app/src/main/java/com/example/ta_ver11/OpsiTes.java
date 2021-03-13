package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpsiTes extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opsi_tes);

        //define button
        Button buttonrapid = findViewById(R.id.buttonRapid);
        Button buttonantigen = findViewById(R.id.buttonAntigen);
        Button buttonpcr = findViewById(R.id.buttonPcr);


        //set click listener button
        buttonrapid.setOnClickListener(this);
        buttonantigen.setOnClickListener(this);
        buttonpcr.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.buttonRapid:
                i = new Intent(this,Tes.class);
                startActivity(i);
                break;
            case R.id.buttonAntigen:
                i = new Intent(this,Tes.class);
                startActivity(i);
                break;
            case R.id.buttonPcr:
                i = new Intent(this,Tes.class);
                startActivity(i);
                break;
        }

    }

}