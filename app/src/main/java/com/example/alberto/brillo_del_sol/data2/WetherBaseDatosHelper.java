package com.example.alberto.brillo_del_sol.data2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.alberto.brillo_del_sol.data2.WetherContract;

/**
 * Created by root on 11/01/15.
 */
public class WetherBaseDatosHelper extends SQLiteOpenHelper{

     public static final String DATABASE_NAME="whether_db";

     public static final int DATABASE_VERSION=1;
    public WetherBaseDatosHelper(Context context ) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     final  String CREATE_WEATHER_TABLE="create table"+ WetherContract.WetherEntry.TABLANOMBRE+
             "("+WetherContract.WetherEntry._ID+"PRIMARY_KEY_AUTOINCREMENT,"+ WetherContract.WetherEntry.COLUMN_LOCK_KEY
             +"INTEGER NOT NULL,"+ WetherContract.WetherEntry.COLUMN_DATATEXT+" TEXT NOT NULL,"+
             WetherContract.WetherEntry.MAX_TEMP+" REAL NOT NULL,"+
             WetherContract.WetherEntry.MIN_TEMP+" REAL NOT NULL,"+
             WetherContract.WetherEntry.COLUMN_HUMIDITY+" REAL NOT NULL,"+
             WetherContract.WetherEntry.PRESURE+" REAL NOT NULL,"+
             WetherContract.WetherEntry.WETHER_ID+"INTEGER NOT NULL,"+
             "FOREIGN KEY ("+ WetherContract.WetherEntry.COLUMN_LOCK_KEY+ ") REFERENCES"+
             WetherContract.LocationEntry.TABLANOMBRE+
              "("
                +WetherContract.LocationEntry._ID+"),"
             //asegura una entrada de tiempo por localizacion por dia
               + " UNIQUE ("+ WetherContract.WetherEntry.COLUMN_DATATEXT+","+
                             WetherContract.WetherEntry.COLUMN_LOCK_KEY+")ON CONFLICT REPLACE;";
        sqLiteDatabase.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
