package com.baybaka.increasingring.config;

public class RingerConfig {
    private int startSoundLevel;
    private int interval;
    private int allowedMaxVolume;

    private boolean vibrateFirst;
    private int vibrateTimes;

    private boolean muteFirst;
    private int muteTimes;

    private boolean useMusicStream;

    public boolean isUseMusicStream() {
        return useMusicStream;
    }

    public void setUseMusicStream(boolean useMusicStream) {
        this.useMusicStream = useMusicStream;
    }


    public RingerConfig() {
        interval = 5;
    }

    public int getStartSoundLevel() {
        return startSoundLevel;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public int getAllowedMaxVolume() {
        return allowedMaxVolume;
    }

    public boolean isVibrateFirst() {
        return vibrateFirst;
    }

    public boolean isMuteFirst() {
        return muteFirst;
    }

    public void setStartSoundLevel(int startSoundLevel) {
        this.startSoundLevel = startSoundLevel;
    }

    public void setAllowedMaxVolume(int allowedMaxVolume) {
        this.allowedMaxVolume = allowedMaxVolume;
    }

    public void setVibrateFirst(boolean vibrateFirst) {
        this.vibrateFirst = vibrateFirst;
    }

    public void setMuteFirst(boolean muteFirst) {
        this.muteFirst = muteFirst;
    }

    public int getVibrateTimes() {
        return vibrateTimes;
    }

    public void setVibrateTimes(int vibrateTimes) {
        this.vibrateTimes = vibrateTimes;
    }

    public int getMuteTimes() {
        return muteTimes;
    }

    public void setMuteTimes(int muteTimes) {
        this.muteTimes = muteTimes;
    }
}
