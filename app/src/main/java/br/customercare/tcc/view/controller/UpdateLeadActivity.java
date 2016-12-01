package br.customercare.tcc.view.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sforce.soap.enterprise.sobject.Lead;

import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.leads.ConsultOneLead;
import br.customercare.tcc.util.leads.DeleteLead;
import br.customercare.tcc.util.leads.UpdateLead;

public class UpdateLeadActivity extends BaseDrawerActivity {

    private String[] classificacaoValues = new String[]{"-- Nenhum --", "Hot", "Warm", "Cold"};
    private String[] statusValues = new String[]{"-- Nenhum --", "Open - Not Contacted", "Working - Contacted", "Closed - Converted", "Closed - Not Converted"};

    String statusSelected, classificacaoSelected, idLead;
    Spinner spinnerStatus, spinnerClassificacao;
    EditText editNome, editSobrenome, editEmpresa, editOrigem, editSetor, editReceita, editTelefone,
            editEmail, editFuncionarios, editEndereco;

    ArrayAdapter<String> adapterStatus, adapterClassificao;
    private Lead[] lead = new Lead[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_leads);
        getLayoutInflater().inflate(R.layout.activity_update_leads, frameLayout);
        adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusValues);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        adapterClassificao = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, classificacaoValues);
        adapterClassificao.setDropDownViewResource(R.layout.spinner_layout);

        editNome = (EditText) findViewById(R.id.edtUpdLeadNome);
        editSobrenome = (EditText) findViewById(R.id.edtUpdLeadSobrenome);
        editEmpresa = (EditText) findViewById(R.id.edtUpdLeadEmpresa);
        editOrigem = (EditText) findViewById(R.id.edtUpdLeadOrigem);
        editSetor = (EditText) findViewById(R.id.edtUpdLeadSetor);
        editReceita = (EditText) findViewById(R.id.edtUpdLeadReceita);

        editTelefone = (EditText) findViewById(R.id.edtUpdLeadTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));

        editEmail = (EditText) findViewById(R.id.edtUpdLeadEmail);
        editFuncionarios = (EditText) findViewById(R.id.edtUpdLeadFuncionarios);
        editEndereco = (EditText) findViewById(R.id.edtUpdLeadEndereco);

        spinnerStatus = (Spinner) findViewById(R.id.spiUpdLeadStatus);
        spinnerClassificacao = (Spinner) findViewById(R.id.spiUpdLeadClassificacao);

        idLead = getIntent().getStringExtra(LeadsActivity.EXTRA_ID);
        ConsultOneLead consultOneLead = new ConsultOneLead(this);

        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    statusSelected = null;
                } else {
                    statusSelected = statusValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerClassificacao.setAdapter(adapterClassificao);
        spinnerClassificacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    classificacaoSelected = null;
                } else {
                    classificacaoSelected = classificacaoValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        try {
            lead = consultOneLead.execute(idLead).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < statusValues.length; i++) {
            if (lead[0].getStatus() != null) {
                if (lead[0].getStatus().equals(statusValues[i])) {
                    spinnerStatus.setSelection(i);
                }
            } else {
                spinnerStatus.setSelection(0);
            }
        }

        for (int i = 0; i < classificacaoValues.length; i++) {
            if (lead[0].getRating() != null) {
                if (lead[0].getRating().equals(classificacaoValues[i])) {
                    spinnerClassificacao.setSelection(i);
                }
            } else {
                spinnerClassificacao.setSelection(0);
            }
        }

        if (lead[0].getFirstName() != null) editNome.setText(lead[0].getFirstName());
        if (lead[0].getLastName() != null) editSobrenome.setText(lead[0].getLastName());
        if (lead[0].getCompany() != null) editEmpresa.setText(lead[0].getCompany());
        if (lead[0].getLeadSource() != null) editOrigem.setText(lead[0].getLeadSource());
        if (lead[0].getIndustry() != null) editSetor.setText(lead[0].getIndustry());
        if (lead[0].getAnnualRevenue() != null)
            editReceita.setText(Double.toString(lead[0].getAnnualRevenue()));
        if (lead[0].getPhone() != null) editTelefone.setText(lead[0].getPhone());
        if (lead[0].getEmail() != null) editEmail.setText(lead[0].getEmail());
        if (lead[0].getNumberOfEmployees() != null)
            editFuncionarios.setText(Integer.toString(lead[0].getNumberOfEmployees()));
        if (lead[0].getStreet() != null) editEndereco.setText(lead[0].getStreet());
    }


    public void updateLead(View view){
        String nome = editNome.getText().toString();
        String sobrenome = editSobrenome.getText().toString();
        String empresa = editEmpresa.getText().toString();
        String origem = editOrigem.getText().toString();
        String setor = editSetor.getText().toString();
        String receita = editReceita.getText().toString();
        String telefone = editTelefone.getText().toString();
        String email = editEmail.getText().toString();
        String funcionarios = editFuncionarios.getText().toString();
        String endereco = editEndereco.getText().toString();
        if(sobrenome.isEmpty() || empresa.isEmpty() || statusSelected == null) {
            Toast.makeText(this, "O(S) CAMPO(S): SOBRENOME, EMPRESA E STATUS É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
        }else{
            UpdateLead updateLead = new UpdateLead(this);
            updateLead.execute(idLead, nome, sobrenome, empresa, origem, setor, receita, telefone, email, statusSelected, classificacaoSelected, funcionarios, endereco);
        }

    }
}
