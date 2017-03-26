package com.baybaka.increasingring.audio

import android.media.AudioManager

enum class RingMode(val mode: Int) {
    RINGER_MODE_SILENT(AudioManager.RINGER_MODE_SILENT),
    RINGER_MODE_VIBRATE(AudioManager.RINGER_MODE_VIBRATE),
    RINGER_MODE_NORMAL(AudioManager.RINGER_MODE_NORMAL);

    companion object {
        fun of(mode: Int): RingMode = when(mode){
            AudioManager.RINGER_MODE_SILENT -> RINGER_MODE_SILENT
            AudioManager.RINGER_MODE_VIBRATE -> RINGER_MODE_VIBRATE
            AudioManager.RINGER_MODE_NORMAL -> RINGER_MODE_NORMAL
            else -> RINGER_MODE_NORMAL
        }
    }

}