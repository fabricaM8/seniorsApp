package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.GlobalParams;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class ProfileFormActivity extends ActionBarActivity {

    private ImageView imgPicEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_primary_color));

        imgPicEdit = (ImageView)findViewById(R.id.imgPicEdit);

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

        String b64Img = prefs.getString(GlobalParams.SHARED_PROPERTY_PROFILE_PHOTO, "");
        if (!b64Img.isEmpty()) {
            imgPicEdit.setImageBitmap(FormHelper.decodeBase64(b64Img));
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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imgPicEdit.setImageBitmap(imageBitmap);

            final SharedPreferences prefs = getSharedPrefs();
            SharedPreferences.Editor editor = prefs.edit();

            // encoding image
            String b64Img = encodeTobase64(imageBitmap);
            editor.putString(GlobalParams.SHARED_PROPERTY_PROFILE_PHOTO, b64Img);
            editor.commit();
        }
    }

    private static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
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
