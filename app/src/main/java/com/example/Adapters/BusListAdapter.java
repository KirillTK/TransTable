package com.example.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;


    public class BusListAdapter extends CursorAdapter {

    public BusListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.buses_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.busID);
        ImageView imageView = (ImageView) view.findViewById(R.id.busTypeIcon);
        String text = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
        String type = cursor.getString(cursor.getColumnIndex("type"));
        textView.setText(text);
        if(type.equals("A")){
            imageView.setImageResource(R.drawable.busnormal);
        }else{
            imageView.setImageResource(R.drawable.busexpress);
        }
    }
}
