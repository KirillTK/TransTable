package com.example.Map;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.kirill.stopping.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import static com.example.Map.busStopsDatabase.ShapeID;
import static com.example.Map.busStopsDatabase.tableName;
import static com.example.Map.busStopsDatabase.tableNameRoutes;
import static com.example.Map.busStopsDatabase.tableNameShapes;
import static com.example.kirill.stopping.Bus_fragment.number;


public class mapViewer extends Fragment {

    MapView mMapView;
    Cursor userCursor;
    busStopsDatabase dataBaseConnection;
    SQLiteDatabase database;

    public mapViewer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                UiSettings currentUiSettings = mMap.getUiSettings();
                currentUiSettings.setMapToolbarEnabled(false);
                currentUiSettings.setCompassEnabled(false);
                currentUiSettings.setMyLocationButtonEnabled(true);
                currentUiSettings.setZoomControlsEnabled(false);
                mMap.setMinZoomPreference(11);
                LatLng centralPoint = new LatLng(53.90308663, 27.56050229);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centralPoint, 11));
                LatLngBounds ADELAIDE = new LatLngBounds(new LatLng(53.83206766, 27.36127853), new LatLng(53.98153138, 27.69086838));
                mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}
                            ,10);
                }else{
                    mMap.setMyLocationEnabled(true);
                }

                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    float previousZoomLevel;

                    @Override
                    public void onCameraIdle() {

                        float zoomLevel = mMap.getCameraPosition().zoom;
                        if (previousZoomLevel != zoomLevel) {
                            mMap.clear();
                        }
                        previousZoomLevel = zoomLevel;

                        dataBaseConnection = new busStopsDatabase(getContext());
                        dataBaseConnection.create_db();
                        database = dataBaseConnection.open();
                        number = "104";
                        userCursor = database.rawQuery("SELECT * FROM "+ tableNameRoutes +" WHERE transport = "+number+";",null);
                        userCursor.moveToFirst();
                        String shapeID = userCursor.getString(userCursor.getColumnIndex(busStopsDatabase.RouteID));

                        userCursor = database.rawQuery("SELECT * FROM "+ tableNameShapes +" WHERE "+ ShapeID +" = "+shapeID+";",null);

                        ArrayList<LatLng> PolyLine = new ArrayList<LatLng>();
                        userCursor.moveToFirst();
                        PolyLine = decodePoly(userCursor.getString(userCursor.getColumnIndex(busStopsDatabase.ShapeString)));

                        PolylineOptions ruta=new PolylineOptions();
                        for(int i=0;i<PolyLine.size();i++){
                            ruta.add(new LatLng(PolyLine.get(i).latitude, PolyLine.get(i).longitude));
                        }
                        ruta.color(Color.RED).width(7);
                        Polyline polygon=mMap.addPolyline(ruta);
                        if (zoomLevel>=16)
                        {
                            LatLngBounds currentRectangle = mMap.getProjection().getVisibleRegion().latLngBounds;
                            userCursor = database.rawQuery("SELECT * FROM "+tableName+" WHERE "+ busStopsDatabase.stopLat + " > " +
                                    currentRectangle.southwest.latitude + " AND " + busStopsDatabase.stopLat + " < " +
                                    currentRectangle.northeast.latitude + " AND " + busStopsDatabase.stopLong + " > " +
                                    currentRectangle.southwest.longitude + " AND " + busStopsDatabase.stopLong + " < " +
                                    currentRectangle.northeast.longitude,null);

                            for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext())
                            {
                                LatLng busStopToDraw = new LatLng(userCursor.getDouble(userCursor.getColumnIndex(busStopsDatabase.stopLat)),userCursor.getDouble(userCursor.getColumnIndex(busStopsDatabase.stopLong)));
                                mMap.addMarker(new MarkerOptions().position(busStopToDraw).title(userCursor.getString(userCursor.getColumnIndex(busStopsDatabase.stopName))));
                            }
                        }
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        Log.i("Location", "String received: "+encoded);
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}
