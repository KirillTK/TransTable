package com.example.kirill.stopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.Map.mapViewer;

public class Main_activity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationBar bottomNavigationBar;

    private BottomNavigationBar.OnTabSelectedListener  mOnNavigationItemSelectedListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
            switch (position){
                case 0: getSupportFragmentManager().beginTransaction().replace(R.id.content, new Bus_fragment()).commit(); toolbar.setBackgroundColor(getResources().getColor(R.color.bus)); break;
                case 1: getSupportFragmentManager().beginTransaction().replace(R.id.content,new Troll_fragment()).commit();toolbar.setBackgroundColor(getResources().getColor(R.color.troll)); break;
                case 2: getSupportFragmentManager().beginTransaction().replace(R.id.content,new Favoutite_fragment()).commit(); toolbar.setBackgroundColor(getResources().getColor(R.color.favorite));break;
                case 3: getSupportFragmentManager().beginTransaction().replace(R.id.content,new mapViewer()).commit();toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); break;
                case 4: getSupportFragmentManager().beginTransaction().replace(R.id.content,new NearHalt()).commit();toolbar.setBackgroundColor(getResources().getColor(R.color.near)); break;
            }

        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

        }
    };

    private void refresh(){
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(3);
        bottomNavigationBar.setBackgroundStyle(1);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.busrapid,null).setActiveColorResource(R.color.bus))
                .addItem(new BottomNavigationItem(R.drawable.troll,null).setActiveColorResource(R.color.troll))
                .addItem(new BottomNavigationItem(R.drawable.ic_star_border_black_24dp,null).setActiveColorResource(R.color.favorite))
               // .addItem(new BottomNavigationItem(R.drawable.ic_map_black_24dp,null).setActiveColorResource(R.color.colorPrimaryDark))
                .addItem(new BottomNavigationItem(R.drawable.ic_place_black_24dp,null).setActiveColorResource(R.color.near))
                .initialise();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        refresh();
        bottomNavigationBar.setTabSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new Bus_fragment()).commitAllowingStateLoss();
    }

}
