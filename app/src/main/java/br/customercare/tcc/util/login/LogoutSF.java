package br.customercare.tcc.util.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.LoginActivity;

/**
 * Created by JeanThomas on 04/09/2016.
 */
public class LogoutSF extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public LogoutSF(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Realizando logout");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando ação...");
        boolean desconecta = Conexao.desconectaSF();
        return desconecta;
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
            alert.setMessage("Falha no logout. Tente novamente.");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        } else{
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        progress.dismiss();
    }
}
