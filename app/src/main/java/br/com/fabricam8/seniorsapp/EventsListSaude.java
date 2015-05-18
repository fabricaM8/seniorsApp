package br.com.fabricam8.seniorsapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import br.com.fabricam8.seniorsapp.fragments.EventsBatimentoCardiocoFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsGlicoseFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsPesoFragment;
import br.com.fabricam8.seniorsapp.fragments.EventsPressaoArterialFragment;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class EventsListSaude extends ActionBarActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saude_list);

        // create toolbar
        mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // Initialize the ViewPager and set an adapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SeniorTabProvider(getSupportFragmentManager()));

        // Bind the mTabs to the ViewPager
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
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
