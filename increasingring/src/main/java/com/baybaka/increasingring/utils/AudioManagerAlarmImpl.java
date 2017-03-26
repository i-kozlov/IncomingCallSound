package com.baybaka.increasingring.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.baybaka.increasingring.di.Getter;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.audio.RingMode;
import com.baybaka.increasingring.config.SoundStateDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioManagerAlarmImpl implements AudioManagerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AudioManagerAlarmImpl.class.getSimpleName());

    private static final int NO_FLAGS = 0;
    private static AudioManager systemAudioManager;

    private final RunTimeSettings mRunTimeSettings;
    SettingsService mSettingsService;
    private int chosenStream = AudioManager.STREAM_ALARM;

    Context appСontext;
    public AudioManagerAlarmImpl(Context appСontext) {
        systemAudioManager = (AudioManager) appСontext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        this.appСontext = appСontext;
        Getter getter = (Getter)appСontext.getApplicationContext();
        mRunTimeSettings = getter.getRunTimeSettings();
        mSettingsService = getter.getSetting();
//        chosenStream = mSettingsService.getSoundStream();
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

    @Override
    public void setAudioLevelRespectingLogging(int soundLevel, int stream) {

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
    public int getStreamMaxHardwareVolumeLevel(int stream) {
        return 0;
    }

    @Override
    public RingMode getRingerMode() {
        return null;
    }


    @Override
    public int getCurrentChosenStreamVolume() {
        return systemAudioManager.getStreamVolume(getChosenStreamType());
    }

//    @Override
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
    public int getStreamVolume(int stream) {
        return 0;
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
    public SoundStateDTO currentStateToDTO() {
        SoundStateDTO state = new SoundStateDTO();

        state.setRingVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_RING));
        state.setMusicVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return state;
    }

//    @Override
    public void maxVolDisableMuteVibrate() {

    }


}
