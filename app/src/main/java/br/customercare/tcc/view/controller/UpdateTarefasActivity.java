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
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.soap.enterprise.sobject.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.tarefas.CampanhaAdapter;
import br.customercare.tcc.util.tarefas.ConsultCampanha;
import br.customercare.tcc.util.tarefas.ConsultContas;
import br.customercare.tcc.util.tarefas.ConsultContatos;
import br.customercare.tcc.util.tarefas.ConsultLeads;
import br.customercare.tcc.util.tarefas.ConsultMetricas;
import br.customercare.tcc.util.tarefas.ConsultOneTarefa;
import br.customercare.tcc.util.tarefas.ConsultOportunidades;
import br.customercare.tcc.util.tarefas.ContaAdapter;
import br.customercare.tcc.util.tarefas.ContatoAdapter;
import br.customercare.tcc.util.tarefas.LeadsAdapter;
import br.customercare.tcc.util.tarefas.MetricaAdapter;
import br.customercare.tcc.util.tarefas.OportunidadeAdapter;
import br.customercare.tcc.util.tarefas.UpdateTarefa;

public class UpdateTarefasActivity extends AppCompatActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Concluída",
            "Em espera", "Deferido"};

    private String[] statusValues = new String[]{"Not Started", "In Progress", "Completed",
            "Waiting on someone else", "Deferred"};

    private String[] priorityNomes = new String[]{"Baixa","Normal", "Alta"};

    private String[] priorityValues = new String[]{"Low", "Normal", "High"};

    private String[] quemNomes = new String[]{"Lead","Contato"};

    private String[] relativoNomes = new String[]{"Campanha","Conta", "Métrica", "Oportunidade"};

    int diaVencimento, mesVencimento, anoVencimento;
    String idQuemNome, idRelativoObjeto, statusSelected, prioritySelected;
    Spinner spinnerStatus, spinnerPrioridade, spinnerQuemNome, spinnerRelativo, spinnerRelativoObjeto;
    EditText editAssunto, editDataVencimento, editComentario;
    TextView txtQuem;
    LinearLayout linearLayout;
    Button btnAtualizar;
    Boolean loadWhat = true;

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
    private Task[] task = new Task[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tarefas);

        adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusNomes);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        adapterPrioridade = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, priorityNomes);
        adapterPrioridade.setDropDownViewResource(R.layout.spinner_layout);

        adapterQuem = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, quemNomes);
        adapterQuem.setDropDownViewResource(R.layout.spinner_layout);

        adapterRelativo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, relativoNomes);
        adapterRelativo.setDropDownViewResource(R.layout.spinner_layout);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutUpdTarefa);

        editAssunto = (EditText) findViewById(R.id.edtUpdTarefaAssunto);

        editDataVencimento = (EditText) findViewById(R.id.edtUpdTarefaDataVencimento);
        editDataVencimento.addTextChangedListener(Mask.insert("##/##/####", editDataVencimento));

        spinnerPrioridade = (Spinner) findViewById(R.id.spiUpdTarefaPrioridade);
        spinnerStatus = (Spinner) findViewById(R.id.spiUpdTarefaStatus);
        txtQuem = (TextView) findViewById(R.id.txtUpdTarefaQuem);
        spinnerQuemNome = (Spinner) findViewById(R.id.spiUpdTarefaQuemNome);
        spinnerRelativo = (Spinner) findViewById(R.id.spiUpdTarefaRelativo);
        spinnerRelativoObjeto = (Spinner) findViewById(R.id.spiUpdTarefaRelativoObjeto);

        editComentario = (EditText) findViewById(R.id.edtUpdTarefaComentario);

        btnAtualizar = (Button) findViewById(R.id.btnUpdTarefaUpdate);

        String idTarefa = getIntent().getStringExtra(ViewTarefaActivity.EXTRA_ID);
        ConsultOneTarefa consultOneTarefa = new ConsultOneTarefa(this);

        try {
            task = consultOneTarefa.execute(idTarefa).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = statusValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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

        spinnerRelativo.setAdapter(adapterRelativo);
        linearLayout.removeView(spinnerRelativo);
        linearLayout.removeView(spinnerRelativoObjeto);

        spinnerRelativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carregaSpinnerQuemRelativo(relativoNomes[position]);
                if (relativoNomes[position].equals("Campanha")) {
                    spinnerRelativoObjeto.setAdapter(adapterCampanha);
                    if (loadWhat){
                        for (int i = 0; i < spinnerRelativoObjeto.getCount(); i++){
                            if(task[0].getWhatId().equals(campaigns.get(i).getId())){
                                spinnerRelativoObjeto.setSelection(i);
                            }
                        }
                        loadWhat = false;
                    }
                } else if (relativoNomes[position].equals("Conta")) {
                    spinnerRelativoObjeto.setAdapter(adapterConta);
                    if (loadWhat){
                        for (int i = 0; i < spinnerRelativoObjeto.getCount(); i++){
                            if(task[0].getWhatId().equals(accounts.get(i).getId())){
                                spinnerRelativoObjeto.setSelection(i);
                            }
                        }
                        loadWhat = false;
                    }
                } else if (relativoNomes[position].equals("Métrica")) {
                    spinnerRelativoObjeto.setAdapter(adapterMetrica);
                    if (loadWhat){
                        for (int i = 0; i < spinnerRelativoObjeto.getCount(); i++){
                            if(task[0].getWhatId().equals(metrics.get(i).getId())){
                                spinnerRelativoObjeto.setSelection(i);
                            }
                        }
                        loadWhat = false;
                    }
                } else if (relativoNomes[position].equals("Oportunidade")) {
                    spinnerRelativoObjeto.setAdapter(adapterOportunidade);
                    if (loadWhat){
                        for (int i = 0; i < spinnerRelativoObjeto.getCount(); i++){
                            if(task[0].getWhatId().equals(opportunities.get(i).getId())){
                                spinnerRelativoObjeto.setSelection(i);
                            }
                        }
                        loadWhat = false;
                    }
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

        spinnerRelativoObjeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRelativoObjeto = view.getTag().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        for(int i = 0; i < statusValues.length; i++){
            if(task[0].getStatus().equals(statusValues[i])){
                spinnerStatus.setSelection(i);
            }
        }

        for(int i = 0; i < priorityValues.length; i++){
            if(task[0].getPriority().equals(priorityValues[i])){
                spinnerPrioridade.setSelection(i);
            }
        }

        editAssunto.setText(task[0].getSubject());
        carregaData();
        editComentario.setText(task[0].getDescription());
        carregaQuem();
    }

    public void carregaData(){
        if(task[0].getActivityDate() != null) {
            Calendar dataVencimento = task[0].getActivityDate();
            diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesVencimento = dataVencimento.get(Calendar.MONTH) + 1;
            anoVencimento = dataVencimento.get(Calendar.YEAR);

            editDataVencimento.setText("");
            editDataVencimento.setText(new DecimalFormat("00").format(diaVencimento) + "/" + new DecimalFormat("00").format(mesVencimento) + "/" + new DecimalFormat("00").format(anoVencimento), TextView.BufferType.EDITABLE);
        }
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
            linearLayout.removeView(btnAtualizar);
            linearLayout.addView(spinnerRelativo);
            spinnerRelativo.setAdapter(adapterRelativo);
            linearLayout.addView(spinnerRelativoObjeto);
            linearLayout.addView(editComentario);
            linearLayout.addView(btnAtualizar);
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

    public void carregaQuem(){
        String whoId = task[0].getWhoId();
        if(whoId.substring(0,3).equals("00Q")){
            txtQuem.setText("Lead");
            carregaSpinnerQuemRelativo("Lead");
            spinnerQuemNome.setAdapter(adapterLeads);
            for (int i = 0; i < spinnerQuemNome.getCount(); i++){
                if(task[0].getWhoId().equals(leads.get(i).getId())){
                    spinnerQuemNome.setSelection(i);
                }
            }

        }else if(whoId.substring(0,3).equals("003")) {
            txtQuem.setText("Contato");
            carregaSpinnerQuemRelativo("Contato");
            spinnerQuemNome.setAdapter(adapterContatos);
            for (int i = 0; i < spinnerQuemNome.getCount(); i++){
                if (task[0].getWhoId().equals(contacts.get(i).getId())){
                    spinnerQuemNome.setSelection(i);
                }
            }
            carregaRelativo();
        }
    }

    public void carregaRelativo(){
        String whatId = task[0].getWhatId();
        if(whatId.substring(0,3).equals("701")){
            spinnerRelativo.setSelection(0);
        }else if(whatId.substring(0,3).equals("001")) {
            spinnerRelativo.setSelection(1);
        }else if(whatId.substring(0,3).equals("0WJ")) {
            spinnerRelativo.setSelection(2);
        }else if(whatId.substring(0,3).equals("006")) {
            spinnerRelativo.setSelection(3);
        }

    }

    public void updateTarefa(View view){
        UpdateTarefa updateTarefa = new UpdateTarefa(this);
        String assunto = editAssunto.getText().toString();
        String dataVencimento = editDataVencimento.getText().toString();
        String comentario = editComentario.getText().toString();
        String idTarefa = task[0].getId();
        updateTarefa.execute(idTarefa, assunto, dataVencimento, prioritySelected, statusSelected, idRelativoObjeto, idQuemNome, comentario);
    }

}