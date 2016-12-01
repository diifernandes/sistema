package br.customercare.tcc.view.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.DeleteLead;
import br.customercare.tcc.util.oportunidades.ConsultOneOportunidade;
import br.customercare.tcc.util.oportunidades.DeleteOportunidade;

public class ViewOportunidadeActivity extends BaseDrawerActivity {

    int diaFechamento, mesFechamento, anoFechamento;

    TextView textNome, textConta, textTipo, textLead, textValor, textDataFechamento, textProxEtapa, textFase, textProbabilidade, textCamapanha;

    private Opportunity[] opportunity = new Opportunity[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    private AlertDialog.Builder alert;
    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_oportunidade);
        getLayoutInflater().inflate(R.layout.activity_view_oportunidade, frameLayout);
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


        if(opportunity[0].getName() != null)textNome.setText(opportunity[0].getName());
        try{
            if(opportunity[0].getAccount().getName() != null)textConta.setText(opportunity[0].getAccount().getName());
        }catch (Exception e){}
        if(opportunity[0].getType() != null)textTipo.setText(opportunity[0].getType());
        if(opportunity[0].getLeadSource() != null)textLead.setText(opportunity[0].getLeadSource());
        if(opportunity[0].getAmount() != null)textValor.setText(Double.toString(opportunity[0].getAmount()));
        carregaData(opportunity[0]);
        if(opportunity[0].getNextStep() != null)textProxEtapa.setText(opportunity[0].getNextStep());
        if(opportunity[0].getStageName() != null)textFase.setText(opportunity[0].getStageName());
        if(opportunity[0].getProbability() != null)textProbabilidade.setText(Double.toString(opportunity[0].getProbability()));
        try{
            textCamapanha.setText(opportunity[0].getCampaign().getName());
        }catch (Exception e){}

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
                    updateOportunidade(v);
                    break;
                case R.id.fab2:
                    deleteOportunidade(v);
                    break;

            }
        }
    };


    public void deleteOportunidade(View view){
        alert = new AlertDialog.Builder(this);
        alert.setTitle("ATENÇÃO");
        alert.setMessage("Tem certeza que deseja excluir este registro?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteOportunidade deleteOportunidade = new DeleteOportunidade(ViewOportunidadeActivity.this);
                deleteOportunidade.execute(opportunity[0].getId());
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

    public void updateOportunidade(View view){
        Intent intent = new Intent(getBaseContext(), UpdateOportunidadesActivity.class);
        String idOportunidade = opportunity[0].getId();
        intent.putExtra(EXTRA_ID, idOportunidade);
        startActivity(intent);

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
