package br.customercare.tcc.view.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import com.sforce.soap.enterprise.sobject.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.customercare.tcc.R;
import br.customercare.tcc.util.leads.LeadsListAdapter;
import br.customercare.tcc.util.leads.ListLeads;
import br.customercare.tcc.util.login.LogoutSF;


public class LeadsActivity extends BaseDrawerActivity {


    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    Toolbar toolbar = null;
    NavigationView navigationView = null;

    private FloatingActionButton fabEdit;

    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_leads);
        getLayoutInflater().inflate(R.layout.activity_leads, frameLayout);

        ListView lvLeads = (ListView) findViewById(R.id.listview_leads);
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
                String idLead = view.getTag().toString();
                intent.putExtra(EXTRA_ID, idLead);
                startActivity(intent);

            }
        });

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);

        fab1.setEnabled(true);
        menuRed.setClosedOnTouchOutside(true);
        //menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menus.add(menuRed);
        fab1.setOnClickListener(clickListener);

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
                    inserirLead(v);

            }
        }
    };

    public void inserirLead(View view) {
        Intent intent = new Intent(this, InsertLeadActivity.class);
        startActivity(intent);
    }



}