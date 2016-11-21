package br.customercare.tcc.util.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 26/10/2016.
 */
public class ConsultNameOwnerTask extends AsyncTask<String, String, String> {
    private ProgressDialog progress;
    private Context context;

    public ConsultNameOwnerTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String idUser = params[0];
        User[] user = new User[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Name FROM User WHERE Id='"+idUser+"'");
            if(query.getSize() > 0){
                for(int i = 0; i < query.getSize(); i++){
                    user[i] = (User)query.getRecords()[i];
                }
                return user[0].getName();
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
