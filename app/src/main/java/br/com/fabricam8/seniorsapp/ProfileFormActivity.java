package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.GlobalParams;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class ProfileFormActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_primary_color));

        loadProfile();
    }

    private void loadProfile() {

        // inicializando shared prefs
        final SharedPreferences prefs = getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
        String userName = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_NAME, "");
        if (!userName.isEmpty()) {
            FormHelper.setTextBoxValue(ProfileFormActivity.this, R.id.profile_form_name, userName);
        }

        String phone = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_PHONE, "");
        if (!phone.isEmpty()) {
            FormHelper.setTextBoxValue(ProfileFormActivity.this, R.id.profile_form_phone, phone);
        }

        String cloudId = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_CLOUD_ID, "");
        if (!cloudId.isEmpty()) {
            FormHelper.setTextBoxValue(ProfileFormActivity.this, R.id.profile_form_cloudId, cloudId);
        }
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

        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     */
    public void saveProfileForm(View view) {
        if (validateForm())
        {
            // inicializando shared prefs
            final SharedPreferences prefs = getSharedPrefs();
            SharedPreferences.Editor editor = prefs.edit();

            String name = FormHelper.getTextBoxValue(ProfileFormActivity.this, R.id.profile_form_name);
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_NAME, name);
            editor.commit();

            showAlert("Informação","Perfil salvo com sucesso.");
            Intent i = new Intent(ProfileFormActivity.this, DashboardActivity.class);
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
