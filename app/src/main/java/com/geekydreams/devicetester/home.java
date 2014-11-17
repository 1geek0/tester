package com.geekydreams.devicetester;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.appflood.AppFlood;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAdListener;
import com.startapp.android.publish.SDKAdPreferences;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;


public class home extends Activity {

    private StartAppAd startAppAd = new StartAppAd(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "105206822", "211783112", new SDKAdPreferences().setAge(10));
        setContentView(R.layout.activity_home);
        StartAppAd.showSlider(this);
        //Initialising All Text Components!
        TextView hello = (TextView) findViewById(R.id.hello);
        TextView info = (TextView) findViewById(R.id.info);
        TextView btnPress = (TextView) findViewById(R.id.pressbtn);
        if (Build.VERSION.SDK_INT >= 11) {
            ActionBar actionBar = getActionBar();
            actionBar.hide();
        }



        //Handling The Button Task
        Button getInfo = (Button) findViewById(R.id.getInfo);
        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }
    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }
    @Override
    public void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(this, "FY924J455MCYYG2YRK73");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
