package br.customercare.tcc.view.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.FunnelChart;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.vendas.ConsultLeads;
import br.customercare.tcc.util.vendas.ConsultOportunidades;

/**
 * Created by JeanThomas on 06/11/2016.
 */
public class VendasPipelineActivity extends BaseDrawerActivity {
    FunnelChart funnelGraph;
    ArrayList<Opportunity> opportunities;
    ArrayList<Lead> leads;
    int stageProspecting, stageQualification, stageProposal, qtdeLeads = 0;
    double amountProspecting, amountQualification, amountProposal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vendas_pipeline);
        getLayoutInflater().inflate(R.layout.activity_vendas_pipeline, frameLayout);
        opportunities = new ArrayList<Opportunity>();
        ConsultOportunidades consultOportunidades = new ConsultOportunidades(this);
        ConsultLeads consultLeads = new ConsultLeads(this);

        try {
            opportunities = consultOportunidades.execute().get();
            leads = consultLeads.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        funnelGraph = (FunnelChart) findViewById(R.id.funnel_chart);

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
            }
        }

        qtdeLeads = leads.size();

        values.add(new ChartData("Leads (Open - Not Contacted) - ("+qtdeLeads+")", qtdeLeads));
        values.add(new ChartData("Prospecting - R$"+amountProspecting + " ("+stageProspecting+")", stageProspecting));
        values.add(new ChartData("Qualification - R$"+amountQualification + " ("+stageQualification+")", stageQualification));
        values.add(new ChartData("Proposal/Price Quote - R$"+amountProposal + " ("+stageProposal+")", stageProposal));
        funnelGraph.setData(values);
    }

}
