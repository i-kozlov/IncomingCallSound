package com.baybaka.increasingring.utils;


import com.baybaka.increasingring.config.SoundStateDTO;

public interface AudioManagerWrapper {

    void setAudioLevelRespectingLogging(int soundLevel);

    int getChosenStreamrMaxHardwareVolumeLevel();

    int getRingerMode();

    int getCurrentChosenStreamVolume();

    void setRingerMode(int mode);

    void normalModeStream();

    void muteStream();

    void vibrateMode();

    void changeOutputStream(int stream);

    void setAudioParamsByPreRingConfig(SoundStateDTO state);

    SoundStateDTO getCurrentSoundState();

    void maxVolDisableMuteVibrate();
}
