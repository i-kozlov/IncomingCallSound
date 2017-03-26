package com.baybaka.increasingring.contoller;


import android.os.Handler;

import com.baybaka.increasingring.settings.RunTimeSettings;
import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.audio.RingMode;
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

    synchronized void saveCurrentSoundLevels() {
        originalVolumes = mAudioManagerWrapper.currentStateToDTO();
    }

    synchronized void restoreVolumeToPreRingingLevel() {

        if (originalVolumes == null) {
            LOG.info("skip restoring volume");
            return;
        }
        usePauseWorkaround();
        calcRestoreRingLevel();

        if (mRunTimeSettings.isLoggingEnabled()) {
            LOG.info("Restoring sound level. Saved sound level = {}", originalVolumes);
        }


        if (isOkToRestore()) {

            mAudioManagerWrapper.setAudioParamsByPreRingConfig(originalVolumes);
            SoundStateDTO wasSetTo = mAudioManagerWrapper.currentStateToDTO();

            //double check
            if (originalVolumes.equals(wasSetTo)) { //todo неактуально при return to user level
                LOG.debug("Successful restore to {}", originalVolumes);
                resetSoundLevelHolder();
            } else {
                LOG.error("Values are not equal. Expected value {}, current value is {}. Trying once more", originalVolumes, wasSetTo);
                final Handler handler = new Handler();

                final Runnable r = new Runnable() {
                    public void run() {
                        mAudioManagerWrapper.setAudioParamsByPreRingConfig(originalVolumes);
                        SoundStateDTO wasSetTo = mAudioManagerWrapper.currentStateToDTO();
                        LOG.info("At 2nd attempt. Current value is {}.", wasSetTo);
//                        handler.postDelayed(this, 1000);
                        resetSoundLevelHolder();
                    }
                };

                handler.postDelayed(r, 3000);


            }

//            resetSoundLevelHolder();

        } else {
            LOG.info("No need to restore. Saved sound are {}", originalVolumes);
        }
    }

    private void calcRestoreRingLevel() {
        int restoreSoundLevel = getSoundLevelToRestore();
        originalVolumes.setRingVolume(restoreSoundLevel);
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

    private boolean isOkToRestore() {
        // -1 =  был в беззвучном todo всё ещё актуально? сейчас -1 == значение не получено ?
        return originalVolumes.getRingVolume() > -1;
    }


    private void resetSoundLevelHolder() {
        originalVolumes = null;
    }

    /**
     * Checks if save level is ok or we need to override it with user level
     * @return sound level to restore
     */
    private int getSoundLevelToRestore() {
        int savedSoundLevel = originalVolumes != null ? originalVolumes.getRingVolume() : -1;

        //if it was 0 -> was in vibrate no need to restore to user level.
        if (savedSoundLevel > 0 ) {
            if (mIsRestoreVolToUserSetLevelEnabled) {
                return userSetVolumeLevelToRestore;
            }
        } else {
            //если громкость 0 а режим нормальный - что-то не так
            if (originalVolumes != null && originalVolumes.getRingMode() == RingMode.RINGER_MODE_NORMAL) {
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
