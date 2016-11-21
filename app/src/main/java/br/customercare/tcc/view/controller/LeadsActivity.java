package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Lead;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.LeadsListAdapter;
import br.customercare.tcc.util.leads.ListLeads;
import br.customercare.tcc.util.login.LogoutSF;


public class LeadsActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    Toolbar toolbar = null;
    NavigationView navigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads);

       ListView lvLeads = (ListView)findViewById(R.id.listview_leads);

        ArrayList<Lead> leads = new ArrayList<Lead>();
        ListLeads listLeads = new ListLeads(this);

        try {
            leads = listLeads.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LeadsListAdapter adapter = new LeadsListAdapter(this, leads);
        lvLeads.setAdapter(adapter);

        lvLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Clicked product id =" + idLead, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ViewLeadActivity.class);
                String idLead =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idLead);
                startActivity(intent);

            }
        });
    }

    public void inserirLead(View view){
        Intent intent = new Intent(this, InsertLeadActivity.class);
        startActivity(intent);
    }

}
