package br.customercare.tcc.util.leads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.BoringLayout;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ConsultCompanyAccount extends AsyncTask<String, String, ArrayList<Account>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultCompanyAccount(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Account> doInBackground(String... params) {
        String idLead = params[0];
        Lead[] consult = new Lead[1];
        ArrayList<Account> acc = new ArrayList<Account>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Company FROM Lead WHERE Id='"+idLead+"'");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    consult[i] = (Lead) query.getRecords()[i];
                }
            }

            Account account = new Account();
            account.setId("0");
            account.setName("Criar nova conta: " + consult[0].getCompany());
            acc.add(0, account);

            QueryResult queryAccount = Conexao.getConnection().query("SELECT Id, Name FROM Account WHERE Name='"+consult[0].getCompany()+"'");
            if(queryAccount.getSize() > 0){
                for(int i = 0; i < queryAccount.getSize(); i++) {
                    acc.add((Account) queryAccount.getRecords()[i]);
                }
            }
            return acc;

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
