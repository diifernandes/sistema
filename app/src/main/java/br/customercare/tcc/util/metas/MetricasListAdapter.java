package br.customercare.tcc.util.metas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Metric;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.view.controller.MetricasActivity;

/**
 * Created by Fernando on 16/10/2016.
 */
public class MetricasListAdapter extends BaseAdapter {
    private Context context;
    private List<Metric> metricasList ;

    public MetricasListAdapter(Context context, List<Metric> metricasList) {
        this.context = context;
        this.metricasList = metricasList;
    }

    @Override
    public int getCount() {
        return metricasList.size();
    }

    @Override
    public Object getItem(int position) {
        return metricasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_metrica_list, null);
        TextView textPropMetrica = (TextView)v.findViewById(R.id.txtListMetricaProprietario);
        TextView textNomeMetrica = (TextView)v.findViewById(R.id.txtListMetricaNome);
        TextView textNomeMeta = (TextView)v.findViewById(R.id.txtListMetricaMeta);
        TextView textTpRegistr = (TextView)v.findViewById(R.id.txtListMetricaTipo);

        ConsultOwnerMeta consult = new ConsultOwnerMeta(v.getContext());
        String propMeta = null;
        try {
            propMeta = consult.execute(metricasList.get(position).getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Set text for TextView
        textPropMetrica.setText(propMeta);
        textNomeMetrica.setText(metricasList.get(position).getName());
        textNomeMeta.setText(metricasList.get(position).getGoal().getName());
        textTpRegistr.setText(metricasList.get(position).getRecordType().getName());

        //Save product id to tag
        v.setTag(metricasList.get(position).getId());

        return v;
    }
}
