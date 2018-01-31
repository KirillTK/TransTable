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
import java.util.List;

public class NearTransport extends AppCompatActivity {
    private Toolbar toolbar;
    private Cursor busCursor;
    private List<String> halt_transport_bus;
    SQLiteDatabase database;
    busStopsDatabase dataBaseConnection;
    private ListView busGrid;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_transport);
        dataBaseConnection = new busStopsDatabase(getApplicationContext());
        database = dataBaseConnection.open();
        dbHelper = new DatabaseHelper(getApplicationContext());
        database2 = dbHelper.open();
        halt_transport_bus = new ArrayList<>();
        busGrid = (ListView)findViewById(R.id.listTransport);
        final String halt = getIntent().getExtras().getString("halt");
        String id = getIntent().getExtras().getString("id");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(halt);
        toolbar.setBackgroundColor(getResources().getColor(R.color.near));
        String sql = "select distinct Routes._id,Routes.transport,Routes.TYPE,Routes.NAME from Coordinates,Routes where Coordinates.NAME = '"+halt+"' and Routes.STOPS like '%"+id+"%' and Routes.NAME not like '%АП%' and Routes.NAME not like '%ТП%'";
        busCursor = database.rawQuery(sql,null);
        NearTransportAdapter adapter = new NearTransportAdapter(getApplicationContext(),busCursor,halt);
        busGrid.setAdapter(adapter);
        busGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Tab_time.class);
                intent.putExtra("time", Integer.toString(getId(i,halt)));
                intent.putExtra("type", "N");
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

        getMenuInflater().inflate(R.menu.menu_halt, menu);
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
}
