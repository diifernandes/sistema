package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Account;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.conta.ContasListAdapter;
import br.customercare.tcc.util.conta.ListContas;

public class ContasActivity extends AppCompatActivity {
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

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
                Intent intent = new Intent(getBaseContext(), ViewContaActivity.class);
                String idConta =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idConta);
                startActivity(intent);
            }
        });
    }


    public void insertConta(View view){
        Intent intent = new Intent(this, InsertContasActivity.class);
        startActivity(intent);
    }
}
