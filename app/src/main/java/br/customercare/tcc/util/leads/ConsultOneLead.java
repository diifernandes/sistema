package br.customercare.tcc.util.leads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ConsultOneLead extends AsyncTask<String, String, Lead[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneLead(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Lead[] doInBackground(String... params) {
        String idLead = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Firstname, Lastname, Name, Company, LeadSource, Industry, AnnualRevenue, Phone, Email, NumberOfEmployees, Street, Status, Rating FROM Lead WHERE Id='"+idLead+"'");
            if(query.getSize() > 0){
                publishProgress("Carregando lead");
                Lead[] consult = new Lead[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Lead)query.getRecords()[i];
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
