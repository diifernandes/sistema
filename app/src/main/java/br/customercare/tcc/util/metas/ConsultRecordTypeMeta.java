package br.customercare.tcc.util.metas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;

import br.customercare.tcc.util.Conexao;

/**
 * Created by Fernando on 19/10/2016.
 */
public class ConsultRecordTypeMeta extends AsyncTask<String, String, ArrayList<RecordType>> {
    private ProgressDialog progress;
    private Context context;

    public ConsultRecordTypeMeta(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<RecordType> doInBackground(String... params) {
        ArrayList<RecordType> tpReg = new ArrayList<RecordType>();
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name FROM RecordType");
            if(query.getSize() > 0) {
                for (int i = 0; i < query.getSize(); i++) {
                    tpReg.add((RecordType)query.getRecords()[i]);
                }
                return tpReg;
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
