package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Contact;

import java.util.ArrayList;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ContatoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> contatos = new ArrayList<Contact>();

    //Constructor

    public ContatoAdapter(Context context, ArrayList<Contact> contatos) {
        this.context = context;
        this.contatos = contatos;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text_spinner);
        tv.setText(contatos.get(position).getName() + " - " + contatos.get(position).getAccount().getName());
        v.setTag(contatos.get(position).getId());
        return v;
    }
}
