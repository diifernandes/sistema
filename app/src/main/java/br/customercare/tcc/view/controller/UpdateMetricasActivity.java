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

import com.sforce.soap.enterprise.sobject.Goal;
import com.sforce.soap.enterprise.sobject.Metric;
import com.sforce.soap.enterprise.sobject.RecordType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;

import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.metas.ConsultMetaAberta;
import br.customercare.tcc.util.metas.ConsultOneMetrica;
import br.customercare.tcc.util.metas.ConsultRecordTypeMeta;
import br.customercare.tcc.util.metas.GoalSpinnerAdapter;
import br.customercare.tcc.util.metas.RecordSpinnerAdapter;
import br.customercare.tcc.util.metas.UpdateMetrica;

public class UpdateMetricasActivity extends AppCompatActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Atrasado", "Crítico", "Concluída",
            "Adiado", "Cancelado", "Não concluído"};

    private String[] statusValues = new String[]{"NotStarted", "OnTrack", "Behind", "Critical",
            "Completed", "Postponed", "Canceled", "Not Completed"};

    int diaInicio, mesInicio, anoInicio, diaVencimento, mesVencimento, anoVencimento;
    String idRecordType, idMeta, statusSelected, recordOption;
    LinearLayout principalLayout;
    TextView textTitleProgress;
    Spinner spinnerRecordType, spinnerMeta, spinnerStatus;
    EditText editNome, editDataInicio, editDataVencimento, editValorDestino, editValorInicial, editValorAtual,
            editDescricao, editComentario;

    private ArrayList<RecordType> rec = new ArrayList<RecordType>();
    private ArrayList<Goal> meta = new ArrayList<Goal>();
    private Metric[] metrica = new Metric[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_metricas);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusNomes);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        principalLayout = (LinearLayout)findViewById(R.id.linearLayoutUpdMetrica);

        editNome = (EditText)findViewById(R.id.edtUpdMetricaNome);

        editDataInicio = (EditText)findViewById(R.id.edtUpdMetricaDataInicio);
        editDataInicio.addTextChangedListener(Mask.insert("##/##/####", editDataInicio));

        editDataVencimento = (EditText)findViewById(R.id.edtUpdMetricaDataVencimento);
        editDataVencimento.addTextChangedListener(Mask.insert("##/##/####", editDataVencimento));

        editValorAtual = (EditText)findViewById(R.id.edtUpdMetricaValorAtual);
        editValorInicial = (EditText)findViewById(R.id.edtUpdMetricaValorInicial);
        editValorDestino = (EditText)findViewById(R.id.edtUpdMetricaValorDestino);

        textTitleProgress = (TextView)findViewById(R.id.txtUpdMetricaTitleProgress);

        editDescricao = (EditText)findViewById(R.id.edtUpdMetricaDescricao);
        editComentario = (EditText)findViewById(R.id.edtUpdMetricaComentario);

        String idMetrica = getIntent().getStringExtra(MetricasActivity.EXTRA_ID);
        ConsultOneMetrica consultOneMetrica = new ConsultOneMetrica(this);
        ConsultRecordTypeMeta consultRecordTypeMeta = new ConsultRecordTypeMeta(this);
        ConsultMetaAberta consultMetaAberta = new ConsultMetaAberta(this);


        try {
            metrica = consultOneMetrica.execute(idMetrica).get();
            rec = consultRecordTypeMeta.execute().get();
            meta = consultMetaAberta.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        spinnerRecordType = (Spinner)findViewById(R.id.spiUpdMetricaTipoRegistro);
        RecordSpinnerAdapter adapterRecord = new RecordSpinnerAdapter(this, rec);
        spinnerRecordType.setAdapter(adapterRecord);


        spinnerRecordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRecordType = view.getTag().toString();
                recordOption = Integer.toString(position);
                carregaValores(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerMeta = (Spinner)findViewById(R.id.spiUpdMetricaMeta);
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

        spinnerStatus = (Spinner)findViewById(R.id.spiUpdMetricaStatus);
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

        for(int i = 0; i < spinnerRecordType.getCount(); i++){
            if(metrica[0].getRecordType().getName().equals(rec.get(i).getName())){
                spinnerRecordType.setSelection(i);
                break;
            }
        }

        for(int i = 0; i < spinnerMeta.getCount(); i++){
            if(metrica[0].getGoal().getName().equals(meta.get(i).getName())){
                spinnerMeta.setSelection(i);
                break;
            }
        }

        for(int i = 0; i < spinnerStatus.getCount(); i++){
            if(metrica[0].getStatus().equals(statusValues[i].toString())){
                spinnerStatus.setSelection(i);
                break;
            }
        }

        //carregaValores(metrica[0].getRecordType().getName());
    }

    public void updateMetrica(View view){
        UpdateMetrica updateMetrica = new UpdateMetrica(this);
        String nome = editNome.getText().toString();
        String dataInicio = editDataInicio.getText().toString();
        String dataVencimento = editDataVencimento.getText().toString();
        String valorAtual = editValorAtual.getText().toString();
        String valorInicial = editValorInicial.getText().toString();
        String valorDestino = editValorDestino.getText().toString();
        String descricao = editDescricao.getText().toString();
        String comentario = editComentario.getText().toString();
        String idMetrica = metrica[0].getId();
        updateMetrica.execute(idMetrica, recordOption, idRecordType, nome ,idMeta, statusSelected, dataInicio, dataVencimento, valorAtual, valorInicial, valorDestino, descricao, comentario);
    }

    public void manipulaData(){
        if(metrica[0].getStartDate() != null) {
            Calendar dataInicio = metrica[0].getStartDate();
            diaInicio = dataInicio.get(Calendar.DAY_OF_MONTH);
            mesInicio = dataInicio.get(Calendar.MONTH) + 1;
            anoInicio = dataInicio.get(Calendar.YEAR);

            editDataInicio.setText("");
            editDataInicio.setText(new DecimalFormat("00").format(diaInicio) + "/" + new DecimalFormat("00").format(mesInicio) + "/" + new DecimalFormat("00").format(anoInicio), TextView.BufferType.EDITABLE);
        }

        if(metrica[0].getDueDate() != null) {
            Calendar dataVencimento = metrica[0].getDueDate();
            diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesVencimento = dataVencimento.get(Calendar.MONTH) + 1;
            anoVencimento = dataVencimento.get(Calendar.YEAR);

            editDataVencimento.setText("");
            editDataVencimento.setText(new DecimalFormat("00").format(diaVencimento) + "/" + new DecimalFormat("00").format(mesVencimento) + "/" + new DecimalFormat("00").format(anoVencimento), TextView.BufferType.EDITABLE);
        }
    }

    public void carregaValores(Integer recordType){
        if(recordType == 0){
            /*Monta os campos necessários*/
            principalLayout.removeAllViews();
            principalLayout.addView(editNome);
            principalLayout.addView(editDataVencimento);
            principalLayout.addView(editDescricao);
            principalLayout.addView(textTitleProgress);
            principalLayout.addView(editValorAtual);
            principalLayout.addView(editValorInicial);
            principalLayout.addView(editValorDestino);
            principalLayout.addView(editComentario);
            /*Carrega Valores*/
            try{
                editNome.setText(metrica[0].getName(), TextView.BufferType.EDITABLE);
                editDescricao.setText(metrica[0].getDescription(), TextView.BufferType.EDITABLE);
                editComentario.setText(metrica[0].getLastComment(), TextView.BufferType.EDITABLE);
                editValorAtual.setText(metrica[0].getCurrentValue().toString(), TextView.BufferType.EDITABLE);
                editValorInicial.setText(metrica[0].getInitialValue().toString(), TextView.BufferType.EDITABLE);
                editValorDestino.setText(metrica[0].getTargetValue().toString(), TextView.BufferType.EDITABLE);
            } catch (NullPointerException e) {

            }
            manipulaData();
        }else{
            /*Monta os campos necessários*/
            principalLayout.removeAllViews();
            principalLayout.addView(editNome);
            principalLayout.addView(editDataInicio);
            principalLayout.addView(editDataVencimento);
            principalLayout.addView(editDescricao);
            principalLayout.addView(editComentario);
            /*Carrega Valores*/
            try {
                editNome.setText(metrica[0].getName(), TextView.BufferType.EDITABLE);
                editDescricao.setText(metrica[0].getDescription(), TextView.BufferType.EDITABLE);
                editComentario.setText(metrica[0].getLastComment(), TextView.BufferType.EDITABLE);
            }catch (NullPointerException e){

            }
            manipulaData();
        }
    }
}
