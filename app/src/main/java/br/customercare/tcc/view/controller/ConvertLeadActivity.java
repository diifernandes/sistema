package br.customercare.tcc.view.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.User;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.AccountSpinnerAdapter;
import br.customercare.tcc.util.leads.ConsultCompanyAccount;
import br.customercare.tcc.util.leads.ConsultOneLead;
import br.customercare.tcc.util.leads.ConsultOwnerLead;
import br.customercare.tcc.util.leads.ConvertLead;

public class ConvertLeadActivity extends BaseDrawerActivity {

    private User[] ownerLead;
    private Lead[] lead;
    String idAcc;
    CheckBox checkBox;
    EditText editOpportunity;
    TextView txtPropetario;
    Spinner spinnerAccount;
    private ArrayList<Account> acc = new ArrayList<Account>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_convert_lead);
        getLayoutInflater().inflate(R.layout.activity_convert_lead, frameLayout);
        checkBox = (CheckBox)findViewById(R.id.chkConvLeadOportunidade);
        editOpportunity = (EditText)findViewById(R.id.edtConvLeadOportunidadeNome);
        txtPropetario = (TextView)findViewById(R.id.txtConvLeadProprietario);
        spinnerAccount = (Spinner)findViewById(R.id.spiConvLeadConta);

        String idLead = getIntent().getStringExtra(LeadsActivity.EXTRA_ID);
        ConsultOwnerLead consultOwnerLead = new ConsultOwnerLead(this);
        ConsultCompanyAccount consultCompanyAccount = new ConsultCompanyAccount(this);
        ConsultOneLead consultOneLead = new ConsultOneLead(this);

        try {
            ownerLead = consultOwnerLead.execute(idLead).get();
            acc = consultCompanyAccount.execute(idLead).get();
            lead = consultOneLead.execute(idLead).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        txtPropetario.setText(ownerLead[0].getName());

        AccountSpinnerAdapter adapter = new AccountSpinnerAdapter(this, acc);
        spinnerAccount.setAdapter(adapter);

        spinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idAcc = view.getTag().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    editOpportunity.setVisibility(View.INVISIBLE);
                }else{
                    editOpportunity.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void convert(View view){
        ConvertLead convertLead = new ConvertLead(this);
        String nameOpp = null;
        if(checkBox.isChecked() == false) {
            nameOpp = editOpportunity.getText().toString();
            if(nameOpp.isEmpty()) {
                Toast.makeText(this, "O(S) CAMPO(S): NOME DA OPORTUNIDADE É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
            }else {
                convertLead.execute(lead[0].getId(), idAcc, nameOpp);
            }
        }else {
            convertLead.execute(lead[0].getId(), idAcc, nameOpp);
        }
    }
}
