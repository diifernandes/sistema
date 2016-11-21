package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class ConsultOwnerTask extends AsyncTask<String, String, String> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOwnerTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String idTask = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT OwnerId FROM Task WHERE Id='"+idTask+"'");
            if(query.getSize() > 0){
                Task[] consult = new Task[1];
                User[] users = new User[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Task) query.getRecords()[i];
                    QueryResult queryOwnerId = Conexao.getConnection().query("SELECT Name FROM User WHERE Id='"+consult[i].getOwnerId()+"'");
                    users[i] = (User)queryOwnerId.getRecords()[i];
                }
                return users[0].getName();
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
