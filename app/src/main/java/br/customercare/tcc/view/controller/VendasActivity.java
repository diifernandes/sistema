package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.customercare.tcc.R;

public class VendasActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);
    }

    public void totalOportunidades(View view){
        Intent intent = new Intent(this, VendasTotalOppActivity.class);
        startActivity(intent);
    }

    public void pipeline(View view){
        Intent intent = new Intent(this, VendasPipelineActivity.class);
        startActivity(intent);
    }

    public void ganhosPerdasSemanal(View view){
        Intent intent = new Intent(this, VendasGanhosPerdasSemanalActivity.class);
        startActivity(intent);
    }

    public void ganhosPerdasMensal(View view){
        Intent intent = new Intent(this, VendasGanhosPerdasMensalActivity.class);
        startActivity(intent);
    }


}
