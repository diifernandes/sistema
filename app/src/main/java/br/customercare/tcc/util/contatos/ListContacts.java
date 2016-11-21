package br.customercare.tcc.util.contatos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.BoringLayout;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ListContacts extends AsyncTask<Void, String, ArrayList<Contact>> {
    private ProgressDialog progress;
    private Context context;

    public ListContacts(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Contact> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Account.Name, Phone FROM Contact ORDER BY CreatedDate DESC");
            if(query.getSize() > 0){
                publishProgress("Carregando contatos");
                ArrayList contacts = new ArrayList<Contact>();
                for(int i = 0; i < query.getSize(); i++){
                    contacts.add((Contact)query.getRecords()[i]);                    
                }
                return contacts;
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
