package com.baybaka.incomingcallsound.di.module;

import android.app.Application;

import com.baybaka.incomingcallsound.di.scopes.ApplicationScope;
import com.baybaka.incomingcallsound.settings.AllSettings;
import com.baybaka.incomingcallsound.settings.PhonePreferenceController;
import com.baybaka.incomingcallsound.settings.RunTimeSettingsImpl;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.settings.SettingsAdapter;
import com.baybaka.notificationlib.NotificationSettings;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigurationModule {

//    @Singleton
    @Provides
    @ApplicationScope
    RunTimeSettings runTimeChanges(Application application) {
        return new RunTimeSettingsImpl(application);
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

    @Provides @Named("adapter")
    @ApplicationScope
    SettingsService settings(PhonePreferenceController phonePreferenceController, Application application) {
        return new SettingsAdapter(phonePreferenceController, application);
    }

    @Provides @Named("sharedPref")
    @ApplicationScope
    SettingsService sharedPrefs(PhonePreferenceController phonePreferenceController) {
        return phonePreferenceController;
    }

    @Provides
    @ApplicationScope
    NotificationSettings notificationSettings(PhonePreferenceController phonePreferenceController) {
        return phonePreferenceController;
    }



}
