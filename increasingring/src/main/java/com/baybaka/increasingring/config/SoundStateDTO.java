package com.baybaka.increasingring.config;

public class SoundStateDTO {

    private int originalRingVolume = -1;
    private int originalRingMode = -1 ;
    private int originalMusicVolume = -1;
    private int originalNotificationVolume = -1;

    public int getOriginalNotificationVolume() {
        return originalNotificationVolume;
    }

    public void setOriginalNotificationVolume(int originalNotificationVolume) {
        this.originalNotificationVolume = originalNotificationVolume;
    }


    public int getOriginalRingVolume() {
        return originalRingVolume;
    }

    public void setOriginalRingVolume(int originalRingVolume) {
        this.originalRingVolume = originalRingVolume;
    }

    public int getOriginalRingMode() {
        return originalRingMode;
    }

    public void setOriginalRingMode(int originalRingMode) {
        this.originalRingMode = originalRingMode;
    }

    public int getOriginalMusicVolume() {
        return originalMusicVolume;
    }

    public void setOriginalMusicVolume(int originalMusicVolume) {
        this.originalMusicVolume = originalMusicVolume;
    }

    @Override
    public String toString() {
        return "SoundStateDTO{" +
                "originalRingVolume=" + originalRingVolume +
                ", originalRingMode=" + originalRingMode +
                ", originalMusicVolume=" + originalMusicVolume +
                ", originalNotificationVolume=" + originalNotificationVolume +
                '}';
    }

    //ignores notification??
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundStateDTO that = (SoundStateDTO) o;

        return originalRingVolume == that.originalRingVolume &&
                originalRingMode == that.originalRingMode &&
                originalMusicVolume == that.originalMusicVolume;

    }

    @Override
    public int hashCode() {
        int result = originalRingVolume;
        result = 31 * result + originalRingMode;
        result = 31 * result + originalMusicVolume;
        return result;
    }
}
