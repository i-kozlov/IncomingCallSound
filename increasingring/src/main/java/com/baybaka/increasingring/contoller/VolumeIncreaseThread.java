package com.baybaka.increasingring.contoller;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.config.RingerConfig;
import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.utils.AudioManagerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolumeIncreaseThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(VolumeIncreaseThread.class.getSimpleName());
    private final RingerConfig config;
    private final AudioManagerWrapper mAudioManagerWrapper;
    private volatile boolean treadStopped = false;
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver mReceiver;

    public void stop() {
        treadStopped = true;
        releasePlayer();

        if (mReceiver != null) {
            rts.getContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                this.mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private RunTimeSettings rts;

    public VolumeIncreaseThread(RingerConfig config, AudioManagerWrapper audioManagerWrapper, RunTimeSettings rts) {
        this.config = config;
        this.rts = rts;
        mAudioManagerWrapper = audioManagerWrapper;
    }

    @Override
    public void run() {

        muteAction();
        vibrateAction();

        int currentSoundLevel = config.getStartSoundLevel();
        //todo подумать, возможно стоит делать ++ в mute и vibrate
        if (currentSoundLevel < 1)
            currentSoundLevel = 1;

        while (!treadStopped && (currentSoundLevel <= config.getAllowedMaxVolume())) {

            if (!configured) {
                configOutputStream();
            }

            LOG.info("Loop volume var = {}. Calling sound++", currentSoundLevel);

            mAudioManagerWrapper.setAudioLevelRespectingLogging(currentSoundLevel);


            currentSoundLevel++;
            waitIntervalSeconds();

        }
    }


    private boolean configured = false;

    private void configOutputStream() {
        //now get current playing ringtone instead of default
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(rts.getContext(), RingtoneManager.TYPE_RINGTONE);
        //
        mAudioManagerWrapper.normalModeStream();

        if (config.isUseMusicStream()) {

            mediaPlayer = MediaPlayer.create(rts.getContext(), uri);
            //mediaPlayer.setDataSource(rts.getContext(), uri);
            //case mediaPlayer creation failed (some sony devices)
            if (mediaPlayer != null) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);

//                mediaPlayer.prepare();
                mediaPlayer.start();

                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//                filter.addAction(Intent.ACTION_SCREEN_OFF);
                mReceiver = new PowerButtonReceiver();
                rts.getContext().registerReceiver(mReceiver, filter);


            } else { //show notify otherwise
                rts.errorNotification(rts.getContext());
            }
        }

        configured = true;
    }


    private void muteAction() {
        if (config.isMuteFirst()) {
            mAudioManagerWrapper.muteStream();
            for (int i = 1; i <= config.getMuteTimes(); i++) {
                if (rts.isLoggingEnabled()) {
                    LOG.info("mute {} time", i);
                }
                waitIntervalSeconds();
            }

        }
    }

    private void vibrateAction() {
        if (config.isVibrateFirst()) {
            mAudioManagerWrapper.vibrateMode();
            for (int i = 1; i <= config.getVibrateTimes(); i++) {
                if (rts.isLoggingEnabled()) {
                    LOG.info("vibrate {} time", i);
                }
                waitIntervalSeconds();
            }
        }

    }

    private void waitIntervalSeconds() {
        try {
            for (int i = 0; i < config.getInterval() * 2; i++) {
                if (treadStopped) break;
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
