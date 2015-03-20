package br.com.fabricam8.seniorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.domain.Exercise;


/**
 * Created by Aercio on 2/12/15.
 */
public class PhysicalEventItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Exercise> mExercises;

    public PhysicalEventItemAdaper(Context context, List<Exercise> exercises) {
        super(context, R.layout.pager_events_physical_list_item, exercises);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mExercises = exercises;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_events_physical_list_item, null, true);

        TextView txtName = (TextView) row.findViewById(R.id.exercise_item_name);
        txtName.setText(mExercises.get(position).getKeyType());



        return row;
    }
}
