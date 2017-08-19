package com.tistory.puzzleleaf.rankofalcohol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tistory.puzzleleaf.rankofalcohol.contents.screenlock.ScreenLockActivity;

/**
 * Created by cmtyx on 2017-08-11.
 */

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Intent i = new Intent(context, ScreenLockActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        }
    }
}
