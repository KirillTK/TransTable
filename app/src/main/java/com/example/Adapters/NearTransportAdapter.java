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
import android.widget.Toast;

import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        int id = 1;
        String text = null;
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView textView = (TextView) view.findViewById(R.id.trasnportnumber);
        TextView route = (TextView) view.findViewById(R.id.route);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageTransport);
        transport = database.rawQuery("select  Halt.*,Transport.number,Transport.type from Halt,Transport where Halt.route = '"+cursor.getString(cursor.getColumnIndex("NAME"))+"' and Halt.halt_transport = Transport._id and Halt.name = '"+halt+"' and Transport.number = '"+cursor.getString(cursor.getColumnIndex("transport"))+"' and Transport.type = '"+cursor.getString(cursor.getColumnIndex("TYPE"))+"'",null);
        if (transport!=null && transport.getCount()>0){
            transport.moveToFirst();
            id = transport.getInt(transport.getColumnIndex("_id"));
            text = transport.getString(transport.getColumnIndex("number"));
            transport.close();
            switch (cursor.getString(cursor.getColumnIndex("TYPE"))) {
                case "A": imageView.setImageResource(R.drawable.busnormal); break;
                case "Т": imageView.setImageResource(R.drawable.busexpress); break;
                default: imageView.setImageResource(R.drawable.busnormal); break;
            }
        }
        textView.setText(text);
        route.setText(cursor.getString(cursor.getColumnIndex("NAME")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currenttime = simpleDateFormat.format(new Date());
        String  sql  ="select time from Time where time_halt = "+id+" and TIME(time) >= TIME('"+currenttime+"')";
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
}
