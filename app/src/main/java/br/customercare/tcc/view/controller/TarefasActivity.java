package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Task;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.metas.ListMetricas;
import br.customercare.tcc.util.metas.MetricasListAdapter;
import br.customercare.tcc.util.tarefas.ListTarefas;
import br.customercare.tcc.util.tarefas.TarefasListAdapter;

public class TarefasActivity extends AppCompatActivity {
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);

        ListView lvTarefas = (ListView)findViewById(R.id.listView_Tarefas);

        ArrayList<Task> tarefas = new ArrayList<Task>();
        ListTarefas listTarefas = new ListTarefas(this);

        try {
            tarefas = listTarefas.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TarefasListAdapter adapter = new TarefasListAdapter(this, tarefas);
        lvTarefas.setAdapter(adapter);

        lvTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewTarefaActivity.class);
                String idTarefa =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idTarefa);
                startActivity(intent);
            }
        });
    }


    public void insertTarefa(View view){
        Intent intent = new Intent(this, InsertTarefasActivity.class);
        startActivity(intent);
    }
}
