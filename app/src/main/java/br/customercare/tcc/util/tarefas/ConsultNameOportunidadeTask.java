package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 26/10/2016.
 */
public class ConsultNameOportunidadeTask extends AsyncTask<String, String, String> {
    private ProgressDialog progress;
    private Context context;

    public ConsultNameOportunidadeTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String idOpportunity = params[0];
        Opportunity[] opportunity = new Opportunity[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Name FROM Opportunity WHERE Id='"+idOpportunity+"'");
            if(query.getSize() > 0){
                for(int i = 0; i < query.getSize(); i++){
                    opportunity[i] = (Opportunity)query.getRecords()[i];
                }
                return opportunity[0].getName();
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
