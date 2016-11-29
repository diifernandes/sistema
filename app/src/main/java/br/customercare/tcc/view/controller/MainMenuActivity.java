package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Lead;

import br.customercare.tcc.R;
import br.customercare.tcc.util.login.LogoutSF;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //How to change elements in the header programatically
        View headerView = navigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.email);
        emailText.setText("usjt2016@gmail.com");

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void menuLeads(View view){
        Intent intent = new Intent(this, LeadsActivity.class);
        startActivity(intent);
    }

    public void menuContas(View view){
        Intent intent = new Intent(this, ContasActivity.class);
        startActivity(intent);
    }

    public void menuContatos(View view){
        Intent intent = new Intent(this, ContatosActivity.class);
        startActivity(intent);
    }

    public void menuMetas(View view){
        Intent intent = new Intent(this, MetricasActivity.class);
        startActivity(intent);
    }

    public void menuCampanhas(View view){
        Intent intent = new Intent(this, CampanhasActivity.class);
        startActivity(intent);
    }

    public void menuTarefas(View view){
        Intent intent = new Intent(this, TarefasActivity.class);
        startActivity(intent);
    }

    public void menuOportunidades(View view){
        Intent intent = new Intent(this, OportunidadesActivity.class);
        startActivity(intent);
    }

    public void menuVendas(View view){
        Intent intent = new Intent(this, VendasActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        LogoutSF logoutSF = new LogoutSF(this);
        logoutSF.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_leads) {
            Intent leadIntent = new Intent(this, LeadsActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_contas) {
            Intent contasIntent = new Intent(this, ContasActivity.class);
            startActivity(contasIntent);
        } else if (id == R.id.nav_contatos) {
            Intent leadIntent = new Intent(this, ContatosActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_opps) {
            Intent leadIntent = new Intent(this, OportunidadesActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_camp) {
            Intent leadIntent = new Intent(this, CampanhasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_jobs) {
            Intent leadIntent = new Intent(this, MetricasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_sales) {
            Intent leadIntent = new Intent(this, VendasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_tasks) {
            Intent leadIntent = new Intent(this, TarefasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_visits) {
            Intent leadIntent = new Intent(this, VisitasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_ajustes) {
            //
        } else if (id == R.id.nav_feedback) {
            //
        } else if (id == R.id.nav_help) {
            //
        } else if (id == R.id.nav_logout) {
            LogoutSF logoutSF = new LogoutSF(this);
            logoutSF.execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
