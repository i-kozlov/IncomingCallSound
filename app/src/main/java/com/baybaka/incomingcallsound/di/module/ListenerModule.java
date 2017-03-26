package com.baybaka.incomingcallsound.di.module;

import android.app.Application;
import android.telephony.PhoneStateListener;

import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.config.CachingConfigFactory;
import com.baybaka.increasingring.contoller.Controller;
import com.baybaka.increasingring.contoller.SoundRestorer;
import com.baybaka.increasingring.statelistener.MyPhoneStateListener2;
import com.baybaka.increasingring.utils.AudioManagerRealImpl;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.notificationlib.NotificationController;
import com.baybaka.notificationlib.NotificationSettings;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ListenerModule {

    @Provides
    @ApplicationScope
    PhoneStateListener listener(Controller controller, RunTimeSettings runTimeSettings) {
        return new MyPhoneStateListener2(controller, runTimeSettings);
    }

    @Provides
    @ApplicationScope
//    @Singleton
    Controller controller(@Named("adapter")SettingsService settingsService,
                          RunTimeSettings runTimeSettings,
                          //todo should be AudioController
                          AudioManagerWrapper audioManagerWrapper,
                          CachingConfigFactory configFactory, SoundRestorer soundRestorer,
                          NotificationController notificationController) {
        return new Controller(settingsService, runTimeSettings, audioManagerWrapper, configFactory, soundRestorer,
                notificationController);
    }

    @Provides
    @ApplicationScope
    CachingConfigFactory cachingConfigFactory(@Named("adapter")SettingsService settingsService) {
        return new CachingConfigFactory(settingsService);
    }

    @Provides
    @ApplicationScope
    public SoundRestorer soundRestorer(@Named("adapter") SettingsService settingsService, AudioManagerWrapper audioManagerWrapper, RunTimeSettings runTimeSettings) {
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
