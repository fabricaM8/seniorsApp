package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fabricam8.seniorsapp.dal.EmergencyContactDAL;
import br.com.fabricam8.seniorsapp.domain.EmergencyContact;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class EmergencyActivity extends ActionBarActivity {

    private boolean isEdit;
    private int selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_red));
        
        loadData();
    }

    private void loadData() {
        int[] arrIds = new int[] {
                R.id.btEmergencyDial_Custom1, R.id.btEmergencyDial_Custom2, R.id.btEmergencyDial_Custom3
        };

        EmergencyContactDAL db = EmergencyContactDAL.getInstance(this);

        for (int i = 0; i< arrIds.length; i++) {
            Button b = (Button)findViewById(arrIds[i]);
            if(b != null) {
                EmergencyContact c = db.findOne(Long.parseLong(b.getTag().toString()));
                if(c != null) {
                    b.setText(c.getName() + "\n(" + c.getPhone()+ ")");
                }
            }
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
     * Evento chamado quando botão cancelar é apertado.
     * <p>
     * Finaliza a Activity e retorna para tela anterior.
     * </p>
     */
    public void doEmergencyCall(View v) {
        String phone = v.getTag() != null ? v.getTag().toString() : "";
        if (phone.length() > 0) {
            Uri number = Uri.parse("tel:" + phone);
            Intent dial = new Intent(Intent.ACTION_CALL, number);
            startActivity(dial);
        }
    }

    public void executeCustomEmergencyAction(View view) {
        if (isEdit) {
            openEditDialog(view);
        }
        else {
            EmergencyContactDAL db = EmergencyContactDAL.getInstance(this);
            EmergencyContact c = db.findOne(Long.parseLong(view.getTag().toString()));

            if (c != null) {
                Uri number = Uri.parse("tel:" + c.getPhone());
                Intent dial = new Intent(Intent.ACTION_CALL, number);
                startActivity(dial);
            }
        }
    }

    public void openEditDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_emergency_edit_phone, null);

        selectedContact = Integer.parseInt(view.getTag().toString());
        EmergencyContactDAL dbContact = EmergencyContactDAL.getInstance(this);
        EmergencyContact c = dbContact.findOne(selectedContact);

        if (c != null) {
            FormHelper.setTextBoxValue(dialogView, R.id.dg_emgcy_name, c.getName());
            FormHelper.setTextBoxValue(dialogView, R.id.dg_emgcy_phone, c.getPhone());
        }

        // montando dialog
        builder.setTitle("Editar contato")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // custom listener
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btOk = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btOk.setOnClickListener(new CustomListener(alertDialog));
    }

    private void updateContactDisplay(String name, String phone) {
        // Saving info
        try {

            EmergencyContactDAL dbContact = EmergencyContactDAL.getInstance(this);
            EmergencyContact emergencyContact = new EmergencyContact();
            emergencyContact.setName(name);
            emergencyContact.setPhone(phone);

            long i = 0;

            if (dbContact.findOne(selectedContact) != null) {
                // atualizacao de dados
                emergencyContact.setID(selectedContact);
                i = dbContact.update(emergencyContact);
            } else {
                i = dbContact.create(emergencyContact);
            }

            if (i > 0) {
                Toast.makeText(this, "Contato cadastrado com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ocorreu uma falha e o cadastro não pode ser cadastrado.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.e("Seniors App - Contato", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e contato não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }

        // Displaying information
        int rid = -1;
        switch (selectedContact) {
            case 1:
                rid = R.id.btEmergencyDial_Custom1;
                break;
            case 2:
                rid = R.id.btEmergencyDial_Custom2;
                break;
            case 3:
                rid = R.id.btEmergencyDial_Custom3;
                break;
        }

        View v = findViewById(rid);
        if (v != null && v instanceof Button) {
            ((Button) v).setText(name + "\n(" + phone + ")");
        }

    }

    private void changeButtonBg() {
        int[] arrIds = new int[] {
            R.id.btEmergencyDial_Custom1, R.id.btEmergencyDial_Custom2, R.id.btEmergencyDial_Custom3
        };

        for (int i = 0; i< arrIds.length; i++) {
            Button b = (Button)findViewById(arrIds[i]);
            if(b!=null) {
                if(!isEdit)// && b.getText().toString().equalsIgnoreCase("(vazio)"))
                    b.setBackgroundColor(Color.LTGRAY);
                else
                    b.setBackgroundColor(Color.GRAY);
            }
        }

        isEdit = !isEdit;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_emergency_edit:
                // edit
                changeButtonBg();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Custom listener
     */
    private class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            String name = ((TextView)dialog.findViewById(R.id.dg_emgcy_name)).getText().toString();
            String phone = ((TextView)dialog.findViewById(R.id.dg_emgcy_phone)).getText().toString();

            if(!name.trim().equals("") && !phone.trim().equals("")) {
                dialog.dismiss();
                updateContactDisplay(name, phone);
                changeButtonBg();
            }
            else {
                Toast.makeText(EmergencyActivity.this, "Preencha nome e telefone", Toast.LENGTH_SHORT).show();
            }
        }
    }
}





