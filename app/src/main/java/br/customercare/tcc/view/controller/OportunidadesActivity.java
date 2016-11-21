package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.oportunidades.ListOportunidades;
import br.customercare.tcc.util.oportunidades.OportunidadesListAdapter;

public class OportunidadesActivity extends AppCompatActivity {
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidades);

        ListView lvOportunidades = (ListView)findViewById(R.id.listView_Oportunidades);

        ArrayList<Opportunity> oportunidades = new ArrayList<Opportunity>();
        ListOportunidades listOportunidades = new ListOportunidades(this);

        try {
            oportunidades = listOportunidades.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        OportunidadesListAdapter adapter = new OportunidadesListAdapter(this, oportunidades);
        lvOportunidades.setAdapter(adapter);

        lvOportunidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewOportunidadeActivity.class);
                String idOportunidade =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idOportunidade);
                startActivity(intent);
            }
        });
    }


    public void insertOportunidade(View view){
        Intent intent = new Intent(this, InsertOportunidadesActivity.class);
        startActivity(intent);
    }
}
