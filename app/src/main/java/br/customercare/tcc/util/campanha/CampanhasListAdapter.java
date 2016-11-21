package br.customercare.tcc.util.campanha;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Campaign;

import java.util.List;

import br.customercare.tcc.R;

/**
 * Created by Fernando on 21/10/2016.
 */
public class CampanhasListAdapter extends BaseAdapter {
    private Context context;
    private List<Campaign> campanhaList ;

    public CampanhasListAdapter(Context context, List<Campaign> campanhaList) {
        this.context = context;
        this.campanhaList = campanhaList;
    }

    @Override
    public int getCount() {
        return campanhaList.size();
    }

    @Override
    public Object getItem(int position) {
        return campanhaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_campanha_list, null);
        TextView textPropCampanha = (TextView)v.findViewById(R.id.txtListCampanhaProprietario);
        TextView textNomCampanha = (TextView)v.findViewById(R.id.txtListCampanhaNome);
        TextView textTipoCampanha = (TextView)v.findViewById(R.id.txtListCampanhaTipo);
        TextView textStatusCampanha = (TextView)v.findViewById(R.id.txtListCampanhaStatus);

        //Set text for TextView

        textPropCampanha.setText(campanhaList.get(position).getOwner().getName());
        textNomCampanha.setText(campanhaList.get(position).getName());
        textTipoCampanha.setText(campanhaList.get(position).getType());
        textStatusCampanha.setText(campanhaList.get(position).getStatus());


        //Save product id to tag
        v.setTag(campanhaList.get(position).getId());

        return v;
    }

}
