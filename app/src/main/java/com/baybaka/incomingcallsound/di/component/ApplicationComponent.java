package com.baybaka.incomingcallsound.di.component;

import android.app.Application;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Application getApplication();

    void inject(MyApp app);
}
