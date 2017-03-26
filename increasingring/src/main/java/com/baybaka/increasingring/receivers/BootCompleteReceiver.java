package com.baybaka.increasingring.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baybaka.increasingring.service.ServiceStarter;

import org.slf4j.LoggerFactory;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LoggerFactory.getLogger(BootCompleteReceiver.class.getSimpleName()).info("Received boot completed event");
        ServiceStarter.INSTANCE.startServiceWithCondition(context);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
//            ScheduleController.startAllEnabled(context);

        }
    }
}
