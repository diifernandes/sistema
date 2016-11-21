package br.customercare.tcc.view.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.contatos.ConsultOneAccount;
import br.customercare.tcc.util.contatos.InsertContact;
import br.customercare.tcc.util.leads.ConsultCompanyAccount;
import com.sforce.soap.enterprise.sobject.Account;

public class InsertContatoActivity extends AppCompatActivity {
    EditText editNome,editSobrenome, editConta, editTelefone, editCelular, editEmail, editTitulo;
    String idAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);

        editNome = (EditText)findViewById(R.id.edtInsContatoNome);
        editSobrenome = (EditText)findViewById(R.id.edtInsContatoSobrenome);
        editConta = (EditText)findViewById(R.id.edtInsContatoConta);
        editTelefone = (EditText)findViewById(R.id.edtInsContatoTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));
        editCelular = (EditText)findViewById(R.id.edtInsContatoCelular);
        editCelular.addTextChangedListener(Mask.insert("(##)#########", editCelular));
        editEmail = (EditText)findViewById(R.id.edtInsContatoEmail);
        editTitulo = (EditText)findViewById(R.id.edtInsContatoTitulo);
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
            Account[] acc = new Account[1];
            ConsultOneAccount consultOneAccount = new ConsultOneAccount(this);
            try {
                acc = consultOneAccount.execute(idAccount).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            editConta.setText(acc[0].getName());
        }
    }

    public void insertContato(View view){
        InsertContact insertContact = new InsertContact(this);
        String nome = editNome.getText().toString();
        String sobrenome = editSobrenome.getText().toString();
        String telefone = editTelefone.getText().toString();
        String celular = editCelular.getText().toString();
        String email = editEmail.getText().toString();
        String titulo = editTitulo.getText().toString();
        insertContact.execute(nome, sobrenome, idAccount, telefone, celular, email, titulo);
    }
}
