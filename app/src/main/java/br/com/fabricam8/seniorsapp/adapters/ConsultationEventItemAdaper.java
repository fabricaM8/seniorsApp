package br.com.fabricam8.seniorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.domain.Consultation;



/**
 * Created by Aercio on 2/12/15.
 */
public class ConsultationEventItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Consultation> mConsultation;

    public ConsultationEventItemAdaper(Context context, List<Consultation> consultation) {
        super(context, R.layout.pager_events_consultation_list_item, consultation);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mConsultation = consultation;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_events_consultation_list_item, null, true);

        TextView txtName = (TextView) row.findViewById(R.id.consultation_item_name);
        txtName.setText(mConsultation.get(position).toString());

        return row;
    }
}
