package br.customercare.tcc.util.metas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Goal;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 16/10/2016.
 */
public class ListMetricas extends AsyncTask<Void, String, ArrayList<Metric>> {
    private ProgressDialog progress;
    private Context context;

    public ListMetricas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Metric> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Goal.Name, OwnerId, RecordType.Name FROM Metric where Ownerid = '" + Conexao.getConnection().getUserInfo().getUserId() +"' ORDER BY CreatedDate DESC");
            if(query.getSize() > 0){
                publishProgress("Carregando Métricas");
                ArrayList metricas = new ArrayList<Metric>();
                for(int i = 0; i < query.getSize(); i++){
                   metricas.add((Metric) query.getRecords()[i]);
                }
                    return metricas;
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
