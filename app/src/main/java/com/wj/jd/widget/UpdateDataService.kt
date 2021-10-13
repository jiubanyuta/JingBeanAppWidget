package com.wj.jd.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateDataService extends Service {
    Timer timer;

    @Nullable
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
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    updata();
                }
            }, 0, 30 * 60 * 1000);
        } else {
//            updata();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
