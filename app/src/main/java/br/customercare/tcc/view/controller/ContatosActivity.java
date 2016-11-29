package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.contatos.ContactsListAdapter;
import br.customercare.tcc.util.contatos.ListContacts;


public class ContatosActivity extends BaseDrawerActivity {

    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    private FloatingActionButton fabEdit;

    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contacts);
        getLayoutInflater().inflate(R.layout.activity_contacts, frameLayout);
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

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);

        fab1.setEnabled(true);
        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuRed.isOpened()) {
                    //Toast.makeText(getActivity(), menuRed.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuRed.toggle(true);
            }
        });




    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    inserirContato(v);

            }
        }
    };


    public void inserirContato(View view){
        Intent intent = new Intent(this, InsertContatoActivity.class);
        startActivity(intent);
    }
}
