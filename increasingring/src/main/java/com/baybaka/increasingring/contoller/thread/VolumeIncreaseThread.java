package com.baybaka.increasingring.contoller.thread;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.baybaka.increasingring.service.RingToneT;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.config.RingerConfig;
import com.baybaka.increasingring.receivers.PowerButtonReceiver;
import com.baybaka.increasingring.utils.AudioManagerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolumeIncreaseThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(com.baybaka.increasingring.contoller.VolumeIncreaseThread.class.getSimpleName());
    private final RingerConfig config;
    private final AudioManagerWrapper mAudioManagerWrapper;
    private String mPhoneNumber;
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

    public VolumeIncreaseThread(RingerConfig config, AudioManagerWrapper audioManagerWrapper, RunTimeSettings rts, String phoneNumber) {
        this.config = config;
        this.rts = rts;
        mAudioManagerWrapper = audioManagerWrapper;
        mPhoneNumber = phoneNumber;
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

        mAudioManagerWrapper.normalModeStream();

        if (config.isUseMusicStream()) {

            RingToneT t = new RingToneT(rts.getContext());
            mediaPlayer = t.configurePlayer(mPhoneNumber);

            if (mediaPlayer != null) {

                mediaPlayer.start();

                //configure music of with power key
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
