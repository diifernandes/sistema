package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;

import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.contatos.ConsultOneAccount;
import br.customercare.tcc.util.contatos.ConsultOneContact;
import br.customercare.tcc.util.contatos.UpdateContact;

public class UpdateContactActivity extends BaseDrawerActivity {
    EditText editNome,editSobrenome, editConta, editTelefone, editCelular, editEmail, editTitulo;
    String idAccount, idContato;
    Account[] acc = new Account[1];
    private Contact[] contact = new Contact[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_contacts);
        getLayoutInflater().inflate(R.layout.activity_update_contacts, frameLayout);
        editNome = (EditText)findViewById(R.id.edtUpdContatoNome);
        editSobrenome = (EditText)findViewById(R.id.edtUpdContatoSobrenome);
        editConta = (EditText)findViewById(R.id.edtUpdContatoConta);
        editTelefone = (EditText)findViewById(R.id.edtUpdContatoTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));
        editCelular = (EditText)findViewById(R.id.edtUpdContatoCelular);
        editCelular.addTextChangedListener(Mask.insert("(##)#########", editCelular));
        editEmail = (EditText)findViewById(R.id.edtUpdContatoEmail);
        editTitulo = (EditText)findViewById(R.id.edtUpdContatoTitulo);

        idContato = getIntent().getStringExtra(ContatosActivity.EXTRA_ID);
        ConsultOneContact consultOneContact = new ConsultOneContact(this);

        try {
            contact = consultOneContact.execute(idContato).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        editNome.setText(contact[0].getFirstName(), TextView.BufferType.EDITABLE);
        editSobrenome.setText(contact[0].getLastName(), TextView.BufferType.EDITABLE);
        editConta.setText(contact[0].getAccount().getName(), TextView.BufferType.EDITABLE);
        editTelefone.setText(contact[0].getPhone(), TextView.BufferType.EDITABLE);
        editCelular.setText(contact[0].getMobilePhone(), TextView.BufferType.EDITABLE);
        editEmail.setText(contact[0].getEmail(), TextView.BufferType.EDITABLE);
        editTitulo.setText(contact[0].getTitle(), TextView.BufferType.EDITABLE);
        idAccount = contact[0].getAccountId();
    }

    public void listAccounts(View view){
        Intent intent = new Intent(this, ListInsertContasToContatoActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Bundle params = intent.getExtras();
        if(params.getString("idString") != null) {
            idAccount = params.getString("idString");
            Account[] contact = new Account[1];
            ConsultOneAccount consultOneAccount = new ConsultOneAccount(this);
            try {
                contact = consultOneAccount.execute(idAccount).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            editConta.setText(contact[0].getName());
        }
    }

    public void updateContato(View view){
        UpdateContact updateContact = new UpdateContact(this);
        String nome = editNome.getText().toString();
        String sobrenome = editSobrenome.getText().toString();
        String telefone = editTelefone.getText().toString();
        String celular = editCelular.getText().toString();
        String email = editEmail.getText().toString();
        String titulo = editTitulo.getText().toString();
        updateContact.execute(idContato, nome, sobrenome, idAccount, telefone, celular, email, titulo);
    }
}
