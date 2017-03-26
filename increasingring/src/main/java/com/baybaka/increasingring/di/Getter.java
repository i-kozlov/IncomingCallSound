package com.baybaka.increasingring.di;

import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.utils.AudioManagerWrapper;

public interface Getter {

    RunTimeSettings getRunTimeSettings();

    SettingsService getSetting();

    AudioManagerWrapper getAudioManger();
}
