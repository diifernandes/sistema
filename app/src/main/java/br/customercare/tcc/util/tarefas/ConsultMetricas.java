package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultMetricas extends AsyncTask<String, String, ArrayList<Metric>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultMetricas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Metric> doInBackground(String... params) {
        ArrayList<Metric> metrics = new ArrayList<Metric>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name FROM Metric");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    metrics.add((Metric)query.getRecords()[i]);
                }
                return metrics;
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
