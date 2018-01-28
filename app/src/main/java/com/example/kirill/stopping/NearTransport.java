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
    private List<String> halt_transport_bus;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    private GridView busGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_transport);
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.open();
        halt_transport_bus = new ArrayList<>();
        busGrid = (GridView)findViewById(R.id.busGrid);
        String halt = getIntent().getExtras().getString("halt");
        String id = getIntent().getExtras().getString("id");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(halt);
        toolbar.setBackgroundColor(getResources().getColor(R.color.near));
        String sql = "select distinct Routes.ID,Routes.TYPE from Coordinates,Routes where Coordinates.NAME = '"+halt+"' and Routes.STOPS like '%"+id+"%'";
        busCursor = database.rawQuery(sql,null);
        BusListAdapter adapter = new BusListAdapter(getApplicationContext(), busCursor);
        busGrid.setAdapter(adapter);
        halt_transport_bus = getId(busCursor,halt_transport_bus,sql);
        busGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Tab_time.class);
                intent.putExtra("time", halt_transport_bus.get(i));
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
