package com.baybaka.increasingring.contoller;


import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.config.SoundStateDTO;
import com.baybaka.increasingring.utils.AudioManagerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SoundRestorer {

    private boolean mIsRestoreVolToUserSetLevelEnabled;
    private int userSetVolumeLevelToRestore;
    private boolean mPauseWaState;
    private int mPauseWaSeconds;

    private SoundStateDTO originalVolumes;

    @Inject
    public SoundRestorer(SettingsService settingsService, AudioManagerWrapper audioManagerWrapper, RunTimeSettings runTimeSettings) {
        mAudioManagerWrapper = audioManagerWrapper;
        mSettingsService = settingsService;
        mRunTimeSettings = runTimeSettings;
        updateConfig();
    }

    private AudioManagerWrapper mAudioManagerWrapper;
    private SettingsService mSettingsService;
    private RunTimeSettings mRunTimeSettings;


    private final Logger LOG = LoggerFactory.getLogger(SoundRestorer.class.getSimpleName());

    public synchronized void saveCurrentSoundLevels() {
        originalVolumes = mAudioManagerWrapper.getCurrentSoundState();
    }

    public synchronized void restoreVolumeToPreRingingLevel() {

        if (originalVolumes == null) return;
        usePauseWorkaround();
        calcRestoreRingLevel();

        if (mRunTimeSettings.isLoggingEnabled()) {
            LOG.info("Restoring sound level. Saved sound level = {}", originalVolumes);
        }


        if (isOkToRestore()) {

            mAudioManagerWrapper.setAudioParamsByPreRingConfig(originalVolumes);
            SoundStateDTO wasSetTo = mAudioManagerWrapper.getCurrentSoundState();

            //double check
            if (originalVolumes.equals(wasSetTo)) {
                LOG.debug("Successful restore to {}", originalVolumes);
            } else {
                LOG.error("Values are not equal. Expected value {}, current value is {}. Trying once more", originalVolumes, wasSetTo);
                mAudioManagerWrapper.setAudioParamsByPreRingConfig(originalVolumes);
            }

            resetSoundLevelHolder();

        } else {
            LOG.info("No need to restore. Saved sound are {}", originalVolumes);
        }
    }

    private void calcRestoreRingLevel() {
        int restoreSoundLevel = getSoundLevelToRestore();
        originalVolumes.setOriginalRingVolume(restoreSoundLevel);
    }

    // todo countDownLatch ?
    private void usePauseWorkaround() {
        int timeToWait = 1;
        if (mPauseWaState) {
            timeToWait += 1000 * mPauseWaSeconds;
        }
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isOkToRestore() {
        // -1 =  был в беззвучном todo всё ещё актуально? сейчас -1 == значение не получено ?
        return originalVolumes.getOriginalRingVolume() > -1;
    }


    private void resetSoundLevelHolder() {
        originalVolumes = null;
    }

    /**
     * Checks if save level is ok or we need to override it with use level
     * @return sound level to restore
     */
    private int getSoundLevelToRestore() {
        int savedSoundLevel = originalVolumes != null ? originalVolumes.getOriginalRingVolume() : -1;

        //if it was 0 -> was in vibrate no need to restore to user level.
        if (savedSoundLevel > 0 ) {
            if (mIsRestoreVolToUserSetLevelEnabled) {
                return userSetVolumeLevelToRestore;
            }
        } else {
            //если громкость 0 а режим нормальный - что-то не так
            if (originalVolumes != null && originalVolumes.getOriginalRingMode() == 2) {
                return 1;
            }
        }
        return savedSoundLevel;
    }

    public void updateConfig() {
        mIsRestoreVolToUserSetLevelEnabled = mSettingsService.isRestoreVolToUserSetLevelEnabled();
        userSetVolumeLevelToRestore = mSettingsService.getUserSetLvl();
        mPauseWaState = mSettingsService.isPauseWAenabled();
        mPauseWaSeconds = mSettingsService.getPauseWAtimeValue();
    }
}
