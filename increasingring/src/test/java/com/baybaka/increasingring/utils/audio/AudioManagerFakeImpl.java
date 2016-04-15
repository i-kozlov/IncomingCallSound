package com.baybaka.increasingring.utils.audio;

import android.media.AudioManager;

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
    public int getChosenStreamrMaxHardwareVolumeLevel() {
        return 7;
    }

    @Override
    public int getRingerMode() {
        return AudioManager.RINGER_MODE_NORMAL;
    }

    @Override
    public int getCurrentChosenStreamVolume() {
        return currentSoundLevelHolder;
    }

    @Override
    public void setRingerMode(int mode) {

    }

    @Override
    public void normalModeStream() {

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
    public SoundStateDTO getCurrentSoundState() {
        return null;
    }

    @Override
    public void maxVolDisableMuteVibrate() {

    }
}
