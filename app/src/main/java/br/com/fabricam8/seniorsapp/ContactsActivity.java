package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import br.com.fabricam8.seniorsapp.domain.Contacts;
import br.com.fabricam8.seniorsapp.fragments.EventsContactsFragment;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class ContactsActivity extends ActionBarActivity {
    private Contacts sessionContacts;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
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
    protected void onResume() {
        super.onResume();
    }

    public void viewAddContatos(View v) {
        startActivity(new Intent(ContactsActivity.this, ContactsFormActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help_events_list:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

   private class SeniorTabProvider extends FragmentPagerAdapter {

          private final String[] TABS = {"Contatos"};
//        private final String[] TABS = {"Todos", "Medicamentos", "Atividades", "Consultas"};

       public SeniorTabProvider(FragmentManager supportFragmentManager) {
           super(supportFragmentManager);
       }

       @Override
       public Fragment getItem(int position) {

           switch (position) {
               case 0:
                  return new EventsContactsFragment();
               //case 1:
                   //return new EventsContactsFragment();
                  // return new EventsConsultationFragment();


               //   case 2:
              //     return new EventsConsultationFragment();
//                case 0:
//                    return new EventsAllFragment();
//                case 1:
//                    return new EventsMedicationFragment();
//                case 2:
//                    return new EventsPhysicalActivitiesFragment();
//                case 3:
//                    return new EventsConsultationFragment();
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
