package br.customercare.tcc.util.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.MainMenuActivity;

/**
 * Created by JeanThomas on 04/09/2016.
 */
public class LogarSF extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public LogarSF(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Autenticando dados");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Autenticando...");
        boolean conecta = Conexao.conectaSF(params[0], params[1]);
        publishProgress("Dados verificados...");
        return conecta;
    }

    @Override
    protected void onProgressUpdate(String... params) {
        progress.setMessage(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean params) {
        if (params == false) {
            alert = new AlertDialog.Builder(context);
            alert.setTitle("Atenção");
            alert.setMessage("Dados inválidos");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        } else{
            Intent intent = new Intent(context, MainMenuActivity.class);
            context.startActivity(intent);
        }
            progress.dismiss();
    }
}
