package com.example.Time;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.kirill.stopping.DatabaseHelper;
import com.example.kirill.stopping.R;


public class Tab_time extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    Toolbar toolbar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setToolbar();
    }


    private void setToolbar(){
        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.open();
        setContentView(R.layout.activity_tab_time);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.busstop);
        String a = getIntent().getExtras().getString("time");
        String type = getIntent().getExtras().getString("type");
//        Cursor stoping = database.rawQuery("select * from Halt where  _id = " + a ,null);
//        stoping.moveToFirst();
//        String name =stoping.getString(stoping.getColumnIndex(DatabaseHelper.COLUMN_NAME));
//        getSupportActionBar().setTitle("    "+name);
//        stoping.close();
        String station = getIntent().getExtras().getString("station");
        getSupportActionBar().setTitle(station);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        AppBarLayout appBarHalt = (AppBarLayout)findViewById(R.id.appbar);
        setColorToolbar(appBarHalt,toolbar,tabLayout,type);
    }

    private void setColorToolbar(AppBarLayout appBarLayout , Toolbar toolbar , TabLayout tabLayout,String type){
        switch (type){
            case "A":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.bus));
                toolbar.setBackgroundColor(getResources().getColor(R.color.bus));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.bus));
                break;
            case "Т":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.troll));
                toolbar.setBackgroundColor(getResources().getColor(R.color.troll));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.troll));
                break;

            case "F":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.favorite));
                toolbar.setBackgroundColor(getResources().getColor(R.color.favorite));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.favorite));
                break;

            case "N":
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.near));
                toolbar.setBackgroundColor(getResources().getColor(R.color.near));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.near));
                break;
        }

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (item.getItemId()){

            case R.id.switcher:
                Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

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
                    Time_Weekday tab1 = new Time_Weekday();
                    return tab1;
                case 1:
                    Time_Holliday tab2 = new Time_Holliday();
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Будний день";
                case 1:
                    return "Выходные";
            }
            return null;
        }
    }


}
