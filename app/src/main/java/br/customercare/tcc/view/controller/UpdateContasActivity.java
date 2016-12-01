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

import com.sforce.soap.enterprise.sobject.Account;
import java.util.concurrent.ExecutionException;
import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.conta.ConsultOneContas;
import br.customercare.tcc.util.conta.UpdateContas;

public class UpdateContasActivity extends BaseDrawerActivity {

    private String[] classificacaoValues = new String[]{"-- Nenhum --", "Hot", "Warm", "Cold"};

    private String[] tipoValues = new String[]{"-- Nenhum --", "Prospect", "Customer - Direct", "Customer - Channel",
            "Channel Partner / Reseller", "Installation Partner", "Technology Partner",
            "Other"};

    String tipoSelected, classificacaoSelected, idConta;

    Spinner spinnerClassificacao, spinnerTipo;

    EditText editNome, editTelefone, editReceita, editFuncionarios, editEndereco, editSetor, editOrigem;

    ArrayAdapter<String> adapterClassificao, adapterTipo;
    private Account[] conta = new Account[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_contas);
        getLayoutInflater().inflate(R.layout.activity_update_contas, frameLayout);
        adapterTipo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, tipoValues);
        adapterTipo.setDropDownViewResource(R.layout.spinner_layout);

        adapterClassificao = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, classificacaoValues);
        adapterClassificao.setDropDownViewResource(R.layout.spinner_layout);

        editNome = (EditText)findViewById(R.id.edtUpdContaNome);

        editTelefone = (EditText)findViewById(R.id.edtUpdContaTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));

        editReceita = (EditText)findViewById(R.id.edtUpdContaReceita);
        editFuncionarios = (EditText)findViewById(R.id.edtUpdContaFuncionarios);
        editEndereco = (EditText)findViewById(R.id.edtUpdContaEndereco);
        editSetor = (EditText)findViewById(R.id.edtUpdContaSetor);
        editOrigem = (EditText)findViewById(R.id.edtUpdContaOrigem);

        spinnerTipo = (Spinner)findViewById(R.id.spiUpdContaTipo);
        spinnerClassificacao = (Spinner)findViewById(R.id.spiUpdContaClassificacao);

        spinnerTipo.setAdapter(adapterTipo);
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    tipoSelected = null;
                }else {
                    tipoSelected = tipoValues[position];
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
                if(position == 0){
                    classificacaoSelected = null;
                }else {
                    classificacaoSelected = classificacaoValues[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        idConta = getIntent().getStringExtra(ContasActivity.EXTRA_ID);
        ConsultOneContas upConta = new ConsultOneContas(this);

        try {
            conta = upConta.execute(idConta).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < tipoValues.length; i++){
            if(conta[0].getType() !=  null) {
                if (conta[0].getType().equals(tipoValues[i])) {
                    spinnerTipo.setSelection(i);
                }
            }else{
                spinnerTipo.setSelection(0);
            }
        }

        for(int i = 0; i < classificacaoValues.length; i++){
            if(conta[0].getRating() !=  null) {
                if (conta[0].getRating().equals(classificacaoValues[i])) {
                    spinnerClassificacao.setSelection(i);
                }
            }else{
                spinnerClassificacao.setSelection(0);
            }
        }


        if(conta[0].getName() != null)editNome.setText(conta[0].getName());
        if(conta[0].getAccountSource() != null)editOrigem.setText(conta[0].getAccountSource());
        if(conta[0].getPhone() != null)editTelefone.setText(conta[0].getPhone());
        if(conta[0].getIndustry() != null)editSetor.setText(conta[0].getIndustry());
        if(conta[0].getAnnualRevenue() != null)editReceita.setText(Double.toString(conta[0].getAnnualRevenue()));
        if(conta[0].getNumberOfEmployees() != null)editFuncionarios.setText(Integer.toString(conta[0].getNumberOfEmployees()));
        if(conta[0].getBillingStreet() != null)editEndereco.setText(conta[0].getBillingStreet());


    }

    public void updateConta(View view){
        String nome = editNome.getText().toString();
        String origem = editOrigem.getText().toString();
        String telefone = editTelefone.getText().toString();
        String setor = editSetor.getText().toString();
        String receita = editReceita.getText().toString();
        String funcionarios = editFuncionarios.getText().toString();
        String endereco = editEndereco.getText().toString();
        if(nome.isEmpty()) {
            Toast.makeText(this, "O(S) CAMPO(S): NOME É(SÃO) OBRIGATÓRIO(S)", Toast.LENGTH_LONG).show();
        }else{
            UpdateContas updateContas = new UpdateContas(this);
            updateContas.execute(idConta, nome, classificacaoSelected, origem, telefone, setor, tipoSelected, receita, funcionarios, endereco);
        }

    }
}
