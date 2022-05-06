package com.paxus.pay.poslinkui.demo.entry.information;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Created by Yanina.Yang on 6/1/2020.
 * Play sound in background (Do not block UI)
 */
public class PlayerService extends Service {
    public static final String PARAM_RESOURCE_ID = "resourceId";
    public static final String PARAM_APPROVAL_SOUND = "approvalSound";

    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForeground(2, buildNotification());
        }
    }

    private Notification buildNotification() {
        int smallIconId = getResources().getIdentifier("small_icon", "mipmap", getPackageName());
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, this.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
        builder.setSmallIcon(smallIconId)
                .setContentText("Playing Audio");
        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int resourcesId = 0;
        String soundUri = "";
        if(intent != null){
            resourcesId = intent.getIntExtra(PARAM_RESOURCE_ID,0);
            soundUri = intent.getStringExtra(PARAM_APPROVAL_SOUND);
        }
        if(resourcesId != 0) {
            if(mediaPlayer != null){
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(this, resourcesId);
        }else if(!TextUtils.isEmpty(soundUri)){
            if(mediaPlayer != null){
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Uri uri = Uri.parse(soundUri);
            if(uri != null) {
                mediaPlayer = MediaPlayer.create(this, uri);
            }
        }

        if (mediaPlayer != null) {
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
                stopSelf();
            });
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        stopForeground(true);
        super.onDestroy();
    }
}
