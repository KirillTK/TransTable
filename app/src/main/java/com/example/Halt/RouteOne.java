package com.example.Halt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.Adapters.BusStopsAdapter;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;
import com.example.kirill.stopping.Stations;
import com.example.Time.Tab_time;
import java.util.ArrayList;
import java.util.List;


public class RouteOne extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<String> halt;
    private Cursor idCursor;
    private  List<String> numberList;
    private  List<String> numbeRoute;
    Cursor userCursor;
    ListView station;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    private String key;
    private String type;
    private String number;
    public String Route;
    List<Stations> stationsList;

    private OnFragmentInteractionListener mListener;

    public RouteOne() {

    }

    public static RouteOne newInstance(String param1, String param2) {
        RouteOne fragment = new RouteOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_route_one, container, false);
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.open();

        key = getActivity().getIntent().getExtras().getString("number");
        type = getActivity().getIntent().getExtras().getString("type");
        Log.d("type",type);
        Cursor busNumber = database.rawQuery("select * from "+ DatabaseHelper.TABLE_1+" where "+ DatabaseHelper.BUS_COLUMN_ID + " = "+ key ,null);
        Cursor stationRoute = database.rawQuery("select route from Halt where halt_transport = '"+key+"' and number_route = 'Первый путь'",null);
        if(stationRoute.getCount()!=0) {
        busNumber.moveToFirst();
        stationRoute.moveToFirst();
        Route = stationRoute.getString(stationRoute.getColumnIndex("route"));
        number = busNumber.getString(busNumber.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
        busNumber.close();
        stationRoute.close();
        userCursor = database.rawQuery("select * from Halt where halt_transport = '" + key +"' and number_route = 'Первый путь'" ,null);
            idCursor = database.rawQuery("select * from Halt where halt_transport = '" + key + "' and number_route = 'Первый путь'", null);
            halt = getList("_id", idCursor);
            numberList = getBusNamber(number);
            numbeRoute = addRoute(idCursor, "Первый путь");
            stationsList = new ArrayList<Stations>();
            stationsList = station(userCursor);
            BusStopsAdapter adapter = new BusStopsAdapter(getContext(), userCursor, stationsList, halt, numberList, Route, type);
            station = (ListView) view.findViewById(R.id.stoppingId);
            station.setAdapter(adapter);
            station.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getContext(), Tab_time.class);
                    Long num = l;
                    String number = num.toString();
                    intent.putExtra("time", number);
                    intent.putExtra("station", stationsList.get(i).getStations());
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 1);
                }
            });
        }
        return  view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private List<Stations> station(Cursor userCursor){
        List<Stations> stationsList = new ArrayList<Stations>();
        while (userCursor.moveToNext()){
            stationsList.add(get(userCursor.getString(userCursor.getColumnIndex("name")),userCursor.getPosition()));
        }
        return stationsList;
    }


    private List<String> getList(String sql,Cursor idCursor){
        List<String> list = new ArrayList<>();
        while (idCursor.moveToNext()){
            list.add(idCursor.getString(idCursor.getColumnIndex(sql)));

        }
        return list;
    }

    private List<String> getBusNamber(String numb){
        List<String> number = new ArrayList<>();
        for(int i = 0; i<idCursor.getCount();i++){
            number.add(numb);
        }
        return number;
    }
    private List<String> addRoute(Cursor cursor,String string){
        List<String> arr = new ArrayList<>();
        for(int i = 0; i<cursor.getCount();i++){
            Log.d("Route",string);
            arr.add(string);
        }
        return arr;
    }

    private Stations get(String s,int pos) {
        return new Stations(s,pos,number);
    }
}