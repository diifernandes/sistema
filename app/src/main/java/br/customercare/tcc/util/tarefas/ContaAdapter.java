package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;


import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ContaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    //Constructor

    public ContaAdapter(Context context, ArrayList<Account> accounts) {
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(accounts.get(position).getName());
        v.setTag(accounts.get(position).getId());
        return v;
    }
}
