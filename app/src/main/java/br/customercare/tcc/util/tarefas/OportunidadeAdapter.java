package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class OportunidadeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();

    //Constructor

    public OportunidadeAdapter(Context context, ArrayList<Opportunity> opportunities) {
        this.context = context;
        this.opportunities = opportunities;
    }

    @Override
    public int getCount() {
        return opportunities.size();
    }

    @Override
    public Object getItem(int position) {
        return opportunities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(opportunities.get(position).getName());
        v.setTag(opportunities.get(position).getId());
        return v;
    }
}
