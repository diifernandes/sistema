package br.customercare.tcc.util.vendas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultLeads extends AsyncTask<String, String, ArrayList<Lead>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultLeads(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Lead> doInBackground(String... params) {
        ArrayList<Lead> leads = new ArrayList<Lead>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Status, OwnerId FROM Lead WHERE Status = 'Open - Not Contacted' and OwnerId = '" + Conexao.getConnection().getUserInfo().getUserId() + "'");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    leads.add((Lead)query.getRecords()[i]);
                }
                return leads;
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
