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
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String halt;

    public NearTransportAdapter(Context context, Cursor c,String halt) {
        super(context, c, 0);
        this.halt = halt;
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
        Cursor Transport = database.rawQuery("select  Halt._id,Halt.name,Halt.route,Transport.number from Halt,Transport where Halt.route = '"+cursor.getString(cursor.getColumnIndex("NAME"))+"' and Halt.halt_transport = Transport._id and Halt.name = '"+halt+"' and Transport.number = '"+cursor.getString(cursor.getColumnIndex("_id"))+"' and Transport.type = '"+cursor.getString(cursor.getColumnIndex("TYPE"))+"'",null);
        while (Transport.moveToNext()){
            String text = Transport.getString(Transport.getColumnIndex("number"));
            Log.d("sql",text);
            textView.setText(text);
        }
    }
}
