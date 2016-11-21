package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Opportunity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.oportunidades.ConsultOneOportunidade;
import br.customercare.tcc.util.oportunidades.DeleteOportunidade;

public class ViewOportunidadeActivity extends AppCompatActivity {

    int diaFechamento, mesFechamento, anoFechamento;

    TextView textNome, textConta, textTipo, textLead, textValor, textDataFechamento, textProxEtapa, textFase, textProbabilidade, textCamapanha;

    private Opportunity[] opportunity = new Opportunity[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_oportunidade);

        textNome = (TextView)findViewById(R.id.txtViewOportunidadeValueNome);
        textConta = (TextView)findViewById(R.id.txtViewOportunidadeValueConta);
        textTipo = (TextView)findViewById(R.id.txtViewOportunidadeValueTipo);
        textLead = (TextView)findViewById(R.id.txtViewOportunidadeValueLead);
        textValor = (TextView)findViewById(R.id.txtViewOportunidadeValueValor);
        textDataFechamento = (TextView)findViewById(R.id.txtViewOportunidadeValueDataFechamento);
        textProxEtapa = (TextView)findViewById(R.id.txtViewOportunidadeValueProxEtapa);
        textFase = (TextView)findViewById(R.id.txtViewOportunidadeValueFase);
        textProbabilidade = (TextView)findViewById(R.id.txtViewOportunidadeValueProbabilidade);
        textCamapanha = (TextView)findViewById(R.id.txtViewOportunidadeValueCampanha);


        String idOportunidade = getIntent().getStringExtra(OportunidadesActivity.EXTRA_ID);
        ConsultOneOportunidade consultOneOportunidade = new ConsultOneOportunidade(this);
        try {
            opportunity = consultOneOportunidade.execute(idOportunidade).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        carregaValores(opportunity[0]);

    }

    public void deleteOportunidade(View view){
        DeleteOportunidade deleteOportunidade = new DeleteOportunidade(this);
        deleteOportunidade.execute(opportunity[0].getId());
    }

    public void updateOportunidade(View view){
        Intent intent = new Intent(getBaseContext(), UpdateOportunidadesActivity.class);
        String idOportunidade = opportunity[0].getId();
        intent.putExtra(EXTRA_ID, idOportunidade);
        startActivity(intent);

    }

    public void carregaValores(Opportunity opportunity){
        try{
            textNome.setText(opportunity.getName());
            textConta.setText(opportunity.getAccount().getName());
            textTipo.setText(opportunity.getType());
            textLead.setText(opportunity.getLeadSource());
            textValor.setText(Double.toString(opportunity.getAmount()));
            carregaData(opportunity);
            textProxEtapa.setText(opportunity.getNextStep());
            textFase.setText(opportunity.getStageName());
            textProbabilidade.setText(Double.toString(opportunity.getProbability()));
            textCamapanha.setText(opportunity.getCampaign().getName());
        }catch (NullPointerException e){

        }
    }

    public void carregaData(Opportunity opportunity){
        if(opportunity.getCloseDate() != null) {
            Calendar dataFechamento = opportunity.getCloseDate();
            diaFechamento = dataFechamento.get(Calendar.DAY_OF_MONTH);
            mesFechamento = dataFechamento.get(Calendar.MONTH) + 1;
            anoFechamento = dataFechamento.get(Calendar.YEAR);

            textDataFechamento.setText("");
            textDataFechamento.setText(new DecimalFormat("00").format(diaFechamento) + "/" + new DecimalFormat("00").format(mesFechamento) + "/" + new DecimalFormat("00").format(anoFechamento));
        }
    }

}
