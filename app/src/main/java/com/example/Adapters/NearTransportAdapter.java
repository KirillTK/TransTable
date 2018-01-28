package com.example.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Map.busStopsDatabase;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;

public class NearTransportAdapter extends CursorAdapter{
    private Cursor Transport;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public NearTransportAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.buses_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        dbHelper = new DatabaseHelper(context);
        dbHelper.create_db();
        database = dbHelper.open();
        TextView textView = (TextView) view.findViewById(R.id.busID);
        ImageView imageView = (ImageView) view.findViewById(R.id.busTypeIcon);
        Transport = database.rawQuery("select Transport.number from Halt,Transport where Halt.route = 'ДС Малиновка-4 - Брилевичи' and Halt.halt_transport = Transport._id and Halt.name = '"+cursor.getColumnIndex("NAME")+"' and Transport.number = '"+cursor.getColumnIndex("_id")+"' and Transport.type = '"+cursor.getColumnIndex("TYPE")+"'",null);
        Transport.moveToFirst();
        textView.setText(String.valueOf(Transport.getColumnIndex("number")));

    }
}
