package br.customercare.tcc.util.campanha;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 21/10/2016.
 */
public class ConsultOneCampanha extends AsyncTask<String, String, Campaign[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneCampanha(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Campaign[] doInBackground(String... params) {
        String idCampanha = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Owner.Name, Name, IsActive, Type, Status, StartDate, EndDate, ExpectedRevenue, BudgetedCost, ActualCost, ExpectedResponse, Parent.Name, NumberOfLeads, NumberOfConvertedLeads, NumberOfContacts, AmountAllOpportunities, AmountWonOpportunities FROM Campaign WHERE Id = '" + idCampanha +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando Campanha");
                Campaign[] consult = new Campaign[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Campaign) query.getRecords()[i];
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
