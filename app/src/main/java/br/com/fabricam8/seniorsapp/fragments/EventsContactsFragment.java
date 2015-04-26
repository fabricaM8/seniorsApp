package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.dal.ContactsDAL;
import br.com.fabricam8.seniorsapp.domain.Contacts;

public class EventsContactsFragment extends Fragment {

    private Activity mContext;
    private View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        load(rootView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pager_events_contacts, container, false);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        ContactsDAL db = ContactsDAL.getInstance(mContext);
        List<Contacts> evts = db.findAll();
/*
        ConsultationEventItemAdaper adapter = new ConsultationEventItemAdaper(mContext.getApplicationContext(), evts);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Consultation entry =  (Consultation) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, ConsultationInfoActivity.class);
                intent.putExtra("_ID_", entry.getID());
                startActivity(intent);
            }
        });
*/
    }
}
