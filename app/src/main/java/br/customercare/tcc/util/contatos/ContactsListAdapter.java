package br.customercare.tcc.util.contatos;

import android.content.Context;
import android.text.BoringLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Contact;

import java.util.List;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 18/09/2016.
 */
public class ContactsListAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> contactList;

    //Constructor

    public ContactsListAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_contact_list, null);
        TextView textName = (TextView)v.findViewById(R.id.txtListContatoNome);
        TextView textAccount = (TextView)v.findViewById(R.id.txtListContatoConta);
        TextView textTelefone = (TextView)v.findViewById(R.id.txtListContatoTelefone);

        //Set text for TextView
        try{
            textName.setText(contactList.get(position).getName());
            textAccount.setText(contactList.get(position).getAccount().getName());
            textTelefone.setText(contactList.get(position).getPhone());
        }catch (NullPointerException e){}

        //Save product id to tag
        v.setTag(contactList.get(position).getId());

        return v;
    }
}