package br.customercare.tcc.view.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.oportunidades.CampanhaAdapter;
import br.customercare.tcc.util.oportunidades.ConsultCampanha;
import br.customercare.tcc.util.oportunidades.ConsultContas;
import br.customercare.tcc.util.oportunidades.ConsultOneOportunidade;
import br.customercare.tcc.util.oportunidades.ContaAdapter;
import br.customercare.tcc.util.oportunidades.UpdateOportunidade;

public class UpdateOportunidadesActivity extends AppCompatActivity {

    private String[] tiposValues = new String[]{"-- Nenhum --", "Existing Customer - Upgrade","Existing Customer - Replacement",
            "Existing Customer - Downgrade", "New Customer"};

    private String[] leadValues = new String[]{"-- Nenhum --", "Web", "Phone Inquiry", "Partner Referral", "Purchased List", "Other"};

    private String[] faseValues = new String[]{"Prospecting", "Qualification", "Need Analysis", "Value Proposition",
            "Id. Decision Makers", "Perception Analysis", "Proposal/Price Quote", "Negotiation/Review", "Closed Won", "Closed Lost"};

    int diaFechamento, mesFechamento, anoFechamento;
    String idOportunidade, idConta, idCampanha, tipoSelected, leadSelected, faseSelected;
    Spinner spinnerTipo, spinnerLead, spinnerFase, spinnerConta, spinnerCampanha;
    EditText editNome, editValor, editDataFechamento, editProxEtapa, editProbabilidade;
    LinearLayout linearLayout;
    Button btnAtualizar;

    ArrayAdapter<String> adapterTipo, adapterLead, adapterFase;

    CampanhaAdapter adapterCampanha;
    ContaAdapter adapterConta;

    private ArrayList<Account> accounts = new ArrayList<Account>();
    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();

    private Opportunity[] opportunity = new Opportunity[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_oportunidades);

