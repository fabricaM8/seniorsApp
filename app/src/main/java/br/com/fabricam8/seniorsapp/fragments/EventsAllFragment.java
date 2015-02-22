package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.com.fabricam8.seniorsapp.R;

public class EventsAllFragment extends Fragment {

    private Activity mContext;

    String[] evts = {};


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

        View rootView = inflater.inflate(R.layout.pager_item, container, false);
        load(rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        ListView listView = (ListView) v.findViewById(R.id.listView);
        ArrayAdapter<String> array = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, evts);
        listView.setAdapter(array);
        listView.setEmptyView(v.findViewById(R.id.empty_list));
    }
}
