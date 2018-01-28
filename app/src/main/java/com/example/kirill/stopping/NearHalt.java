package com.example.kirill.stopping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.Adapters.NearHaltAdapter;
import com.example.Map.busStopsDatabase;
import java.util.ArrayList;
import java.util.List;


public class NearHalt extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    busStopsDatabase dataBaseConnection;
    private Cursor NearCursorStopiing;
    private Cursor Buses;
    SQLiteDatabase database;
    private LocationListener locationListener;
    private LocationManager locationMangaer;
    private ListView NearHalts;
    private ArrayList<String> nearHaltList;
    private ArrayList<String> IdList;
    private List<Integer> distance;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static NearHalt newInstance(String param1, String param2) {
        NearHalt fragment = new NearHalt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseConnection = new busStopsDatabase(getContext());
        dataBaseConnection.create_db();
        database = dataBaseConnection.open();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.near_halt, container, false);
        NearHalts = (ListView) rootView.findViewById(R.id.nearhaltlist);
        locationMangaer = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        NearCursorStopiing = database.rawQuery("select * from Coordinates",null);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                nearHaltList = new ArrayList<>();
                IdList = new ArrayList<>();
                distance = new ArrayList<>();
                double latitide = location.getLatitude();
                double longitude = location.getLongitude();
                while (NearCursorStopiing.moveToNext()){
                    double endlatitide = NearCursorStopiing.getDouble(NearCursorStopiing.getColumnIndex("LONG")) ;
                    double endlongitude = NearCursorStopiing.getDouble(NearCursorStopiing.getColumnIndex("LAT")) ;
                    float [] dist= new float[1];
                    location.distanceBetween(latitide,longitude,endlatitide,endlongitude,dist);
                    if(dist[0]<=250){
                        nearHaltList.add(NearCursorStopiing.getString(NearCursorStopiing.getColumnIndex("NAME")));
                        IdList.add(NearCursorStopiing.getString(NearCursorStopiing.getColumnIndex("ID")));
                        distance.add(Math.round(dist[0]));
                    }
                }
                NearCursorStopiing.close();
                if(getActivity()!=null){
                    NearHaltAdapter adapter = new NearHaltAdapter(getContext(),nearHaltList,distance,IdList);
                    NearHalts.setAdapter(adapter);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}
            ,10);
        }else{
            getUPD();
        }
        NearHalts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),NearTransport.class);
                intent.putExtra("halt",nearHaltList.get(i));
                intent.putExtra("id",IdList.get(i));
                startActivityForResult(intent,1);
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getUPD();
                return;
        }
    }

    private void getUPD() {
        locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 20, locationListener); ///сделать больше задержку для обновления координат
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
