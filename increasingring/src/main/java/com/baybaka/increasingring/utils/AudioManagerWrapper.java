package com.baybaka.increasingring.utils;


import com.baybaka.increasingring.audio.RingMode;
import com.baybaka.increasingring.config.SoundStateDTO;

public interface AudioManagerWrapper {

    void setAudioLevelRespectingLogging(int soundLevel);

    void setAudioLevelRespectingLogging(int soundLevel, int stream);

    @Deprecated
    int getChosenStreamrMaxHardwareVolumeLevel();

    int getStreamMaxHardwareVolumeLevel(int stream);

    RingMode getRingerMode();

    int getCurrentChosenStreamVolume();

//    void setRingerMode(int mode);

//    void normalModeStream();

    int getStreamVolume(int stream);

//    void muteStream();

//    void vibrateMode();

//    @Deprecated
//    void changeOutputStream(int stream);

    void setAudioParamsByPreRingConfig(SoundStateDTO state);

    SoundStateDTO currentStateToDTO();

//    @Deprecated
//    void maxVolDisableMuteVibrate();
}
