package com.baybaka.notificationlib.flash;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;

import java.io.IOException;

public class FlashLight extends AsyncTask<Void, Void, Void> {

    private Camera camera;
    private Camera.Parameters params;
    private boolean treadStopped = false;

    @Override
    protected void onPreExecute() {
        camera = Camera.open();
        params = camera.getParameters();
    }


    @Override
    protected Void doInBackground(Void... none) {
        while (!treadStopped){
            turnOnFlash();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void turnOnFlash() {
        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
        try {
            camera.setPreviewTexture(mPreviewTexture);
        } catch (IOException ex) {
            // Ignore
        }
        camera.startPreview();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void stop() {
        treadStopped = true;
    }
}
