package com.geekydreams.devicetester;

import android.app.Activity;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import com.geekydreams.devicetester.home;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaration & Initialisation of all the Textviews in the layout
        TextView osView = (TextView) findViewById(R.id.os);
        TextView brandView = (TextView) findViewById(R.id.brandName);
        TextView socView = (TextView) findViewById(R.id.SoC);


        //Getting All The Info!
        String osString = Build.VERSION.RELEASE;
        String brandString = Build.BRAND;
        String socString = Build.CPU_ABI2;

        //Setting the texts of the textviews to all the gathered info!
        osView.setText(osString);
        brandView.setText(brandString);
        socView.setText(socString);


        final android.os.Handler handler = new Handler();
        handler.post(new Runnable() {
            final TextView toTimeView = (TextView) findViewById(R.id.toTime);
            final TextView upTimeView = (TextView) findViewById(R.id.upTime);
            final TextView screenTimeView = (TextView) findViewById(R.id.screenTime);

            @Override
            public void run() {
                Long toTimeLong = SystemClock.elapsedRealtime();
                String toTimeString = "" + toTimeLong;
                int toTimeInt = Integer.parseInt(toTimeString);
                String toTimehms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(toTimeInt),
                        TimeUnit.MILLISECONDS.toMinutes(toTimeInt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(toTimeInt)),
                        TimeUnit.MILLISECONDS.toSeconds(toTimeInt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(toTimeInt)));
                Long upTimeLong = SystemClock.uptimeMillis();
                String upTimeString = "" + upTimeLong;
                int upTimeInt = Integer.parseInt(upTimeString);
                String upTimehms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(upTimeInt),
                        TimeUnit.MILLISECONDS.toMinutes(upTimeInt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(upTimeInt)),
                        TimeUnit.MILLISECONDS.toSeconds(upTimeInt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(upTimeInt)));
                String screenTimeString = "" + screenService.screenOnTime;
                int screenTimeInt = Integer.parseInt(screenTimeString);
                String screenTimehms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(screenTimeInt),
                        TimeUnit.MILLISECONDS.toMinutes(screenTimeInt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(screenTimeInt)),
                        TimeUnit.MILLISECONDS.toSeconds(screenTimeInt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(screenTimeInt)));


                toTimeView.setText(toTimehms);
                upTimeView.setText(upTimehms);
                screenTimeView.setText(screenTimehms);
                handler.postDelayed(this, 1);
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
}
