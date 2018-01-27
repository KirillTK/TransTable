package com.example.kirill.stopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.Adapters.FavouriteAdapter;
import com.example.Time.Tab_time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Favoutite_fragment extends Fragment {
    private ListView favourits;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Favoutite_fragment newInstance(String param1, String param2) {
        Favoutite_fragment fragment = new Favoutite_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favoutite_fragment, container, false);
        favourits = (ListView)view.findViewById(R.id.favoiritelistId);
        SharedPreferences mSettings = getContext().getSharedPreferences("Stations", Context.MODE_PRIVATE);
        final List<String> stations = new ArrayList<String>();
        Map<String,?> hashMap = mSettings.getAll();
        for(Map.Entry<String,?> entry:hashMap.entrySet()){
                stations.add(entry.getValue().toString());
        }
        SharedPreferences busnamber = getContext().getSharedPreferences("Busnamber", 0);
        final List<String> bus = new ArrayList<String>();
        Map<String,?> busMap = busnamber.getAll();
        for(Map.Entry<String,?> entry:busMap.entrySet()){
            String buffer = entry.getValue().toString().replaceAll(" ","");
            bus.add(buffer);
        }
        SharedPreferences Route = getContext().getSharedPreferences("Route", Context.MODE_PRIVATE);
        final List<String> rote = new ArrayList<String>();
        Map<String,?> routeMap = Route.getAll();
        for(Map.Entry<String,?> entry:routeMap.entrySet()){
            rote.add(entry.getValue().toString());

        }


        SharedPreferences Type = getContext().getSharedPreferences("Type", Context.MODE_PRIVATE);
        final List<String> type = new ArrayList<String>();
        Map<String,?> typeMap = Type.getAll();
        for(Map.Entry<String,?> entry:typeMap.entrySet()){
            type.add(entry.getValue().toString());

        }

        FavouriteAdapter adapter = new FavouriteAdapter(getContext(),stations,bus,rote,type);
        favourits.setAdapter(adapter);

        favourits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), Tab_time.class);
                String sql = "select Halt._id from Halt,Transport where name = '"+ stations.get(i)+"' and route = '"+rote.get(i)+"'  and  halt_transport = Transport._id and Transport._id = (select _id from Transport where number = '"+ bus.get(i)+"' and type = '"+type.get(i)+"' )";
                Cursor cursor = database.rawQuery(sql,null);
                cursor.moveToFirst();
                intent.putExtra("time", cursor.getString(cursor.getPosition()));
                cursor.close();
                intent.putExtra("type","F");
                startActivityForResult(intent, 1);
            }
        });

        return view;
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

}
