package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Lead;

import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.DeleteLead;
import br.customercare.tcc.util.leads.ConsultOneLead;

public class ViewLeadActivity extends AppCompatActivity {

    TextView textNome, textEmpresa, textOrigem, textSetor, textReceita, textTelefone,
            textEmail, textFuncionarios, textEndereco, textStatus, textClassificacao;


    private Lead[] lead = new Lead[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lead);

        textNome = (TextView)findViewById(R.id.txtViewLeadValueNome);
        textEmpresa = (TextView)findViewById(R.id.txtViewLeadValueEmpresa);
        textOrigem = (TextView)findViewById(R.id.txtViewLeadValueOrigem);
        textSetor = (TextView)findViewById(R.id.txtViewLeadValueSetor);
        textReceita = (TextView)findViewById(R.id.txtViewLeadValueReceita);
        textTelefone = (TextView)findViewById(R.id.txtViewLeadValueTelefone);
        textEmail = (TextView)findViewById(R.id.txtViewLeadValueEmail);
        textFuncionarios = (TextView)findViewById(R.id.txtViewLeadValueFuncionarios);
        textEndereco = (TextView)findViewById(R.id.txtViewLeadValueEndereco);
        textStatus = (TextView)findViewById(R.id.txtViewLeadValueStatus);
        textClassificacao = (TextView)findViewById(R.id.txtViewLeadValueClassificacao);

        String idLead = getIntent().getStringExtra(LeadsActivity.EXTRA_ID);
        ConsultOneLead consultOneLead = new ConsultOneLead(this);

        try {
            lead = consultOneLead.execute(idLead).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try{
            textNome.setText(lead[0].getName());
            textEmpresa.setText(lead[0].getCompany());
            textOrigem.setText(lead[0].getLeadSource());
            textSetor.setText(lead[0].getIndustry());
            textReceita.setText(Double.toString(lead[0].getAnnualRevenue()));
            textTelefone.setText(lead[0].getPhone());
            textEmail.setText(lead[0].getEmail());
            textFuncionarios.setText(Integer.toString(lead[0].getNumberOfEmployees()));
            textEndereco.setText(lead[0].getStreet());
            textStatus.setText(lead[0].getStatus());
            textClassificacao.setText(lead[0].getRating());
        }catch (NullPointerException e) {

        }
    }

    public void deleteLead(View view){
        DeleteLead deleteLead = new DeleteLead(this);
        deleteLead.execute(lead[0].getId());
    }

    public void updateLead(View view){
        Intent intent = new Intent(getBaseContext(), UpdateLeadActivity.class);
        String idLead = lead[0].getId();
        intent.putExtra(EXTRA_ID, idLead);
        startActivity(intent);
    }

    public void convertLead(View view){
        Intent intent = new Intent(getBaseContext(), ConvertLeadActivity.class);
        String idLead = lead[0].getId();
        intent.putExtra(EXTRA_ID, idLead);
        startActivity(intent);
    }
}
