package com.baybaka.increasingring.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baybaka.increasingring.Injector;
import com.baybaka.increasingring.contoller.Controller;

import javax.inject.Inject;

public class PowerButtonReceiver extends BroadcastReceiver {

    @Inject
    Controller mController;

    @Override
    public void onReceive(Context context, Intent intent) {
        Injector injector = (Injector)context.getApplicationContext();
        injector.inject(this);
        mController.stopVolumeIncrease();
    }
}
