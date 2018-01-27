package com.example.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirill.stopping.R;
import com.example.kirill.stopping.Stations;
import com.like.LikeButton;
import com.like.OnLikeListener;
import java.util.List;

public class BusStopsAdapter extends CursorAdapter {

    private Cursor cursor;
    private String route;
    private String typestr;
    private Activity context;
    private List<Stations> stationsList;
    private SharedPreferences favourite;
    private SharedPreferences stationsname;
    private SharedPreferences busnumber;
    private SharedPreferences Route;
    private SharedPreferences type;
    private List<String> haltId;
    private List<String> RouteId;
    private List<String> typeID;
    private List<String> numberId;

    public BusStopsAdapter(Context context, Cursor cursor, List<Stations> stationsList,List<String> haltID,List<String> numberId,String route,String type) {
        super(context, cursor, 0);
        this.numberId = numberId;
        this.route = route;
        this.RouteId = haltID;
        this.cursor = cursor;
        this.typeID = haltID;
        this.typestr = type;
        this.stationsList = stationsList;
        this.haltId = haltID;
        this.context = (Activity) context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.stations_layout, parent, false);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.stations_layout, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.stationName);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.busStopIcon);
        textView.setText(stationsList.get(position).getStations());
        switch (typestr){
            case "A":        if (position == 0) {
                imageView.setImageResource(R.drawable.firststation);
            } else if (position == cursor.getCount()-1) {
                imageView.setImageResource(R.drawable.laststation);
            } else {
                imageView.setImageResource(R.drawable.middlestation);
            }
            break;
            case "Т":
                if (position == 0) {
                imageView.setImageResource(R.drawable.firststationtroll);
            } else if (position == cursor.getCount()-1) {
                imageView.setImageResource(R.drawable.laststationtroll);
            } else {
                imageView.setImageResource(R.drawable.middlestationtroll);
            }
            break;
        }

        LikeButton button = (LikeButton) convertView.findViewById(R.id.star_button);
        button.setLiked(Boolean.valueOf(this.context.getSharedPreferences("Favourite",Context.MODE_PRIVATE).getBoolean(haltId.get(position), false)));
        button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton, View view) {
                BusStopsAdapter.this.favourite = BusStopsAdapter.this.context.getSharedPreferences("Favourite", 0);
                BusStopsAdapter.this.stationsname = BusStopsAdapter.this.context.getSharedPreferences("Stations", 0);
                SharedPreferences.Editor editor = BusStopsAdapter.this.favourite.edit();
                SharedPreferences.Editor editor2 = BusStopsAdapter.this.stationsname.edit();
                editor.putBoolean(haltId.get(position), true);
                editor2.putString(haltId.get(position), ((Stations) BusStopsAdapter.this.stationsList.get(position)).getStations().toString());
                editor.commit();
                editor2.commit();
                Log.d("Busnumber",BusStopsAdapter.this.stationsList.get(position).getBusnamber());
                busnumber = context.getSharedPreferences("Busnamber",0);
                SharedPreferences.Editor  editor3 = busnumber.edit();
                Log.d("Number",numberId.get(position));
                editor3.putString(haltId.get(position),numberId.get(position));
                editor3.commit();
                Route = context.getSharedPreferences("Route",0);
                SharedPreferences.Editor editor4 = Route.edit();
                editor4.putString(RouteId.get(position),route);
                editor4.commit();

                type = context.getSharedPreferences("Type",0);
                SharedPreferences.Editor editor5 =type.edit();
                editor5.putString(typeID.get(position),typestr);
                editor5.commit();
            }

            @Override
            public void unLiked(LikeButton likeButton, View view) {
                BusStopsAdapter.this.favourite = BusStopsAdapter.this.context.getSharedPreferences("Favourite", 0);
                SharedPreferences.Editor editor = BusStopsAdapter.this.favourite.edit();
                editor.remove(haltId.get(position));
                editor.commit();
                BusStopsAdapter.this.stationsname = BusStopsAdapter.this.context.getSharedPreferences("Stations", 0);
                SharedPreferences.Editor editor2 = BusStopsAdapter.this.stationsname.edit();
                editor2.remove(haltId.get(position));
                editor2.commit();
                BusStopsAdapter.this.busnumber = BusStopsAdapter.this.context.getSharedPreferences("Busnamber",0);
                SharedPreferences.Editor editor3 = BusStopsAdapter.this.busnumber.edit();
                editor3.remove(haltId.get(position));
                editor3.commit();
                Route = context.getSharedPreferences("Route",0);
                SharedPreferences.Editor editor4  = BusStopsAdapter.this.Route.edit();
                editor4.remove(RouteId.get(position));
                editor4.commit();

                type = context.getSharedPreferences("Type",0);
                SharedPreferences.Editor editor5 = type.edit();
                editor5.remove(typeID.get(position));
                editor5.commit();
            }
        });

        return convertView;
    }
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
    }


}