package br.customercare.tcc.util.contatos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.sforce.ws.ConnectionException;

import br.customercare.tcc.util.Conexao;
import br.customercare.tcc.view.controller.ContatosActivity;

/**
 * Created by JeanThomas on 05/09/2016.
 */
public class DeleteContact extends AsyncTask<String, String, Boolean> {
    private Context context;
    private ProgressDialog progress;
    private AlertDialog.Builder alert;

    public DeleteContact(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Deletando contato");
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        publishProgress("Executando operação...");
        boolean success = false;

        String[] id = new String[1];
        id[0] = params[0];
        try {
            Conexao.getConnection().delete(id);
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
            alert.setMessage("Contato excluido com sucesso!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, ContatosActivity.class);
                    context.startActivity(intent);
                }
            });
            alert.create();
            alert.show();
        }else{
            alert.setTitle("Atenção");
            alert.setMessage("O cadastro não pôde ser excluido no momento, tente novamente.");
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
