package com.baybaka.incomingcallsound.settings;

import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.notificationlib.NotificationSettings;

public interface AllSettings extends SettingsService, NotificationSettings {

    void changeServiceEnabledSettings(boolean newValue);
    void setNewIntervalValue(int value);
    void toggleMaxVolumeLimit(boolean newValue);
    void setMaxVolumeLimit(int maxLevel);
    void enableMinVolumeLimit(boolean newValue);
    void setMinVolumeLimit(int minLevel);
    void togglePauseWAState(boolean state);
    void setPauseWAtimeValue(int pauseSec);
    void toggleRestoreVolToUserSetLevel(boolean state);
    void setUserSetLvl(int pauseSec);
    void toggleIgnoreSilenceVibrate(boolean state);
    void setVibrateFirst(boolean newValue);
    void setRunForeground(boolean newValue);
    void setLoggingState(boolean newValue);
    void setSoundStream(boolean STREAM_RING);
    void setAsapState(boolean newValue);
    void setVibrateTimesCount(int count);
    void setMuteFirst(boolean newValue);
    void setMuteTimesCount(int count);
    void resetConfig();
    void smartResetConfig();
    void setFindPhoneEnabled(boolean checked);
    void setFindPhoneCount(int times);
    void setMinNotificationPriory(boolean checked);
    void setSkipRing(boolean checked);
    void setSimpleMode(boolean checked);
}
