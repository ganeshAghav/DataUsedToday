package com.everestadvanced.datausedtoday;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 04-12-2017.
 */

public class BackgroundService extends Service {

    public Handler mHandler = new Handler();
    public long mStartRX = 0;
    public long mStartTX = 0;

    public boolean notiResult=false;
    public float Send;
    public float Receviced;

    public long sendData =0;
    public long recevedData=0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       try
       {
           mStartRX = TrafficStats.getTotalRxBytes();
           mStartTX = TrafficStats.getTotalTxBytes();
           if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED)
           {
               AlertDialog.Builder alert = new AlertDialog.Builder(this);
               alert.setTitle("Uh Oh!");
               alert.setMessage("Your device does not support traffic stat monitoring.");
               alert.show();
           }
           else
           {
               mHandler.postDelayed(mRunnable, 1000);
           }

       }
       catch (Exception er)
       {

       }
        return  START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notiResult=true;
    }


    private final Runnable mRunnable = new Runnable() {

        public void run() {

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            Send=Float.parseFloat(String.valueOf(txBytes));
            Receviced= Float.parseFloat(String.valueOf(rxBytes));


            showRecordingNotification(Send,Receviced);

            mHandler.postDelayed(mRunnable, 1000);
            Log.e("Total", "Bytes Send" + android.net.TrafficStats.getTotalTxBytes());
            Log.e("Total", "Bytes received" + android.net.TrafficStats.getTotalRxBytes());
        }
    };


    //for notification
    private void showRecordingNotification(float SsendData,float SrecevedData) {


        sendData+=SsendData;
        recevedData+=SrecevedData;

        String StrsendData=formatFileSize(sendData);
        String StrrecevedData=formatFileSize(recevedData);

        Notification notification = null;

        if(notiResult==false)
        {
            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            builder.setSmallIcon(getNotificationSmallIcon())
                    .setContentText("Send: "+ StrsendData + "\n" + "Received: " +StrrecevedData)
                    .setAutoCancel(false);
            notification = builder.getNotification();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notificationManager.notify(1, notification);


        }
        else
        {
            NotificationManager notifManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();

            if(notification!=null)
                notification.flags |= Notification.FLAG_AUTO_CANCEL ;
        }


    }
    public void ClearData() {

        sendData=00;
        recevedData=00;
    }

    private int getNotificationSmallIcon() {

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notifi : R.drawable.notifi;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }
        return hrSize;
    }
}
