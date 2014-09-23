package com.example.alberto.brillo_del_sol;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alberto on 20/09/14.
 */
public class DetalleElemento extends Activity {

    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_layout);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.contenedor, new DetalleElemento_fragment())
                    .commit();
        }

        /*textView=(TextView)findViewById(R.id.list_item_textView);
        textView.setText(item.toString());*/


    }


    public static class DetalleElemento_fragment extends Fragment {
        public DetalleElemento_fragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
           inflater.inflate(R.menu.my,menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id=item.getItemId();
            if(id==R.id.action_settings)
                startActivity(new Intent(getActivity(),SettingActivity.class));
             return true;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView=inflater.inflate(R.layout.fragment_detalle,container,false);
            String texto=getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            TextView textView=(TextView)rootView.findViewById(R.id.texto_detalle);
            textView.setText(texto);

            return rootView;

        }
    }
}