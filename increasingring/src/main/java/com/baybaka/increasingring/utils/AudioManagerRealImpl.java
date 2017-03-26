package com.baybaka.increasingring.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.baybaka.increasingring.di.Getter;
import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.audio.RingMode;
import com.baybaka.increasingring.config.SoundStateDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioManagerRealImpl implements AudioManagerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AudioManagerRealImpl.class.getSimpleName());

    private static final int NO_FLAGS = 0;
    private static AudioManager systemAudioManager;

    private final RunTimeSettings mRunTimeSettings;
//    private SettingsService mSettingsService;
//    private int chosenStream;

    private Context appСontext;
    public AudioManagerRealImpl(Context appСontext) {
        systemAudioManager = (AudioManager) appСontext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        this.appСontext = appСontext;
        Getter getter = (Getter)appСontext.getApplicationContext();
        mRunTimeSettings = getter.getRunTimeSettings();
//        mSettingsService = getter.getSetting();
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
        int flags = NO_FLAGS;

        if (mRunTimeSettings.isTestPageOpened()) {
            flags = flags | AudioManager.FLAG_SHOW_UI;
            LOG.info("Sound level set to {}", soundLevel);
            setAudioLevel(soundLevel,stream, flags);
            LOG.info("Now level equals to  {}", getCurrentChosenStreamVolume());
        } else {
            setAudioLevel(soundLevel,stream, flags);
        }
    }

    private void setAudioLevel(int soundLevel, int stream, int flags) {
        systemAudioManager.setStreamVolume(stream, soundLevel, flags);
    }

    private void setAudioLevelShowingUi(int soundLevel) {
        systemAudioManager.setStreamVolume(getChosenStreamType(), soundLevel, AudioManager.FLAG_SHOW_UI);
    }

    private void setAudioLevelNoUi(int soundLevel) {
        //todo should not use getChosenStreamType()
        systemAudioManager.setStreamVolume(getChosenStreamType(), soundLevel, NO_FLAGS);
    }

    @Override
    public int getChosenStreamrMaxHardwareVolumeLevel() {
        return systemAudioManager.getStreamMaxVolume(getChosenStreamType());
    }

    @Override
    public int getStreamMaxHardwareVolumeLevel(int stream) {
        return systemAudioManager.getStreamMaxVolume(stream);
    }

    @Override
    public RingMode getRingerMode() {
        return RingMode.Companion.of(systemAudioManager.getRingerMode());
    }

    @Override
    public int getCurrentChosenStreamVolume() {
        return systemAudioManager.getStreamVolume(getChosenStreamType());
    }


    @Override
    public int getStreamVolume(int stream) {
        return systemAudioManager.getStreamVolume(stream);
    }


//    @Override
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
//                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, NO_FLAGS);
            } else {
                systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false);


                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE);
//                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, NO_FLAGS);
            }

        }

        setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        LOG.debug("vibrate on. mode is : {}. must be 1", systemAudioManager.getRingerMode());
    }


    //do not use adjustVolume - it increase conversation volume
    //systemAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);

    @Override
    public void normalModeStream() {

        if (getChosenStreamType() == AudioManager.STREAM_MUSIC) {
            setRingVolume(0);
            systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        } else if (getChosenStreamType() == AudioManager.STREAM_RING) {

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
        }
    }

    @Override
    public void changeOutputStream(int stream) {

//        chosenStream = stream;
    }

    private int getChosenStreamType() {
        return AudioManager.STREAM_RING;
    }

    private void adjustRingerUp() {
        systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

//    private void down() {
//        systemAudioManager.adjustStreamVolume(getChosenStreamType(), AudioManager.ADJUST_LOWER, AudioManager.FLAG_ALLOW_RINGER_MODES);
//    }

    public void setAudioParamsByPreRingConfig(SoundStateDTO state) {
        if (state != null) {
            setRingVolume(state.getRingVolume());
            setRingerMode(state.getRingMode().getMode());
            setRingVolume(state.getRingVolume());
            setMediaVolume(state.getMusicVolume());
            systemAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, state.getNotificationVolume(), NO_FLAGS);

        }
    }

    @Override
    public SoundStateDTO currentStateToDTO() {
        SoundStateDTO state = new SoundStateDTO();
        state.setRingMode(RingMode.Companion.of(systemAudioManager.getRingerMode()));
        state.setRingVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_RING));
        state.setMusicVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        state.setNotificationVolume(systemAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        return state;
    }

//    @Override
//    public void maxVolDisableMuteVibrate() {
//        boolean fullMode = true;
//        systemAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//
//        if (fullMode){
//            mSettingsService.setSoundStream(false);
//
//            mSettingsService.setMuteFirst(false);
//            mSettingsService.setVibrateFirst(false);
//
//            mSettingsService.enableMinVolumeLimit(true);
//            mSettingsService.setMinVolumeLimit(getChosenStreamrMaxHardwareVolumeLevel());
//
//            mSettingsService.toggleIgnoreSilenceVibrate(true);
//        }
//    }


}
