package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sforce.soap.enterprise.sobject.Campaign;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.campanha.CampanhasListAdapter;
import br.customercare.tcc.util.campanha.ListCampanhas;

public class CampanhasActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "br.customercare.tcc.view.controller.ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanhas);

        ListView lvCampanhas = (ListView)findViewById(R.id.listView_Campanhas);

        ArrayList<Campaign> campanha = new ArrayList<Campaign>();
        ListCampanhas listCamp = new ListCampanhas(this);

        try {
            campanha = listCamp.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CampanhasListAdapter adapter = new CampanhasListAdapter(this, campanha);
        lvCampanhas.setAdapter(adapter);

        lvCampanhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewCampanhaActivity.class);
                String idCampanha =  view.getTag().toString();
                intent.putExtra(EXTRA_ID, idCampanha);
                startActivity(intent);
            }
        });
    }
}
