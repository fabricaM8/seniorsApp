package br.com.fabricam8.seniorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.domain.Medication;


/**
 * Created by Aercio on 2/12/15.
 */
public class MedicationEventItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Medication> mMeds;

    public MedicationEventItemAdaper(Context context, List<Medication> medications) {
        super(context, R.layout.pager_events_medication_list_item, medications);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mMeds = medications;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_events_medication_list_item, null, true);

        TextView txtName = (TextView) row.findViewById(R.id.med_item_name);
        txtName.setText(mMeds.get(position).getName());

        TextView txtDesc = (TextView) row.findViewById(R.id.med_item_desc);
        txtDesc.setText(mMeds.get(position).toString());

        return row;
    }
}
