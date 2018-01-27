package com.example.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirill.stopping.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeAdapter extends ArrayAdapter<String> {
    private final Context context;
    private List<String> values = new ArrayList<>();
    private List<String> icons = new ArrayList<>();
    private int hour;
    private String type;
    private TextView textView;
    public TextView clockView;

    public TimeAdapter(Context context, List<String> values, List<String> icons,String type) {
        super(context, R.layout.time_layout, values);
        this.context = context;
        this.values = values;
        this.icons = icons;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.time_layout, parent, false);
        textView = (TextView) rowView.findViewById(R.id.timeString);
        clockView = (TextView) rowView.findViewById(R.id.clockPicture);
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            Date currentDate  = format.parse(icons.get(position));
            hour = currentDate.getHours();
            clockView.setText(Integer.toString(hour));
            setColorText(type);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView.setText(values.get(position));

        if (hour==(Calendar.getInstance().getTime().getHours()))
        {
            switch (type){
                case "A": textView.setTextColor(ContextCompat.getColor(context, R.color.bus)); break;
                case "Т": textView.setTextColor(ContextCompat.getColor(context, R.color.troll)); break;
                case "F": textView.setTextColor(ContextCompat.getColor(context, R.color.favorite)); break;
                case "N": textView.setTextColor((ContextCompat.getColor(context, R.color.near))); break;
            }
        }
        return rowView;
    }

    private void setColorText(String type){
        switch (type){
            case "A":  clockView.setTextColor(context.getResources().getColor(R.color.bus)); break;
            case "Т":  clockView.setTextColor(context.getResources().getColor(R.color.troll)); break;
            case "F":  clockView.setTextColor(context.getResources().getColor(R.color.favorite)); break;
            case "N":  clockView.setTextColor(context.getResources().getColor(R.color.near)); break;
        }
    }
}
