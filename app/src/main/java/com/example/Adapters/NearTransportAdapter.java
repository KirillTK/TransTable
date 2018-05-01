package com.example.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NearTransportAdapter extends ArrayAdapter<String> {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor transport;
    private Context context;
    private List<String> transport_number = new ArrayList<>();
    private List<String> type = new ArrayList<>();
    private List<String> route2 = new ArrayList<>();
    private List<Integer> id = new ArrayList<>();
    public int id2;

    public NearTransportAdapter(@NonNull Context context,List<String> transport_number,List<String> type,List<String> route,List<Integer> id) {
        super(context, R.layout.near_transport, transport_number);
        this.context = context;
        this.transport_number = transport_number;
        this.type = type;
        this.route2 = route;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.near_transport, parent, false);
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.open();
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView textView = (TextView) view.findViewById(R.id.trasnportnumber);
        TextView route = (TextView) view.findViewById(R.id.route);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageTransport);
        id2 = id.get(position);
        switch (type.get(position)) {
            case "A": imageView.setImageResource(R.drawable.busnormal); break;
            case "Т": imageView.setImageResource(R.drawable.busexpress); break;
            default: imageView.setImageResource(R.drawable.busnormal); break;
        }
        textView.setText(transport_number.get(position));
        Log.d("Ner","number = "+transport_number.get(position));
        route.setText(route2.get(position));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currenttime = simpleDateFormat.format(new Date());
        String sql = getSql(currenttime);
//        String  sql  ="select time from Time where time_halt = "+id2+" and TIME(time) >= TIME('"+currenttime+"')";
        transport = database.rawQuery(sql,null);
        if(transport.moveToFirst()){
            String neartime = transport.getString(transport.getPosition());
            try {
                Log.d("time","currenttime  "+currenttime);
                Log.d("time","neartime  "+ neartime);
                time.setText(getNearTime(currenttime,neartime)+" мин");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transport.close();
        }else {
            time.setText("завтра");
        }
        return view;
    }


    public String getNearTime(String currenttime,String neartime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date currentdate = simpleDateFormat.parse(currenttime);
        Date neardate = simpleDateFormat.parse(neartime);
        int currentvalue = getTime(currentdate.getHours(),currentdate.getMinutes());
        Log.d("time","currentvalue  "+currentvalue);
        int nearvalue = getTime(neardate.getHours(),neardate.getMinutes());
        Log.d("time","nearvalue "+nearvalue);
        int result = nearvalue - currentvalue;
        Log.d("time","result    "+result);
        return String.valueOf(result);
    }

    public int getTime(int h, int m){
        int sum = 0;
        sum = h*60+m;
        return sum;
    }

    public String getSql(String currenttime){
        String sql = "";
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            Log.d("week","Выходной");
            sql  ="select time from Time_hollyday where time_halt = "+id2+" and TIME(time) >= TIME('"+currenttime+"')";

        } else {
            Log.d("week","Будний");
            sql  ="select time from Time where time_halt = "+id2+" and TIME(time) >= TIME('"+currenttime+"')";
            }
        return sql;
    }
}
