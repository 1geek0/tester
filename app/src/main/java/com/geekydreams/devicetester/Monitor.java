//Copyright (c) 2014 Nilay Kulkarni
/*
 * This file is part of Know Your Droid.
 *
 *     Know Your Droid is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Know Your Droid is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Know Your Droid.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.geekydreams.devicetester;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Monitor extends Activity {

    private Handler mHandler = new Handler();

    private long mStartRX = 0;

    private long mStartTX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        mStartRX = TrafficStats.getTotalRxBytes();

        mStartTX = TrafficStats.getTotalTxBytes();

        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Uh Oh!");

            alert.setMessage("Your device does not support traffic stat monitoring.");

            alert.show();

        } else {

            mHandler.postDelayed(mRunnable, 1000);

        }
    }
    private final Runnable mRunnable = new Runnable() {

        public void run() {

            TextView RX = (TextView) findViewById(R.id.RX);

            TextView TX = (TextView) findViewById(R.id.TX);

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

            RX.setText(Long.toString(rxBytes));

            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            TX.setText(Long.toString(txBytes));

            mHandler.postDelayed(mRunnable, 1000);

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.monitor, menu);
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
