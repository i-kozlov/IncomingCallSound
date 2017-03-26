package com.baybaka.incomingcallsound.log.logsender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.baybaka.incomingcallsound.settings.PhonePreferenceController;
import com.baybaka.incomingcallsound.utils.PhoneInfo;

import java.util.ArrayList;

public class EmailIntentCreator {


    private static final String email = "com.baybaka+ringerVolumeReport@gmail.com";
    private static final String[] files = new String[]{"report.log", "config.xml"};

    public static Intent getSendEmailIntent(String subject, String text, Context context) {

        copyFiles(context);

        String fullBody = PhoneInfo.getAppVersionAndPhoneModelAndVersion() +  text;
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{email});

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, fullBody);

        ArrayList<Uri> uris = new ArrayList<>();
        for (String fileName : files) {

            Uri uri = Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/" + fileName);
            Log.d("Uri is", String.valueOf(uri));
            uris.add(uri);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        return emailIntent;
    }

    private static void copyFiles(Context context) {
        String logFileName = context.getApplicationContext().getPackageName() + ".0.0.log";
        String configFile = "shared_prefs/"  + PhonePreferenceController.FILE_NAME + ".xml";
        FileCopier fc = new FileCopier(context);
        fc.copyToCacheFolder(logFileName, "report.log");
        fc.copyToCacheFolder(configFile, "config.xml");
    }
}
