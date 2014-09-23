package com.example.alberto.brillo_del_sol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

import java.text.SimpleDateFormat;

/**
 * Dado el String devuelto por la apiwhether y un index del dia
 * esta clase detorna la maxima temperatura para el dia indicado en el index
 */
public class WetherDateParser {

    public static double devuelveTemperaturaMaxima(String jsonString, int diaIndex) throws JSONException {

        JSONObject obj = new JSONObject(jsonString);
        JSONArray tiempo = obj.getJSONArray("list");
        JSONObject informacionDia = tiempo.getJSONObject(diaIndex);
        JSONObject infoTemperatura = informacionDia.getJSONObject("temp");
        return infoTemperatura.getDouble("max");


    }




}
