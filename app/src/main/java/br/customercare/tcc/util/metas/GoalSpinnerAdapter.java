package br.customercare.tcc.util.metas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Goal;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by Fernando on 19/10/2016.
 */
public class GoalSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Goal> meta = new ArrayList<Goal>();

    //Constructor

    public GoalSpinnerAdapter(Context context, ArrayList<Goal> meta) {
        this.context = context;
        this.meta = meta;
    }

    @Override
    public int getCount() {
        return meta.size();
    }

    @Override
    public Object getItem(int position) {
        return meta.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(meta.get(position).getName());
        v.setTag(meta.get(position).getId());
        return v;
    }
}
