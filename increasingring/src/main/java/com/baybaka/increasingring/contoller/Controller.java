package com.baybaka.increasingring.contoller;

import android.media.AudioManager;

import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.config.CachingConfigFactory;
import com.baybaka.increasingring.config.RingerConfig;
import com.baybaka.increasingring.receivers.TestPageOnCallEvenReceiver;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.notificationlib.NotificationController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class Controller {

    private final Logger LOG = LoggerFactory.getLogger(Controller.class.getSimpleName());

    private final SettingsService mSettingsService;
    private final CachingConfigFactory configFactory;

    private RunTimeSettings mRunTimeSettings;
    private final AudioManagerWrapper mAudioManagerWrapper;


    private volatile VolumeIncreaseThread currentThread;


    private boolean fastModeEnabled = false;
    private boolean ringWhenMute;

    private SoundRestorer mSoundRestorer;
    NotificationController notificationController;

    private String lastNumber = "";
    private int numCount;

    private int numMaxCount;
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

        updateAllConfigs();
    }

    public void startVolumeIncrease() {
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
            currentThread = new VolumeIncreaseThread(config, mAudioManagerWrapper, mRunTimeSettings);
            new Thread(currentThread).start();

            TestPageOnCallEvenReceiver.sendBroadcastToLogReceiver(mRunTimeSettings.getContext());

        } else {
            LOG.info("Abort increasing volume. Phone mode is {} and ringWhenMute function set to {}", ringerMode, false);
        }

        notificationController.startNotify();
    }

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
//        fastModeEnabled = mSettingsService.getAsapState();
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


    public void findPhoneMaximizeVolume() {
        LOG.info("Calling Find phone function");
        mAudioManagerWrapper.maxVolDisableMuteVibrate();
        updateAllConfigs();
        mRunTimeSettings.findPhoneNotification();
    }

    public void findPhone(String incomingNumber) {
        if (!findPhoneEnabled) return;

        if (lastNumber.equals(incomingNumber)) {
            numCount++;
            if (numCount >= numMaxCount) {
                findPhoneMaximizeVolume();
                LOG.info("Calling find my phone function from number {}", incomingNumber);
            }
        } else {
            lastNumber = incomingNumber;
            numCount = 1;
        }
    }
}
