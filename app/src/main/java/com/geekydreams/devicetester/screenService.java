package com.geekydreams.devicetester;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

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
        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT);
        Log.d("hello", "NIlay");

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        mReceiver = new ScreenOnReciever();
        registerReceiver(mReceiver, filter);
        Log.d(Debug_oncreate,"OnCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        PowerManager pw = (PowerManager) getSystemService(Context.POWER_SERVICE);

        while (pw.isScreenOn()) {
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
