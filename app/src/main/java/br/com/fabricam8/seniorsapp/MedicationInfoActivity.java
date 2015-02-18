package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class MedicationInfoActivity extends ActionBarActivity {

    private long medicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_info);

        // create toolbar
        Toolbar toolbar = ToolbarBuilder.build(this, true);
        toolbar.setTitle("Informações");

        // recuperando id passada no clique
        medicationId = getIntent().getLongExtra("_ID_", -1);
        if (medicationId == -1) {
            finish(); // se nao enviar bundle, sai do activity
        } else {
            loadMedication(medicationId);
        }
    }

    private void loadMedication(long id) {
        MedicationDAL db = MedicationDAL.getInstance(this);
        Medication mObj = db.findOne(id);
        if (mObj != null) {
            // setando valores
            FormHelper.setTextBoxValue(this, R.id.med_info_name, mObj.getName());
        }
    }

    public void editMedication(View view) {
        Intent intent = new Intent(this, MedicationFormActivity.class);
        intent.putExtra("_ID_", medicationId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
