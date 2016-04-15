package com.baybaka.incomingcallsound.utils;

import android.os.Build;

import com.baybaka.incomingcallsound.BuildConfig;
import com.baybaka.incomingcallsound.MyApp;

import java.lang.reflect.Field;

public class PhoneInfo {

    public static String getAppVersionAndPhoneModelAndVersion() {
        String appVersion = BuildConfig.VERSION_NAME + " " + BuildConfig.VERSION_CODE +"\n";
        String os = os();
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return appVersion + os + model + maxSound();
        } else {
            return appVersion +os + manufacturer + " " + model + maxSound();
        }
    }

    private static String maxSound(){
        return "\n" + "max volume: " + MyApp.get().getMaxVol() + "\n";
    }

    private static String os() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | NullPointerException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }
        builder.append("\n");
        return builder.toString();
    }
}
