package br.customercare.tcc.view.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.contatos.ConsultOneContact;
import br.customercare.tcc.util.contatos.DeleteContact;
import br.customercare.tcc.util.leads.DeleteLead;

public class ViewContatoActivity extends BaseDrawerActivity {

    TextView textProp, textNome, textConta, textTelefone, textCelular, textEmail, textTitulo;
    private Contact[] contact = new Contact[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    private AlertDialog.Builder alert;
    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_contact);
        getLayoutInflater().inflate(R.layout.activity_view_contact, frameLayout);
        textProp = (TextView)this.findViewById(R.id.txtViewContatoValuePropietario);
        textNome = (TextView)this.findViewById(R.id.txtViewContatoValueNome);
        textConta = (TextView)this.findViewById(R.id.txtViewContatoValueConta);
        textTelefone = (TextView)this.findViewById(R.id.txtViewContatoValueTelefone);
        textCelular = (TextView)this.findViewById(R.id.txtViewContatoValueCelular);
        textEmail = (TextView)this.findViewById(R.id.txtViewContatoValueEmail);
        textTitulo = (TextView)this.findViewById(R.id.txtViewContatoValueTitulo);

        String idContact = getIntent().getStringExtra(ContatosActivity.EXTRA_ID);
        ConsultOneContact consultOneContact = new ConsultOneContact(this);

        try {
            contact = consultOneContact.execute(idContact).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(contact[0].getOwner().getName() != null)textProp.setText(contact[0].getOwner().getName());
        if(contact[0].getName() != null)textNome.setText(contact[0].getName());
        if(contact[0].getAccount().getName() != null)textConta.setText(contact[0].getAccount().getName());
        if(contact[0].getPhone() != null)textTelefone.setText(contact[0].getPhone());
        if(contact[0].getMobilePhone() != null)textCelular.setText(contact[0].getMobilePhone());
        if(contact[0].getEmail() != null)textEmail.setText(contact[0].getEmail());
        if(contact[0].getTitle() != null)textTitulo.setText(contact[0].getTitle());

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab1.setEnabled(true);
        fab2.setEnabled(true);

        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);


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
                    updateContact(v);
                    break;
                case R.id.fab2:
                    deleteContact(v);
                    break;

            }
        }
    };

    public void deleteContact(View view){
        alert = new AlertDialog.Builder(this);
        alert.setTitle("ATENÇÃO");
        alert.setMessage("Tem certeza que deseja excluir este cadastro?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteContact deleteContact = new DeleteContact(ViewContatoActivity.this);
                deleteContact.execute(contact[0].getId());
            }
        });
        alert.setNeutralButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create();
        alert.show();

    }

    public void updateContact(View view){
        Intent intent = new Intent(getBaseContext(), UpdateContactActivity.class);
        String idContact = contact[0].getId();
        intent.putExtra(EXTRA_ID, idContact);
        startActivity(intent);
    }

}
