package br.customercare.tcc.util.contatos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ConsultOneContact extends AsyncTask<String, String, Contact[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneContact(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Contact[] doInBackground(String... params) {
        String idContact = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, FirstName, LastName, Name, Account.Name, AccountId, Owner.Name, Phone, MobilePhone, Email, Title FROM Contact WHERE Id='"+idContact+"'");
            if(query.getSize() > 0){
                publishProgress("Carregando contato");
                Contact[] consult = new Contact[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Contact)query.getRecords()[i];
                }
                return consult;
            }

        } catch (ConnectionException e) {
            e.printStackTrace();
        }
                return null;
    }

    @Override
    protected void onProgressUpdate(String... params) {
        progress.setMessage(params[0]);
    }


}
