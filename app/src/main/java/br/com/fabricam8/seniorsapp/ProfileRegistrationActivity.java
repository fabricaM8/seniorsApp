package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.GlobalParams;
import br.com.fabricam8.seniorsapp.util.SysUtil;
import br.com.fabricam8.seniorsapp.util.WebPortalConsumer;


public class ProfileRegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_registration);
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
        if (!FormHelper.validateFormTextInput(this, R.id.profile_rego_name, getString(R.string.validation_error_message)))
            return false;

        if (!FormHelper.validateFormTextInput(this, R.id.profile_rego_phone, getString(R.string.validation_error_message)))
            return false;

        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     */
    public void saveRegistrationProfile(View view) {
        if (validateForm())
        {
            // inicializando shared prefs
            final SharedPreferences prefs = getSharedPrefs();
            SharedPreferences.Editor editor = prefs.edit();

            if(SysUtil.isOnline(ProfileRegistrationActivity.this)) {
                String key = "";

                // Acessando api rest

                if(!key.isEmpty()) {
                    // so setar se conseguiu sncronizar
                    editor.putString(GlobalParams.SHARED_PROPERTY_REG_CLOUD_ID, key);
                    editor.putBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SYNC, true);
                }
            }

            String name = FormHelper.getTextBoxValue(ProfileRegistrationActivity.this, R.id.profile_rego_name);
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_NAME, name);

            String phone = FormHelper.getTextBoxValue(ProfileRegistrationActivity.this, R.id.profile_rego_phone);
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_PHONE, phone);

            editor.putBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SET, true);
            editor.commit();

            // try to register senior (if not set yet)
            WebPortalConsumer.getInstance(ProfileRegistrationActivity.this).registerSenior();

            Intent i = new Intent(ProfileRegistrationActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
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
