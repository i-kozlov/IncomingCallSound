package com.baybaka.increasingring.config;

public class SoundStateDTO {

    private int originalRingVolume = -1;
    private int originalRingMode = -1 ;
    private int originalMusicVolume = -1;

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
        return "{" +
                "originalRingVolume=" + originalRingVolume +
                ", originalRingMode=" + originalRingMode +
                ", originalMusicVolume=" + originalMusicVolume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundStateDTO that = (SoundStateDTO) o;

        if (originalRingVolume != that.originalRingVolume) return false;
        if (originalRingMode != that.originalRingMode) return false;
        return originalMusicVolume == that.originalMusicVolume;

    }

    @Override
    public int hashCode() {
        int result = originalRingVolume;
        result = 31 * result + originalRingMode;
        result = 31 * result + originalMusicVolume;
        return result;
    }
}
