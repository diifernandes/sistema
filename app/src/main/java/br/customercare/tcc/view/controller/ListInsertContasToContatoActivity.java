package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Account;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.conta.ContasListAdapter;
import br.customercare.tcc.util.conta.ListContas;

public class ListInsertContasToContatoActivity extends BaseDrawerActivity {
    String idConta = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contas_to_contato);
        getLayoutInflater().inflate(R.layout.activity_contas_to_contato, frameLayout);
        ListView lvContas = (ListView)findViewById(R.id.listView_contas);

        ArrayList<Account> contas = new ArrayList<Account>();
        ListContas listcontas = new ListContas(this);

        try {
            contas = listcontas.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ContasListAdapter adapter = new ContasListAdapter(this, contas);
        lvContas.setAdapter(adapter);

        lvContas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idConta =  view.getTag().toString();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra("idString", idConta);
        setResult(1, it);
        super.onBackPressed();
    }

}
