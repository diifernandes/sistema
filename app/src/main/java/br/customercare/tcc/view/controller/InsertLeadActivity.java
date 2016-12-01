package br.customercare.tcc.view.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.customercare.tcc.R;
import br.customercare.tcc.util.Mask;
import br.customercare.tcc.util.leads.InsertLead;

public class InsertLeadActivity extends BaseDrawerActivity {

    private String[] classificacaoValues = new String[]{"-- Nenhum --", "Hot", "Warm", "Cold"};
    private String[] statusValues = new String[]{"-- Nenhum --", "Open - Not Contacted", "Working - Contacted", "Closed - Converted", "Closed - Not Converted"};

    String statusSelected, classificacaoSelected;
    Spinner spinnerStatus, spinnerClassificacao;
    EditText editNome, editSobrenome, editEmpresa, editOrigem, editSetor, editReceita, editTelefone,
            editEmail, editFuncionarios, editEndereco;

    ArrayAdapter<String> adapterStatus, adapterClassificao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_insert_leads);
        getLayoutInflater().inflate(R.layout.activity_insert_leads, frameLayout);
        adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, statusValues);
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        adapterClassificao = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, classificacaoValues);
        adapterClassificao.setDropDownViewResource(R.layout.spinner_layout);

        editNome = (EditText)findViewById(R.id.edtInsLeadNome);
        editSobrenome = (EditText)findViewById(R.id.edtInsLeadSobrenome);
        editEmpresa = (EditText)findViewById(R.id.edtInsLeadEmpresa);
        editOrigem = (EditText)findViewById(R.id.edtInsLeadOrigem);
        editSetor = (EditText)findViewById(R.id.edtInsLeadSetor);
        editReceita = (EditText)findViewById(R.id.edtInsLeadReceita);

        editTelefone = (EditText)findViewById(R.id.edtInsLeadTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));

        editEmail = (EditText)findViewById(R.id.edtInsLeadEmail);
        editFuncionarios = (EditText)findViewById(R.id.edtInsLeadFuncionarios);
        editEndereco = (EditText)findViewById(R.id.edtInsLeadEndereco);

        spinnerStatus = (Spinner)findViewById(R.id.spiInsLeadStatus);
        spinnerClassificacao = (Spinner)findViewById(R.id.spiInsLeadClassificacao);

        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    statusSelected = null;
                }else {
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

    }

    public void insertLead(View view){
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
            InsertLead insertLead = new InsertLead(this);
            insertLead.execute(nome, sobrenome, empresa, origem, setor, receita, telefone, email, statusSelected, classificacaoSelected, funcionarios, endereco);
        }
    }
}
