package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Isoman extends AppCompatActivity {
    String items[] = new String[] {"RS Jantung Harapan Kita", "RS Kanker Dharmais, RS Pelni", "Lab FK Universitas Tarumanegara", "Genelab,IntiBios Lab", "Klinik Rosella", "RS Graha Kedoya" , "RS Cinta Kasih Tzu cHI" , "RS Ciputra Citra Garden City" , "RS Pondok Indah Puri Indah" , "RS Royal Taruma" , "Siloam Hospital Lippo Village"};
    String namadb = "isoman";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isoman);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_lokasi);
        new FirebaseDatabaseHelper(namadb).readLokasis(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Lokasi> books, List<String> keys) {
                findViewById(R.id.progressBar4).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView, Isoman.this, books, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });




    }
}