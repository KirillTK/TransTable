package com.example.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by work on 16.12.2017.
 */

public class busStopsDatabase extends SQLiteOpenHelper {

    private static String databasePath;
    private  static String databaseName = "Coordinates.db";
    private static final  int SCHEMA = 1;
    static final String tableName = "Coordinates";
    static final String tableNameRoutes = "Routes";
    static final String tableNameShapes = "Shapes";

    static final String stopID = "ID";
    static final String stopName = "NAME";
    static final String stopLat = "LONG";
    static final String stopLong = "LAT";

    static final String ShapeID = "ID";
    static final String ShapeString = "Shape";

    static final String RouteIDMain = "ID";
    static final String RouteID = "ROUTEID";

    private Context myContext;


    public busStopsDatabase(Context context) {
        super(context, databaseName, null, SCHEMA);
        this.myContext = context;
        databasePath = context.getFilesDir().getPath() +databaseName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;

        try{
            File file = new File(databasePath);
            if(!file.exists()){
                this.getReadableDatabase();
                myInput = myContext.getAssets().open(databaseName);
                String outFileName = databasePath;
                myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = myInput.read(buffer))>0){
                    myOutput.write(buffer,0,length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }

        }catch (IOException ex){
            Log.d("busStopsDatabase:",ex.getMessage());
        }
    }
    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(databasePath,null,SQLiteDatabase.OPEN_READONLY);
    }
}
