package com.example.Halt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.Map.MapActivity;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;

public class Halt extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    Toolbar toolbar;
    public String key;
    public String type;
    private String number;
    public String Route;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halt);
        setToolbar();
    }

    private  void setToolbar(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.open();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.busprofile1);
        key = getIntent().getExtras().getString("number");
        type = getIntent().getExtras().getString("type");
        Log.d("type",type);
        Cursor busNumber = database.rawQuery("select * from "+ DatabaseHelper.TABLE_1+" where "+ DatabaseHelper.BUS_COLUMN_ID + " = "+ key ,null);
        Cursor stationRoute = database.rawQuery("select route from Halt where halt_transport = '"+key+"' and number_route = 'Первый путь'",null);
        busNumber.moveToFirst();
        stationRoute.moveToFirst();
        Route = stationRoute.getString(stationRoute.getColumnIndex("route"));
        number = busNumber.getString(busNumber.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
        getSupportActionBar().setTitle(" "+number);
        busNumber.close();
        stationRoute.close();
        tabLayout.getTabAt(0).setText(Route);
        stationRoute = database.rawQuery("select route from Halt where halt_transport = '"+key+"' and number_route = 'Второй путь'",null);
        if(stationRoute.getCount()!=0){
            stationRoute.moveToFirst();
            Route = stationRoute.getString(stationRoute.getColumnIndex("route"));
            tabLayout.getTabAt(1).setText(Route);
            stationRoute.close();
        }
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        AppBarLayout appBarHalt = (AppBarLayout)findViewById(R.id.appbar);
        setColorToolbar(appBarHalt,toolbar,tabLayout);
    }

    private void setColorToolbar(AppBarLayout appBarLayout , Toolbar toolbar , TabLayout tabLayout){
        switch (type){
            case "A":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.bus));
                toolbar.setBackgroundColor(getResources().getColor(R.color.bus));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.bus));
                setColorSystemBar(getResources().getColor(R.color.mapColor));
                break;
            case "Т":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.troll));
                toolbar.setBackgroundColor(getResources().getColor(R.color.troll));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.troll));
                setColorSystemBar(getResources().getColor(R.color.systemBar2));
                break;

            case "N":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.near));
                toolbar.setBackgroundColor(getResources().getColor(R.color.near));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.near));
                setColorSystemBar(getResources().getColor(R.color.systemBar4));
                break;
        }
    }

    public void setColorSystemBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_halt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home: finish(); break;
            case R.id.maps:
                Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                intent.putExtra("number",number);
                intent.putExtra("type",type);
                startActivityForResult(intent,1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return null;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    RouteOne tab1 = new RouteOne();
                    return tab1;
                case 1:
                    RouteTwo tab2 = new RouteTwo();
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
