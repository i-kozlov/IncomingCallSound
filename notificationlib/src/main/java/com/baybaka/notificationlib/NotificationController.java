package com.baybaka.notificationlib;

import android.app.Application;

import com.baybaka.notificationlib.flash.FlashLight;
import com.baybaka.notificationlib.led.TwoColorBlink;

import javax.inject.Inject;


public class NotificationController {

//    private final Logger LOG = LoggerFactory.getLogger(Controller.class.getSimpleName());

    private volatile TwoColorBlink blink;
    private volatile FlashLight flashLight;

    private boolean useLED;
    private boolean useFlash;

    private int flashOnInterval;
    private int flashOffInterval;

    private NotificationSettings mNotificationSettings;
    private Application application;

    @Inject
    public NotificationController(NotificationSettings notificationSettings, Application application) {
        mNotificationSettings = notificationSettings;
        this.application = application;

        updateConfig();
    }

    public void startNotify() {
        //to make sure no old increase task left
        useLed();
        useFlashLight();
    }


    public void updateConfig() {
        useLED = mNotificationSettings.isUseLed();
        useFlash = mNotificationSettings.isProVersion() && mNotificationSettings.isUsingFlash();
    }

    public void stopNotify() {

        if (blink != null) {
            blink.stop();
            blink = null;
        }
        if (flashLight != null) {
            flashLight.stop();
            flashLight = null;
        }
    }

    private void useLed() {
        if (useLED) {
            blink = new TwoColorBlink(application);
            blink.execute();
        }
//        else System.out.println("disabled");
    }

    private void useFlashLight() {
        if (useFlash) {
            flashLight = new FlashLight();
            flashLight.execute();
        }
//        else System.out.println("flash disabled");
    }

}
