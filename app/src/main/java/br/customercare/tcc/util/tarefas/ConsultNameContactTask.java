package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 26/10/2016.
 */
public class ConsultNameContactTask extends AsyncTask<String, String, String> {
    private ProgressDialog progress;
    private Context context;

    public ConsultNameContactTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String idContact = params[0];
        Contact[] contact = new Contact[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Name FROM Contact WHERE Id='"+idContact+"'");
            if(query.getSize() > 0){
                for(int i = 0; i < query.getSize(); i++){
                    contact[i] = (Contact)query.getRecords()[i];
                }
                return contact[0].getName();
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
