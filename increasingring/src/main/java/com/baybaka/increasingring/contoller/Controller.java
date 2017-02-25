package com.baybaka.increasingring.contoller;

import android.media.AudioManager;

import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.config.CachingConfigFactory;
import com.baybaka.increasingring.config.RingerConfig;
import com.baybaka.increasingring.receivers.TestPageOnCallEvenReceiver;
import com.baybaka.increasingring.service.RingToneT;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.notificationlib.NotificationController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class Controller {

    private final Logger LOG = LoggerFactory.getLogger(Controller.class.getSimpleName());

    private final SettingsService mSettingsService;
    private final CachingConfigFactory configFactory;
    private final RingToneT ringtone;

    private RunTimeSettings mRunTimeSettings;
    private final AudioManagerWrapper mAudioManagerWrapper;


    private volatile VolumeIncreaseThread currentThread;


    private boolean fastModeEnabled = false;
    private boolean ringWhenMute;

    private SoundRestorer mSoundRestorer;
    private NotificationController notificationController;

    private String lastNumber = "";
    private int numCount;

    private int numMaxCount;
    private long lastCallTimeForTHISNumber = new Date().getTime();
    private boolean findPhoneEnabled;

    @Inject
    public Controller(SettingsService settingsService, RunTimeSettings runTimeSettings,
                      AudioManagerWrapper audioManagerWrapper,
                      CachingConfigFactory configFactory, SoundRestorer soundRestorer,
                      NotificationController notificationController) {

        mSettingsService = settingsService;
        mRunTimeSettings = runTimeSettings;
        mAudioManagerWrapper = audioManagerWrapper;

        this.configFactory = configFactory;
        mSoundRestorer = soundRestorer;
        this.notificationController = notificationController;
        this.ringtone = new RingToneT(runTimeSettings.getContext());
        updateAllConfigs();
    }

    public void startVolumeIncrease(String callerNumber) {
        //to make sure no old increase task left
        stopVolumeIncrease();

        int ringerMode = mAudioManagerWrapper.getRingerMode();
        int preRingLevel = mAudioManagerWrapper.getCurrentChosenStreamVolume();
        if (mRunTimeSettings.isLoggingEnabled()) {
            LOG.debug("Inside increaseVolume, Ringer_MODE is {} and volume is {}", ringerMode, preRingLevel);
        }

        if (ringWhenMute || isPhoneInNormalRingMode()) {
            mSoundRestorer.saveCurrentSoundLevels();

            asapAction();
            checkForConfigUpdate();

            RingerConfig config = configFactory.getConfig(preRingLevel);
            currentThread = createThread(config, callerNumber) ; //new VolumeIncreaseThread(config, mAudioManagerWrapper, mRunTimeSettings, callerNumber);
            new Thread(currentThread).start();

            TestPageOnCallEvenReceiver.sendBroadcastToLogReceiver(mRunTimeSettings.getContext());

        } else {
            LOG.info("Abort increasing volume. Phone mode is {} and ringWhenMute function set to {}", ringerMode, false);
        }

        notificationController.startNotify();
    }

    /**
     * Fix for first call with max sound on some phones
     */
    private void asapAction() {
        if (fastModeEnabled) {
            mAudioManagerWrapper.setAudioLevelRespectingLogging(1);
        }
    }

    public void restoreVolumeToPreRingingLevel() {
        stopVolumeIncrease();
        mSoundRestorer.restoreVolumeToPreRingingLevel();
    }

    private void checkForConfigUpdate() {
        if (mRunTimeSettings.isConfigChanged()) {
            updateAllConfigs();
            mRunTimeSettings.configIsUpdated();
        }
    }

    public void updateAllConfigs() {
        mAudioManagerWrapper.changeOutputStream(getSoundStream());
        configFactory.updateStream(getSoundStream());

        configFactory.setMaxHardwareVolumeLevel(mAudioManagerWrapper.getChosenStreamrMaxHardwareVolumeLevel());
        configFactory.updateConfig();
        mSoundRestorer.updateConfig();
        updateLocal();
    }

    private void updateLocal() {
//        fastModeEnabled = settings.getAsapState();
        fastModeEnabled = true;
        ringWhenMute = mSettingsService.ringWhenMute();

        numMaxCount = mSettingsService.getFindPhoneCount();
        findPhoneEnabled = mSettingsService.isFindPhoneEnabled();
    }

    private boolean isPhoneInNormalRingMode() {
        return mAudioManagerWrapper.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }


    private int getSoundStream() {
        return mSettingsService.getSoundStream();
    }

    public void stopVolumeIncrease() {
        if (currentThread != null) {
            currentThread.stop();
            currentThread = null;
        }
        notificationController.stopNotify();

    }

    private VolumeIncreaseThread createThread(RingerConfig config, String callerNumber){
        int stream = mAudioManagerWrapper.getCurrentChosenStreamVolume();
        if( stream == AudioManager.STREAM_MUSIC){
            return new RingTone(config, mAudioManagerWrapper, mRunTimeSettings);
        } else {
            return new Music(config, mAudioManagerWrapper, mRunTimeSettings, callerNumber, ringtone);
        }
    }

    private void findPhoneMaximizeVolume(String callerNumber) {
        stopVolumeIncrease();
        mAudioManagerWrapper.changeOutputStream(AudioManager.STREAM_MUSIC);
        RingerConfig config = configFactory.getFindPhoneConfig();
        currentThread = createThread(config, callerNumber); //new VolumeIncreaseThread(config, mAudioManagerWrapper, mRunTimeSettings, callerNumber);
        new Thread(currentThread).start();

        mRunTimeSettings.configurationChanged(); // to reset stream on next call

//        mAudioManagerWrapper.maxVolDisableMuteVibrate();
//        updateAllConfigs();
        mRunTimeSettings.findPhoneNotification();
    }

    public void findPhone(String incomingNumber) {
        if (!findPhoneEnabled) return;
        long newCallTime = new Date().getTime();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(newCallTime - lastCallTimeForTHISNumber);
        boolean timeIsOk = minutes < 6;

        if (lastNumber.equals(incomingNumber) && timeIsOk) {

            numCount++;
            if (numCount >= numMaxCount) {
                LOG.info("Calling find my phone function for number {}", incomingNumber);
                findPhoneMaximizeVolume(incomingNumber);
            }

        } else {
            numCount = 1;
            lastNumber = incomingNumber;
        }
        lastCallTimeForTHISNumber = newCallTime;
    }
}
