package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener{

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
    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.button1:
                i = new Intent(this,OpsiTes.class);
                startActivity(i);
                break;
            case R.id.button2:
                i = new Intent(this,Isoman.class);
                startActivity(i);
                break;
            case R.id.button3:
                i = new Intent(this,Rsrujukan.class);
                startActivity(i);
                break;

        }

    }
}