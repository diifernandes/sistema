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
import android.widget.Toast;

import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.soap.enterprise.sobject.Campaign;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.tarefas.CampanhaAdapter;
import br.customercare.tcc.util.tarefas.ConsultCampanha;
import br.customercare.tcc.util.tarefas.ConsultContas;
import br.customercare.tcc.util.tarefas.ConsultContatos;
import br.customercare.tcc.util.tarefas.ConsultLeads;
import br.customercare.tcc.util.tarefas.ConsultMetricas;
import br.customercare.tcc.util.tarefas.ConsultOportunidades;
import br.customercare.tcc.util.tarefas.ContaAdapter;
import br.customercare.tcc.util.tarefas.ContatoAdapter;
import br.customercare.tcc.util.tarefas.InsertTarefa;
import br.customercare.tcc.util.tarefas.LeadsAdapter;
import br.customercare.tcc.util.tarefas.MetricaAdapter;
import br.customercare.tcc.util.tarefas.OportunidadeAdapter;

public class InsertTarefasActivity extends BaseDrawerActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Concluída",
            "Em espera", "Deferido"};

    private String[] statusValues = new String[]{"Not Started", "In Progress", "Completed",
            "Waiting on someone else", "Deferred"};

    private String[] priorityNomes = new String[]{"Baixa","Normal", "Alta"};

    private String[] priorityValues = new String[]{"Low", "Normal", "High"};

    private String[] quemNomes = new String[]{"Lead","Contato"};

    private String[] relativoNomes = new String[]{"Campanha","Conta", "Métrica", "Oportunidade"};


    String idQuemNome, idRelativoObjeto, statusSelected, prioritySelected;
    Spinner spinnerStatus, spinnerPrioridade, spinnerQuem, spinnerQuemNome, spinnerRelativo, spinnerRelativoObjeto;
    EditText editAssunto, editDataVencimento, editComentario;
    LinearLayout linearLayout;
    Button btnInserir;

    LeadsAdapter adapterLeads;
    ContatoAdapter adapterContatos;
    CampanhaAdapter adapterCampanha;
    ContaAdapter adapterConta;
    MetricaAdapter adapterMetrica;
    OportunidadeAdapter adapterOportunidade;

    ArrayAdapter<String> adapterRelativo, adapterQuem, adapterPrioridade, adapterStatus;


    private ArrayList<Lead> leads = new ArrayList<Lead>();
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private ArrayList<Account> accounts = new ArrayList<Account>();
    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();
    private ArrayList<Metric> metrics = new ArrayList<Metric>();
    private ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_insert_tarefas);
        getLayoutInflater().inflate(R.layout.activity_insert_tarefas, frameLayout);
        adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusNomes);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        adapterPrioridade = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, priorityNomes);
        adapterPrioridade.setDropDownViewResource(R.layout.spinner_layout);

        adapterQuem = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, quemNomes);
        adapterQuem.setDropDownViewResource(R.layout.spinner_layout);

        adapterRelativo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, relativoNomes);
        adapterRelativo.setDropDownViewResource(R.layout.spinner_layout);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutInsTarefa);

        btnInserir = (Button)findViewById(R.id.btnInsTarefaInsert);

        editAssunto = (EditText)findViewById(R.id.edtInsTarefaAssunto);

        editDataVencimento = (EditText)findViewById(R.id.edtInsTarefaDataVencimento);
        editDataVencimento.addTextChangedListener(Mask.insert("##/##/####", editDataVencimento));

        editComentario = (EditText)findViewById(R.id.edtInsTarefaComentario);
        spinnerQuemNome = (Spinner)findViewById(R.id.spiInsTarefaQuemNome);
        spinnerRelativoObjeto = (Spinner)findViewById(R.id.spiInsTarefaRelativoObjeto);

        spinnerStatus = (Spinner)findViewById(R.id.spiInsTarefaStatus);
        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = statusValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        spinnerPrioridade = (Spinner)findViewById(R.id.spiInsTarefaPrioridade);
        spinnerPrioridade.setAdapter(adapterPrioridade);

        spinnerPrioridade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySelected = priorityValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerQuem = (Spinner)findViewById(R.id.spiInsTarefaQuem);
        spinnerQuem.setAdapter(adapterQuem);

        spinnerQuem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carregaSpinnerQuemRelativo(quemNomes[position]);
                if (quemNomes[position].equals("Lead")) {
                    spinnerQuemNome.setAdapter(adapterLeads);
                } else if (quemNomes[position].equals("Contato")) {
                    spinnerQuemNome.setAdapter(adapterContatos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinnerQuemNome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idQuemNome = view.getTag().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerRelativo = (Spinner)findViewById(R.id.spiInsTarefaRelativo);
        linearLayout.removeView(spinnerRelativo);
        linearLayout.removeView(spinnerRelativoObjeto);

        spinnerRelativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carregaSpinnerQuemRelativo(relativoNomes[position]);
                if(relativoNomes[position].equals("Campanha")){
                    spinnerRelativoObjeto.setAdapter(adapterCampanha);
                }else if(relativoNomes[position].equals("Conta")){
                    spinnerRelativoObjeto.setAdapter(adapterConta);
                }else if(relativoNomes[position].equals("Métrica")){
                    spinnerRelativoObjeto.setAdapter(adapterMetrica);
                }else if(relativoNomes[position].equals("Oportunidade")){
                    spinnerRelativoObjeto.setAdapter(adapterOportunidade);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerRelativoObjeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRelativoObjeto = view.getTag().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void carregaSpinnerQuemRelativo(String optionSelected){
        if(optionSelected.equals("Lead")){
            linearLayout.removeView(spinnerRelativo);
            linearLayout.removeView(spinnerRelativoObjeto);
            idRelativoObjeto = null;
            ConsultLeads consultLead = new ConsultLeads(this);
            try {
                leads = consultLead.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            adapterLeads = new LeadsAdapter(this, leads);
        }else if(optionSelected.equals("Contato")){
            linearLayout.removeView(editComentario);
            linearLayout.removeView(btnInserir);
            linearLayout.addView(spinnerRelativo);
            spinnerRelativo.setAdapter(adapterRelativo);
            linearLayout.addView(spinnerRelativoObjeto);
            linearLayout.addView(editComentario);
            linearLayout.addView(btnInserir);
            spinnerRelativo.setSelection(0);
            ConsultContatos consultContatos = new ConsultContatos(this);
            try {
                contacts = consultContatos.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapterContatos = new ContatoAdapter(this, contacts);
        }else if(optionSelected.equals("Campanha")){
            ConsultCampanha consultCampanha = new ConsultCampanha(this);
            try {
                campaigns = consultCampanha.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapterCampanha = new CampanhaAdapter(this, campaigns);
        }else if(optionSelected.equals("Conta")){
            ConsultContas consultContas = new ConsultContas(this);
            try {
                accounts = consultContas.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapterConta = new ContaAdapter(this, accounts);
        }else if(optionSelected.equals("Métrica")){
            ConsultMetricas consultMetricas = new ConsultMetricas(this);
            try {
                metrics = consultMetricas.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapterMetrica = new MetricaAdapter(this, metrics);
        }else if(optionSelected.equals("Oportunidade")){
            ConsultOportunidades consultOportunidades = new ConsultOportunidades(this);
            try {
                opportunities = consultOportunidades.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapterOportunidade = new OportunidadeAdapter(this, opportunities);
        }
    }

    public void insertTarefa(View view){
        String assunto = editAssunto.getText().toString();
        String dataVencimento = editDataVencimento.getText().toString();
        String comentario = editComentario.getText().toString();
        if(assunto.isEmpty() || prioritySelected == null || statusSelected == null) {
            Toast.makeText(this, "O(S) CAMPO(S): ASSUNTO, PRIORIDADE E STATUS É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
        }else{
            InsertTarefa insertTarefa = new InsertTarefa(this);
            insertTarefa.execute(assunto, dataVencimento, prioritySelected, statusSelected, idQuemNome, idRelativoObjeto, comentario);
        }

    }

}