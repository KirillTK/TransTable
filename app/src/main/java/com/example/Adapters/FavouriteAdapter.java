package com.example.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class FavouriteAdapter extends ArrayAdapter<String> {
    private  List<String> namestations;
    private  List<String> Route;
    private  List<String> type;
    private  List<String> busnumber;
    private Context context;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;



    public FavouriteAdapter(Context context, List<String> namestations,  List<String> busnumber, List<String> Route,List<String> type){
        super(context, R.layout.favouritefadapter, namestations);
        this.context = context;
        this.Route = Route;
        this.type = type;
        this.namestations = namestations;
        this.busnumber = busnumber;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.favouritefadapter, parent, false);
        ImageView imageTransport = (ImageView)rowView.findViewById(R.id.imageTransport);
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.open();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currenttime = simpleDateFormat.format(new Date());
        switch (type.get(position)){
            case "A": imageTransport.setImageResource(R.drawable.busnormal);break;
            case "Т": imageTransport.setImageResource(R.drawable.busexpress);break;

        }

        TextView textRoute = (TextView)rowView.findViewById(R.id.route);
        textRoute.setText(Route.get(position));
        TextView namestation = (TextView) rowView.findViewById(R.id.namestationID);
        namestation.setText(namestations.get(position));
        TextView busnumber = (TextView) rowView.findViewById(R.id.busnumber);
        busnumber.setText(this.busnumber.get(position));
        return rowView;
    }


}
