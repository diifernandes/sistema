package br.customercare.tcc.util.oportunidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultContas extends AsyncTask<String, String, ArrayList<Account>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultContas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Account> doInBackground(String... params) {
        ArrayList<Account> accounts = new ArrayList<Account>();
        Account account = new Account();
        account.setId(null);
        account.setName("-- Nenhum --");
        accounts.add(0, account);
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name FROM Account");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    accounts.add((Account)query.getRecords()[i]);
                }
                return accounts;
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
