package br.customercare.tcc.view.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sforce.soap.enterprise.sobject.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.tarefas.ConsultNameCampanhaTask;
import br.customercare.tcc.util.tarefas.ConsultNameContaTask;
import br.customercare.tcc.util.tarefas.ConsultNameContactTask;
import br.customercare.tcc.util.tarefas.ConsultNameLeadTask;
import br.customercare.tcc.util.tarefas.ConsultNameMetricaTask;
import br.customercare.tcc.util.tarefas.ConsultNameOportunidadeTask;
import br.customercare.tcc.util.tarefas.ConsultNameOwnerTask;
import br.customercare.tcc.util.tarefas.ConsultOneTarefa;
import br.customercare.tcc.util.tarefas.DeleteTarefa;

public class ViewTarefaActivity extends BaseDrawerActivity {

    private String[] statusNomes = new String[]{"Não iniciado","Em andamento", "Concluída",
            "Em espera", "Deferido"};

    private String[] statusValues = new String[]{"Not Started", "In Progress", "Completed",
            "Waiting on someone else", "Deferred"};

    private String[] priorityNomes = new String[]{"Baixa","Normal", "Alta"};

    private String[] priorityValues = new String[]{"Low", "Normal", "High"};

    private String[] quemNomes = new String[]{"Lead","Contato"};

    private String[] relativoNomes = new String[]{"Campanha","Conta", "Métrica", "Oportunidade"};

    int diaVencimento, mesVencimento, anoVencimento;

    TextView textOwnerTask, textAssunto, textDataVencimento, textPrioridade, textStatus, textNome, textRelativo, textComentario;

    private Task[] tarefa = new Task[1];
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
        //setContentView(R.layout.activity_view_tarefa);
        getLayoutInflater().inflate(R.layout.activity_view_tarefa, frameLayout);
        textOwnerTask = (TextView)findViewById(R.id.txtViewTarefaValueProprietario);
        textAssunto = (TextView)findViewById(R.id.txtViewTarefaValueAssunto);
        textDataVencimento = (TextView)findViewById(R.id.txtViewTarefaValueDataVencimento);
        textPrioridade = (TextView)findViewById(R.id.txtViewTarefaValuePrioridade);
        textStatus = (TextView)findViewById(R.id.txtViewTarefaValueStatus);
        textNome = (TextView)findViewById(R.id.txtViewTarefaValueNome);
        textRelativo = (TextView)findViewById(R.id.txtViewTarefaValueRelativo);
        textComentario = (TextView)findViewById(R.id.txtViewTarefaValueComentario);


        String idTask = getIntent().getStringExtra(TarefasActivity.EXTRA_ID);
        ConsultOneTarefa consultOneTarefa = new ConsultOneTarefa(this);
        try {
            tarefa = consultOneTarefa.execute(idTask).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < statusValues.length ; i++){
            if(tarefa[0].getStatus().equals(statusValues[i])){
                textStatus.setText(statusNomes[i]);
                break;
            }
        }

