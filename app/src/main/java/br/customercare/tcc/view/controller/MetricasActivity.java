package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Metric;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.metas.ListMetricas;
import br.customercare.tcc.util.metas.MetricasListAdapter;

public class MetricasActivity extends AppCompatActivity {
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metricas);

        ListView lvMetas = (ListView)findViewById(R.id.listView_Metricas);

        ArrayList<Metric> metricas = new ArrayList<Metric>();
        ListMetricas listMetricas = new ListMetricas(this);

        try {
            metricas = listMetricas.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        MetricasListAdapter adapter = new MetricasListAdapter(this, metricas);
        lvMetas.setAdapter(adapter);

        lvMetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewMetricasActivity.class);
                String idMetrica =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idMetrica);
                startActivity(intent);
            }
        });
    }


    public void insertMetrica(View view){
        Intent intent = new Intent(this, InsertMetricasActivity.class);
        startActivity(intent);
    }
}
