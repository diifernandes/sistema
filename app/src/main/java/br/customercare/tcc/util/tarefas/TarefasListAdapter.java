package br.customercare.tcc.util.tarefas;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Task;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.metas.ConsultOwnerMeta;

/**
 * Created by JeanThomas on 25/10/2016.
 */
public class TarefasListAdapter extends BaseAdapter {
    private Context context;
    private List<Task> tarefasList ;

    public TarefasListAdapter(Context context, List<Task> tarefasList) {
        this.context = context;
        this.tarefasList = tarefasList;
    }

    @Override
    public int getCount() {
        return tarefasList.size();
    }

    @Override
    public Object getItem(int position) {
        return tarefasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_tarefa_list, null);
        TextView textProprietario = (TextView)v.findViewById(R.id.txtListTarefaProprietario);
        TextView textAssunto = (TextView)v.findViewById(R.id.txtListTarefaAssunto);
        TextView textDataVencimento = (TextView)v.findViewById(R.id.txtListTarefaDataVencimento);
        TextView textStatus = (TextView)v.findViewById(R.id.txtListTarefaStatus);
        LinearLayout linearLayoutTarefa = (LinearLayout)v.findViewById(R.id.layoutListTarefa);

        ConsultOwnerTask consultOwnerTask = new ConsultOwnerTask(v.getContext());
        String proprietarioTarefa = null;
        try {
            proprietarioTarefa = consultOwnerTask.execute(tarefasList.get(position).getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Set text for TextView
        textProprietario.setText(proprietarioTarefa);
        textAssunto.setText(tarefasList.get(position).getSubject());
        textDataVencimento.setText("");
        setDataVencimento(tarefasList.get(position).getActivityDate(), textDataVencimento);
        textStatus.setText(tarefasList.get(position).getSubject());
        setColorPriority(tarefasList.get(position).getPriority(), linearLayoutTarefa);

        //Save product id to tag
        v.setTag(tarefasList.get(position).getId());

        return v;
    }

    public void setDataVencimento(Calendar calendarVencimento, TextView textDataVencimento){
        if(calendarVencimento != null) {
            Calendar dataVencimento = calendarVencimento;
            int diaVencimento, mesVencimento, anoVencimento;
            diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesVencimento = dataVencimento.get(Calendar.MONTH) + 1;
            anoVencimento = dataVencimento.get(Calendar.YEAR);

            textDataVencimento.setText(new DecimalFormat("00").format(diaVencimento) + "/" + new DecimalFormat("00").format(mesVencimento) + "/" + new DecimalFormat("00").format(anoVencimento), TextView.BufferType.EDITABLE);
        }
    }

    public void setColorPriority(String priority, LinearLayout linearLayout){
        if(priority.equals("High")){
            linearLayout.setBackgroundColor(Color.RED);
        }else if(priority.equals("Normal")){
            linearLayout.setBackgroundColor(Color.BLUE);
        }else if(priority.equals("Low")){
            linearLayout.setBackgroundColor(Color.GRAY);
        }
    }
}
