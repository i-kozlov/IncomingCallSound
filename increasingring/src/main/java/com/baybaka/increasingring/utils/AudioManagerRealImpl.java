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

public class AudioManagerRealImpl implements AudioManagerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AudioManagerRealImpl.class.getSimpleName());

    private static final int NO_FLAGS = 0;
    private static AudioManager systemAudioManager;

    private final RunTimeSettings mRunTimeSettings;
    SettingsService mSettingsService;
    private int chosenStream;

    Context appСontext;
    public AudioManagerRealImpl(Context appСontext) {
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

    private void setMediaVolume(int index) {
//        systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, NO_FLAGS);
    }

    private void setRingVolume(int level) {
        systemAudioManager.setStreamVolume(AudioManager.STREAM_RING, level, NO_FLAGS);
    }


    @Override
    public void muteStream() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, NO_FLAGS);
        } else {
            systemAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
        }

        systemAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        LOG.debug("mute on. mode is : {}. must be 0", systemAudioManager.getRingerMode());
    }

    @Override
    public void vibrateMode() {

        if (getChosenStreamType() == AudioManager.STREAM_MUSIC) {
            setRingVolume(0);
            // check do we still need it  ?
            systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_VIBRATE);
            } else {
                systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false);


                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE);
            }

        }

        setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        LOG.debug("vibrate on. mode is : {}. must be 1", systemAudioManager.getRingerMode());
    }


    //do not use adjustVolume - it increase conversation volume
    //systemAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);

    @Override
    public void normalModeStream() {

        switch (getChosenStreamType()) {
            case AudioManager.STREAM_MUSIC:
                setRingVolume(0);
                systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//            systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, NO_FLAGS);

                break;

            case AudioManager.STREAM_RING:
            default:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                } else {
                    systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
                    setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    adjustRingerUp();
//                systemAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
                }

                setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                setRingVolume(1);
                break;
        }
    }

    @Override
    public void changeOutputStream(int stream) {
        chosenStream = stream;
    }

    private int getChosenStreamType() {
        return chosenStream;
    }

    private void adjustRingerUp() {
        systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

//    private void down() {
//        systemAudioManager.adjustStreamVolume(getChosenStreamType(), AudioManager.ADJUST_LOWER, AudioManager.FLAG_ALLOW_RINGER_MODES);
//    }

    public void setAudioParamsByPreRingConfig(SoundStateDTO state) {
        if (state != null) {
            setRingerMode(state.getOriginalRingMode());
            setRingVolume(state.getOriginalRingVolume());
            setMediaVolume(state.getOriginalMusicVolume());
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
        boolean fullMode = true;
        systemAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        if (fullMode){
            mSettingsService.setSoundStream(false);

            mSettingsService.setMuteFirst(false);
            mSettingsService.setVibrateFirst(false);

            mSettingsService.enableMinVolumeLimit(true);
            mSettingsService.setMinVolumeLimit(getChosenStreamrMaxHardwareVolumeLevel());

            mSettingsService.toggleIgnoreSilenceVibrate(true);
        }
    }


}
