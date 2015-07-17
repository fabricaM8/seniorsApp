package br.com.fabricam8.seniorsapp.gcm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.dal.ExerciseDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.ReminderType;


public class GcmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // handle intent

        Object objMsgType = intent.getExtras().get("class");
        if(objMsgType != null) {
            String strMsgType = objMsgType.toString();
            Object objMsgValue = intent.getExtras().get("message");

            switch (strMsgType) {
                case "Medicacao" :
                    parseMedication(context, objMsgValue);
                    break;
                case "Atividade" :
                    parseExercise(context, objMsgValue);
                    break;
                case "ConsultaMedica" :
                    parseConsultation(context, objMsgValue);
                    break;
            }

        }

        setResultCode(Activity.RESULT_OK);
    }

    /**
     * {"id":0,"id_usuario":4,"id_cuidador":2,"nomeMedico":"Dr. João Costa",
     * "descricao":"Cardiologista","data":"2015-07-10T03:00:00.000Z","hora":"15:15","lembrar":"1d"}
     */
    private void parseConsultation(Context context, Object objJson) {
        if(objJson != null) {
            String strJsonValue = objJson.toString();

            try {
                JSONObject jsonData= new JSONObject(strJsonValue);

                // creating Exercise Object
                Consultation oCon = new Consultation();

                if(jsonData.has("id")) {
                    int cloudId = jsonData.getInt("id");
                    oCon.setCloudId(cloudId);
                }
                if(jsonData.has("nomeMedico")) {
                    String drName = jsonData.get("nomeMedico").toString();
                    oCon.setName(drName);
                }
                if(jsonData.has("descricao")) {
                    String descricao = jsonData.get("descricao").toString();
                    oCon.setDetails(descricao);
                }
                if(jsonData.has("data") && jsonData.has("hora")) {
                    String data = jsonData.getString("data");
                    String hora = jsonData.getString("hora");
                    int hod = Integer.parseInt(hora.split(":")[0]);
                    int m = Integer.parseInt(hora.split(":")[1]);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date d = dateFormat.parse(data);
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    c.set(Calendar.HOUR_OF_DAY, hod);
                    c.set(Calendar.MINUTE, m);

                    oCon.setStartDate(c.getTime());
                }

                // setting a default reminder
                oCon.setReminderType(ReminderType.DUAS_HORAS_ANTES);

                ConsultationDAL db = ConsultationDAL.getInstance(context);
                db.create(oCon);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {"id":0,"id_usuario":4,"id_cuidador":2,"descricao":"Musculação",
     * "dataInicial":"Jul 9, 2015 12:00:00 AM","hora":"16:00","diasSemana":"1,3,5,"}
     *
     */
    private void parseExercise(Context context, Object objMsgValue) {
        if(objMsgValue != null) {
            String strJsonValue = objMsgValue.toString();

            // parsing message
            try {
                JSONObject jsonData= new JSONObject(strJsonValue);

                // creating Exercise Object
                Exercise oExe = new Exercise();
                if(jsonData.has("id")) {
                    int cloudId = jsonData.getInt("id");
                    oExe.setCloudId(cloudId);
                }
                if(jsonData.has("nome")) {
                    String name = jsonData.getString("nome");
                    oExe.setName(name);
                }
                if(jsonData.has("dataInicial") && jsonData.has("hora")) {
                    String dataInicial = jsonData.getString("dataInicial");
                    String hora = jsonData.getString("hora");
                    int hod = Integer.parseInt(hora.split(":")[0]);
                    int m = Integer.parseInt(hora.split(":")[1]);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
                    Date d = dateFormat.parse(dataInicial);
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    c.set(Calendar.HOUR_OF_DAY, hod);
                    c.set(Calendar.MINUTE, m);

                    oExe.setStartDate(c.getTime());
                }
                if(jsonData.has("diaSemana")) {
                    String dias = jsonData.getString("diaSemana");
                }

                ExerciseDAL db = ExerciseDAL.getInstance(context);
                db.create(oExe);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {"id":0,"id_usuario":4,"id_cuidador":2,"nome":"Paracetamol 750mg","obs":"Quando dor",
     * "dataInicial":"Jul 9, 2015 12:00:00 AM","hora":"10:00","duracao":2,"dosagem":7,"tipo":"0"}
     *
     */
    private void parseMedication(Context context, Object objMsgValue) {
        if(objMsgValue != null) {
            String strJsonValue = objMsgValue.toString();

            try {
                // parsing message
                JSONObject jsonData= new JSONObject(strJsonValue);

                // creating Medication Object
                Medication oMed = new Medication();

                if(jsonData.has("id")) {
                    int cloudId = jsonData.getInt("id");
                    oMed.setCloudId(cloudId);
                }
                if(jsonData.has("nome")) {
                    String medication = jsonData.getString("nome");
                    oMed.setName(medication);
                }
                if(jsonData.has("obs")) {
                    String description = jsonData.getString("obs");
                    oMed.setDescription(description);
                }
                if(jsonData.has("dataInicial") && jsonData.has("hora")) {
                    String dataInicial = jsonData.getString("dataInicial");
                    String hora = jsonData.getString("hora");
                    int hod = Integer.parseInt(hora.split(":")[0]);
                    int m = Integer.parseInt(hora.split(":")[1]);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
                    Date d = dateFormat.parse(dataInicial);
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    c.set(Calendar.HOUR_OF_DAY, hod);
                    c.set(Calendar.MINUTE, m);

                    oMed.setStartDate(c.getTime());
                }
                if(jsonData.has("duracao")) {
                    int duracao = jsonData.getInt("duracao");
                    //oMed.setDescription(description);
                }
                if(jsonData.has("dosagem")) {
                    int dosagem = jsonData.getInt("dosagem");
                    //oMed.setDescription(description);
                }
                if(jsonData.has("tipo")) {
                    int tipo = jsonData.getInt("tipo");
                    //oMed.setDescription(description);
                }

                MedicationDAL db = MedicationDAL.getInstance(context);
                db.create(oMed);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

