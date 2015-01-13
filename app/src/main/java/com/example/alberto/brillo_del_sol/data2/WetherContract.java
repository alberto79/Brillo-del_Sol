package com.example.alberto.brillo_del_sol.data2;


import android.provider.BaseColumns;

/**
 * Created by root on 10/01/15.
 */
public class WetherContract {

    public static class WetherEntry implements BaseColumns
    {
        public static final String TABLANOMBRE="tiempo";
        public static final String DESCRIPCION="descripcion";
        public static final String MAX_TEMP="maximas";
        public static final String MIN_TEMP="minimas";
        public static final String COLUMN_LOCK_KEY="lock_key";
        public static final String COLUMN_DATATEXT="date";
        public static final String WETHER_ID="wether_id";
        public static final String  COLUMN_HUMIDITY="humidity";
        public static final String PRESURE="presure";


    }
    public static class LocationEntry implements BaseColumns
    {
        public static final String TABLANOMBRE="localizacion";
        public static final String COLUMN_LOCATION_SETTING="location_setting";
        public static final String COLUMN_CITY_NAME="city_name";
        public static final String COLUMN_CORD_LAT="latitud";
        public static final String COLUMN_CORD_LON="longitud";
    }






}
