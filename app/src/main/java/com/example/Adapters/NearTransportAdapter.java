package com.example.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.example.kirill.stopping.R;

public class NearTransportAdapter extends CursorAdapter{

    public NearTransportAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.buses_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
