package com.baybaka.incomingcallsound;

import android.app.Application;
import android.content.Context;

import com.baybaka.incomingcallsound.di.component.ApplicationComponent;
import com.baybaka.incomingcallsound.di.component.DaggerApplicationComponent;
import com.baybaka.incomingcallsound.di.component.DaggerListenerComponent;
import com.baybaka.incomingcallsound.di.component.ListenerComponent;
import com.baybaka.incomingcallsound.di.module.ApplicationModule;
import com.baybaka.incomingcallsound.log.ReceiverLogHandler;
import com.baybaka.increasingring.Getter;
import com.baybaka.increasingring.Injector;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.service.VolumeService;
import com.baybaka.increasingring.utils.AudioManagerWrapper;

import org.slf4j.LoggerFactory;

import pl.brightinventions.slf4android.LoggerConfiguration;

public class MyApp extends Application implements Injector, Getter {

    public static Context getContext() {
        return context;
    }

    private static Context context;
    private ApplicationComponent appComponent;
    private ListenerComponent mListenerComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        init();
        LoggerFactory.getLogger(MyApp.class.getSimpleName()).info("Application started. App version {} (ver.{})", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }

    @Override
    public void onLowMemory() {
        LoggerFactory.getLogger(MyApp.class.getSimpleName()).info("onLowMemory method. App version {} (ver.{})", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        LoggerFactory.getLogger(MyApp.class.getSimpleName()).info("onTerminate method. App version {} (ver.{})", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        super.onTerminate();
    }

    private void init() {
        context = getApplicationContext();
        configureDagger();
        configureLogging();
    }

    private void configureDagger() {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mListenerComponent = DaggerListenerComponent.builder()
                .applicationComponent(appComponent)
                .build();
    }

    private void configureLogging() {
        LoggerConfiguration.configuration().addHandlerToLogger("", LoggerConfiguration.fileLogHandler(this));
        LoggerConfiguration.configuration().addHandlerToLogger("", new ReceiverLogHandler());
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }

    public ListenerComponent getListenerComponent() {
        return mListenerComponent;
    }

    public static MyApp get() {
        return (MyApp) MyApp.getContext();
    }

    @Override
    public void inject(VolumeService volumeService) {
        mListenerComponent.inject(volumeService);
    }

    @Override
    public void inject(PowerButtonReceiver receiver) {
        mListenerComponent.inject(receiver);
    }

    @Override
    public RunTimeSettings getRunTimeSettings() {
        return mListenerComponent.runTimeChanges();
    }

    @Override
    public SettingsService getSetting() {
        return mListenerComponent.settings();
    }

    @Override
    public AudioManagerWrapper getAudioManger() {
        return mListenerComponent.provideAudioWrapper();
    }

    //на карточках
    public int getMaxVol() {
        return getListenerComponent().provideAudioWrapper().getChosenStreamrMaxHardwareVolumeLevel();
    }
}
