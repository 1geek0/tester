package com.geekydreams.devicetester;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "105206822", "211783112", true);
        setContentView(R.layout.activity_main);
        StartAppAd.showSlider(this);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Declaration & Initialisation of all the Textviews in the layout
        TextView osView = (TextView) findViewById(R.id.os);
        TextView brandView = (TextView) findViewById(R.id.brandName);
        TextView socView = (TextView) findViewById(R.id.SoC);
        TextView toRAMView = (TextView) findViewById(R.id.toRAM);
        TextView imeiView = (TextView) findViewById(R.id.imeiNumber);
        TextView avIntStorageView = (TextView) findViewById(R.id.avInt);
        TextView intStorageView = (TextView) findViewById(R.id.intStorage);
        TextView toExtView = (TextView) findViewById(R.id.extStorage);
        TextView avExtView = (TextView) findViewById(R.id.avExtStorage);
        TextView phNumberView = (TextView) findViewById(R.id.phNumber);
        final TextView usRAMView = (TextView) findViewById(R.id.usRAM);
        final TextView avRAMView = (TextView) findViewById(R.id.avRAM);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        imeiView.setText(imei);
        CellLocation location = telephonyManager.getCellLocation();
        String locationSring = location.toString();
        phNumberView.setText(locationSring);

        intStorageView.setText(getTotalInternalMemorySize());
        avIntStorageView.setText(getAvailableInternalMemorySize());
        toExtView.setText(getTotalExternalMemorySize());
        avExtView.setText(getAvailableExternalMemorySize());

        if (Build.VERSION.SDK_INT >= 16) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            Long toRAMlong = memoryInfo.totalMem;
            String toRAMString = "" + toRAMlong;
            final Integer toRAMInt = Integer.parseInt(toRAMString)/1048576;


            String toRAMGB, toRAMMB;
            if (toRAMInt >= 1024){
                toRAMGB = String.valueOf(toRAMInt/1024);
                toRAMView.setText(toRAMGB + " GB");
            } else {
                toRAMMB = String.valueOf(toRAMInt);
                toRAMView.setText(toRAMMB + " MB");
            }

        } else if(Build.VERSION.SDK_INT < 16){
            toRAMView.setVisibility(View.GONE);
        }

        //Getting All The Info!
        String osString = Build.VERSION.RELEASE;
        String brandString = Build.BRAND;
        String socString = Build.CPU_ABI;

        //Setting the texts of the textviews to all the gathered info!
        osView.setText(osString);
        brandView.setText(brandString);
        socView.setText(socString);



        final android.os.Handler handler = new Handler();
        handler.post(new Runnable() {
            final TextView toTimeView = (TextView) findViewById(R.id.toTime);
            final TextView upTimeView = (TextView) findViewById(R.id.upTime);



            @Override
            public void run() {
                Long toTimeLong = SystemClock.elapsedRealtime();
                String toTimeString = "" + toTimeLong;
                int toTimeInt = Integer.parseInt(toTimeString);
                String toTimehms = String.format("%02d H:%02d M:%02d S", TimeUnit.MILLISECONDS.toHours(toTimeInt),
                        TimeUnit.MILLISECONDS.toMinutes(toTimeInt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(toTimeInt)),
                        TimeUnit.MILLISECONDS.toSeconds(toTimeInt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(toTimeInt)));
                Long upTimeLong = SystemClock.uptimeMillis();
                String upTimeString = "" + upTimeLong;
                int upTimeInt = Integer.parseInt(upTimeString);
                String upTimehms = String.format("%02d H:%02d M:%02d S", TimeUnit.MILLISECONDS.toHours(upTimeInt),
                        TimeUnit.MILLISECONDS.toMinutes(upTimeInt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(upTimeInt)),
                        TimeUnit.MILLISECONDS.toSeconds(upTimeInt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(upTimeInt)));


                toTimeView.setText(toTimehms);
                upTimeView.setText(upTimehms);

                // Now About The RAM Stuff

                if(Build.VERSION.SDK_INT >= 16){
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                    activityManager.getMemoryInfo(memoryInfo);
                    Long toRAMLong = memoryInfo.totalMem;
                    Long avRAMLong = memoryInfo.availMem;
                    String toRAMString = ""+toRAMLong;
                    String avRAMString = ""+avRAMLong;
                    Integer avRAMInt = Integer.parseInt(avRAMString)/1048576;
                    Integer toRAMInt = Integer.parseInt(toRAMString)/1048576;
                    Integer usRAM = toRAMInt - avRAMInt;
                    if (avRAMInt >= 1024){
                        String avRAMGB = String.valueOf(avRAMInt/1024);
                        avRAMView.setText(avRAMGB + " GB");
                    } else {
                        String avRAMMB = String.valueOf(avRAMInt);
                        avRAMView.setText(avRAMMB+" MB");
                    }
                    if (usRAM >= 1024){
                        String usRAMGB = String.valueOf(usRAM/1024 + " GB");
                        usRAMView.setText(usRAMGB);
                    }
                    else {
                        String usRAMMB = String.valueOf(usRAM + " MB");
                        usRAMView.setText(usRAMMB);
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return null;
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return null;
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
}
