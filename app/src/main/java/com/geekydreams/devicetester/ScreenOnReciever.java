package com.geekydreams.devicetester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nilay on 08/11/2014.
 */
public class ScreenOnReciever extends BroadcastReceiver {

    static boolean screenOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceLauncher = new Intent(context, screenService.class);
            context.startService(serviceLauncher);
            Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT);
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = false;
        }
    }
}
