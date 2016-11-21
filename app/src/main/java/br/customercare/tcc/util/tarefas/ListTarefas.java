package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ListTarefas extends AsyncTask<Void, String, ArrayList<Task>> {
    private ProgressDialog progress;
    private Context context;

    public ListTarefas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Task> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Owner.Name, Subject, ActivityDate, Priority, Status FROM Task WHERE OwnerId = '" + Conexao.getConnection().getUserInfo().getUserId() +"' ORDER BY CreatedDate DESC");
            if(query.getSize() > 0){
                publishProgress("Carregando Tarefas");
                ArrayList tarefas = new ArrayList<Task>();
                for(int i = 0; i < query.getSize(); i++){
                   tarefas.add((Task) query.getRecords()[i]);
                }
                    return tarefas;
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
