package com.example.alberto.brillo_del_sol;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            {
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ForecastFragment extends Fragment {

        private ArrayAdapter<String> adapter;

        public ForecastFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.forecastfragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_refresh) {
                FetchWetherTask fetchWetherTask = new FetchWetherTask();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String localidad = preferences.getString(getString(R.string.pref_location_key)
                        , getString(R.string.pref_location_default));


                fetchWetherTask.execute(localidad);

                return true;
            }


            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            String[] items = new String[]{"hoy-soleado-88/33", "Lunes-nuboso-44/00", "Martes-sol-33/77", "Jueves-tormenta-88/9", "Sabado-huracan-55/22"};

            List<String> tiempo_semanal = new ArrayList<String>(Arrays.asList(items));
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_textView, tiempo_semanal);
            ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapter.getItem(i);
                    Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), DetalleElemento.class).putExtra(Intent.EXTRA_TEXT, item));
                }
            });

            return rootView;
        }


        public class FetchWetherTask extends AsyncTask<String, Void, String[]> {

            private final String TAG = FetchWetherTask.class.getSimpleName();

            @Override
            protected String[] doInBackground(String... params) {
                BufferedReader br = null;
                HttpURLConnection httpURLConnection = null;
                String jsonString = null;
                InputStream inputStream = null;
                String mode = "json";
                String units = "metric";
                int cnt = 7;
                try {
                    final String base_uri = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                    final String QUERY_PARAM = "q";
                    final String MODE_PARAM = "mode";
                    final String UNITS_PARAM = "units";
                    final String DAYS_PARAM = "cnt";

                    Uri buildUri = Uri.parse(base_uri).buildUpon().appendQueryParameter(QUERY_PARAM, params[0])
                            .appendQueryParameter(MODE_PARAM, mode).appendQueryParameter(UNITS_PARAM, units)
                            .appendQueryParameter(DAYS_PARAM, Integer.toString(cnt)).build();

                    URL url = new URL(buildUri.toString());
                    Log.e(TAG, "BUILD_URI" + buildUri.toString());

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    String message = httpURLConnection.getResponseMessage();

                    inputStream = httpURLConnection.getInputStream();

                    br = new BufferedReader(new InputStreamReader(inputStream));


// leer linea por linea
                    String l = null;
                    StringBuilder sb = new StringBuilder();


                    while ((l = br.readLine()) != null)
                        sb.append(l);
// convertir stringbuilder a stirng
                    jsonString = sb.toString();
// parsear el string como jsonarr o jsonobject, dependiendo

                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                    if (br != null)
                        try {
                            br.close();
                        } catch (IOException e) {
                            Log.w(TAG, "error closing bufer");
                        }

                }

                if (inputStream == null)
                    return null;
                try {
                    return getWeatherDataFromJson(jsonString, cnt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String[] lista) {

                super.onPostExecute(lista);
                if (lista != null)
                    adapter.clear();
                for (String dias : lista) {
                    adapter.add(dias);
                }


            }
        }

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

// These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DATETIME = "dt";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            String[] resultStrs = new String[numDays];
            for (int i = 0; i < weatherArray.length(); i++) {
// For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

// Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

// The date/time is returned as a long. We need to convert that
// into something human-readable, since most people won't read "1400356800" as
// "this saturday".
                long dateTime = dayForecast.getLong(OWM_DATETIME);
                day = getReadableDateString(dateTime);

// description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

// Temperatures are in a child object called "temp". Try not to name variables
// "temp" when working with temperature. It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            return resultStrs;
        }


        private String getReadableDateString(long time) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMMMM d");
            return format.format(date).toString();
        }

        private String formatHighLows(double high, double low) {
// For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }
    }


}



