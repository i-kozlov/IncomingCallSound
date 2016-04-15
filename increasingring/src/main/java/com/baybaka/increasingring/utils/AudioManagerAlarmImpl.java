package com.baybaka.increasingring.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.baybaka.increasingring.Getter;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.config.SoundStateDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioManagerAlarmImpl implements AudioManagerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AudioManagerAlarmImpl.class.getSimpleName());

    private static final int NO_FLAGS = 0;
    private static AudioManager systemAudioManager;

    private final RunTimeSettings mRunTimeSettings;
    SettingsService mSettingsService;
    private int chosenStream;

    Context appСontext;
    public AudioManagerAlarmImpl(Context appСontext) {
        systemAudioManager = (AudioManager) appСontext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        this.appСontext = appСontext;
        Getter getter = (Getter)appСontext.getApplicationContext();
        mRunTimeSettings = getter.getRunTimeChanges();
        mSettingsService = getter.getSetting();
        chosenStream = mSettingsService.getSoundStream();
    }

    @Override
    public void setAudioLevelRespectingLogging(int soundLevel) {
        int flags = 0;

        if (mRunTimeSettings.isTestPageOpened()) {
            flags = flags | AudioManager.FLAG_SHOW_UI;
            LOG.info("Sound level set to {}", soundLevel);
            setAudioLevelShowingUi(soundLevel);
            LOG.info("Now level equals to  {}", getCurrentChosenStreamVolume());
        } else {
            setAudioLevelNoUi(soundLevel);
        }
    }

    private void setAudioLevelShowingUi(int soundLevel) {
        systemAudioManager.setStreamVolume(getChosenStreamType(), soundLevel, AudioManager.FLAG_SHOW_UI);
    }

    private void setAudioLevelNoUi(int soundLevel) {
        systemAudioManager.setStreamVolume(getChosenStreamType(), soundLevel, NO_FLAGS);
    }

    @Override
    public int getChosenStreamrMaxHardwareVolumeLevel() {
        return systemAudioManager.getStreamMaxVolume(getChosenStreamType());
    }

    @Override
    public int getRingerMode() {
        return systemAudioManager.getRingerMode();
    }

    @Override
    public int getCurrentChosenStreamVolume() {
        return systemAudioManager.getStreamVolume(getChosenStreamType());
    }

    @Override
    public void setRingerMode(int mode) {
        systemAudioManager.setRingerMode(mode);
    }

    @Override
    public void muteStream() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, NO_FLAGS);
        } else {
            systemAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
        }
    }

    @Override
    public void vibrateMode() {

        systemAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }



    @Override
    public void normalModeStream() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, NO_FLAGS);
        } else {
            systemAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        }
        systemAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    @Override
    public void changeOutputStream(int stream) {
        chosenStream = stream;
    }

    private int getChosenStreamType() {
        return AudioManager.STREAM_ALARM;
    }



    public void setAudioParamsByPreRingConfig(SoundStateDTO state) {
        if (state != null) {
            //restore alarm
        }
    }

    @Override
    public SoundStateDTO getCurrentSoundState() {
        SoundStateDTO state = new SoundStateDTO();
        state.setOriginalRingMode(systemAudioManager.getRingerMode());
        state.setOriginalRingVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_RING));
        state.setOriginalMusicVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return state;
    }

    @Override
    public void maxVolDisableMuteVibrate() {

    }


}
