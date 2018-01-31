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
    private Cursor transport;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String halt;

    public NearTransportAdapter(Context context, Cursor c,String halt) {
        super(context, c, 0);
        this.halt = halt;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.near_transport, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.open();
        String text = null;
        TextView textView = (TextView) view.findViewById(R.id.trasnportnumber);
        TextView route = (TextView) view.findViewById(R.id.route);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageTransport);
        transport = database.rawQuery("select * from Halt,Transport where Halt.route = '"+cursor.getString(cursor.getColumnIndex("NAME"))+"' and Halt.halt_transport = Transport._id and Halt.name = '"+halt+"' and Transport.number = '"+cursor.getString(cursor.getColumnIndex("transport"))+"' and Transport.type = '"+cursor.getString(cursor.getColumnIndex("TYPE"))+"'",null);
        if (transport!=null && transport.getCount()>0){
            transport.moveToFirst();
            text = transport.getString(transport.getColumnIndex("number"));
            transport.close();
            switch (cursor.getString(cursor.getColumnIndex("TYPE"))){
                case "A": imageView.setImageResource(R.drawable.busnormal); break;
                case "Т": imageView.setImageResource(R.drawable.busexpress);break;
                default: imageView.setImageResource(R.drawable.busnormal); break;
            }

        }
        textView.setText(text);
        route.setText(cursor.getString(cursor.getColumnIndex("NAME")));
    }
}
