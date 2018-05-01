package com.example.Map;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.kirill.stopping.R;

public class MapActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String key = getIntent().getExtras().getString("number");
        getSupportActionBar().setTitle(key);
        setColorSystemBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setColorSystemBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            String type = getIntent().getExtras().getString("type");
            switch (type){
                case "A": window.setStatusBarColor(getResources().getColor(R.color.mapColor)); toolbar.setBackgroundColor(getResources().getColor(R.color.bus)); break;
                case "Т": window.setStatusBarColor(getResources().getColor(R.color.systemBar2));toolbar.setBackgroundColor(getResources().getColor(R.color.troll)); break;
            }
        }
    }
}
