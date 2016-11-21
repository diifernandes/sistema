package br.customercare.tcc.util.conta;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 08/10/2016.
 */
public class ListContas extends AsyncTask<Void, String, ArrayList<Account>>{
    private ProgressDialog progress;
    private Context context;

    public ListContas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Account> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Owner.Name, Rating, AccountSource, Phone, Industry, Type, AnnualRevenue, NumberOfEmployees, BillingStreet FROM Account where Ownerid = '" + Conexao.getConnection().getUserInfo().getUserId() +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando contas");
                ArrayList contas = new ArrayList<Account>();
                for(int i = 0; i < query.getSize(); i++){
                    contas.add((Account)query.getRecords()[i]);
                }
                return contas;
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
