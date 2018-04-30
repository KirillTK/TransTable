package com.example.kirill.stopping;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.Adapters.BusListAdapter;
import com.example.Adapters.NearTransportAdapter;
import com.example.Map.busStopsDatabase;
import com.example.Time.Tab_time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearTransport extends AppCompatActivity {
    private Toolbar toolbar;
    private Cursor busCursor;
    private Cursor forAdapter;
    private List<String> halt_transport_bus;
    SQLiteDatabase database;
    busStopsDatabase dataBaseConnection;
    private ListView busGrid;
    private DatabaseHelper dbHelper;
    private List<String> transport_number;
    private List<String> type;
    private List<String> route;
    private List<Integer> id2;
    private String halt;
    DatabaseHelper dbHelper2;
    SQLiteDatabase database2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_transport);
        dataBaseConnection = new busStopsDatabase(getApplicationContext());
        database = dataBaseConnection.open();
        dbHelper = new DatabaseHelper(getApplicationContext());
        database2 = dbHelper.open();
        halt_transport_bus = new ArrayList<>();
        transport_number = new ArrayList<>();
        type = new ArrayList<>();
        route = new ArrayList<>();
        id2 = new ArrayList<>();
        busGrid = (ListView)findViewById(R.id.listTransport);
        halt = getIntent().getExtras().getString("halt");
        String id = getIntent().getExtras().getString("id");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(halt);
        toolbar.setBackgroundColor(getResources().getColor(R.color.near));
        String sql = "select distinct Routes._id,Routes.transport,Routes.TYPE,Routes.NAME from Coordinates,Routes where Coordinates.NAME = '"+halt+"' and Routes.STOPS like '%"+id+"%' and Routes.NAME not like '%АП%' and Routes.NAME not like '%ТП%'";
        busCursor = database.rawQuery(sql,null);
        getValue(busCursor);
        NearTransportAdapter adapter = new NearTransportAdapter(getApplicationContext(),transport_number,type,route,id2);
        busGrid.setAdapter(adapter);
        busGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Tab_time.class);
                intent.putExtra("time", Integer.toString(getId(i,halt)));
                intent.putExtra("type", "N");
                intent.putExtra("station",halt);
                startActivityForResult(intent, 1);
            }
        });
    }

    private int getId(int position,String halt){
        busCursor.moveToPosition(position);
        Cursor transport = database2.rawQuery("select Halt._id from Halt,Transport where Halt.route = '"+busCursor.getString(busCursor.getColumnIndex("NAME"))+"' and Halt.halt_transport = Transport._id and Halt.name = '"+halt+"' and Transport.number = '"+busCursor.getString(busCursor.getColumnIndex("transport"))+"' and Transport.type = '"+busCursor.getString(busCursor.getColumnIndex("TYPE"))+"'",null);
        int id =0;
        if(transport!=null && transport.moveToFirst()){
            id = transport.getInt(transport.getColumnIndex("_id"));
        }
        transport.close();
      return id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tab_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getValue(Cursor cursor){
        dbHelper2 = new DatabaseHelper(getApplicationContext());
        database2 = dbHelper2.open();
        while (busCursor.moveToNext()){
            forAdapter = database2.rawQuery("select  Halt.*,Transport.number,Transport.type from Halt,Transport where Halt.route = '"+cursor.getString(cursor.getColumnIndex("NAME"))+"' and Halt.halt_transport = Transport._id and Halt.name = '"+halt+"' and Transport.number = '"+cursor.getString(cursor.getColumnIndex("transport"))+"' and Transport.type = '"+cursor.getString(cursor.getColumnIndex("TYPE"))+"'",null);
            if (forAdapter.getCount()>0){
                forAdapter.moveToFirst();
                transport_number.add(forAdapter.getString(forAdapter.getColumnIndex("number")));
                type.add(forAdapter.getString(forAdapter.getColumnIndex("type")));
                id2.add(forAdapter.getInt(forAdapter.getColumnIndex("_id")));
                route.add(forAdapter.getString(forAdapter.getColumnIndex("route")));
            }
        }
        forAdapter.close();
    }
}
