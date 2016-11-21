package br.customercare.tcc.util.conta;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 15/10/2016.
 */
public class ConsultOneContas extends AsyncTask<String, String, Account[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneContas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Account[] doInBackground(String... params) {
        String idConta = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Owner.Name, Rating, AccountSource, Phone, Industry, Type, AnnualRevenue, NumberOfEmployees, BillingStreet FROM Account where id = '" + idConta +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando Conta");
                Account[] consult = new Account[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Account) query.getRecords()[i];
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
