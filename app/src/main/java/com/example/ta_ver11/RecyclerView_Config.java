package com.example.ta_ver11;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private BooksAdapter mLokasisAdapter;


    public void setConfig(RecyclerView recyclerView, Context context, List<Lokasi> lokasis, List<String> keys){
        mContext =context;
        mLokasisAdapter = new BooksAdapter(lokasis, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mLokasisAdapter);

    }

    class BookItemView extends RecyclerView.ViewHolder {
        private TextView mCoordinat;
        private TextView mNama;
        private TextView mJarak;
        private TextView mLon;
        private TextView mLat;

        private String key;
        private String lat;
        private String lon;

        public BookItemView(ViewGroup parent){
            super (LayoutInflater.from(mContext).
                    inflate(R.layout.lokasi_list_item,parent,false));

            mCoordinat = (TextView) itemView.findViewById(R.id.title_txtView);
            mNama = (TextView) itemView.findViewById(R.id.author_txtView);
            mLon = (TextView) itemView.findViewById(R.id.category_txtView);
            mLat = (TextView) itemView.findViewById(R.id.lat_txtView);
            mJarak = (TextView) itemView.findViewById(R.id.isbn_txtView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, map.class);
                    intent.putExtra("key",key);
                    intent.putExtra("lon",mLon.getText().toString());
                    intent.putExtra("lat",mLat.getText().toString());
                    intent.putExtra("coordinat",mCoordinat.getText().toString());
                    intent.putExtra("nama",mNama.getText().toString());
                    mContext.startActivity(intent);
                    Toast.makeText(mContext,"Tujuan: " + mNama.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        public void bind(Lokasi lokasi, String key){
            mCoordinat.setText(lokasi.getCoordinat());
            mNama.setText(lokasi.getNama());
            mLon.setText(lokasi.getLon());
            mLat.setText(lokasi.getLat());
            mJarak.setText(lokasi.getJarak());

            this.key = key;

        }
    }
    class BooksAdapter extends RecyclerView.Adapter<BookItemView>{
        private List<Lokasi> mLokasiList;
        private List<String> mKeys;
        //private RecyclerViewClickListener listener;

        public BooksAdapter(List<Lokasi> mLokasiList, List<String> mKeys) {
            this.mLokasiList = mLokasiList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public BookItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookItemView holder, int position) {
            holder.bind(mLokasiList.get(position),mKeys.get(position));

//            holder.itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                   // Toast.makeText(menuRS.this,items[position],Toast.LENGTH_SHORT).show();
//                     Toast.makeText(mContext, "tes", Toast.LENGTH_SHORT).show();
//
//
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return mLokasiList.size();
        }




    }


}
