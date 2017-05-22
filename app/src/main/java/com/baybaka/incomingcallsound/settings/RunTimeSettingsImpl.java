package com.baybaka.incomingcallsound.settings;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.baybaka.incomingcallsound.BuildConfig;
import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.utils.NotificationFactory;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.interfaces.NotificationProvider;

import org.jetbrains.annotations.NotNull;

public class RunTimeSettingsImpl implements RunTimeSettings {

    private static volatile boolean testPageOpened = false;
    private static volatile boolean configChanged = false;
    private static volatile boolean globalLogging = false;
    private static volatile boolean phoneStateListenerRegistered = false;
    private final Context appContext;


    public RunTimeSettingsImpl(Application application) {
        globalLogging = ((MyApp) application).getSetting().isLoggingEnabled();
        this.appContext = application.getApplicationContext();
    }

    @Override
    public boolean isLoggingEnabled() {
        return BuildConfig.DEBUG || testPageOpened || globalLogging;
    }

    @Override
    public void setListenerIsRegistered() {
        phoneStateListenerRegistered = true;
    }

    @Override
    public void setListenerUnregistered() {
        phoneStateListenerRegistered = false;
    }

    @Override
    public boolean isPhoneStateListenerRegistered() {
        return phoneStateListenerRegistered;
    }


    @Override
    public void configurationChanged() {
        configChanged = true;
    }

    @Override
    public void configIsUpdated() {
        configChanged = false;
    }

    @Override
    public boolean isConfigChanged() {
        return configChanged;
    }

    @Override
    public Context getContext() {
        return appContext;
    }

    @Override
    public Notification persistentNotification(Context context, boolean minPriority) {
        return NotificationFactory.persistent(context, minPriority);
    }

    @Override
    public void errorNotification(Context context) {
        NotificationFactory.showMusicStreamErrotNotify(context);
    }

    @Override
    public void findPhoneNotification() {
        NotificationFactory.findPhone();
    }

    @Override
    public boolean isTestPageOpened() {
        return testPageOpened;
    }

    @Override
    public void setTestPageOpened(boolean state) {
        testPageOpened = state;
    }

    @Override
    public void setGlobalLoggingState(boolean state) {
        globalLogging = state;
    }


    @NotNull
    @Override
    public NotificationProvider getNotifyProvider() {
        return new NotificationProvider() {
            @Override
            public void playerInitError() {
                errorNotification(getContext());
            }

            @Override
            public void findPhoneCalled() {
                NotificationFactory.findPhone();
            }
        };
    }
}
