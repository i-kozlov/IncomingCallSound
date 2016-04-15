package com.baybaka.incomingcallsound.di.module;

import android.app.Application;

import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.incomingcallsound.settings.AllSettings;
import com.baybaka.incomingcallsound.settings.PhonePreferenceController;
import com.baybaka.incomingcallsound.settings.RunTimeSettingsIpml;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.notificationlib.NotificationSettings;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigurationModule {

    @Provides
    @ApplicationScope
    RunTimeSettings runTimeChanges() {
        return new RunTimeSettingsIpml();
    }

    @Provides
    @ApplicationScope
    PhonePreferenceController mPreferenceController(Application application) {
        return new PhonePreferenceController(application);
    }

    @Provides
    @ApplicationScope
    AllSettings allSettings(PhonePreferenceController phonePreferenceController) {
        return phonePreferenceController;
    }

    @Provides
    @ApplicationScope
    SettingsService settings(PhonePreferenceController phonePreferenceController) {
        return phonePreferenceController;
    }

    @Provides
    @ApplicationScope
    NotificationSettings mNotificationSettings(PhonePreferenceController phonePreferenceController) {
        return phonePreferenceController;
    }



}
