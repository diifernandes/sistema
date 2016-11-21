package br.customercare.tcc.util.oportunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import java.util.Calendar;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.OportunidadesActivity;


/**
 * Created by JeanThomas on 05/11/2016.
 */
public class UpdateOportunidade extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public UpdateOportunidade(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Cadastrando Oportunidade");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        Opportunity[] record = new Opportunity[1];
        String idOportunidade = params[0];
        Calendar dataFechamento = Calendar.getInstance();

        /*Manipulação de data*/
        if(!params[6].isEmpty()) {
            int diaFechamento, mesFechamento, anoFechamento;
            diaFechamento = Integer.parseInt(params[6].substring(0, 2));
            mesFechamento = Integer.parseInt(params[6].substring(3, 5));
            anoFechamento = Integer.parseInt(params[6].substring(6, 10));

            dataFechamento.set(anoFechamento, mesFechamento - 1, diaFechamento);
        }
        /*Manipulação de data*/

        try {
            QueryResult query = Conexao.getConnection().query("SELECT Id, Name, AccountId, Type, LeadSource, Amount, CloseDate, NextStep, StageName, Probability, CampaignId FROM Opportunity WHERE Id = '" + idOportunidade + "'");
            Opportunity opportunity = (Opportunity) query.getRecords()[0];
            opportunity.setOwnerId(Conexao.getConnection().getUserInfo().getUserId());
            opportunity.setName(params[1]);
            opportunity.setAccountId(params[2]);
            opportunity.setType(params[3]);
            opportunity.setLeadSource(params[4]);
            opportunity.setAmount(Double.parseDouble(params[5]));
            opportunity.setCloseDate(dataFechamento);
            opportunity.setNextStep(params[7]);
            opportunity.setStageName(params[8]);
            opportunity.setProbability(Double.parseDouble(params[9]));
            opportunity.setCampaignId(params[10]);
            record[0] = opportunity;

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
            alert.setMessage("Atualização da oportunidade realizada com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, OportunidadesActivity.class);
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
