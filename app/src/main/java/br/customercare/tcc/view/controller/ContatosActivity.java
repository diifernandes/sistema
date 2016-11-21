package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Contact;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.contatos.ContactsListAdapter;
import br.customercare.tcc.util.contatos.ListContacts;


public class ContatosActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ListView lvContatcs = (ListView)findViewById(R.id.listview_contacts);

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        ListContacts listContacts = new ListContacts(this);

        try {
            contacts = listContacts.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        ContactsListAdapter adapter = new ContactsListAdapter(this, contacts);
        lvContatcs.setAdapter(adapter);

        lvContatcs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Clicked product id =" + idLead, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ViewContatoActivity.class);
                String idContact =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idContact);
                startActivity(intent);

            }
        });
    }

    public void inserirContato(View view){
        Intent intent = new Intent(this, InsertContatoActivity.class);
        startActivity(intent);
    }
}
