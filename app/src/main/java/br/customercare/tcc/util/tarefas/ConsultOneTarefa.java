package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 26/10/2016.
 */
public class ConsultOneTarefa extends AsyncTask<String, String, Task[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOneTarefa(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected Task[] doInBackground(String... params) {
        String idTask = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, OwnerId, Subject, ActivityDate, Priority, Status, WhoId, WhatId, Description FROM Task WHERE Id = '" + idTask +"'");
            if(query.getSize() > 0){
                publishProgress("Carregando Tarefa");
                Task[] consult = new Task[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Task) query.getRecords()[i];
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
