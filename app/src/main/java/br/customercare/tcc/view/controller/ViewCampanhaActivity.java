package br.customercare.tcc.view.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sforce.soap.enterprise.sobject.Campaign;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import br.customercare.tcc.R;
import br.customercare.tcc.util.campanha.ConsultOneCampanha;

public class ViewCampanhaActivity extends AppCompatActivity {

    private Campaign[] campanha = new Campaign[1];

    TextView textOwnerCampanha, textNome, textTipo, textStatus, textDataInicio, textDataTermino, textReceitaEsperada,
            textCustoEstimado, textCustoReal, textRespostaEsperada, textPai, textTotalLeads, textTotalLeadsConvertidos,
            textTotalContatos, textTotalOportunidades, textTotalOportunidadesGanhas;
    CheckBox chkAtivo;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_campanha);

        textOwnerCampanha = (TextView)findViewById(R.id.txtViewCampanhaValueProprietario);
        textNome = (TextView)findViewById(R.id.txtViewCampanhaValueNome);
        chkAtivo = (CheckBox)findViewById(R.id.chkViewCampanhaValueAtivo);
        textTipo = (TextView)findViewById(R.id.txtViewCampanhaValueTipo);
        textStatus = (TextView)findViewById(R.id.txtViewCampanhaValueStatus);
        textDataInicio = (TextView)findViewById(R.id.txtViewCampanhaValueDataInicio);
        textDataTermino = (TextView)findViewById(R.id.txtViewCampanhaValueDataTermino);
        textReceitaEsperada = (TextView)findViewById(R.id.txtViewCampanhaValueReceitaEsperada);
        textCustoEstimado = (TextView)findViewById(R.id.txtViewCampanhaValueCustoEstimado);
        textCustoReal = (TextView)findViewById(R.id.txtViewCampanhaValueCustoReal);
        textRespostaEsperada = (TextView)findViewById(R.id.txtViewCampanhaValueReceitaEsperada);
        textPai = (TextView)findViewById(R.id.txtViewCampanhaValuePai);
        textTotalLeads = (TextView)findViewById(R.id.txtViewCampanhaValueTotalLeads);
        textTotalLeadsConvertidos = (TextView)findViewById(R.id.txtViewCampanhaValueTotalLeadsConvertidos);
        textTotalContatos = (TextView)findViewById(R.id.txtViewCampanhaValueTotalContatos);
        textTotalOportunidades = (TextView)findViewById(R.id.txtViewCampanhaValueValorTotalOportunidades);
        textTotalOportunidadesGanhas = (TextView)findViewById(R.id.txtViewCampanhaValueValorTotalOportunidadesGanhas);

        String idCampanha = getIntent().getStringExtra(CampanhasActivity.EXTRA_ID);
        ConsultOneCampanha consultOneCampanha = new ConsultOneCampanha(this);

        try {
            campanha = consultOneCampanha.execute(idCampanha).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
        textOwnerCampanha.setText(campanha[0].getOwner().getName());
        textNome.setText(campanha[0].getName());
        chkAtivo.setChecked(campanha[0].getIsActive());
        textTipo.setText(campanha[0].getType());
        textStatus.setText(campanha[0].getStatus());
        textDataInicio.setText("");
        textDataTermino.setText("");
        textReceitaEsperada.setText(Double.toString(campanha[0].getExpectedRevenue()));
        textCustoEstimado.setText(Double.toString(campanha[0].getBudgetedCost()));
        textCustoReal.setText(Double.toString(campanha[0].getActualCost()));
        textRespostaEsperada.setText(Double.toString(campanha[0].getExpectedResponse()));
        textPai.setText(campanha[0].getParent().getName());
        textTotalLeads.setText(Integer.toString(campanha[0].getNumberOfLeads()));
        textTotalLeadsConvertidos.setText(Integer.toString(campanha[0].getNumberOfConvertedLeads()));
        textTotalContatos.setText(Integer.toString(campanha[0].getNumberOfContacts()));
        textTotalOportunidades.setText(Double.toString(campanha[0].getAmountAllOpportunities()));
        textTotalOportunidadesGanhas.setText(Double.toString(campanha[0].getAmountWonOpportunities()));
        }catch (NullPointerException e){

        }
        carregaDatas();
    }

    public void carregaDatas(){
        if(campanha[0].getStartDate() != null) {
            int diaInicio, mesInicio, anoInicio;
            Calendar dataInicio = campanha[0].getStartDate();
            diaInicio = dataInicio.get(Calendar.DAY_OF_MONTH);
            mesInicio = dataInicio.get(Calendar.MONTH) + 1;
            anoInicio = dataInicio.get(Calendar.YEAR);

            textDataInicio.setText(new DecimalFormat("00").format(diaInicio) + "/" + new DecimalFormat("00").format(mesInicio) + "/" + new DecimalFormat("00").format(anoInicio), TextView.BufferType.EDITABLE);
        }

        if(campanha[0].getEndDate() != null) {
            Calendar dataVencimento = campanha[0].getEndDate();
            int diaTermino, mesTermino, anoTermino;
            diaTermino = dataVencimento.get(Calendar.DAY_OF_MONTH);
            mesTermino = dataVencimento.get(Calendar.MONTH) + 1;
            anoTermino = dataVencimento.get(Calendar.YEAR);

            textDataTermino.setText(new DecimalFormat("00").format(diaTermino) + "/" + new DecimalFormat("00").format(mesTermino) + "/" + new DecimalFormat("00").format(anoTermino), TextView.BufferType.EDITABLE);
        }
    }
}
