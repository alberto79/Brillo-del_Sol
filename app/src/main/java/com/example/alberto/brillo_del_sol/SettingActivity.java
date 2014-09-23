package com.example.alberto.brillo_del_sol;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alberto on 21/09/14.
 */
public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        getFragmentManager().beginTransaction().add(R.id.containedor,new FragmentSetting()).commit();



    }
    public static class FragmentSetting extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferencias);
            bindPreferenceSumaryToValue(findPreference(getString(R.string.pref_location_key)));



        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue=o.toString();
            preference.setSummary(stringValue);
            return true;
        }
        private void bindPreferenceSumaryToValue(Preference preference)
        {
          preference.setOnPreferenceChangeListener(this);
            onPreferenceChange(preference,getPreferenceManager()
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(),""));
        }
    }

}
