package com.example.ta_ver11;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class ListAdapter extends ArrayAdapter {

    private String[] Headline;
    private String[] Subhead;
    private Context context;

    public ListAdapter(Context context, String[] headline, String[] subhead) {
        super(context, R.layout.custom_listview, headline);

        this.Headline = headline;
        this.Subhead = subhead;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.custom_listview,null);
        }

        if (getItem(position) != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_headline);
            TextView tt2 = (TextView) v.findViewById(R.id.text_subhead);

            if (tt1 != null) {
                tt1.setText((Headline[position]));
            }

            if (tt2 != null) {
                tt2.setText(Subhead[position]);
            }

        }
        return v;
    }

}