package br.customercare.tcc.util.leads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;

/**
 * Created by JeanThomas on 07/09/2016.
 */
public class ConsultOwnerLead extends AsyncTask<String, String, User[]> {
    private ProgressDialog progress;
    private Context context;

    public ConsultOwnerLead(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected User[] doInBackground(String... params) {
        String idLead = params[0];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT OwnerId FROM Lead WHERE Id='"+idLead+"'");
            if(query.getSize() > 0){
                Lead[] consult = new Lead[1];
                User[] users = new User[1];
                for(int i = 0; i < query.getSize(); i++){
                    consult[i] = (Lead)query.getRecords()[i];
                    QueryResult queryOwnerId = Conexao.getConnection().query("SELECT Name FROM User WHERE Id='"+consult[i].getOwnerId()+"'");
                    users[i] = (User)queryOwnerId.getRecords()[i];
                }
                return users;
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
