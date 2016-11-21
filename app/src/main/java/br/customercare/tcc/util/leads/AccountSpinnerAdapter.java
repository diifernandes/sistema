package br.customercare.tcc.util.leads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;

import java.util.ArrayList;

import br.customercare.tcc.R;


/**
 * Created by JeanThomas on 18/09/2016.
 */
public class AccountSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Account> acc = new ArrayList<Account>();

    //Constructor

    public AccountSpinnerAdapter(Context context, ArrayList<Account> acc) {
        this.context = context;
        this.acc = acc;
    }

    @Override
    public int getCount() {
        return acc.size();
    }

    @Override
    public Object getItem(int position) {
        return acc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(acc.get(position).getName());
        v.setTag(acc.get(position).getId());
        return v;
    }
}