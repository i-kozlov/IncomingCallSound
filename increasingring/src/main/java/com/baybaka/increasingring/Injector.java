package com.baybaka.increasingring;

import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.service.VolumeService;

public interface Injector {

    void inject(VolumeService volumeService);

    void inject(PowerButtonReceiver object);
//    public ObjectGraph getObjectGraph();
}
