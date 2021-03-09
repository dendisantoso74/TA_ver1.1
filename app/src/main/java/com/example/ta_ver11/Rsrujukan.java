package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Rsrujukan extends AppCompatActivity {
    String items[] = new String[] {"RS Cinta Kasih Tzu cHI" , "RS Ciputra Citra Garden City" , "RS Pondok Indah Puri Indah" , "RS Royal Taruma" , "Siloam Hospital Lippo Village","RS Jantung Harapan Kita", "RS Kanker Dharmais, RS Pelni", "Lab FK Universitas Tarumanegara", "Genelab,IntiBios Lab", "Klinik Rosella", "RS Graha Kedoya" , "RS Cinta Kasih Tzu cHI" , "RS Ciputra Citra Garden City" , "RS Pondok Indah Puri Indah" , "RS Royal Taruma" , "Siloam Hospital Lippo Villag"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsrujukan);


        ListView listView =(ListView) findViewById(R.id.view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Rsrujukan.this,items[position],Toast.LENGTH_SHORT).show();
                Intent i;
                i = new Intent(Rsrujukan.this,map.class);
                startActivity(i);
            }
        });


    }
}