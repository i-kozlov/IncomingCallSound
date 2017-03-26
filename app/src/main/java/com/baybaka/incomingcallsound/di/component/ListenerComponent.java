package com.baybaka.incomingcallsound.di.component;

import com.baybaka.incomingcallsound.di.module.ConfigurationModule;
import com.baybaka.incomingcallsound.di.module.ListenerModule;
import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.incomingcallsound.settings.AllSettings;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.contoller.Controller;
import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.service.VolumeService;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.notificationlib.NotificationController;

import dagger.Component;

@ApplicationScope
@Component( modules = {ListenerModule.class, ConfigurationModule.class},
dependencies = ApplicationComponent.class)
public interface ListenerComponent {

    VolumeService inject(VolumeService volumeService);

    PowerButtonReceiver inject(PowerButtonReceiver receiver);

//    Activity inject(Activity activity);

    AudioManagerWrapper provideAudioWrapper();

    /**
     * SharedPrefs direct access
     * @return
     */
    AllSettings settings();

    Controller controller();

    NotificationController notification();

    RunTimeSettings runTimeChanges();
}
