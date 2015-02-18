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

import br.com.fabricam8.seniorsapp.MedicationInfoActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.MedicationEventItemAdaper;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Medication;

public class EventsMedicationFragment extends Fragment {

    private Activity mContext;

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

        View rootView = inflater.inflate(R.layout.pager_events_medication, container, false);
        load(rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        MedicationDAL db = MedicationDAL.getInstance(mContext);
        List<Medication> meds = db.findAll();

        MedicationEventItemAdaper adapter = new MedicationEventItemAdaper(mContext.getApplicationContext(), meds);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medication entry =  (Medication) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, MedicationInfoActivity.class);
                intent.putExtra("_ID_", entry.getID());
                startActivity(intent);
            }
        });

    }
}
