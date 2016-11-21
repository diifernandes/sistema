package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultContatos extends AsyncTask<String, String, ArrayList<Contact>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultContatos(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Contact> doInBackground(String... params) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Account.Name FROM Contact");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
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
