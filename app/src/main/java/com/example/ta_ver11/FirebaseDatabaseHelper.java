package com.example.ta_ver11;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceLokasi;
    private List<Lokasi> lokasis = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Lokasi> lokasis, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseDatabaseHelper(String namadb) {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceLokasi = mDatabase.getReference(namadb);
    }

    public void readLokasis(final DataStatus dataStatus){
        mReferenceLokasi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lokasis.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Lokasi lokasi = keyNode.getValue(Lokasi.class);
                    lokasis.add(lokasi);
                }
                dataStatus.DataIsLoaded(lokasis,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
