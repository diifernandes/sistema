package br.customercare.tcc.util.oportunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.ws.ConnectionException;

import java.util.Calendar;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.OportunidadesActivity;


/**
 * Created by JeanThomas on 05/11/2016.
 */
public class InsertOportunidade extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public InsertOportunidade(Context context){
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
        Opportunity opportunity = new Opportunity();
        Calendar dataFechamento = Calendar.getInstance();

        /*Manipulação de data*/
        if(!params[5].isEmpty()) {
            int diaFechamento, mesFechamento, anoFechamento;
            diaFechamento = Integer.parseInt(params[5].substring(0, 2));
            mesFechamento = Integer.parseInt(params[5].substring(3, 5));
            anoFechamento = Integer.parseInt(params[5].substring(6, 10));

            dataFechamento.set(anoFechamento, mesFechamento - 1, diaFechamento);
        }
        /*Manipulação de data*/

        try {
            opportunity.setOwnerId(Conexao.getConnection().getUserInfo().getUserId());
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        opportunity.setName(params[0]);
        opportunity.setAccountId(params[1]);
        opportunity.setType(params[2]);
        opportunity.setLeadSource(params[3]);
        opportunity.setAmount(Double.parseDouble(params[4]));
        opportunity.setCloseDate(dataFechamento);
        opportunity.setNextStep(params[6]);
        opportunity.setStageName(params[7]);
        opportunity.setProbability(Double.parseDouble(params[8]));
        opportunity.setCampaignId(params[9]);

        record[0] = opportunity;
        try {
            SaveResult[] saveResult = Conexao.getConnection().create(record);
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
            alert.setMessage("Cadastro de oportunidade realizado com sucesso!");
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
