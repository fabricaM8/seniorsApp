package br.com.fabricam8.seniorsapp.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.adapters.BloodPressureRowItemAdaper;
import br.com.fabricam8.seniorsapp.adapters.GlucosisRowItemAdaper;
import br.com.fabricam8.seniorsapp.dal.BloodPressureDAL;
import br.com.fabricam8.seniorsapp.domain.BloodPressure;

public class BloodPressurePageFragment extends Fragment {

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
        rootView = inflater.inflate(R.layout.pager_health_pressure, container, false);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void load(View v) {
        BloodPressureDAL db = BloodPressureDAL.getInstance(mContext);
        List<BloodPressure> data = db.findAll();

        BloodPressureRowItemAdaper adapter = new BloodPressureRowItemAdaper(mContext.getApplicationContext(), data);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_list));

        GraphView graph = (GraphView) v.findViewById(R.id.graph_blood_pressure);

        if (data != null && data.size() > 1) {
            Date minValue = data.get(0).getDate();
            Date maxValue = minValue;

            DataPoint[] arr1 = new DataPoint[data.size()];
            DataPoint[] arr2 = new DataPoint[data.size()];
            for (int i = 0; i < data.size(); i++) {
                Date d = data.get(i).getDate();
                int sys = data.get(i).getSystolic();
                int dia = data.get(i).getSystolic();
                arr1[i] = new DataPoint(d, sys);
                arr2[i] = new DataPoint(d, dia);

                // se o valor de 'd' for menor, associe-o a minDate
                if (d.compareTo(minValue) == -1)
                    minValue = d;

                // se o valor de d for maior que maxDate, associe-o a maxDate
                if (d.compareTo(maxValue) == 1)
                    maxValue = d;
            }

            LineGraphSeries<DataPoint> s1 = new LineGraphSeries<DataPoint>(arr1);
            s1.setColor(Color.RED);
            graph.addSeries(s1);

            LineGraphSeries<DataPoint> s2 = new LineGraphSeries<DataPoint>(arr2);
            graph.addSeries(s2);

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
        } else {
            graph.setVisibility(View.GONE);
        }
    }
}
