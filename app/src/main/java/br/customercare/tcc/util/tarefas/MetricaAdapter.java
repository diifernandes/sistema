package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Metric;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class MetricaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Metric> metrics = new ArrayList<Metric>();

    //Constructor

    public MetricaAdapter(Context context, ArrayList<Metric> metrics) {
        this.context = context;
        this.metrics = metrics;
    }

    @Override
    public int getCount() {
        return metrics.size();
    }

    @Override
    public Object getItem(int position) {
        return metrics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(metrics.get(position).getName());
        v.setTag(metrics.get(position).getId());
        return v;
    }
}
