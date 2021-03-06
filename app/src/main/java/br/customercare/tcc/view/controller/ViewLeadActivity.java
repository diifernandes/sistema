package br.customercare.tcc.view.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.DeleteLead;
import br.customercare.tcc.util.leads.ConsultOneLead;

public class ViewLeadActivity extends BaseDrawerActivity {

    TextView textNome, textEmpresa, textOrigem, textSetor, textReceita, textTelefone,
            textEmail, textFuncionarios, textEndereco, textStatus, textClassificacao;

    private AlertDialog.Builder alert;
    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();


    private Lead[] lead = new Lead[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_lead);
        getLayoutInflater().inflate(R.layout.activity_view_lead, frameLayout);
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

        if(lead[0].getName() != null)textNome.setText(lead[0].getName());
        if(lead[0].getCompany() != null)textEmpresa.setText(lead[0].getCompany());
        if(lead[0].getLeadSource() != null)textOrigem.setText(lead[0].getLeadSource());
        if(lead[0].getIndustry() != null)textSetor.setText(lead[0].getIndustry());
        if(lead[0].getAnnualRevenue() != null)textReceita.setText(Double.toString(lead[0].getAnnualRevenue()));
        if(lead[0].getPhone() != null)textTelefone.setText(lead[0].getPhone());
        if(lead[0].getEmail() != null)textEmail.setText(lead[0].getEmail());
        if(lead[0].getNumberOfEmployees() != null)textFuncionarios.setText(Integer.toString(lead[0].getNumberOfEmployees()));
        if(lead[0].getStreet() != null)textEndereco.setText(lead[0].getStreet());
        if(lead[0].getStatus() != null)textStatus.setText(lead[0].getStatus());
        if(lead[0].getRating() != null)textClassificacao.setText(lead[0].getRating());


        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);


        fab1.setEnabled(true);
        fab2.setEnabled(true);
        fab3.setEnabled(true);
        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

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
                    updateLead(v);
                    break;
                case R.id.fab2:
                    convertLead(v);
                    break;
                case R.id.fab3:
                    deleteLead(v);
                    break;
            }
        }
    };


    public void deleteLead(View view){
        alert = new AlertDialog.Builder(this);
        alert.setTitle("ATENÇÃO");
        alert.setMessage("Tem certeza que deseja excluir este cadastro?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteLead deleteLead = new DeleteLead(ViewLeadActivity.this);
                deleteLead.execute(lead[0].getId());
            }
        });
        alert.setNeutralButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alert.create();
        alert.show();
    }

    public void updateLead(View view){
        Intent intent = new Intent(this, UpdateLeadActivity.class);
        String idLead = lead[0].getId();
        intent.putExtra(EXTRA_ID, idLead);
        startActivity(intent);
    }

    public void convertLead(View view){
        Intent intent = new Intent(this, ConvertLeadActivity.class);
        String idLead = lead[0].getId();
        intent.putExtra(EXTRA_ID, idLead);
        startActivity(intent);
    }


}
