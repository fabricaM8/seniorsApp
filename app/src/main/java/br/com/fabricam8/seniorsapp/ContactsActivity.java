package br.com.fabricam8.seniorsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.fabricam8.seniorsapp.dal.ContactsDAL;
import br.com.fabricam8.seniorsapp.domain.Contacts;
import br.com.fabricam8.seniorsapp.util.FormHelper;


public class ContactsActivity extends ActionBarActivity {
    private Contacts sessionContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveContacts(View v) {
        try {
            Context context = this;
            // TODO verificar salvamento na activity de Activity
            ContactsDAL dbConta = ContactsDAL.getInstance(this);
            if (validateForm())
            {
               System.out.println("teste");
                long id = -1;
                if (sessionContacts.getID() > 0)
                {
                    id = sessionContacts.getID();
                    // atualizacao de dados
                    dbConta.update(sessionContacts);
                } else {
                    id = dbConta.create(sessionContacts);
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

        if (!FormHelper.validateFormTextInput(this, R.id.nome_contacts, getString(R.string.validation_error_message))) {
            return false;
        }
        if (!FormHelper.validateFormTextInput(this, R.id.fone1_contacts, getString(R.string.validation_error_message))) {
            return false;
        }

        return true;
    }


}