        adapterTipo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, tiposValues);
        adapterTipo.setDropDownViewResource(R.layout.spinner_layout);

        adapterLead = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, leadValues);
        adapterLead.setDropDownViewResource(R.layout.spinner_layout);

        adapterFase = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, faseValues);
        adapterFase.setDropDownViewResource(R.layout.spinner_layout);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutUpdOportunidade);

        btnAtualizar = (Button)findViewById(R.id.btnUpdOportunidadeUpdate);

        editNome = (EditText)findViewById(R.id.edtUpdOportunidadeNome);
        editValor = (EditText)findViewById(R.id.edtUpdOportunidadeValor);

        editDataFechamento = (EditText)findViewById(R.id.edtUpdOportunidadeDataFechamento);
        editDataFechamento.addTextChangedListener(Mask.insert("##/##/####", editDataFechamento));

        editProxEtapa = (EditText)findViewById(R.id.edtUpdOportunidadeProxEtapa);
        editProbabilidade = (EditText)findViewById(R.id.edtUpdOportunidadeProbabilidade);

        spinnerTipo = (Spinner)findViewById(R.id.spiUpdOportunidadeTipo);
        spinnerLead = (Spinner)findViewById(R.id.spiUpdOportunidadeOrigemLead);
        spinnerFase = (Spinner)findViewById(R.id.spiUpdOportunidadeFase);
        spinnerConta = (Spinner)findViewById(R.id.spiUpdOportunidadeConta);
        spinnerCampanha = (Spinner)findViewById(R.id.spiUpdOportunidadeCampanha);

        idOportunidade = getIntent().getStringExtra(ViewOportunidadeActivity.EXTRA_ID);
        ConsultOneOportunidade consultOneOportunidade = new ConsultOneOportunidade(this);
        ConsultCampanha consultCampanha = new ConsultCampanha(this);
        ConsultContas consultContas = new ConsultContas(this);

        try {
            opportunity = consultOneOportunidade.execute(idOportunidade).get();
            accounts = consultContas.execute().get();
            campaigns = consultCampanha.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        spinnerTipo.setAdapter(adapterTipo);
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    tipoSelected = null;
                }else {
                    tipoSelected = tiposValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerLead.setAdapter(adapterLead);
        spinnerLead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    tipoSelected = null;
                }else {
                    leadSelected = leadValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerFase.setAdapter(adapterFase);
        spinnerFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    tipoSelected = null;
                }else {
                    faseSelected = faseValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapterConta = new ContaAdapter(this, accounts);
        spinnerConta.setAdapter(adapterConta);
        spinnerConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idConta = view.getTag().toString();
                }catch(NullPointerException e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapterCampanha = new CampanhaAdapter(this, campaigns);
        spinnerCampanha.setAdapter(adapterCampanha);
        spinnerCampanha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idCampanha = view.getTag().toString();
                }catch (NullPointerException e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        for(int i = 0; i < tiposValues.length; i++){
            if(opportunity[0].getType() !=  null) {
                if (opportunity[0].getType().equals(tiposValues[i])) {
                    spinnerTipo.setSelection(i);
                }
            }else{
                spinnerTipo.setSelection(0);
            }
        }

        for(int i = 0; i < leadValues.length; i++){
            if(opportunity[0].getLeadSource() !=  null) {
                if (opportunity[0].getLeadSource().equals(leadValues[i])) {
                    spinnerLead.setSelection(i);
                }
            }else{
                spinnerLead.setSelection(0);
            }
        }

        for(int i = 0; i < faseValues.length; i++){
            if(opportunity[0].getStageName().equals(faseValues[i])){
                spinnerFase.setSelection(i);
            }
        }

        for(int i = 0; i < accounts.size(); i++){
            if(opportunity[0].getAccountId() !=  null) {
                if (opportunity[0].getAccountId().equals(accounts.get(i).getId())) {
                    spinnerConta.setSelection(i);
                }
            }else{
                spinnerConta.setSelection(0);
            }
        }

        for(int i = 0; i < campaigns.size(); i++){
            if(opportunity[0].getCampaignId() !=  null) {
                if (opportunity[0].getCampaignId().equals(campaigns.get(i).getId())) {
                    spinnerCampanha.setSelection(i);
                }
            }else{
                spinnerCampanha.setSelection(0);
            }
        }

        editNome.setText(opportunity[0].getName());
        editValor.setText(Double.toString(opportunity[0].getAmount()));
        carregaData();
        editProxEtapa.setText(opportunity[0].getNextStep());
        editProbabilidade.setText(Double.toString(opportunity[0].getProbability()));

    }

    public void carregaData(){
        if(opportunity[0].getCloseDate() != null) {
            Calendar dataFechamento = opportunity[0].getCloseDate();
            diaFechamento = dataFechamento.get(Calendar.DAY_OF_MONTH);
            mesFechamento = dataFechamento.get(Calendar.MONTH) + 1;
            anoFechamento = dataFechamento.get(Calendar.YEAR);

            editDataFechamento.setText("");
            editDataFechamento.setText(new DecimalFormat("00").format(diaFechamento) + "/" + new DecimalFormat("00").format(mesFechamento) + "/" + new DecimalFormat("00").format(anoFechamento), TextView.BufferType.EDITABLE);
        }
    }


    public void updateOportunidade(View view){
        UpdateOportunidade updateOportunidade = new UpdateOportunidade(this);
        String nome = editNome.getText().toString();
        String valor = editValor.getText().toString();
        String dataFechamento = editDataFechamento.getText().toString();
        String proxEtapa = editProxEtapa.getText().toString();
        String probabilidade = editProbabilidade.getText().toString();
        updateOportunidade.execute(idOportunidade, nome, idConta, tipoSelected, leadSelected, valor, dataFechamento, proxEtapa, faseSelected, probabilidade, idCampanha);
    }

}