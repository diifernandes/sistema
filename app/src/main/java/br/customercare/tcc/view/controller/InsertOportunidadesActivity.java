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
import android.widget.Toast;


import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Campaign;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.oportunidades.CampanhaAdapter;
import br.customercare.tcc.util.oportunidades.ConsultCampanha;
import br.customercare.tcc.util.oportunidades.ConsultContas;
import br.customercare.tcc.util.oportunidades.ContaAdapter;
import br.customercare.tcc.util.oportunidades.InsertOportunidade;

public class InsertOportunidadesActivity extends BaseDrawerActivity {

    private String[] tiposValues = new String[]{"-- Nenhum --", "Existing Customer - Upgrade","Existing Customer - Replacement",
            "Existing Customer - Downgrade", "New Customer"};

    private String[] leadValues = new String[]{"-- Nenhum --", "Web", "Phone Inquiry", "Partner Referral", "Purchased List", "Other"};

    private String[] faseValues = new String[]{"-- Nenhum --","Prospecting", "Qualification", "Need Analysis", "Value Proposition",
    "Id. Decision Makers", "Perception Analysis", "Proposal/Price Quote", "Negotiation/Review", "Closed Won", "Closed Lost"};

    String idConta, idCampanha, tipoSelected, leadSelected, faseSelected;
    Spinner spinnerTipo, spinnerLead, spinnerFase, spinnerConta, spinnerCampanha;
    EditText editNome, editValor, editDataFechamento, editProxEtapa, editProbabilidade;
    LinearLayout linearLayout;
    Button btnInserir;

    ArrayAdapter<String> adapterTipo, adapterLead, adapterFase;

    CampanhaAdapter adapterCampanha;
    ContaAdapter adapterConta;

    private ArrayList<Account> accounts = new ArrayList<Account>();
    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_insert_oportunidades);
        getLayoutInflater().inflate(R.layout.activity_insert_oportunidades, frameLayout);
        adapterTipo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, tiposValues);
        adapterTipo.setDropDownViewResource(R.layout.spinner_layout);

        adapterLead = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, leadValues);
        adapterLead.setDropDownViewResource(R.layout.spinner_layout);

        adapterFase = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, faseValues);
        adapterFase.setDropDownViewResource(R.layout.spinner_layout);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutInsOportunidade);

        btnInserir = (Button)findViewById(R.id.btnInsOportunidadeInsert);

        editNome = (EditText)findViewById(R.id.edtInsOportunidadeNome);
        editValor = (EditText)findViewById(R.id.edtInsOportunidadeValor);

        editDataFechamento = (EditText)findViewById(R.id.edtInsOportunidadeDataFechamento);
        editDataFechamento.addTextChangedListener(Mask.insert("##/##/####", editDataFechamento));

        editProxEtapa = (EditText)findViewById(R.id.edtInsOportunidadeProxEtapa);
        editProbabilidade = (EditText)findViewById(R.id.edtInsOportunidadeProbabilidade);

        spinnerTipo = (Spinner)findViewById(R.id.spiInsOportunidadeTipo);
        spinnerLead = (Spinner)findViewById(R.id.spiInsOportunidadeOrigemLead);
        spinnerFase = (Spinner)findViewById(R.id.spiInsOportunidadeFase);
        spinnerConta = (Spinner)findViewById(R.id.spiInsOportunidadeConta);
        spinnerCampanha = (Spinner)findViewById(R.id.spiInsOportunidadeCampanha);

        ConsultCampanha consultCampanha = new ConsultCampanha(this);
        ConsultContas consultContas = new ConsultContas(this);

        try {
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
    }

    public void insertOportunidade(View view){
        String nome = editNome.getText().toString();
        String valor = editValor.getText().toString();
        String dataFechamento = editDataFechamento.getText().toString();
        String proxEtapa = editProxEtapa.getText().toString();
        String probabilidade = editProbabilidade.getText().toString();
        if(nome.isEmpty() || dataFechamento.isEmpty() || faseSelected == null) {
            Toast.makeText(this, "O(S) CAMPO(S): NOME, DATA DE FECHAMENTO E FASE É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
        }else{
            InsertOportunidade insertOportunidade = new InsertOportunidade(this);
            insertOportunidade.execute(nome, idConta, tipoSelected, leadSelected, valor, dataFechamento, proxEtapa, faseSelected, probabilidade, idCampanha);
        }

    }

}