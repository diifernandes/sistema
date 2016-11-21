package br.customercare.tcc.util.oportunidades;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Campaign;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class CampanhaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();

    //Constructor

    public CampanhaAdapter(Context context, ArrayList<Campaign> campaigns) {
        this.context = context;
        this.campaigns = campaigns;
    }

    @Override
    public int getCount() {
        return campaigns.size();
    }

    @Override
    public Object getItem(int position) {
        return campaigns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(campaigns.get(position).getName());
        v.setTag(campaigns.get(position).getId());
        return v;
    }
}
