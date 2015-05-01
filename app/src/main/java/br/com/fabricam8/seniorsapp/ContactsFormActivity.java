package br.com.fabricam8.seniorsapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import br.com.fabricam8.seniorsapp.dal.ContactsDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.enums.ReminderType;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class ContactsFormActivity extends ActionBarActivity
{

   private Consultation sessionContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // adicionando edit listeners aos campos de texto
        addTextChangeListeners();

        // recuperando id passada no clique
        long consultaId = getIntent().getLongExtra("_ID_", -1);
        if (consultaId == -1) {
            this.sessionContacts = initConsultation();
        } else {
            //this.sessionContacts = ContactsDAL.getInstance(this).findOne(consultaId);
        }
        // atulizando a view de atividades
        //updateConsultationView();

    }


    private Consultation initConsultation() {
        Consultation eObj = new Consultation();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 0);

        eObj.setReminderType(ReminderType.TRES_DIAS_ANTES);
        eObj.setStartDate(c.getTime());

        return eObj;
    }

    private void addTextChangeListeners() {
        EditText txtName = (EditText) findViewById(R.id.nome_medico);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String seq = s.toString().toLowerCase();
                if(seq.length() > 1)
                    seq = seq.substring(0,1).toUpperCase() + seq.substring(1);

                sessionContacts.setName(seq);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText txtObserv = (EditText) findViewById(R.id.detalhe);
        txtObserv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionContacts.setDetails(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


        public void saveContacts(View v) {
            try {
                Context context = this;
                // TODO verificar salvamento na activity de Activity
                ContactsDAL dbConta = ContactsDAL.getInstance(this);
                if (validateForm())
                {
                    long id = -1;
                    if (sessionContacts.getID() > 0)
                    {
                        id = sessionContacts.getID();
                        // atualizacao de dados
                       // dbConta.update(sessionContacts);
                    } else {
                       // id = dbConta.create(sessionContacts);
                    }

                    if (id > 0) {
                        // TODO salvar alarme (de acordo) com modelo em MedicationFormActivity

                        Toast.makeText(this, "A atividade foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                        finish(); // finalizando activty e retornando para tela anterior
                    } else {
                        // TODO remover alarme (se existir) ?!!
                        Toast.makeText(this, "Ocorreu uma falha e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception ex)
            {
                Log.e("Seniors App - Atividades", ex.getMessage());
                Toast.makeText(this, "Ocorreu um erro e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
            }
        }
    private boolean validateForm() {

        if (!FormHelper.validateFormTextInput(this, R.id.nome1, getString(R.string.validation_error_message))) {
            return false;
        }
        if (!FormHelper.validateFormTextInput(this, R.id.nome2, getString(R.string.validation_error_message))) {
            return false;
        }
        if (!FormHelper.validateFormTextInput(this, R.id.fone1_contacts, getString(R.string.validation_error_message))) {
            return false;
        }
        if (!FormHelper.validateFormTextInput(this, R.id.fone2_contacts, getString(R.string.validation_error_message))) {
            return false;
        }
        return true;
    }

}





