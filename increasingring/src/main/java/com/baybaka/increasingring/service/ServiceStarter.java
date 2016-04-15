package com.baybaka.increasingring.service;

import android.content.Context;
import android.content.Intent;

import com.baybaka.increasingring.Getter;
import com.baybaka.increasingring.SettingsService;

import org.slf4j.LoggerFactory;

public class ServiceStarter {

    public static void startServiceWithCondition(Context context) {
        if (checkIsServiceEnabledInConfig(context)) {
            context.startService(new Intent(context, VolumeService.class));
        }
    }

    private static boolean checkIsServiceEnabledInConfig(Context context) {
        SettingsService settingsService = ((Getter)context.getApplicationContext()).getSetting();
        boolean result = settingsService.isServiceEnabledInConfig();
        LoggerFactory.getLogger(ServiceStarter.class.getSimpleName()).info("Service config status is {}: ", result);
        return result;
    }

    public static void stopServiceRestartIfEnabled(Context context) {
        LoggerFactory.getLogger(ServiceStarter.class.getSimpleName()).info("Stopping service");
        context.stopService(new Intent(context, VolumeService.class));
        startServiceWithCondition(context);
    }
}
