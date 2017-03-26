package com.baybaka.notificationlib.led;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.baybaka.notificationlib.R;


public class TwoColorBlink extends AsyncTask<Void, Void, Void> {

    //An object that acts as a handle to the system notification service
    private NotificationManager notificationManager;
    //The object responsible for sending a notification
    private Notification notification;
    //Sets whether the LED should blink indeterminately or at fixed rate
    private boolean indeterminate = true;

    //Initialize an integer (that will act as a counter) to zero
    private int counter = 0;

    private final int notificationId = 481516;

    public void stop(){
        indeterminate = false;
    }

    private Context appContext;

    public TwoColorBlink(Application application) {
        this.appContext = application.getApplicationContext();
        notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onPreExecute() {
        //Create a new notification
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            notification = new Notification();
        } else  {
            notification = new Notification.Builder(appContext).setSmallIcon(R.drawable.empty).build();
        }
        //The notification should turn on the device's notification LED
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            // Get the current background thread's token
            synchronized (this) {
                //Set the initial LED color to red
                notification.ledARGB = 0xFFFF0000;

                //Check if the indeterminate boolean is true
                int msInterval = 850;
                if (indeterminate) {
                    while (indeterminate) {
                        //Post the notification
                        notificationManager.notify(notificationId, notification);
                        //Wait the number of milliseconds defined by 'msInterval'
                        this.wait(msInterval);
                        //Cancel the notification
                        notificationManager.cancel(notificationId);
                        //Add one to the counter while obtaining the remaining part of its division by 2 and checking if it's equal to zero
                        if (counter++ % 2 == 0) {
                            //Change color to green
                            notification.ledARGB = 0xFF00FF00;
                        } else {
                            //Change color to red
                            notification.ledARGB = 0xFFFF0000;
                        }
                    }

                } else //Notification LED should blink a predefined number of times
                {
                    //While the counter is smaller or equal to the number of transitions
                    int transitions = 4;
                    while (counter <= transitions) {
                        //Post the notification
                        notificationManager.notify(notificationId, notification);
                        //Wait the number of milliseconds defined by 'msInterval'
                        this.wait(msInterval);
                        //Cancel the notification
                        notificationManager.cancel(notificationId);

                        if (counter % 2 == 0) {
                            // Change color to green
                            notification.ledARGB = 0xFF00FF00;
                        } else {
                            // Change color to red
                            notification.ledARGB = 0xFFFF0000;
                        }

                        //Add one to the counter
                        counter++;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //Cancel the notification
        notificationManager.cancel(notificationId);

    }

}
