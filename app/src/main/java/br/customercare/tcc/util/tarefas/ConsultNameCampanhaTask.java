package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 26/10/2016.
 */
public class ConsultNameCampanhaTask extends AsyncTask<String, String, String> {
    private ProgressDialog progress;
    private Context context;

    public ConsultNameCampanhaTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String idCampaign = params[0];
        Campaign[] campaign = new Campaign[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Name FROM Campaign WHERE Id='"+idCampaign+"'");
            if(query.getSize() > 0){
                for(int i = 0; i < query.getSize(); i++){
                    campaign[i] = (Campaign)query.getRecords()[i];
                }
                return campaign[0].getName();
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