        for(int i = 0; i < priorityValues.length ; i++){
            if(tarefa[0].getPriority().equals(priorityValues[i])){
                textPrioridade.setText(priorityNomes[i]);
                break;
            }
        }
        carregaValores(tarefa[0]);

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
                    updateTarefa(v);
                    break;
                case R.id.fab2:
                    deleteTarefa(v);
                    break;

            }
        }
    };


    public void deleteTarefa(View view){
        DeleteTarefa deleteTarefa = new DeleteTarefa(this);
        deleteTarefa.execute(tarefa[0].getId());
    }

    public void updateTarefa(View view){
        Intent intent = new Intent(getBaseContext(), UpdateTarefasActivity.class);
        String idTask = tarefa[0].getId();
        intent.putExtra(EXTRA_ID, idTask);
        startActivity(intent);

    }

    public void carregaValores(Task task){
        try{
            textAssunto.setText(task.getSubject());
            carregaData(task);
            carregaOwnerQuemRelativo(task);
            textComentario.setText(task.getDescription());
        }catch (NullPointerException e){

        }
    }

    public void carregaData(Task task){
        if(task.getActivityDate() != null) {
            Calendar dataVencimento = task.getActivityDate();
            diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesVencimento = dataVencimento.get(Calendar.MONTH) + 1;
            anoVencimento = dataVencimento.get(Calendar.YEAR);

            textDataVencimento.setText("");
            textDataVencimento.setText(new DecimalFormat("00").format(diaVencimento) + "/" + new DecimalFormat("00").format(mesVencimento) + "/" + new DecimalFormat("00").format(anoVencimento));
        }
    }

    public void carregaOwnerQuemRelativo(Task task){
        String ownerId = task.getOwnerId();
        String whoId = task.getWhoId();
        String whatId = task.getWhatId();
        String nameOwner = "";
        String nameQuem = "";
        String nameRelativo = "";
        if(whoId.substring(0,3).equals("00Q")){
            ConsultNameLeadTask consultNameLeadTask = new ConsultNameLeadTask(this);
            ConsultNameOwnerTask consultNameOwnerTask = new ConsultNameOwnerTask(this);
            try {
                nameOwner = consultNameOwnerTask.execute(ownerId).get();
                nameQuem = consultNameLeadTask.execute(whoId).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            textOwnerTask.setText(nameOwner);
            textNome.setText("Lead - " + nameQuem);
            textRelativo.setText("");
        }else if(whoId.substring(0,3).equals("003")){
            ConsultNameContactTask consultNameContactTask = new ConsultNameContactTask(this);
            ConsultNameOwnerTask consultNameOwnerTask = new ConsultNameOwnerTask(this);
            if(whatId.substring(0,3).equals("701")){
                ConsultNameCampanhaTask consultNameCampanhaTask = new ConsultNameCampanhaTask(this);
                try {
                    nameOwner = consultNameOwnerTask.execute(ownerId).get();
                    nameQuem = consultNameContactTask.execute(whoId).get();
                    nameRelativo = consultNameCampanhaTask.execute(whatId).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                textOwnerTask.setText(nameOwner);
                textNome.setText("Contato - " + nameQuem);
                textRelativo.setText("Campanha - " + nameRelativo);
            }else if(whatId.substring(0,3).equals("001")){
                ConsultNameContaTask consultNameContaTask = new ConsultNameContaTask(this);
                try {
                    nameOwner = consultNameOwnerTask.execute(ownerId).get();
                    nameQuem = consultNameContactTask.execute(whoId).get();
                    nameRelativo = consultNameContaTask.execute(whatId).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                textOwnerTask.setText(nameOwner);
                textNome.setText("Contato - " + nameQuem);
                textRelativo.setText("Conta - " + nameRelativo);
            }else if(whatId.substring(0,3).equals("0WJ")){
                ConsultNameMetricaTask consultNameMetricaTask = new ConsultNameMetricaTask(this);
                try {
                    nameOwner = consultNameOwnerTask.execute(ownerId).get();
                    nameQuem = consultNameContactTask.execute(whoId).get();
                    nameRelativo = consultNameMetricaTask.execute(whatId).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                textOwnerTask.setText(nameOwner);
                textNome.setText("Contato - " + nameQuem);
                textRelativo.setText("Métrica - " + nameRelativo);
            }else if(whatId.substring(0,3).equals("006")){
                ConsultNameOportunidadeTask consultNameOportunidadeTask = new ConsultNameOportunidadeTask(this);
                try {
                    nameOwner = consultNameOwnerTask.execute(ownerId).get();
                    nameQuem = consultNameContactTask.execute(whoId).get();
                    nameRelativo = consultNameOportunidadeTask.execute(whatId).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                textOwnerTask.setText(nameOwner);
                textNome.setText("Contato - " + nameQuem);
                textRelativo.setText("Oportunidade - " + nameRelativo);
            }
        }
    }
}
