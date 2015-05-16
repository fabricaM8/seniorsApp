package br.com.fabricam8.seniorsapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.fabricam8.seniorsapp.fragments.EventsBatimentoCardiocoFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsGlicoseFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsPesoFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsPressaoArterialFragment;


public class EventsListSaude extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saude_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_saude_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Tab adapter
     */
    private class SeniorTabProvider extends FragmentPagerAdapter {

        private final String[] TABS = {"Peso", "Pressão Arterial", "Glicose","Batimentos Cardiacos"};
//        private final String[] TABS = {"Todos", "Medicamentos", "Atividades", "Consultas"};

        public SeniorTabProvider(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new EventsPesoFragment();
                case 1:
                    return new EventsPressaoArterialFragment();
                case 2:
                    return new EventsGlicoseFragment();
                case 3:
                    return new EventsBatimentoCardiocoFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABS[position];
        }
    }
}
