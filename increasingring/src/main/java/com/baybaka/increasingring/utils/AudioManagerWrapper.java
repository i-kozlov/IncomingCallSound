package com.baybaka.increasingring.utils;


import com.baybaka.increasingring.audio.RingMode;
import com.baybaka.increasingring.config.SoundStateDTO;

public interface AudioManagerWrapper {


    void setAudioLevelRespectingLogging(int soundLevel, int stream);

    int getStreamMaxHardwareVolumeLevel(int stream);

    int getStreamVolume(int stream);

    RingMode getRingerMode();

    void setAudioParamsByPreRingConfig(SoundStateDTO state);

    SoundStateDTO currentStateToDTO();

//    void setAudioLevelRespectingLogging(int soundLevel);
//    int getChosenStreamrMaxHardwareVolumeLevel();

//    int getCurrentChosenStreamVolume();

//    void setRingerMode(int mode);

//    void normalModeStream();


//    void muteStream();

//    void vibrateMode();

//    void changeOutputStream(int stream);


//    @Deprecated
//    void maxVolDisableMuteVibrate();
}
