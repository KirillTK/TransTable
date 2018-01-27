package com.example.kirill.stopping;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.Adapters.BusListAdapter;
import com.example.Time.Tab_time;
import java.util.ArrayList;
import java.util.List;

public class NearTransport extends AppCompatActivity {
    private Toolbar toolbar;
    private Cursor busCursor;
    private Cursor trollCursor;
    private List<String> halt_transport_bus;
    private List<String> halt_transport_troll;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    private GridView busGrid;
    private GridView trollGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_transport);
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.open();
        halt_transport_bus = new ArrayList<>();
        halt_transport_troll = new ArrayList<>();
        busGrid = (GridView)findViewById(R.id.busGrid);
        trollGrid = (GridView)findViewById(R.id.trollGrid);
        String halt = getIntent().getExtras().getString("halt");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(halt);
        toolbar.setBackgroundColor(getResources().getColor(R.color.near));
        String sql = "select distinct number,type,Halt._id from  Transport,Halt where name ='"+ halt +"' and Transport._id = Halt.halt_transport and Transport.type = 'A'";
        String sql2 = "select distinct * from  Transport,Halt where name ='"+ halt +"' and Transport._id = Halt.halt_transport and Transport.type = 'Т'";
        busCursor = database.rawQuery(sql,null);
        trollCursor = database.rawQuery(sql2,null);
        BusListAdapter adapter = new BusListAdapter(getApplicationContext(), busCursor);
        BusListAdapter adapter2 = new BusListAdapter(getApplicationContext(), trollCursor);
        busGrid.setAdapter(adapter);
        halt_transport_bus = getId(busCursor,halt_transport_bus,sql);
        halt_transport_troll = getId(trollCursor,halt_transport_troll,sql2);
        busGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Tab_time.class);
                intent.putExtra("time", halt_transport_bus.get(i));
                intent.putExtra("type", "N");
                startActivityForResult(intent, 1);
            }
        });
        trollGrid.setAdapter(adapter2);
        trollGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Tab_time.class);
                intent.putExtra("time", halt_transport_troll.get(i));
                intent.putExtra("type", "N");
                startActivityForResult(intent, 1);
            }
        });
    }

    private List<String> getId(Cursor cursor, List<String> id ,String sql){
        cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            id.add(cursor.getString((cursor.getColumnIndex("_id"))));
        }
        cursor.close();
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
