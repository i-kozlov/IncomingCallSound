package com.baybaka.increasingring.config;

import android.media.AudioManager;

import com.baybaka.increasingring.SettingsService;

import javax.inject.Inject;

public class CachingConfigFactory {

    private SettingsService settingsService;

    private final RingerConfig config;
    private int maxHardwareVolumeLevel;
    private int oldPreRingLevel;

    private boolean isMinVolLimited;
    private boolean isMinLimitToPreRing;
    private int minVolLimit;

    private boolean isMaxVolLimited;
    private boolean isMaxLimitToPrering;
    private int maxVolLimit;


    @Inject
    public CachingConfigFactory(SettingsService settingsService) {
        this.settingsService = settingsService;
        config = new RingerConfig();
    }

    public void setMaxHardwareVolumeLevel(int maxHardwareVolumeLevel) {
        this.maxHardwareVolumeLevel = maxHardwareVolumeLevel;
    }

    public RingerConfig getConfig(int preRingLevel) {

        if (preRingLevel != oldPreRingLevel) {
            if (isMinLimitToPreRing || isMaxLimitToPrering) {
                updateMinMax(preRingLevel);
            }
            oldPreRingLevel = preRingLevel;
        }
        return config;
    }

    public RingerConfig getFindPhoneConfig(){
        RingerConfig config = new RingerConfig();
        config.setStartSoundLevel(14);
        config.setAllowedMaxVolume(14);
        config.setUseMusicStream(true);
        config.setInterval(1);
        config.setVibrateFirst(false);
        config.setMuteFirst(false);
        return config;
    }


    public void updateConfig() {

        localConfigUpdate();

        config.setInterval(readIntervalConfig());

        config.setMuteFirst(readMuteConfig());
        config.setMuteTimes(readMuteTimes());

        config.setVibrateFirst(readVibrateConfig());
        config.setVibrateTimes(readVibrateTimes());
        // in case of change upper volume limit
        updateMinMax(1);
    }

    private int readMuteTimes() {
        return settingsService.getMuteTimesCount();
    }

    private int readVibrateTimes() {
        return settingsService.getVibrateTimesCount();
    }

    private void localConfigUpdate(){
        minVolLimit = settingsService.getMinVolumeLimit();
        isMinVolLimited = settingsService.isMinVolumeLimited();

        isMinLimitToPreRing = isMinVolLimited && isLimitEqualsToPreRing(minVolLimit);

        maxVolLimit = settingsService.getMaxVolumeLimit();
        isMaxVolLimited = settingsService.isMaxVolumeLimited();

        isMaxLimitToPrering = isMaxVolLimited && isLimitEqualsToPreRing(maxVolLimit);

    }

    private void updateMinMax(int preRingLevel) {

        int minLimitCalc = 1;
        if(isMinVolLimited) {
            minLimitCalc = isMinLimitToPreRing ? preRingLevel : minVolLimit;
        }

        int maxLimitCalc = maxHardwareVolumeLevel;
        if(isMaxVolLimited) {
            maxLimitCalc = isMaxLimitToPrering ? preRingLevel : maxVolLimit;
        }

        config.setStartSoundLevel(minLimitCalc);
        config.setAllowedMaxVolume(maxLimitCalc);
    }





    private boolean isLimitEqualsToPreRing(int resultLevel) {
        return resultLevel == maxHardwareVolumeLevel + 1;
    }

    private int readIntervalConfig() {
        return settingsService.getInterval();
    }

    private boolean readMuteConfig() {
        return settingsService.isMuteFirst();
    }

    private boolean readVibrateConfig() {
        return settingsService.isVibrateFirst();
    }

    public void updateStream(int soundStream) {
        if (soundStream == AudioManager.STREAM_MUSIC) {
            config.setUseMusicStream(true);
        } else {
            config.setUseMusicStream(false);
        }
    }


}
