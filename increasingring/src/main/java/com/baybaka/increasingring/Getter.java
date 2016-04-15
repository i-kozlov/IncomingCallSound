package com.baybaka.increasingring;

import com.baybaka.increasingring.utils.AudioManagerWrapper;

public interface Getter {

    RunTimeSettings getRunTimeChanges();

    SettingsService getSetting();

    AudioManagerWrapper getAudioManger();
}
