package com.example.kirill.stopping;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.Adapters.BusListAdapter;
import com.example.Halt.Halt;

public class Bus_fragment extends Fragment {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    Cursor userCursor;
    GridView gridView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static String number;

    public static Bus_fragment newInstance(String param1, String param2) {
        Bus_fragment fragment = new Bus_fragment();
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
        number = "36";
        View view =  inflater.inflate(R.layout.fragment_bus_fragment, container, false);
        gridView = (GridView)view.findViewById(R.id.Table);
        dbHelper = new DatabaseHelper(getContext());
        dbHelper.create_db();
        database = dbHelper.open();
        userCursor = database.rawQuery("select * from Transport where type ='A'" ,null);
        BusListAdapter adapter = new BusListAdapter(this.getContext(), userCursor);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),Halt.class);
                Long num = l;
                number =  num.toString();
                intent.putExtra("number",number);
                intent.putExtra("type","A");
                startActivityForResult(intent,1);
            }
        });
        return view;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
