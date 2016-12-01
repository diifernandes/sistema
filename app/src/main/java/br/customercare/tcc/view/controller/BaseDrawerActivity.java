package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import br.customercare.tcc.R;
import br.customercare.tcc.util.login.LogoutSF;
import android.widget.FrameLayout;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_leads) {
            Intent leadIntent = new Intent(getApplicationContext(), LeadsActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_contas) {
            Intent contasIntent = new Intent(getApplicationContext(), ContasActivity.class);
            startActivity(contasIntent);
        } else if (id == R.id.nav_contatos) {
            Intent leadIntent = new Intent(getApplicationContext(), ContatosActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_opps) {
            Intent leadIntent = new Intent(getApplicationContext(), OportunidadesActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_camp) {
            Intent leadIntent = new Intent(getApplicationContext(), CampanhasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_jobs) {
            Intent leadIntent = new Intent(getApplicationContext(), MetricasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_sales) {
            Intent leadIntent = new Intent(getApplicationContext(), VendasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_tasks) {
            Intent leadIntent = new Intent(getApplicationContext(), TarefasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_visits) {
            Intent leadIntent = new Intent(getApplicationContext(), VisitasActivity.class);
            startActivity(leadIntent);
        } else if (id == R.id.nav_ajustes) {
            Intent leadIntent = new Intent(getApplicationContext(), ProfileScreenActivity.class);
            startActivity(leadIntent);
            //
        } else if (id == R.id.nav_logout) {
            LogoutSF logoutSF = new LogoutSF(this);
            logoutSF.execute();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}