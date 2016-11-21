package br.customercare.tcc.util.oportunidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 05/11/2016.
 */
public class ListOportunidades extends AsyncTask<Void, String, ArrayList<Opportunity>> {
    private ProgressDialog progress;
    private Context context;

    public ListOportunidades(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Opportunity> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, CloseDate, StageName, Account.Name FROM Opportunity WHERE OwnerId = '" + Conexao.getConnection().getUserInfo().getUserId() +"' ORDER BY CreatedDate DESC");
            if(query.getSize() > 0){
                publishProgress("Carregando Oportunidades");
                ArrayList opportunities = new ArrayList<Opportunity>();
                for(int i = 0; i < query.getSize(); i++){
                   opportunities.add((Opportunity) query.getRecords()[i]);
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
