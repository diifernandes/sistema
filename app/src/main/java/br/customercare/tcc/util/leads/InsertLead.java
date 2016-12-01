package br.customercare.tcc.util.leads;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.LeadsActivity;

/**
 * Created by JeanThomas on 05/09/2016.
 */
public class InsertLead extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public InsertLead(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Cadastrando lead");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        try {
            Lead[] record = new Lead[1];
            Lead lead = new Lead();
            lead.setFirstName(params[0]);
            lead.setLastName(params[1]);
            lead.setCompany(params[2]);
            lead.setLeadSource(params[3]);
            lead.setIndustry(params[4]);
            if (!params[5].isEmpty() && params[5] != null) {
                lead.setAnnualRevenue(Double.parseDouble(params[5]));
            }
            lead.setPhone(params[6]);
            lead.setEmail(params[7]);
            lead.setStatus(params[8]);
            lead.setRating(params[9]);
            if (!params[10].isEmpty() && params[10] != null ) {
                lead.setNumberOfEmployees(Integer.parseInt(params[10]));
            }
            lead.setStreet(params[11]);
            record[0] = lead ;
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
                    Intent intent = new Intent(context, LeadsActivity.class);
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
