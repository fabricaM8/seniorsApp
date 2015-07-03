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
import br.com.fabricam8.seniorsapp.WeightFormActivity;
import br.com.fabricam8.seniorsapp.dal.WeightDAL;
import br.com.fabricam8.seniorsapp.domain.Weight;


/**
 * Created by Aercio on 2/12/15.
 */
public class WeightRowItemAdaper extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Weight> mWeights;

    public WeightRowItemAdaper(Context context, List<Weight> weights) {
        super(context, R.layout.pager_health_weight_list_item, weights);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWeights = weights;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.pager_health_weight_list_item, null, true);

        TextView txtVal = (TextView) row.findViewById(R.id.health_weight_val);
        txtVal.setText(mWeights.get(position).getValue() + "");

        TextView txtDate = (TextView) row.findViewById(R.id.health_weight_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText(dateFormat.format(mWeights.get(position).getDate()));

        Button but = (Button) row.findViewById(R.id.btDeleteWeight);
        if (but != null) {
            but.setTag(mWeights.get(position).getID());
            but.setOnClickListener(new AdapterView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long id = Long.parseLong(v.getTag().toString());

                    WeightDAL db = WeightDAL.getInstance(mContext);
                    Weight w = db.findOne(id);
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
