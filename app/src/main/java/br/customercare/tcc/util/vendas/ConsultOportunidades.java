package br.customercare.tcc.util.vendas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultOportunidades extends AsyncTask<String, String, ArrayList<Opportunity>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOportunidades(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Opportunity> doInBackground(String... params) {
        ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, StageName, Amount FROM Opportunity WHERE OwnerId = '" + Conexao.getConnection().getUserInfo().getUserId() + "'");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    opportunities.add((Opportunity)query.getRecords()[i]);
                }
                return opportunities;
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
