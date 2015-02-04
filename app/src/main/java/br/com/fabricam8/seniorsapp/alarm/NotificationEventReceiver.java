package br.com.fabricam8.seniorsapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Aercio on 1/27/15.
 */
public class NotificationEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent svcIntent = new Intent(context, NotificationEventService.class);
        // recuperando bundle - id do alerta se encontra aqui
        Bundle extras = intent.getExtras();

        if (extras != null)
            svcIntent.putExtras(extras);

        // iniciando servico de notificacao
        context.startService(svcIntent);
    }
}