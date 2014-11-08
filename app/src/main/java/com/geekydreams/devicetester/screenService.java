package com.geekydreams.devicetester;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.geekydreams.devicetester.ScreenOnReciever.*;

/**
 * Created by Nilay on 08/11/2014.
 */
public class screenService extends Service {

    private String Debug_oncreate;

    static long screenOnTime;
    BroadcastReceiver mReceiver = null;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        mReceiver = new ScreenOnReciever();
        registerReceiver(mReceiver, filter);
        Log.d(Debug_oncreate,"OnCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        while (ScreenOnReciever.screenOn == true) {
            Log.d(Debug_oncreate, "Goal!");
            screenOnTime++;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Debug_oncreate, "Binder");
        return null;
    }
}
