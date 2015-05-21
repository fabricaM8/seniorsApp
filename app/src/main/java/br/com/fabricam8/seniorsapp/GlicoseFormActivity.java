package br.com.fabricam8.seniorsapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class GlicoseFormActivity extends ActionBarActivity
{

    private static String[] PICKER_QUANTITY_DEC = {
            ",0", ",1", ",2", ",3", ",4", "1/2",
            ",6", ",7", ",9", "1/4", "3/4"
    };

    private Medication sessionMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glicose_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // adicionando edit listeners aos campos de texto
        // addTextChangeListeners();
/*
        // recuperando id passada no clique
        long medicationId = getIntent().getLongExtra("_ID_", -1);
        if (medicationId == -1) {
            this.sessionMedication = initMedication();
        } else {
            this.sessionMedication = MedicationDAL.getInstance(this).findOne(medicationId);
        }
        // atulizando a view de medicamento
        //updateMedicationView();

        findViewById(R.id.med_form_qty).requestFocus();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication_form, menu);
        return true;
    }

*/
    }

}