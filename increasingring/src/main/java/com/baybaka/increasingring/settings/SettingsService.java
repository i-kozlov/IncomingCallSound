package com.baybaka.increasingring.settings;

public interface SettingsService {
    String FILE_NAME = "settings_v2.13";

    int getInterval();

    boolean isServiceEnabledInConfig();

    int getMaxVolumeLimit();

    boolean isMaxVolumeLimited();

    boolean isMinVolumeLimited();

    int getMinVolumeLimit();

    boolean isPauseWAenabled();

    int getPauseWAtimeValue();

    boolean isRestoreVolToUserSetLevelEnabled();

    int getUserSetLvl();

    boolean ringWhenMute();

    boolean isVibrateFirst();

    boolean startInForeground();

    boolean isLoggingEnabled();

    int getSoundStream();

    boolean getAsapState();

    int getVibrateTimesCount();

    boolean isMuteFirst();

    int getMuteTimesCount();

    String all();

    boolean isFindPhoneEnabled();

    int getFindPhoneCount();

    boolean showNotificationWithMinPriority();

    boolean isProVersion();

    boolean isSkipRing();

    boolean useSimpleMode();

    boolean canUseMusicStream();
}
