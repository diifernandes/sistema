package br.customercare.tcc.util.oportunidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 05/11/2016.
 */
public class ConsultOneOportunidade extends AsyncTask<String, String, Opportunity[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneOportunidade(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Opportunity[] doInBackground(String... params) {
        String idOportunidade = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Account.Name, AccountID, Type, LeadSource, Amount, CloseDate, NextStep, StageName, Probability, Campaign.Name, CampaignId FROM Opportunity WHERE Id = '" + idOportunidade +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando Tarefa");
                Opportunity[] consult = new Opportunity[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Opportunity) query.getRecords()[i];
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
