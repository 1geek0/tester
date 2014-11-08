package com.geekydreams.devicetester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Nilay on 08/11/2014.
 */
public class ScreenOnReciever extends BroadcastReceiver {

    static boolean screenOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = false;
        }
        Intent i = new Intent(context, screenService.class);
        i.putExtra("screen_state", screenOn);
        context.startService(i);
    }
}
