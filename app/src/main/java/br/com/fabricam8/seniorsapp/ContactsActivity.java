package br.com.fabricam8.seniorsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import br.com.fabricam8.seniorsapp.dal.ContactsDAL;
import br.com.fabricam8.seniorsapp.domain.Contacts;
import br.com.fabricam8.seniorsapp.util.FormHelper;


public class ContactsActivity extends ActionBarActivity {
    private Contacts sessionContacts;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_form);
        EditText telefone = (EditText) findViewById(R.id.fone1_contacts);
        telefone.addTextChangedListener(Mask.insert("(##)####-####", telefone));
        EditText telefone2 = (EditText) findViewById(R.id.fone2_contacts);
        telefone2.addTextChangedListener(Mask.insert("(##)####-####", telefone2));
        // adicionando edit listeners aos campos de texto
       // addTextChangeListeners();

    }

    private void addTextChangeListeners()
    {
        EditText txtName = (EditText) findViewById(R.id.nome1);
        txtName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionContacts.setNome1(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText txtName2 = (EditText) findViewById(R.id.nome2);
        txtName2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionContacts.setNome2(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        EditText txtFone1 = (EditText) findViewById(R.id.fone1_contacts);
        txtName2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionContacts.setFone1(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        EditText txtFone2 = (EditText) findViewById(R.id.fone2_contacts);
        txtName2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionContacts.setFone2(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }

    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help_events_list:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateContactsView()
    {
        FormHelper.setTextBoxValue(this, R.id.nome1, sessionContacts.getName1());

        FormHelper.setTextBoxValue(this, R.id.fone1_contacts, sessionContacts.getFone1());
        FormHelper.setTextBoxValue(this, R.id.nome2, sessionContacts.getName2());

       }

    public void saveContacts(View v) {
        try
        {
            Context context = this;
            ContactsDAL db = ContactsDAL.getInstance(context);
           // TODO verificar salvamento na activity de Activity
            ContactsDAL dbConta = ContactsDAL.getInstance(this);
/*
            if (validateForm())
            {
*/
             long id = -1;


             id = dbConta.create(sessionContacts);


                /*
                if (sessionContacts.getID() > 0)
                {
                    id = sessionContacts.getID();
                      //atualizacao de dados
                       dbConta.update(sessionContacts);
                } else
                {
               //      id = dbConta.create(sessionContacts);
                     //System.out.println("######################LAECYO##############################");
                }
               */

               // Toast.makeText(this,id, Toast.LENGTH_LONG).show();
                 //Log.i("balasasas",""+id);

               if (id > 0)
                {
                    Toast.makeText(this, "A atividade foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                    finish(); // finalizando activty e retornando para tela anterior
                }
                else if(id == -1)
                {
                    // TODO remover alarme (se existir) ?!!
                    Toast.makeText(this, "Ocorreu uma falha e a consulta não pode ser cadastrada.", Toast.LENGTH_LONG).show();
                }
        }
       /* }*/ catch (Exception ex)
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


    public abstract static class Mask {

        public static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "");
        }

        public static TextWatcher insert(final String mask, final EditText ediTxt) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    String str = Mask.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            };
        }

    }




}





