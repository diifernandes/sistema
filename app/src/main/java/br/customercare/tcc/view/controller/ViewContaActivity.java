package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Account;

import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.conta.ConsultOneContas;
import br.customercare.tcc.util.conta.DeleteConta;

public class ViewContaActivity extends BaseDrawerActivity {

    TextView textProp, textNome, textClassificao, textOrigem, textTelefone, textSetor, textTipo, textReceita, textFuncionarios, textEndereco;
    private Account[] conta = new Account[1];
    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_view_conta);
        getLayoutInflater().inflate(R.layout.activity_view_conta, frameLayout);
        textProp = (TextView)findViewById(R.id.txtViewContaValuePropietario);
        textNome = (TextView)findViewById(R.id.txtViewContaValueNome);
        textClassificao = (TextView)findViewById(R.id.txtViewContaValueClassificao);
        textOrigem = (TextView)findViewById(R.id.txtViewContaValueOrigem);
        textTelefone = (TextView)findViewById(R.id.txtViewContaValueTelefone);
        textSetor = (TextView)findViewById(R.id.txtViewContaValueSetor);
        textTipo = (TextView)findViewById(R.id.txtViewContaValueTipo);
        textReceita = (TextView)findViewById(R.id.txtViewContaValueReceita);
        textFuncionarios = (TextView)findViewById(R.id.txtViewContaValueFuncionarios);
        textEndereco = (TextView)findViewById(R.id.txtViewContaValueEndereco);

        String idConta = getIntent().getStringExtra(ContasActivity.EXTRA_ID);
        ConsultOneContas consultOneConta = new ConsultOneContas(this);

        try {
            conta = consultOneConta.execute(idConta).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try{
            textProp.setText(conta[0].getOwner().getName());
            textNome.setText(conta[0].getName());
            textClassificao.setText(conta[0].getRating());
            textOrigem.setText(conta[0].getAccountSource());
            textTelefone.setText(conta[0].getPhone());
            textSetor.setText(conta[0].getIndustry());
            textTipo.setText(conta[0].getType());
            textReceita.setText(Double.toString(conta[0].getAnnualRevenue()));
            textFuncionarios.setText(Integer.toString(conta[0].getNumberOfEmployees()));
            textEndereco.setText(conta[0].getBillingStreet());
        }catch (NullPointerException e){}

    }

    public void deleteConta(View view){
        DeleteConta deleteConta = new DeleteConta(this);
        deleteConta.execute(conta[0].getId());
    }

    public void updateConta(View view){
        Intent intent = new Intent(getBaseContext(), UpdateContasActivity.class);
        String idConta = conta[0].getId();
        intent.putExtra(EXTRA_ID, idConta);
        startActivity(intent);
    }

}
