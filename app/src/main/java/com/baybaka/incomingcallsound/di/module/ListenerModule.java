package com.baybaka.incomingcallsound.di.module;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;

import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.increasingring.audio.AudioController;
import com.baybaka.increasingring.audio.IAudioController;
import com.baybaka.increasingring.audio.ModeSwitcher;
import com.baybaka.increasingring.audio.RingtoneImpl;
import com.baybaka.increasingring.config.CachingConfigFactory;
import com.baybaka.increasingring.config.ConfigProvider;
import com.baybaka.increasingring.config.ThreadProvider;
import com.baybaka.increasingring.contoller.Controller;
import com.baybaka.increasingring.contoller.SoundRestorer;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
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
    IAudioController audioController(Application application, AudioManagerWrapper audioManagerWrapper) {
        AudioManager audioManager = (AudioManager) application.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        ModeSwitcher ringtone = new RingtoneImpl(audioManager);
        return new AudioController(ringtone, audioManagerWrapper, audioManager);
    }

    @Provides
    @ApplicationScope
    AudioManager am(Application application) {
        return (AudioManager) application.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }


    @Provides
    @ApplicationScope
    ThreadProvider tp(RunTimeSettings runTimeSettings,
                      IAudioController audioController) {
        return new ThreadProvider(runTimeSettings,audioController);
    }


    @Provides
    @ApplicationScope
    ConfigProvider config(CachingConfigFactory configFactory,
                          ThreadProvider tp,
                          IAudioController audioController
                          ) {
        return new ConfigProvider(configFactory,tp,audioController);
    }



    @Provides
    @ApplicationScope
//    @Singleton
    Controller controller(@Named("adapter")SettingsService settingsService,
                          RunTimeSettings runTimeSettings,
//                          IAudioController audioController,
//                          CachingConfigFactory configFactory,
                          ConfigProvider config,
                          SoundRestorer soundRestorer,
                          NotificationController notificationController) {
        return new Controller(settingsService, runTimeSettings, config, soundRestorer, notificationController);
    }

    @Provides
    @ApplicationScope
    CachingConfigFactory cachingConfigFactory(@Named("adapter")SettingsService settingsService,
                                              IAudioController audioController) {
        return new CachingConfigFactory(settingsService, audioController);
    }

    @Provides
    @ApplicationScope
    public SoundRestorer soundRestorer(@Named("adapter") SettingsService settingsService,
                                       AudioManagerWrapper audioManagerWrapper,
                                       RunTimeSettings runTimeSettings) {
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
