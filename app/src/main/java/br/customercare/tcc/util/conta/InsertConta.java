package br.customercare.tcc.util.conta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.ContasActivity;
import br.customercare.tcc.view.controller.LeadsActivity;

/**
 * Created by Fernando on 09/10/2016.
 */
public class InsertConta extends AsyncTask<String, String, Boolean> {

    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public InsertConta(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Cadastrando Conta");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;

        Account[] record = new Account[1];
        Account conta = new Account();
        conta.setName(params[0]);
        conta.setRating(params[1]);
        conta.setAccountSource(params[2]);
        conta.setPhone(params[3]);
        conta.setIndustry(params[4]);
        conta.setType(params[5]);
        conta.setAnnualRevenue(Double.parseDouble(params[6]));
        conta.setNumberOfEmployees(Integer.parseInt(params[7]));
        conta.setBillingStreet(params[8]);

        record[0] = conta ;
        try {
            Conexao.getConnection().create(record);
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
            alert.setMessage("Cadastro realizado com sucesso!");
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
            alert.setMessage("O cadastro não pôde ser realizado no momento, tente novamente.");
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
