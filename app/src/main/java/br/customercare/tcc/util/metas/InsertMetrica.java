package br.customercare.tcc.util.metas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.ws.ConnectionException;

import java.util.Calendar;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.MetricasActivity;

/**
 * Created by Fernando on 18/10/2016.
 */
public class InsertMetrica extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public InsertMetrica(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Cadastrando Métrica");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        Metric[] record = new Metric[1];
        Metric metric = new Metric();
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataVencimento = Calendar.getInstance();

        /*Manipulação das datas*/
        if(!params[5].isEmpty()) {
            int diaInicio, mesInicio, anoInicio;
            diaInicio = Integer.parseInt(params[5].substring(0, 2));
            mesInicio = Integer.parseInt(params[5].substring(3, 5));
            anoInicio = Integer.parseInt(params[5].substring(6, 10));

            dataInicio.set(anoInicio, mesInicio-1, diaInicio);
        }

        if(!params[6].isEmpty()) {
            int diaVencimento, mesVencimento, anoVencimento;
            diaVencimento = Integer.parseInt(params[6].substring(0, 2));
            mesVencimento = Integer.parseInt(params[6].substring(3, 5));
            anoVencimento = Integer.parseInt(params[6].substring(6, 10));

            dataVencimento.set(anoVencimento, mesVencimento - 1, diaVencimento);
        }
        /*Manipulação das datas*/

        if(params[0].equals("0")){
            metric.setRecordTypeId(params[1]);
            metric.setName(params[2]);
            metric.setGoalId(params[3]);
            metric.setDueDate(dataVencimento);
            metric.setStatus(params[4]);
            metric.setDescription(params[10]);
            metric.setCurrentValue(Double.parseDouble(params[7]));
            metric.setInitialValue(Double.parseDouble(params[8]));
            metric.setTargetValue(Double.parseDouble(params[9]));
            metric.setLastComment(params[11]);
        }else{
            metric.setRecordTypeId(params[1]);
            metric.setName(params[2]);
            metric.setGoalId(params[3]);
            metric.setStartDate(dataInicio);
            metric.setDueDate(dataVencimento);
            metric.setDescription(params[10]);
            metric.setStatus(params[4]);
            metric.setLastComment(params[11]);
        }
        record[0] = metric;
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
            alert.setMessage("Cadastro de métrica realizado com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, MetricasActivity.class);
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
