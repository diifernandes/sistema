package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Contact;

import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.contatos.ConsultOneContact;
import br.customercare.tcc.util.contatos.DeleteContact;

public class ViewContatoActivity extends BaseDrawerActivity {

    TextView textProp, textNome, textConta, textTelefone, textCelular, textEmail, textTitulo;
    private Contact[] contact = new Contact[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

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

        try {
            textProp.setText(contact[0].getOwner().getName());
            textNome.setText(contact[0].getName());
            textConta.setText(contact[0].getAccount().getName());
            textTelefone.setText(contact[0].getPhone());
            textCelular.setText(contact[0].getMobilePhone());
            textEmail.setText(contact[0].getEmail());
            textTitulo.setText(contact[0].getTitle());
        }catch (NullPointerException e){}
    }

    public void deleteContact(View view){
        DeleteContact deleteContact = new DeleteContact(this);
        deleteContact.execute(contact[0].getId());
    }

    public void updateContact(View view){
        Intent intent = new Intent(getBaseContext(), UpdateContactActivity.class);
        String idContact = contact[0].getId();
        intent.putExtra(EXTRA_ID, idContact);
        startActivity(intent);
    }

}
