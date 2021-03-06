package br.com.fabricam8.seniorsapp;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.fabricam8.seniorsapp.dal.EmergencyContactDAL;
import br.com.fabricam8.seniorsapp.domain.EmergencyContact;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class ContactsFormActivity extends ActionBarActivity {

    private boolean isEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_emergency);
        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // recuperando contato
        EmergencyContact contact = EmergencyContactDAL.getInstance(this).findOne(1);
        if (contact != null) {
            isEdit = true;
            FormHelper.setTextBoxValue(this, R.id.nome1, contact.getName());
//            FormHelper.setTextBoxValue(this, R.id.nome2, contact.getName2());
//            FormHelper.setTextBoxValue(this, R.id.fone1_contacts, contact.getFone1());
//            FormHelper.setTextBoxValue(this, R.id.fone2_contacts, contact.getFone2());
        }
    }

    public void saveContacts(View v) {
        try {
            EmergencyContactDAL dbConta = EmergencyContactDAL.getInstance(this);
            if (validateForm()) {
                EmergencyContact emergencyContact = new EmergencyContact();
                emergencyContact.setName(FormHelper.getTextBoxValue(this, R.id.nome1));
//                contacts.setFone2(FormHelper.getTextBoxValue(this, R.id.fone1_contacts));
//                contacts.setName2(FormHelper.getTextBoxValue(this, R.id.nome2));
//                contacts.setFone2(FormHelper.getTextBoxValue(this, R.id.fone2_contacts));

                long i = 0;

                if (isEdit) {
                    // atualizacao de dados
                    emergencyContact.setID(1);
                    i = dbConta.update(emergencyContact);
                } else {
                    i = dbConta.create(emergencyContact);
                }

                if (i > 0) {
                    Toast.makeText(this, "A atividade foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                    finish(); // finalizando activty e retornando para tela anterior
                } else {
                    Toast.makeText(this, "Ocorreu uma falha e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Log.e("Seniors App - Atividades", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateForm() {

//        if (!FormHelper.validateFormTextInput(this, R.id.nome1, getString(R.string.validation_error_message))) {
//            return false;
//        }
//        if (!FormHelper.validateFormTextInput(this, R.id.nome2, getString(R.string.validation_error_message))) {
//            return false;
//        }
//        if (!FormHelper.validateFormTextInput(this, R.id.fone1_contacts, getString(R.string.validation_error_message))) {
//            return false;
//        }
//        if (!FormHelper.validateFormTextInput(this, R.id.fone2_contacts, getString(R.string.validation_error_message))) {
//            return false;
//        }
        return true;
    }


    /**
     * Evento chamado quando botão cancelar é apertado.
     * <p>
     * Finaliza a Activity e retorna para tela anterior.
     * </p>
     */
    public void cancel(View v) {
        // end activity
        finish();
    }

}





