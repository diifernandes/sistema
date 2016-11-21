package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Lead;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class LeadsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Lead> leads = new ArrayList<Lead>();

    //Constructor

    public LeadsAdapter(Context context, ArrayList<Lead> leads) {
        this.context = context;
        this.leads = leads;
    }

    @Override
    public int getCount() {
        return leads.size();
    }

    @Override
    public Object getItem(int position) {
        return leads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(leads.get(position).getName() + " - " + leads.get(position).getCompany());
        v.setTag(leads.get(position).getId());
        return v;
    }
}
