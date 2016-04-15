package com.baybaka.incomingcallsound.utils;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;

public class Description {

    public static String getVolumeLevelText(int volumeLevel, int maxLevel) {
        if (volumeLevel == -1) {
            return getString(R.string.descriptions_level_silence);
        } else if (volumeLevel == 0) {
            return getString(R.string.descriptions_level_vibrate);
        } else if (volumeLevel > maxLevel) {
            return getString(R.string.descriptions_level_prering);
        } else {
            return getString(R.string.descriptions_level_number_plus_level) + volumeLevel;
        }
    }

    public static String getString(int resId) {
        return MyApp.getContext().getString(resId);
    }
}
