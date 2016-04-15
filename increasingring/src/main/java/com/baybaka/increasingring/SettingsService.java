package com.baybaka.increasingring;

public interface SettingsService {
    String FILE_NAME = "settings_v2";

    int getInterval();

    boolean isServiceEnabledInConfig();

    void changeServiceEnabledSettings(boolean newValue);

    void setNewIntervalValue(int value);

    // max volume
    boolean isMaxVolumeLimited();

    void toggleMaxVolumeLimit(boolean newValue);

    int getMaxVolumeLimit();

    void setMaxVolumeLimit(int maxLevel);

    //////////////////////min volume
    boolean isMinVolumeLimited();

    void enableMinVolumeLimit(boolean newValue);

    int getMinVolumeLimit();

    void setMinVolumeLimit(int minLevel);

    boolean isPauseWAenabled();

    void togglePauseWAState(boolean newValue);

    int getPauseWAtimeValue();

    void setPauseWAtimeValue(int pauseSec);

    boolean isRestoreVolToUserSetLevelEnabled();

    void toggleRestoreVolToUserSetLevel(boolean newValue);

    int getUserSetLvl();

    void setUserSetLvl(int pauseSec);

    ////////////
    boolean ringWhenMute();

    void toggleIgnoreSilenceVibrate(boolean newValue);

    ////////////
    boolean isVibrateFirst();

    void setVibrateFirst(boolean newValue);

    ////////////
    boolean startInForeground();

    void setRunForeground(boolean newValue);

    ////////////
    boolean isLoggingEnabled();

    void setLoggingState(boolean newValue);

    int getSoundStream();

    void setSoundStream(boolean newValue);

    void setAsapState(boolean newValue);

    boolean getAsapState();

    int getVibrateTimesCount();

    void setVibrateTimesCount(int newValue);

    boolean isMuteFirst();

    void setMuteFirst(boolean newValue);

    int getMuteTimesCount();

    void setMuteTimesCount(int newValue);

    void resetConfig();

    String all();

    boolean isFindPhoneEnabled();

    void setFindPhoneEnabled(boolean checked);

    void setFindPhoneCount(int progress);

    int getFindPhoneCount();

    boolean showNotificationWithMinPriority();

    void setMinNotificationPriory(boolean checked);

    boolean isProVersion();
}
