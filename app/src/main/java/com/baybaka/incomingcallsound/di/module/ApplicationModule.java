package com.baybaka.incomingcallsound.di.module;

import android.app.Application;

import com.baybaka.incomingcallsound.MyApp;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module
public class ApplicationModule {

    private final MyApp application;

    public ApplicationModule(MyApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application application() {
        return application;
    }


}
