package br.com.fabricam8.seniorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fabricam8.seniorsapp.R;


/**
 * Created by Aercio on 2/12/15.
 */
public class AppointmentsEventItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mItems;

    public AppointmentsEventItemAdaper(Context context, List<String> items) {
        super(context, R.layout.pager_events_appointments_list_item, items);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.pager_events_appointments_list_item, null, true);

        TextView txtName = (TextView) row.findViewById(R.id.appointments_item_name);
        txtName.setText(mItems.get(position));

        return row;
    }
}
