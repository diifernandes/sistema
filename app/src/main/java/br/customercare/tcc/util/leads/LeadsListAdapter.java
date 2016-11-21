package br.customercare.tcc.util.leads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 18/09/2016.
 */
public class LeadsListAdapter extends BaseAdapter {
    private Context context;
    private List<Lead> leadsList;

    //Constructor

    public LeadsListAdapter(Context context, List<Lead> leadsList) {
        this.context = context;
        this.leadsList = leadsList;
    }

    @Override
    public int getCount() {
        return leadsList.size();
    }

    @Override
    public Object getItem(int position) {
        return leadsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_lead_list, null);
        TextView textName = (TextView)v.findViewById(R.id.txtListLeadNome);
        TextView textEmpresa = (TextView)v.findViewById(R.id.txtListLeadEmpresa);
        TextView textClassificacao = (TextView)v.findViewById(R.id.txtListLeadClassificacao);

        //Set text for TextView
        textName.setText(leadsList.get(position).getName());
        textEmpresa.setText(leadsList.get(position).getCompany());
        textClassificacao.setText(leadsList.get(position).getRating());

        //Save product id to tag
        v.setTag(leadsList.get(position).getId());

        return v;
    }
}