package br.customercare.tcc.view.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.PieChartL;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.vendas.ConsultOportunidadesSemanal;

/**
 * Created by JeanThomas on 06/11/2016.
 */
public class VendasGanhosPerdasSemanalActivity extends AppCompatActivity {
    PieChartL pieGraph;
    ArrayList<Opportunity> opportunities;
    float stageClosedWon, stageClosedLost = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas_ganhos_perdas_semanal);

        opportunities = new ArrayList<Opportunity>();
        ConsultOportunidadesSemanal consultOportunidadesSemanal = new ConsultOportunidadesSemanal(this);

        try {
            opportunities = consultOportunidadesSemanal.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        pieGraph = (PieChartL) findViewById(R.id.pie_semanal_chart);

        List<ChartData> values = new ArrayList<>();

        for (int i = 0; i < opportunities.size(); i++){
            if(opportunities.get(i).getStageName().equals("Closed Won")){
                stageClosedWon++;
            }else if(opportunities.get(i).getStageName().equals("Closed Lost")) {
                stageClosedLost++;
            }

        }

        values.add(new ChartData("Closed Won ("+Math.round(stageClosedWon)+")" , stageClosedWon));
        values.add(new ChartData("Closed Won ("+Math.round(stageClosedLost)+")" , stageClosedLost));
        pieGraph.setData(values);
    }

}
