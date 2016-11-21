package br.customercare.tcc.util.conta;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;

import java.util.List;

import br.customercare.tcc.R;

/**
 * Created by Fernando on 08/10/2016.
 */
public class ContasListAdapter extends BaseAdapter {
    private Context context;
    private List<Account> contasList ;

    public ContasListAdapter(Context context, List<Account> contasList) {
        this.context = context;
        this.contasList = contasList;
    }

    @Override
    public int getCount() {
        return contasList.size();
    }

    @Override
    public Object getItem(int position) {
        return contasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_conta_list, null);
        TextView textPropConta = (TextView)v.findViewById(R.id.txtListContaPropietario);
        TextView textNomConta = (TextView)v.findViewById(R.id.txtListContaNome);
        TextView textClassificao = (TextView)v.findViewById(R.id.txtListContaClassificacao);

        //Set text for TextView
        try {
            textPropConta.setText(contasList.get(position).getOwner().getName());
            textNomConta.setText(contasList.get(position).getName());
            textClassificao.setText(contasList.get(position).getRating());
        }catch (NullPointerException e){}


        //Save product id to tag
        v.setTag(contasList.get(position).getId());

        return v;
    }

}
