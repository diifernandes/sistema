package br.customercare.tcc.util.contatos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ConsultOneAccount extends AsyncTask<String, String, Account[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneAccount(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Account[] doInBackground(String... params) {
        String idAccount = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Name FROM Account WHERE Id='"+idAccount+"'");
            if(query.getSize() > 0){
                publishProgress("Carregando conta");
                Account[] consult = new Account[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Account)query.getRecords()[i];
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
