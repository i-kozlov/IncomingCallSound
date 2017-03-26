package com.baybaka.increasingring.di;

import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.service.VolumeService;

public interface Injector {

    void inject(VolumeService volumeService);

    void inject(PowerButtonReceiver object);

//    void inject(Activity activity);
//    public ObjectGraph getObjectGraph();
}
