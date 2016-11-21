package br.customercare.tcc.util.contatos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.ContatosActivity;

/**
 * Created by JeanThomas on 05/09/2016.
 */
public class UpdateContact extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public UpdateContact(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Atualizando contato");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        String idContato = params[0];
        Contact[] record = new Contact[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, FirstName, LastName, AccountId, Phone, MobilePhone, Email, Title FROM Contact WHERE Id='"+idContato+"'");
            Contact contact = (Contact)query.getRecords()[0];
            contact.setFirstName(params[1]);
            contact.setLastName(params[2]);
            contact.setAccountId(params[3]);
            contact.setPhone(params[4]);
            contact.setMobilePhone(params[5]);
            contact.setEmail(params[6]);
            contact.setTitle(params[7]);
            record[0] = contact;
            Conexao.getConnection().update(record);
            success = true;
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        publishProgress("Operação finalizada...");
        return success;
    }

    @Override
    protected void onProgressUpdate(String... params) {
        progress.setMessage(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean params) {
        alert = new AlertDialog.Builder(context);
        progress.dismiss();
        if(params == true){
            alert.setTitle("Informação");
            alert.setMessage("Contato atualizado com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, ContatosActivity.class);
                    context.startActivity(intent);
                }
            });
            alert.create();
            alert.show();
        }else{
            alert.setTitle("Atenção");
            alert.setMessage("O contato não pôde ser atualizado no momento, tente novamente.");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        }
    }
}
