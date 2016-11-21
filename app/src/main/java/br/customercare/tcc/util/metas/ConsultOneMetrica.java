package br.customercare.tcc.util.metas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 16/10/2016.
 */
public class ConsultOneMetrica extends AsyncTask<String, String, Metric[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneMetrica(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Metric[] doInBackground(String... params) {
        String idMetric = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, RecordType.Name, Goal.Name, Status, Name, Progress, StartDate, DueDate, Description, CurrentValue, InitialValue, TargetValue, LastComment FROM Metric WHERE Id = '" + idMetric +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando Metrica");
                Metric[] consult = new Metric[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Metric) query.getRecords()[i];
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
