package br.customercare.tcc.util.leads;

import android.app.AlertDialog;
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
public class ListLeads extends AsyncTask<Void, String, ArrayList<Lead>> {
    private ProgressDialog progress;
    private Context context;

    public ListLeads(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Lead> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Firstname, Lastname, Name, Company, LeadSource, Industry, AnnualRevenue, Phone, Email, NumberOfEmployees, Street, Status, Rating FROM Lead WHERE IsConverted = false ORDER BY CreatedDate DESC");
            if(query.getSize() > 0){
                publishProgress("Carregando leads");
                ArrayList lead = new ArrayList<Lead>();
                for(int i = 0; i < query.getSize(); i++){
                    lead.add((Lead)query.getRecords()[i]);
                }
                return lead;
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
