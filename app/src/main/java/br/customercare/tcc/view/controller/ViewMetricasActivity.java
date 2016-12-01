package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Metric;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.conta.ConsultOneContas;
import br.customercare.tcc.util.conta.DeleteConta;
import br.customercare.tcc.util.metas.ConsultOneMetrica;
import br.customercare.tcc.util.metas.ConsultOwnerMeta;
import br.customercare.tcc.util.metas.DeleteMetrica;
import br.customercare.tcc.util.leads.DeleteLead;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ViewMetricasActivity extends BaseDrawerActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Atrasado", "Crítico", "Concluída",
            "Adiado", "Cancelado", "Não concluído"};

    private String[] statusValues = new String[]{"NotStarted", "OnTrack", "Behind", "Critical",
            "Completed", "Postponed", "Canceled", "Not Completed"};

    String propMeta;
    private AlertDialog.Builder alert;

    int diaInicio, mesInicio, anoInicio, diaVencimento, mesVencimento, anoVencimento;

    TextView textTipo, textStatus, textOwnerMeta, textMeta, textNome, textDataInicio, textDataVencimento, textValorDestino, textValorInicial, textValorAtual,
            textDescricao, textComentario, textTitleProgress, textProgresso;

    LinearLayout principalLayout;

    private Metric[] metrica = new Metric[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_metricas);
        getLayoutInflater().inflate(R.layout.activity_view_metricas, frameLayout);
        principalLayout = (LinearLayout)findViewById(R.id.linearLayoutViewMetrica);

        textNome = (TextView)findViewById(R.id.txtViewMetricaValueNome);

        textTipo = (TextView)findViewById(R.id.txtViewMetricaValueTipo);
        textStatus = (TextView)findViewById(R.id.txtViewMetricaValueStatus);
        textOwnerMeta = (TextView)findViewById(R.id.txtViewMetricaValueOwnerMeta);
        textMeta = (TextView)findViewById(R.id.txtViewMetricaValueMeta);
        textProgresso = (TextView)findViewById(R.id.txtViewMetricaValueProgresso);

        textDataInicio = (TextView)findViewById(R.id.txtViewMetricaValueDataInicio);
        textDataVencimento = (TextView)findViewById(R.id.txtViewMetricaValueDataVencimento);

        textTitleProgress = (TextView)findViewById(R.id.txtViewMetricaTitleProgress);

        textValorAtual = (TextView)findViewById(R.id.txtViewMetricaValueValorAtual);
        textValorInicial = (TextView)findViewById(R.id.txtViewMetricaValueValorInicial);
        textValorDestino = (TextView)findViewById(R.id.txtViewMetricaValueValorDestino);

        textDescricao = (TextView)findViewById(R.id.txtViewMetricaValueDescricao);
        textComentario = (TextView)findViewById(R.id.txtViewMetricaValueComentario);

        String idMetrica = getIntent().getStringExtra(MetricasActivity.EXTRA_ID);
        ConsultOneMetrica consultOneMetrica = new ConsultOneMetrica(this);
        ConsultOwnerMeta consult = new ConsultOwnerMeta(this);

        try {
            metrica = consultOneMetrica.execute(idMetrica).get();
            propMeta = consult.execute(idMetrica).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        textTipo.setText(metrica[0].getRecordType().getName());
        textOwnerMeta.setText(propMeta);
        textMeta.setText(metrica[0].getGoal().getName());
        textProgresso.setText(metrica[0].getProgress() + "%");

        for(int i = 0; i < statusValues.length ; i++){
            if(metrica[0].getStatus().equals(statusValues[i].toString())){
                textStatus.setText(statusNomes[i]);
                break;
            }
        }
        carregaValores(metrica[0].getRecordType().getName());

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);



        fab1.setEnabled(true);
        fab2.setEnabled(true);

        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);


        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuRed.isOpened()) {
                    //Toast.makeText(getActivity(), menuRed.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuRed.toggle(true);
            }
        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    updateMetrica(v);
                    break;
                case R.id.fab2:
                    deleteMetrica(v);
                    break;

            }
        }
    };
    public void deleteMetrica(View view){
        alert = new AlertDialog.Builder(this);
        alert.setTitle("ATENÇÃO");
        alert.setMessage("Tem certeza que deseja excluir este cadastro?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteMetrica delMeta = new DeleteMetrica(ViewMetricasActivity.this);
                delMeta.execute(metrica[0].getId());
            }
        });
        alert.setNeutralButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create();
        alert.show();
    }

    public void updateMetrica(View view){
        Intent intent = new Intent(getBaseContext(), UpdateMetricasActivity.class);
        String idMetrica = metrica[0].getId();
        intent.putExtra(EXTRA_ID, idMetrica);
        startActivity(intent);

    }

    public void carregaValores(String recordType){
        if(recordType.equals("Progress")){
            if(metrica[0].getName() != null)textNome.setText(metrica[0].getName());
            if(metrica[0].getDescription() != null)textDescricao.setText(metrica[0].getDescription());
            if(metrica[0].getLastComment() != null)textComentario.setText(metrica[0].getLastComment());
            if(metrica[0].getCurrentValue() != null)textValorAtual.setText(metrica[0].getCurrentValue().toString());
            if(metrica[0].getInitialValue() != null)textValorInicial.setText(metrica[0].getInitialValue().toString());
            if(metrica[0].getTargetValue() != null)textValorDestino.setText(metrica[0].getTargetValue().toString());
            carregaDatas();
        }else{
            if(metrica[0].getName() != null)textNome.setText(metrica[0].getName());
            if(metrica[0].getDescription() != null)textDescricao.setText(metrica[0].getDescription());
            if(metrica[0].getLastComment() != null)textComentario.setText(metrica[0].getLastComment());
            carregaDatas();
        }
    }

    public void carregaDatas(){
        if(metrica[0].getStartDate() != null) {
            Calendar dataInicio = metrica[0].getStartDate();
            diaInicio = dataInicio.get(Calendar.DAY_OF_MONTH);
            mesInicio = dataInicio.get(Calendar.MONTH) + 1;
            anoInicio = dataInicio.get(Calendar.YEAR);

            textDataInicio.setText("");
            textDataInicio.setText(new DecimalFormat("00").format(diaInicio) + "/" + new DecimalFormat("00").format(mesInicio) + "/" + new DecimalFormat("00").format(anoInicio), TextView.BufferType.EDITABLE);
        }

        if(metrica[0].getDueDate() != null) {
            Calendar dataVencimento = metrica[0].getDueDate();
            diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesVencimento = dataVencimento.get(Calendar.MONTH) + 1;
            anoVencimento = dataVencimento.get(Calendar.YEAR);

            textDataVencimento.setText("");
            textDataVencimento.setText(new DecimalFormat("00").format(diaVencimento) + "/" + new DecimalFormat("00").format(mesVencimento) + "/" + new DecimalFormat("00").format(anoVencimento), TextView.BufferType.EDITABLE);
        }
    }
}
