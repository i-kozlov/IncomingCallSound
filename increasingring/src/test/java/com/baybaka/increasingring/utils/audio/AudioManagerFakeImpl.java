package com.baybaka.increasingring.utils.audio;

import com.baybaka.increasingring.audio.RingMode;
import com.baybaka.increasingring.config.SoundStateDTO;
import com.baybaka.increasingring.utils.AudioManagerWrapper;


public class AudioManagerFakeImpl implements AudioManagerWrapper {
    private int currentSoundLevelHolder;

    public AudioManagerFakeImpl(int currentSoundLevel) {
        currentSoundLevelHolder = currentSoundLevel;
    }

    @Override
    public void setAudioLevelRespectingLogging(int soundLevel) {
        currentSoundLevelHolder = soundLevel;
    }

    @Override
    public void setAudioLevelRespectingLogging(int soundLevel, int stream) {

    }

    @Override
    public int getChosenStreamrMaxHardwareVolumeLevel() {
        return 7;
    }

    @Override
    public RingMode getRingerMode() {
        return RingMode.RINGER_MODE_NORMAL;
    }

    @Override
    public int getCurrentChosenStreamVolume() {
        return currentSoundLevelHolder;
    }

//    @Override
    public void setRingerMode(int mode) {

    }

    @Override
    public void normalModeStream() {

    }

    @Override
    public int getStreamVolume(int stream) {
        return 0;
    }

    @Override
    public void muteStream() {

    }

    @Override
    public void vibrateMode() {

    }

    @Override
    public void changeOutputStream(int stream) {

    }

    @Override
    public void setAudioParamsByPreRingConfig(SoundStateDTO state) {

    }

    @Override
    public SoundStateDTO currentStateToDTO() {
        return null;
    }

//    @Override
    public void maxVolDisableMuteVibrate() {

    }
}
