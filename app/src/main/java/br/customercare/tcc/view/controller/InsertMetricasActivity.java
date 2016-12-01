package br.customercare.tcc.view.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.metas.ConsultMetaAberta;
import br.customercare.tcc.util.metas.GoalSpinnerAdapter;
import br.customercare.tcc.util.metas.InsertMetrica;
import br.customercare.tcc.util.metas.RecordSpinnerAdapter;
import br.customercare.tcc.util.metas.ConsultRecordTypeMeta;

import com.sforce.soap.enterprise.sobject.Goal;
import com.sforce.soap.enterprise.sobject.RecordType;

public class InsertMetricasActivity extends BaseDrawerActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Atrasado", "Crítico", "Concluída",
            "Adiado", "Cancelado", "Não concluído"};

    private String[] statusValues = new String[]{"NotStarted", "OnTrack", "Behind", "Critical",
            "Completed", "Postponed", "Canceled", "Not Completed"};

    String idRecordType, idMeta, statusSelected, recordOption;
    LinearLayout principalLayout;
    TextView textTitleProgress;
    Spinner spinnerRecordType, spinnerMeta, spinnerStatus;
    EditText editNome, editDataInicio, editDataVencimento, editValorDestino, editValorInicial, editValorAtual,
             editDescricao, editComentario;

    private ArrayList<RecordType> rec = new ArrayList<RecordType>();
    private ArrayList<Goal> meta = new ArrayList<Goal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_insert_metricas);
        getLayoutInflater().inflate(R.layout.activity_insert_metricas, frameLayout);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusNomes);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        principalLayout = (LinearLayout)findViewById(R.id.linearLayoutInsMetrica);

        editNome = (EditText)findViewById(R.id.edtInsMetricaNome);

        editDataInicio = (EditText)findViewById(R.id.edtInsMetricaDataInicio);
        editDataInicio.addTextChangedListener(Mask.insert("##/##/####", editDataInicio));

        editDataVencimento = (EditText)findViewById(R.id.edtInsMetricaDataVencimento);
        editDataVencimento.addTextChangedListener(Mask.insert("##/##/####", editDataVencimento));

        editValorAtual = (EditText)findViewById(R.id.edtInsMetricaValorAtual);
        editValorInicial = (EditText)findViewById(R.id.edtInsMetricaValorInicial);
        editValorDestino = (EditText)findViewById(R.id.edtInsMetricaValorDestino);

        textTitleProgress = (TextView)findViewById(R.id.txtInsMetricaTitleProgress);

        editDescricao = (EditText)findViewById(R.id.edtInsMetricaDescricao);
        editComentario = (EditText)findViewById(R.id.edtInsMetricaComentario);

        recordOption = "0";

        ConsultRecordTypeMeta consultRecordTypeMeta = new ConsultRecordTypeMeta(this);
        ConsultMetaAberta consultMetaAberta = new ConsultMetaAberta(this);

        try {
            rec = consultRecordTypeMeta.execute().get();
            meta = consultMetaAberta.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        spinnerRecordType = (Spinner)findViewById(R.id.spiInsMetricaTipoRegistro);
        RecordSpinnerAdapter adapter = new RecordSpinnerAdapter(this, rec);
        spinnerRecordType.setAdapter(adapter);


        spinnerRecordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRecordType = view.getTag().toString();
                recordOption = Integer.toString(position);
                carregaCampos(recordOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerMeta = (Spinner)findViewById(R.id.spiInsMetricaMeta);
        GoalSpinnerAdapter adapterMeta = new GoalSpinnerAdapter(this, meta);
        spinnerMeta.setAdapter(adapterMeta);

        spinnerMeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idMeta = view.getTag().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerStatus = (Spinner)findViewById(R.id.spiInsMetricaStatus);
        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = statusValues[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

    }

    public void carregaCampos(String recordOption){
        if(recordOption.equals("0")){
            principalLayout.removeAllViews();
            principalLayout.addView(editNome);
            principalLayout.addView(editDataVencimento);
            principalLayout.addView(editDescricao);
            principalLayout.addView(textTitleProgress);
            principalLayout.addView(editValorAtual);
            principalLayout.addView(editValorInicial);
            principalLayout.addView(editValorDestino);
            principalLayout.addView(editComentario);
        }else{
            principalLayout.removeAllViews();
            principalLayout.addView(editNome);
            principalLayout.addView(editDataInicio);
            principalLayout.addView(editDataVencimento);
            principalLayout.addView(editDescricao);
            principalLayout.addView(editComentario);
        }
    }

    public void insertMetrica(View view){
        String nome = editNome.getText().toString();
        String dataInicio = editDataInicio.getText().toString();
        String dataVencimento = editDataVencimento.getText().toString();
        String valorAtual = editValorAtual.getText().toString();
        String valorInicial = editValorInicial.getText().toString();
        String valorDestino = editValorDestino.getText().toString();
        String descricao = editDescricao.getText().toString();
        String comentario = editComentario.getText().toString();
        if(recordOption.equals("0")){
            if(nome.isEmpty() || valorDestino.isEmpty() || statusSelected == null) {
                Toast.makeText(this, "O(S) CAMPO(S): NOME, VALOR DE DESTINO E STATUS É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
            }else{
                InsertMetrica insertMetrica = new InsertMetrica(this);
                insertMetrica.execute(recordOption, idRecordType, nome ,idMeta, statusSelected, dataInicio, dataVencimento, valorAtual, valorInicial, valorDestino, descricao, comentario);
            }
        }else if(recordOption.equals("1")){
            if(nome.isEmpty() || statusSelected == null) {
                Toast.makeText(this, "O(S) CAMPO(S): NOME E STATUS É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
            }else{
                InsertMetrica insertMetrica = new InsertMetrica(this);
                insertMetrica.execute(recordOption, idRecordType, nome ,idMeta, statusSelected, dataInicio, dataVencimento, valorAtual, valorInicial, valorDestino, descricao, comentario);
            }
        }
    }

}