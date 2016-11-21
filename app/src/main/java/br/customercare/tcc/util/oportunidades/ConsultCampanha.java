package br.customercare.tcc.util.oportunidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultCampanha extends AsyncTask<String, String, ArrayList<Campaign>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultCampanha(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Campaign> doInBackground(String... params) {
        ArrayList<Campaign> campaigns = new ArrayList<Campaign>();
        Campaign campaign = new Campaign();
        campaign.setId(null);
        campaign.setName("-- Nenhum --");
        campaigns.add(0, campaign);
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name FROM Campaign");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    campaigns.add((Campaign)query.getRecords()[i]);
                }
                return campaigns;
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
