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
import br.customercare.tcc.util.vendas.ConsultOportunidades;

/**
 * Created by JeanThomas on 06/11/2016.
 */
public class VendasTotalOppActivity extends BaseDrawerActivity {
    PieChartL pieGraph;
    ArrayList<Opportunity> opportunities;
    float stageProspecting, stageQualification, stageProposal, stageNegotiation;
    double amountProspecting, amountQualification, amountProposal, amountNegotiation = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vendas_total_oportunidades);
        getLayoutInflater().inflate(R.layout.activity_vendas_total_oportunidades, frameLayout);
        opportunities = new ArrayList<Opportunity>();
        ConsultOportunidades consultOportunidades = new ConsultOportunidades(this);

        try {
            opportunities = consultOportunidades.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        pieGraph = (PieChartL) findViewById(R.id.pie_chart);

        List<ChartData> values = new ArrayList<>();

        for (int i = 0; i < opportunities.size(); i++){
            if(opportunities.get(i).getStageName().equals("Prospecting")){
                stageProspecting++;
                if(opportunities.get(i).getAmount() != null) {
                    amountProspecting += opportunities.get(i).getAmount();
                }
            }else if(opportunities.get(i).getStageName().equals("Qualification")){
                stageQualification++;
                if(opportunities.get(i).getAmount() != null) {
                    amountQualification += opportunities.get(i).getAmount();
                }
            }else if(opportunities.get(i).getStageName().equals("Proposal/Price Quote")){
                stageProposal++;
                if(opportunities.get(i).getAmount() != null) {
                    amountProposal += opportunities.get(i).getAmount();
                }
            }else if(opportunities.get(i).getStageName().equals("Negotiation/Review")){
                stageNegotiation++;
                if(opportunities.get(i).getAmount() != null) {
                    amountNegotiation += opportunities.get(i).getAmount();
                }
            }
        }

        values.add(new ChartData("Prospecting - R$"+amountProspecting + " ("+Math.round(stageProspecting)+")", stageProspecting));
        values.add(new ChartData("Qualification - R$"+amountQualification + " ("+Math.round(stageQualification)+")", stageQualification));
        values.add(new ChartData("Proposal/Price Quote - R$"+amountProposal + " ("+Math.round(stageProposal)+")", stageProposal));
        values.add(new ChartData("Negotiation/Review - R$"+amountNegotiation + " ("+Math.round(stageNegotiation)+")", stageNegotiation));
        pieGraph.setData(values);
    }

}
