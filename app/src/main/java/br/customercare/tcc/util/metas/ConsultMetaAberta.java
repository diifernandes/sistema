package br.customercare.tcc.util.metas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Goal;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 19/10/2016.
 */
public class ConsultMetaAberta extends AsyncTask<String, String, ArrayList<Goal>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultMetaAberta(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Goal> doInBackground(String... params) {
        ArrayList<Goal> metas = new ArrayList<Goal>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id,Name FROM Goal Where Status= 'Published'");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    metas.add((Goal)query.getRecords()[i]);
                }
                return metas;
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
