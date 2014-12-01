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


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.appfireworks.android.listener.AppModuleListener;
import com.appfireworks.android.track.AppTracker;
import com.ewefkqqipbhzwxfqgmrm.AdController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class home extends Activity {

    Account[] accounts;
    private static final long CACHE_APP = Long.MAX_VALUE;
    private CachePackageDataObserver mClearCacheObserver;


    private AdController banner;
    private Activity act = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(savedInstanceState == null) {
            initializeLeadBolt();
        }


        Button ng = (Button) findViewById(R.id.bg);



        accounts = AccountManager.get(this).getAccountsByType("com.google");

        Typeface spaceage = Typeface.createFromAsset(getAssets(), "fonts/s.ttf");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ng.setTypeface(spaceage);
        ng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Background Apps Killed", Toast.LENGTH_LONG).show();
            }
        });



        //Package Manager Stuff

        //Initialising All Text Components!
        Button bkpButton = (Button) findViewById(R.id.bkp);
        Button cCacheButton = (Button) findViewById(R.id.cCache);
        TextView info = (TextView) findViewById(R.id.info);
        TextView upGrade = (TextView) findViewById(R.id.upGrade);
        TextView xda = (TextView) findViewById(R.id.xdalink);

        xda.setTypeface(spaceage);
        info.setTypeface(spaceage);
        upGrade.setTypeface(spaceage);
        bkpButton.setTypeface(spaceage);
        cCacheButton.setTypeface(spaceage);


        cCacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
                Toast.makeText(home.this,"Cache Cleaned!\n Enjoy Faster Internet!", Toast.LENGTH_LONG).show();
            }
        });

        //Handling The Button Task
        Button getInfo = (Button) findViewById(R.id.getInfo);
        getInfo.setTypeface(spaceage);
        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });
        bkpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendemail();
            }
        });
    }


    private void sendemail() {
        Log.i("Send email", "");
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        String[] TO = {"amrood.admin@gmail.com"};
        String[] CC = {"mcmohd@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, accounts);
        emailIntent.putExtra(Intent.EXTRA_BCC, "nilayscience@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Backup IMEI");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your IMEI Backup Has Been Created In Case of theft you can contact the phone company to deactivate the device" + "\n Your IMEI IS :" + telephonyManager.getDeviceId());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(home.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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



    void clearCache()
    {
        if (mClearCacheObserver == null)
        {
            mClearCacheObserver=new CachePackageDataObserver();
        }

        PackageManager mPM=getPackageManager();

        @SuppressWarnings("rawtypes")
        final Class[] classes= { Long.TYPE, IPackageDataObserver.class };

        Long localLong=Long.valueOf(CACHE_APP);

        try
        {
            Method localMethod=
                    mPM.getClass().getMethod("freeStorageAndNotify", classes);

      /*
       * Start of inner try-catch block
       */
            try
            {
                localMethod.invoke(mPM, localLong, mClearCacheObserver);
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      /*
       * End of inner try-catch block
       */
        }
        catch (NoSuchMethodException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }//End of clearCache() method

    private class CachePackageDataObserver extends IPackageDataObserver.Stub
    {
        public void onRemoveCompleted(String packageName, boolean succeeded)
        {

        }//End of onRemoveCompleted() method
    }//End of CachePackageDataObserver instance inner class

    private void initializeLeadBolt() {
        AppTracker.startSession(act, "bFWFUgPQVIczqbYg9hcQwomKMK5Jcgy2", new AppModuleListener() {
            @Override
            public void onModuleLoaded() {
                loadDisplayAd();
            }

            @Override
            public void onModuleFailed() {
                loadDisplayAd();
            }

            @Override
            public void onModuleClosed() {
            }

            @Override
            public void onModuleCached() {
            }
        });
    }

    private void loadDisplayAd() {
        // use this else where in your app to load a Leadbolt Interstitial Ad
        banner = new AdController(act, "434861798");
        banner.loadAd();
    }

    public void onPause() {
        super.onPause();
        if (!isFinishing()) {
            AppTracker.pause(getApplicationContext());
        }
    }

    public void onResume() {
        super.onResume();
        AppTracker.resume(getApplicationContext());
    }

    public void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            AppTracker.closeSession(getApplicationContext(), true);
        }
    }
}
