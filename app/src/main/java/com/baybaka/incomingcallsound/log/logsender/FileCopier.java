package com.baybaka.incomingcallsound.log.logsender;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopier {

    private Context context;
    public FileCopier(Context context) {
        this.context = context;
    }
    public void copyToCacheFolder(String source, String dest) {

        InputStream in = null;
        OutputStream out = null;
        File file = new File(context.getCacheDir(), dest);
        try {
            file.createNewFile();
            in = new FileInputStream(new File(context.getApplicationInfo().dataDir + "/" + source));
            out = new FileOutputStream(file);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("FileCopier", e.getMessage());
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
