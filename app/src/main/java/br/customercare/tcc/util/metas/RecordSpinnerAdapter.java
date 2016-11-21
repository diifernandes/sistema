package br.customercare.tcc.util.metas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.RecordType;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by Fernando on 19/10/2016.
 */
public class RecordSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RecordType> rt = new ArrayList<RecordType>();

    //Constructor

    public RecordSpinnerAdapter(Context context, ArrayList<RecordType> rt) {
        this.context = context;
        this.rt = rt;
    }

    @Override
    public int getCount() {
        return rt.size();
    }

    @Override
    public Object getItem(int position) {
        return rt.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(rt.get(position).getName());
        v.setTag(rt.get(position).getId());
        return v;
    }
}
