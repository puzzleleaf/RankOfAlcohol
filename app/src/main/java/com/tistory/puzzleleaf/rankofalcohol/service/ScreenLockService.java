package com.tistory.puzzleleaf.rankofalcohol.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tistory.puzzleleaf.rankofalcohol.receiver.ScreenReceiver;


/**
 * Created by cmtyx on 2017-07-18.
 */

public class ScreenLockService extends Service {

    private ScreenReceiver screenReceiver = null;

    @Override
    public void onCreate() {
        super.onCreate();
        screenReceiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //@TODO 이후에 죽지않는 서비스 작업 수행하기
        if(intent==null){
            startForeground(1,new Notification());
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(screenReceiver != null){
            unregisterReceiver(screenReceiver);
        }
    }

}
