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

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class background extends Activity {

    ArrayList<AppStructure> appStructurelist = new ArrayList<AppStructure>();

    AppStructure appStructure;
    ListView listView;
    List<RunningTaskInfo> runningAPps;
    Button killApps;
    ActivityManager activityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        listView = (ListView) findViewById(R.id.appList);
        killApps = (Button)findViewById(R.id.kilBtn);
        activityManager = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);


        runningAPps = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        for (int i = 0; i < runningAPps.size(); i++) {
            appStructure = new AppStructure();

            try {
                appStructure.appName =(String) getPackageManager().getApplicationLabel(getPackageManager()
                        .getApplicationInfo(runningAPps.get(i).topActivity.getPackageName(),
                                PackageManager.GET_META_DATA));
                appStructure.appIcons = getPackageManager().getApplicationIcon(runningAPps.get(i).topActivity.getPackageName());
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            appStructurelist.add(appStructure);

        }

        killApps.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                for (int i = 0; i < runningAPps.size(); i++) {
                    if(!runningAPps.get(i).topActivity.getPackageName().equalsIgnoreCase("com.example.retriveinstallapps")){
                        activityManager.killBackgroundProcesses(runningAPps.get(i).topActivity.getPackageName());

                    }
                    try {
                        appStructure.appName =(String) getPackageManager().getApplicationLabel(getPackageManager()
                                .getApplicationInfo(runningAPps.get(i).topActivity.getPackageName(),
                                        PackageManager.GET_META_DATA));
                        appStructure.appIcons = getPackageManager().getApplicationIcon(runningAPps.get(i).topActivity.getPackageName());
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    appStructurelist.clear();
                    appStructurelist.add(appStructure);

                }

                updateAdpater();
            }
        });

        updateAdpater();
    }


    public void updateAdpater(){
        InstalledAppAdapter adapter = new InstalledAppAdapter(background.this,
                appStructurelist);
        listView.setAdapter(adapter);
    }



}
