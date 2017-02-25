package com.baybaka.increasingring;

import com.baybaka.increasingring.utils.AudioManagerWrapper;

public interface Getter {

    RunTimeSettings getRunTimeSettings();

    SettingsService getSetting();

    AudioManagerWrapper getAudioManger();
}
