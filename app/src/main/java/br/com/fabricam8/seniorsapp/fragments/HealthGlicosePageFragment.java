package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.MedicationInfoActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.GlucosisRowItemAdaper;
import br.com.fabricam8.seniorsapp.adapters.MedicationEventItemAdaper;
import br.com.fabricam8.seniorsapp.adapters.WeightRowItemAdaper;
import br.com.fabricam8.seniorsapp.dal.GlucosisDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Glucosis;
import br.com.fabricam8.seniorsapp.domain.Medication;

public class HealthGlicosePageFragment extends Fragment {

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
        rootView = inflater.inflate(R.layout.pager_health_glucosis, container, false);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        GlucosisDAL db = GlucosisDAL.getInstance(mContext);
        List<Glucosis> data = db.findAll();

        GlucosisRowItemAdaper adapter = new GlucosisRowItemAdaper(mContext, data);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));

        GraphView graph = (GraphView) v.findViewById(R.id.graph_glucosis);

        if (data != null && data.size() > 1) {
            Date minValue = data.get(0).getDate();
            Date maxValue = minValue;

            DataPoint[] arr = new DataPoint[data.size()];
            for (int i = 0; i < arr.length; i++) {
                Date d = data.get(i).getDate();
                float val = data.get(i).getRate();
                arr[i] = new DataPoint(d, val);

                // se o valor de 'd' for menor, associe-o a minDate
                if (d.compareTo(minValue) == -1)
                    minValue = d;

                // se o valor de d for maior que maxDate, associe-o a maxDate
                if (d.compareTo(maxValue) == 1)
                    maxValue = d;
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(arr);
            graph.addSeries(series);

            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mContext));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3);

            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(minValue.getTime());
            graph.getViewport().setMaxX(maxValue.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setBackgroundColor(Color.argb(50, 255, 165, 0));
            graph.getGridLabelRenderer().setGridColor(Color.argb(255, 255, 165, 0));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.argb(255, 255, 165, 0));
            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.argb(255, 255, 165, 0));
            graph.setVisibility(View.VISIBLE);
        } else {
            graph.setVisibility(View.GONE);
        }
    }
}
