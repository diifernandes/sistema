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
import br.customercare.tcc.util.conta.InsertConta;

public class InsertContasActivity extends BaseDrawerActivity {

    private String[] classificacaoValues = new String[]{"-- Nenhum --", "Hot", "Warm", "Cold"};
    private String[] tipoValues = new String[]{"-- Nenhum --", "Prospect", "Customer - Direct", "Customer - Channel",
                                                "Channel Partner / Reseller", "Installation Partner", "Technology Partner",
                                                "Other"};
    String tipoSelected, classificacaoSelected;
    Spinner spinnerClassificacao, spinnerTipo;
    EditText editNome, editTelefone, editReceita, editFuncionarios, editEndereco, editSetor, editOrigem;

    ArrayAdapter<String> adapterClassificao, adapterTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_insert_contas);
        getLayoutInflater().inflate(R.layout.activity_insert_contas, frameLayout);
        adapterTipo = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, tipoValues);
        adapterTipo.setDropDownViewResource(R.layout.spinner_layout);

        adapterClassificao = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text_spinner, classificacaoValues);
        adapterClassificao.setDropDownViewResource(R.layout.spinner_layout);

        editNome = (EditText)findViewById(R.id.edtInsContaNome);

        editTelefone = (EditText)findViewById(R.id.edtInsContaTelefone);
        editTelefone.addTextChangedListener(Mask.insert("(##)#########", editTelefone));

        editReceita = (EditText)findViewById(R.id.edtInsContaReceita);
        editFuncionarios = (EditText)findViewById(R.id.edtInsContaFuncionarios);
        editEndereco = (EditText)findViewById(R.id.edtInsContaEndereco);
        editSetor = (EditText)findViewById(R.id.edtInsContaSetor);
        editOrigem = (EditText)findViewById(R.id.edtInsContaOrigem);

        spinnerTipo = (Spinner)findViewById(R.id.spiInsContaTipo);
        spinnerClassificacao = (Spinner)findViewById(R.id.spiInsContaClassificacao);

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


    }

    public void insertConta(View view){
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
            InsertConta insertconta = new InsertConta(this);
            insertconta.execute(nome, classificacaoSelected, origem, telefone, setor, tipoSelected, receita, funcionarios, endereco);
        }
    }


}
