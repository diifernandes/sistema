package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.conta.ConsultOneContas;
import br.customercare.tcc.util.conta.DeleteConta;

public class ViewContaActivity extends BaseDrawerActivity {

    TextView textProp, textNome, textClassificao, textOrigem, textTelefone, textSetor, textTipo, textReceita, textFuncionarios, textEndereco;
    private Account[] conta = new Account[1];
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
                    updateConta(v);
                    break;
                case R.id.fab2:
                    deleteConta(v);
                    break;

            }
        }
    };

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
