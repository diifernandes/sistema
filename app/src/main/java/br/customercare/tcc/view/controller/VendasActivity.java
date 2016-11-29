package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import br.customercare.tcc.R;

public class VendasActivity extends BaseDrawerActivity{
    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vendas);
        getLayoutInflater().inflate(R.layout.activity_vendas, frameLayout);
        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab1.setEnabled(true);
        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        fab4.setOnClickListener(clickListener);

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
                    pipeline(v);
                    break;
                case R.id.fab2:
                    totalOportunidades(v);
                    break;
                case R.id.fab3:
                    ganhosPerdasSemanal(v);
                    break;
                case R.id.fab4:
                    ganhosPerdasMensal(v);
                    break;

            }
        }
    };

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
