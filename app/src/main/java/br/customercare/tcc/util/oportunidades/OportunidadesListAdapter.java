package br.customercare.tcc.util.oportunidades;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Opportunity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import br.customercare.tcc.R;

/**
 * Created by JeanThomas on 05/11/2016.
 */
public class OportunidadesListAdapter extends BaseAdapter {
    private Context context;
    private List<Opportunity> oportunidadeList ;

    public OportunidadesListAdapter(Context context, List<Opportunity> oportunidadeList) {
        this.context = context;
        this.oportunidadeList = oportunidadeList;
    }

    @Override
    public int getCount() {
        return oportunidadeList.size();
    }

    @Override
    public Object getItem(int position) {
        return oportunidadeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_oportunidade_list, null);
        TextView textNomeOportunidade = (TextView)v.findViewById(R.id.txtListOportunidadeNome);
        TextView textContaOportunidade = (TextView)v.findViewById(R.id.txtListOportunidadeConta);
        TextView textDataFechamentoOportunidade = (TextView)v.findViewById(R.id.txtListOportunidadeDataFechamento);
        TextView textFaseOportunidade = (TextView)v.findViewById(R.id.txtListOportunidadeFase);

        //Set text for TextView

        if(oportunidadeList.get(position).getName() != null)textNomeOportunidade.setText(oportunidadeList.get(position).getName());
        try{
        textContaOportunidade.setText(oportunidadeList.get(position).getAccount().getName());
        }catch (Exception e){
            textContaOportunidade.setText("-----");
        }
        if(oportunidadeList.get(position).getCloseDate() != null)setDataVencimento(oportunidadeList.get(position).getCloseDate(), textDataFechamentoOportunidade);
        if(oportunidadeList.get(position).getStageName() != null)textFaseOportunidade.setText(oportunidadeList.get(position).getStageName());


        //Save product id to tag
        v.setTag(oportunidadeList.get(position).getId());

        return v;
    }

    public void setDataVencimento(Calendar calendarFechamento, TextView textDataFechamento){
        if(calendarFechamento != null) {
            Calendar dataFechamento = calendarFechamento;
            int diaFechamento, mesFechamento, anoFechamento;
            diaFechamento = dataFechamento.get(Calendar.DAY_OF_MONTH);
            mesFechamento = dataFechamento.get(Calendar.MONTH) + 1;
            anoFechamento = dataFechamento.get(Calendar.YEAR);

            textDataFechamento.setText(new DecimalFormat("00").format(diaFechamento) + "/" + new DecimalFormat("00").format(mesFechamento) + "/" + new DecimalFormat("00").format(anoFechamento), TextView.BufferType.EDITABLE);
        }
    }
}
