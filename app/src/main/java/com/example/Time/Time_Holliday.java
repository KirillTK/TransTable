package com.example.Time;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.example.Adapters.TimeAdapter;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Time_Holliday extends Fragment {

    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    Cursor userCursor;
    ListView time;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Time_Holliday() {

    }


    public static Time_Holliday newInstance(String param1, String param2) {
        Time_Holliday fragment = new Time_Holliday();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.time_hollyday_fragment, container, false);

        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.open();

        time  = (ListView) view.findViewById(R.id.timeView);



        String a = getActivity().getIntent().getExtras().getString("time");
        String type = getActivity().getIntent().getExtras().getString("type");
        userCursor = database.rawQuery("select * from Time_hollyday where time_halt =  '" + a +"'",null);
        if(userCursor.getCount()!=0){

            DateFormat format = new SimpleDateFormat("HH:mm");
            String prevTime;
            String buffer = new String("");



            List<String> timeListForCurrentStop = new ArrayList<>();
            //Anton, 16.11.2017
            List<String> currentTimeIcon = new ArrayList<>();
            //
            String arr[] = new String[userCursor.getCount()];
            String min[] = new String[userCursor.getCount()];
            Date minutes = new Date();
            while (userCursor.moveToNext()){

                String temp = userCursor.getString(userCursor
                        .getColumnIndex("time"));

                try {
                    minutes = format.parse(temp);
                    arr[userCursor.getPosition()] = temp;
                    if(minutes.getMinutes()<10){
                        min[userCursor.getPosition()] = "0"+String.valueOf(minutes.getMinutes());
                    }else{
                        min[userCursor.getPosition()] = String.valueOf(minutes.getMinutes());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            prevTime = arr[0];
            for(int i =0 ; i<=userCursor.getCount()-1;i++){

                try {
                    Date currentDate  = format.parse(arr[i]);
                    Date previousDate = format.parse(prevTime);
                    int hours1 = currentDate.getHours();
                    int hours2 = previousDate.getHours();


                    if(hours1 == hours2 || buffer==""){
                        if(i==0){
                            buffer +=min[i];
                        }
                        else{
                            buffer +="    " + min[i];
                        }

                    }else{
                        timeListForCurrentStop.add(buffer);
                        //Anton, 16.11.2017
                        currentTimeIcon.add(prevTime);
                        //
                        buffer = min[i];
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                prevTime = arr[i];
            }
            try {
                minutes = format.parse(arr[userCursor.getCount()-1]);
                if(minutes.getMinutes()<10){
                    timeListForCurrentStop.add("0"+String.valueOf(minutes.getMinutes()));
                }else {
                    timeListForCurrentStop.add(String.valueOf(minutes.getMinutes()));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            currentTimeIcon.add(prevTime);

            //Anton, 16.11.2016
            TimeAdapter adapter = new TimeAdapter(getContext(), timeListForCurrentStop, currentTimeIcon,type);
            time  = (ListView)view.findViewById(R.id.timeView);
            time.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "Не ходит по выходным", Toast.LENGTH_SHORT).show();
        }

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
