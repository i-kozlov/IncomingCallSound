package com.baybaka.increasingring.config

import com.baybaka.increasingring.audio.RingMode

class SoundStateDTO {

    var ringVolume = -1
    lateinit var ringMode: RingMode

    var musicVolume = -1

    var notificationVolume = -1

    override fun toString(): String {
        return "SoundStateDTO{" +
                "ringMode=" + ringMode +
                ", ringVolume=" + ringVolume +
                ", musicVolume=" + musicVolume +
                ", notificationVolume=" + notificationVolume +
                '}'
    }

    //ignores notification??
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false

        val that = other as? SoundStateDTO

        return that?.let {
            ringMode === it.ringMode &&
                    ringVolume == it.ringVolume &&
                    musicVolume == it.musicVolume
        } ?: false


    }

    override fun hashCode(): Int {
        var result = ringVolume
        result = 31 * result + ringMode.ordinal
        result = 31 * result + musicVolume
        return result
    }
}
