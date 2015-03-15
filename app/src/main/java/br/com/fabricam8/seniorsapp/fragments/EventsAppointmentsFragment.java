package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.AppointmentsEventItemAdaper;
import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;

public class EventsAppointmentsFragment extends Fragment {

    private Activity mContext;

 //   String[] evts = {"Dr. João Calado"};


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pager_appointments_fragmente_activities, container, false);
        load(rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        ConsultationDAL db = ConsultationDAL.getInstance(mContext);
        List<Consultation> lstEvts = db.findAll();

        String[] evts = new String[lstEvts.size()];
        for (int i = 0; i < lstEvts.size(); i++) {
            evts[i] = lstEvts.get(i).getName();
        }

        AppointmentsEventItemAdaper adapter = new AppointmentsEventItemAdaper(mContext.getApplicationContext(), Arrays.asList(evts));
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));
    }
}
