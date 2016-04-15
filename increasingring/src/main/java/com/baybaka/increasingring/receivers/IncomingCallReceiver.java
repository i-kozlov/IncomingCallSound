package com.baybaka.increasingring.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baybaka.increasingring.Getter;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.service.ServiceStarter;

import org.slf4j.LoggerFactory;

public class IncomingCallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        RunTimeSettings timeChanges= ((Getter)context.getApplicationContext()).getRunTimeChanges();
        LoggerFactory.getLogger(IncomingCallReceiver.class.getSimpleName()).debug("IncomingCallReceiver onReceive. Listener registarion {}", timeChanges.isPhoneStateListenerRegistered());

        if (! timeChanges.isPhoneStateListenerRegistered()) {
            ServiceStarter.startServiceWithCondition(context);
        }

    }

}
