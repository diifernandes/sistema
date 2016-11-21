package br.customercare.tcc.util.tarefas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.ws.ConnectionException;

import java.util.Calendar;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.TarefasActivity;

/**
 * Created by JeanThomas on 18/10/2016.
 */
public class InsertTarefa extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public InsertTarefa(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Cadastrando Tarefa");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;
        Task[] record = new Task[1];
        Task task = new Task();
        Calendar dataVencimento = Calendar.getInstance();

        /*Manipulação de data*/
        if(!params[1].isEmpty()) {
            int diaVencimento, mesVencimento, anoVencimento;
            diaVencimento = Integer.parseInt(params[1].substring(0, 2));
            mesVencimento = Integer.parseInt(params[1].substring(3, 5));
            anoVencimento = Integer.parseInt(params[1].substring(6, 10));

            dataVencimento.set(anoVencimento, mesVencimento - 1, diaVencimento);
        }
        /*Manipulação de data*/

        try {
            task.setOwnerId(Conexao.getConnection().getUserInfo().getUserId());
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        task.setSubject(params[0]);
        task.setActivityDate(dataVencimento);
        task.setPriority(params[2]);
        task.setStatus(params[3]);
        task.setWhoId(params[4]);
        if(params[5] != null) {
            task.setWhatId(params[5]);
        }
        task.setDescription(params[6]);

        record[0] = task;
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
            alert.setMessage("Cadastro de tarefa realizado com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, TarefasActivity.class);
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
