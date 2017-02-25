package com.baybaka.increasingring.config

class RingerConfig {
    var startSoundLevel: Int = 0
    var interval: Int = 0
    var allowedMaxVolume: Int = 0

    var isVibrateFirst: Boolean = false
    var vibrateTimes: Int = 0

    var isMuteFirst: Boolean = false
    var muteTimes: Int = 0

    var isUseMusicStream: Boolean = false

    init {
        interval = 5
    }

    companion object{
        fun findPhoneConfig() : RingerConfig{
            val config = RingerConfig()

            with(config) {
                startSoundLevel = 14
                allowedMaxVolume = 14
                isUseMusicStream = true
                interval = 1
                isVibrateFirst = false
                isMuteFirst = false
            }

            return config
        }
    }
}


