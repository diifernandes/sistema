package br.customercare.tcc.util.conta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.ContasActivity;

/**
 * Created by Fernando on 15/10/2016.
 */
public class UpdateContas extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public UpdateContas(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Atualizando conta");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        String idConta = params[0];
        Account[] record = new Account[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, Rating, AccountSource, Phone, Industry, Type, AnnualRevenue, NumberOfEmployees, BillingStreet  FROM Account where id = '" + idConta +"'");;
            Account conta = (Account)query.getRecords()[0];
            conta.setName(params[1]);
            conta.setRating(params[2]);
            conta.setAccountSource(params[3]);
            conta.setPhone(params[4]);
            conta.setIndustry(params[5]);
            conta.setType(params[6]);
            conta.setAnnualRevenue(Double.parseDouble(params[7]));
            conta.setNumberOfEmployees(Integer.parseInt(params[8]));
            conta.setBillingStreet(params[9]);
            record[0] = conta;
            Conexao.getConnection().update(record);
            success = true;
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        publishProgress("Operação finalizada...");
        return success;
    }

    @Override
    protected void onProgressUpdate(String... params) {
        progress.setMessage(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean params) {
        alert = new AlertDialog.Builder(context);
        progress.dismiss();
        if(params == true){
            alert.setTitle("Informação");
            alert.setMessage("Conta atualizada com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, ContasActivity.class);
                    context.startActivity(intent);
                }
            });
            alert.create();
            alert.show();
        }else{
            alert.setTitle("Atenção");
            alert.setMessage("A atualização não pôde ser realizada no momento, tente novamente.");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        }
    }
}
