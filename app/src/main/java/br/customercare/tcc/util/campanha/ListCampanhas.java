package br.customercare.tcc.util.campanha;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 21/10/2016.
 */
public class ListCampanhas extends AsyncTask<Void, String, ArrayList<Campaign>> {
    private ProgressDialog progress;
    private Context context;

    public ListCampanhas(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<Campaign> doInBackground(Void... params) {
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Owner.Name, Status, Type FROM Campaign Where Ownerid = '" + Conexao.getConnection().getUserInfo().getUserId() +"' ");
            if(query.getSize() > 0){
                publishProgress("Carregando Campanhas");
                ArrayList campanhas = new ArrayList<Campaign>();
                for(int i = 0; i < query.getSize(); i++){
                    campanhas.add((Campaign)query.getRecords()[i]);
                }
                return campanhas;
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
