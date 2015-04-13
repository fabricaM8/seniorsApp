package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.GlobalParams;
import br.com.fabricam8.seniorsapp.util.SysUtil;


public class ProfileFormActivity extends Activity {//ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        // create toolbar
//        Toolbar mToolbar = ToolbarBuilder.build(this, true);
//        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_primary_color));

        // adicionando edit listeners aos campos de texto
        addTextChangeListeners();
    }

    /**
     * Este método adiciona text edit listeners aos campos de texto da página.
     */
    private void addTextChangeListeners() {
        EditText txtName = (EditText)findViewById(R.id.profile_form_name);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String seq = s.toString().toLowerCase();
                if (seq.length() > 1)
                    seq = seq.substring(0, 1).toUpperCase() + seq.substring(1);

//                sessionMedication.setName(seq);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText inputField = (EditText) findViewById(R.id.profile_form_phone);
        inputField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
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

    /**
     * Efetua a validação do formulário.
     *
     * @return Retorna falso se algo está errado com o formulário e indica erro na tela.
     */
    private boolean validateForm() {
        // validating name
        if (!FormHelper.validateFormTextInput(this, R.id.profile_form_name, getString(R.string.validation_error_message)))
            return false;

        if (!FormHelper.validateFormTextInput(this, R.id.profile_form_phone, getString(R.string.validation_error_message)))
            return false;

        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     */
    public void saveProfile(View view) {
        if (validateForm())
        {
            // inicializando shared prefs
            final SharedPreferences prefs = getSharedPrefs();
            SharedPreferences.Editor editor = prefs.edit();

            if(SysUtil.isOnline(ProfileFormActivity.this)) {
                String key = "";

                // Acessando api rest

                if(!key.isEmpty()) {
                    // so setar se conseguiu sncronizar
                    editor.putString(GlobalParams.SHARED_PROPERTY_REG_CLOUD_ID, key);
                    editor.putBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SYNC, true);
                }
            }

            String name = FormHelper.getTextBoxValue(ProfileFormActivity.this, R.id.profile_form_name);
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_NAME, name);

            String phone = FormHelper.getTextBoxValue(ProfileFormActivity.this, R.id.profile_form_phone);
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_PHONE, phone);

            editor.putBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SET, true);
            editor.commit();
        }
        else {
            showAlert("Alerta", "Por favor, preencha os campos destacados.");
        }

    }

    private void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // montando dialog
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getSharedPrefs() {

        return getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);

    }
}
