package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.fabricam8.seniorsapp.ConsultationInfoActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.ConsultationEventItemAdaper;
import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;

public class EventsConsultationFragment extends Fragment {

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
        rootView = inflater.inflate(R.layout.pager_events_consultation, container, false);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        ConsultationDAL db = ConsultationDAL.getInstance(mContext);
        List<Consultation> evts = db.findAll();

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

    }
}
