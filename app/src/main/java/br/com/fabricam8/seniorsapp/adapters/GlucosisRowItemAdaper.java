package br.com.fabricam8.seniorsapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.fabricam8.seniorsapp.HealthListActivity;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        txtDate.setText(dateFormat.format(mGlucosis.get(position).getDate()));

        Button but = (Button) row.findViewById(R.id.btDeleteGlucosis);
        if (but != null) {
            but.setTag(mGlucosis.get(position).getID());
            but.setOnClickListener(new AdapterView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final long id = Long.parseLong(v.getTag().toString());

                    new AlertDialog.Builder(mContext)
                            .setTitle("Confirme")
                            .setMessage("Deseja realmente deletar o registro?")
                            .setIcon(R.drawable.ic_action_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    GlucosisDAL db = GlucosisDAL.getInstance(mContext);
                                    Glucosis w = db.findOne(id);
                                    if (w != null) {
                                        if (db.remove(w) > 0) {
                                            Toast.makeText(mContext, mContext.getString(R.string.success_pressure_deletion), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(mContext, mContext.getString(R.string.fail_health_deletion), Toast.LENGTH_LONG).show();
                                        }
                                        Intent i = new Intent(mContext, HealthListActivity.class);
                                        i.putExtra(HealthListActivity.BUNDLE_KEY, 2);
                                        mContext.startActivity(i);
                                        // close this activity
                                        ((Activity) mContext).finish();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

        }

        return row;
    }
}
