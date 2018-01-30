package com.example.kirill.stopping;

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

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH;
    private  static String DB_NAME = "stop.db";
    private static final  int SCHEMA = 1;
    public static final String TABLE_1 = "Transport";
    public static final String BUS_COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TYPE = "type";
    static final String TABLE_2 = "Halt";
    static final String HALT_COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    static final String HALT_ID_BUS = "halt_transport";
    static final String ROUTE = "route";
    static final String NUMBER_ROUTE = "number_route";
    static final String TABLE_4 = "Time";
    static final String TIME_COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    static final String TIME_ID_HALT = "time_halt";
    static final String TABLE_5 = "Time_hollyday";
    static final String TIMEh_COLUMN_ID = "_id";
    public static final String COLUMNh_TIME = "time";
    static final String TIMEh_ID_HALT = "time_halt";



    private Context myContext;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() +DB_NAME;
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
            File file = new File(DB_PATH);
            if(!file.exists()){
                this.getReadableDatabase();
                myInput = myContext.getAssets().open(DB_NAME);
                String outFileName = DB_PATH;
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
            Log.d("DatabaseHelper",ex.getMessage());
        }
    }
    public SQLiteDatabase open() throws SQLException{
        return SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READONLY);
    }
}
