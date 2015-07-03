package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import br.com.fabricam8.seniorsapp.fragments.EventsGlicoseFragment;
import br.com.fabricam8.seniorsapp.fragments.WeightPageFragment;
import br.com.fabricam8.seniorsapp.fragments.BloodPressurePageFragment;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class HealthListActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.activity_health_list);

        // create toolbar
        mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_orange));

        // Initialize the ViewPager and set an adapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SeniorTabProvider(getSupportFragmentManager()));

        // Bind the mTabs to the ViewPager
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
    }

    public void viewAddWeightMeasure(View v) {
        startActivity(new Intent(HealthListActivity.this, WeightFormActivity.class));
    }

    public void viewAddBloodPreasureMeasure(View v) {
        startActivity(new Intent(HealthListActivity.this, BloodPressureFormActivity.class));
    }

    public void viewAddGlucosisMeasure(View v) {
        startActivity(new Intent(HealthListActivity.this, GlucosisFormActivity.class));
    }

    public void viewAddBatimento(View v) {
        startActivity(new Intent(HealthListActivity.this, BatimentoFormActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Tab adapter
     */
    private class SeniorTabProvider extends FragmentPagerAdapter {

        private final String[] TABS = {"Peso", "Pressão Arterial", "Glicose"};//, "Batimentos Cardiacos"};

        public SeniorTabProvider(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new WeightPageFragment();
                case 1:
                    return new BloodPressurePageFragment();
                case 2:
                    return new EventsGlicoseFragment();
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
