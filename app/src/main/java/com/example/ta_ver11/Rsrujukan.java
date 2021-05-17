package com.example.ta_ver11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.List;

public class Rsrujukan extends AppCompatActivity {

    String namadb = "rumah_sakit";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsrujukan);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_lokasi);
        new FirebaseDatabaseHelper(namadb).readLokasis(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Lokasi> books, List<String> keys) {
                findViewById(R.id.progressBar4).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView, Rsrujukan.this, books, keys);
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