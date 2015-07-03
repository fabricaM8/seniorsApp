package br.com.fabricam8.seniorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.dal.GlucosisDAL;
import br.com.fabricam8.seniorsapp.domain.Glucosis;


/**
 * Created by Aercio on 2/12/15.
 */
public class GlucosisRowItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Glucosis> mGlucosis;

    public GlucosisRowItemAdaper(Context context, List<Glucosis> glucosis) {
        super(context, R.layout.pager_health_glucosis_list_item, glucosis);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mGlucosis = glucosis;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_health_glucosis_list_item, null, true);

        TextView txtVal = (TextView) row.findViewById(R.id.health_glucosis_val);
        txtVal.setText(mGlucosis.get(position).getRate() + "");

        TextView txtDate = (TextView) row.findViewById(R.id.health_glucosis_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText(dateFormat.format(mGlucosis.get(position).getDate()));

        Button but = (Button) row.findViewById(R.id.btDeleteGlucosis);
        if (but != null) {
            but.setTag(mGlucosis.get(position).getID());
            but.setOnClickListener(new AdapterView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long id = Long.parseLong(v.getTag().toString());

                    GlucosisDAL db = GlucosisDAL.getInstance(mContext);
                    Glucosis w = db.findOne(id);
                    if(w != null) {
                        db.remove(w);
                    }
                    notifyDataSetChanged();
                }
            });

        }

        return row;
    }
}
