package br.com.fabricam8.seniorsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Aercio on 2/3/15.
 */
public class CustomAdapter<T> extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<T> mObjects;

    public CustomAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mObjects = Arrays.asList(objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.custom_spinner, parent, false);
        TextView label = (TextView) row.findViewById(R.id.cs_spn_text);
        label.setText(mObjects.get(position).toString());

        return row;
    }

    public void setError(View v, CharSequence s) {
        TextView name = (TextView) v.findViewById(R.id.cs_spn_text);
        name.setError(s);
    }
}
