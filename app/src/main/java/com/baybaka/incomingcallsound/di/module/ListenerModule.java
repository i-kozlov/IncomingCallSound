package com.baybaka.incomingcallsound.di.module;

import android.app.Application;
import android.telephony.PhoneStateListener;

import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.config.CachingConfigFactory;
import com.baybaka.increasingring.contoller.Controller;
import com.baybaka.increasingring.contoller.SoundRestorer;
import com.baybaka.increasingring.statelistener.MyPhoneStateListener;
import com.baybaka.increasingring.utils.AudioManagerRealImpl;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.notificationlib.NotificationController;
import com.baybaka.notificationlib.NotificationSettings;

import dagger.Module;
import dagger.Provides;

@Module
public class ListenerModule {

    @Provides
    @ApplicationScope
    PhoneStateListener listener(Controller controller, SettingsService settingsService, RunTimeSettings runTimeSettings) {
        return new MyPhoneStateListener(controller, settingsService, runTimeSettings);
    }

    @Provides
    @ApplicationScope
    Controller controller(SettingsService settingsService, RunTimeSettings runTimeSettings,
                          AudioManagerWrapper audioManagerWrapper,
                          CachingConfigFactory configFactory, SoundRestorer soundRestorer,
                          NotificationController notificationController) {
        return new Controller(settingsService, runTimeSettings, audioManagerWrapper, configFactory, soundRestorer,
                notificationController);
    }

    @Provides
    @ApplicationScope
    CachingConfigFactory cachingConfigFactory(SettingsService settingsService){
        return new CachingConfigFactory(settingsService);
    }

    @Provides
    @ApplicationScope
    public SoundRestorer soundRestorer(SettingsService settingsService, AudioManagerWrapper audioManagerWrapper, RunTimeSettings runTimeSettings) {
        return new SoundRestorer(settingsService, audioManagerWrapper, runTimeSettings);
    }

    @Provides
    @ApplicationScope
    AudioManagerWrapper provideAudioWrapper(Application application) {
        return new AudioManagerRealImpl(application);
    }

    @Provides
    @ApplicationScope
    NotificationController notificationController(NotificationSettings notificationSettings, Application application) {
        return new NotificationController(notificationSettings, application);
    }


}
