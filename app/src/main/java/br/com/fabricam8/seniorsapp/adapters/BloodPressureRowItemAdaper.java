package br.com.fabricam8.seniorsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.fabricam8.seniorsapp.HealthListActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.dal.BloodPressureDAL;
import br.com.fabricam8.seniorsapp.domain.BloodPressure;


/**
 * Created by Aercio on 2/12/15.
 */
public class BloodPressureRowItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BloodPressure> mBloodPressure;

    public BloodPressureRowItemAdaper(Context context, List<BloodPressure> bloodPressure) {
        super(context, R.layout.pager_health_pressure_list_item, bloodPressure);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mBloodPressure = bloodPressure;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_health_pressure_list_item, null, true);

        TextView txtVal = (TextView) row.findViewById(R.id.health_pressure_val);
        txtVal.setText(mBloodPressure.get(position).getSystolic() + " x " +
                mBloodPressure.get(position).getDiastolic());

        TextView txtDate = (TextView) row.findViewById(R.id.health_pressure_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        txtDate.setText(dateFormat.format(mBloodPressure.get(position).getDate()));

        Button but = (Button) row.findViewById(R.id.btDeleteBloodPressure);
        if (but != null) {
            but.setTag(mBloodPressure.get(position).getID());
            but.setOnClickListener(new AdapterView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long id = Long.parseLong(v.getTag().toString());

                    BloodPressureDAL db = BloodPressureDAL.getInstance(mContext);
                    BloodPressure w = db.findOne(id);
                    if(w != null) {
                        db.remove(w);
                    }

                    Intent i = new Intent(mContext, HealthListActivity.class);
                    i.putExtra(HealthListActivity.BUNDLE_KEY, 1);
                    mContext.startActivity(i);
                    // close this activity
                    ((Activity) mContext).finish();
                }
            });

        }

        return row;
    }
}
