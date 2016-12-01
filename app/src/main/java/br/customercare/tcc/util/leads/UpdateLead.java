package br.customercare.tcc.util.leads;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.LeadsActivity;

/**
 * Created by JeanThomas on 05/09/2016.
 */
public class UpdateLead extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public UpdateLead(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Atualizando lead");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        String idLead = params[0];
        Lead[] record = new Lead[1];
        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Firstname, Lastname, Company, LeadSource, Industry, AnnualRevenue, Phone, Email, NumberOfEmployees, Street, Status, Rating FROM Lead WHERE Id='"+idLead+"'");
            Lead lead = (Lead)query.getRecords()[0];
            lead.setFirstName(params[1]);
            lead.setLastName(params[2]);
            lead.setCompany(params[3]);
            lead.setLeadSource(params[4]);
            lead.setIndustry(params[5]);
            if (!params[6].isEmpty() && params[6] != null) {
                lead.setAnnualRevenue(Double.parseDouble(params[6]));
            }
            lead.setPhone(params[7]);
            lead.setEmail(params[8]);
            lead.setStatus(params[9]);
            lead.setRating(params[10]);
            if (!params[11].isEmpty() && params[11] != null) {
                lead.setNumberOfEmployees(Integer.parseInt(params[11]));
            }
            lead.setStreet(params[12]);
            record[0] = lead;
            SaveResult[] saveResult = Conexao.getConnection().update(record);
            if(saveResult[0].isSuccess()){
                System.out.println("SUCCESS");
            }else{
                System.out.println("MENSAGEM DE ERRO: "+ saveResult[0].getErrors()[0].getMessage() + "\n");
            }

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
            alert.setMessage("Lead atualizado com sucesso!");
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
            alert.setMessage("O cadastro não pôde ser atualizado no momento, tente novamente.");
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
