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

import br.com.fabricam8.seniorsapp.ExerciseInfoActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.PhysicalEventItemAdaper;
import br.com.fabricam8.seniorsapp.dal.ExerciseDAL;
import br.com.fabricam8.seniorsapp.domain.Exercise;

public class EventsPhysicalActivitiesFragment extends Fragment {

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

        rootView = inflater.inflate(R.layout.pager_events_physical_activities, container, false);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        ExerciseDAL db = ExerciseDAL.getInstance(mContext);
        List<Exercise> exerc = db.findAll();

        PhysicalEventItemAdaper adapter = new PhysicalEventItemAdaper(mContext.getApplicationContext(), exerc);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise entry = (Exercise) parent.getItemAtPosition(position);
               Intent intent = new Intent(mContext, ExerciseInfoActivity.class);

                intent.putExtra("_ID_", entry.getID());
                startActivity(intent);
            }
        });
    }
}
