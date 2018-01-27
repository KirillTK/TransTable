package com.example.Adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.Map.busStopsDatabase;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;
import java.util.List;


public class NearHaltAdapter extends ArrayAdapter<String> {
    private List<String> nearHaltList;
    List<String> IdList;
    private List<Integer> distance;
    private Context context;
    private TextView namestation;
    private TextView near;
    private TextView transport;
    private TextView transport2;
    private String bus ;
    private String troll;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    busStopsDatabase dataBaseConnection;

    public NearHaltAdapter(Context context,List<String> nearHaltList,List<Integer> distance, List<String> IdList) {
        super(context, R.layout.near_halt_adapter, nearHaltList);
        this.nearHaltList = nearHaltList;
        this.distance = distance;
        this.context = context;
        this.IdList = IdList;
        dataBaseConnection = new busStopsDatabase(getContext());
        dataBaseConnection.create_db();
        database = dataBaseConnection.open();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.near_halt_adapter, parent, false);
        namestation = (TextView)rowView.findViewById(R.id.namestationID);
        near = (TextView)rowView.findViewById(R.id.near);
        transport = (TextView)rowView.findViewById(R.id.transport);
        transport2 = (TextView)rowView.findViewById(R.id.transport2);
        namestation.setText(nearHaltList.get(position));
        near.setText("Около "+Integer.toString(distance.get(position))+" м");
        bus = "";
        troll="";
        Cursor busCursor = database.rawQuery("select distinct Routes.ID,Routes.TYPE from Coordinates,Routes where Coordinates.NAME = '"+nearHaltList.get(position)+"' and Routes.STOPS like '%"+IdList.get(position)+"%'",null);
        while (busCursor.moveToNext()){
                switch (busCursor.getString(busCursor.getColumnIndex("TYPE"))){
                    case "A": if (bus.length()<35){bus+= busCursor.getString(busCursor.getColumnIndex("ID")) +" ";} break;
                    case "Т": if (troll.length()<35){troll+= busCursor.getString(busCursor.getColumnIndex("ID")) +" ";} break;
                }
        }
        busCursor.close();
        if(bus.length()>=35){
            bus+=" ...";
        }
        if(troll.length()>=35){
            troll += " ...";
        }
        transport.setText(bus);
        transport2.setText(troll);
        return rowView;
    }
}
