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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class backup extends ActionBarActivity {
    List appPakageList ;
    Intent intent ;
    BackApkStructure backStr;
    ListView listView;
    ArrayList<BackApkStructure> appList = new ArrayList<BackApkStructure>();
    ProgressBar progressBar;
    Button backUpAllBtn;
    TextView scnView;
    RelativeLayout scnrel;
    private HashMap<Integer, Integer> myChecked = new HashMap<Integer, Integer>();

    int selectedPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolup);
        toolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        setSupportActionBar(toolbar);

        intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        appPakageList = getPackageManager().queryIntentActivities(intent, 0);

        listView = (ListView)findViewById(R.id.appList);
        backUpAllBtn = (Button )findViewById(R.id.backUpBtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        scnView = (TextView) findViewById(R.id.scn);
        scnrel = (RelativeLayout) findViewById(R.id.real);

        ListAppAsyncTask listApp = new ListAppAsyncTask();
        listApp.execute();
        backUpAllBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BackUpAllAppAsyncTask backupAllApk = new BackUpAllAppAsyncTask();
                backupAllApk.execute();

            }
        });
    }

    public class ListAppAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(String... params) {
            for (Object object : appPakageList) {
                ResolveInfo resolveInFo = (ResolveInfo) object;
                backStr = new BackApkStructure();
                backStr.apkSourceDir = resolveInFo.activityInfo.applicationInfo.publicSourceDir;
                backStr.apkName = resolveInFo.loadLabel(getPackageManager()).toString();
                backStr.icons  =resolveInFo.loadIcon(getPackageManager());
                appList.add(backStr);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            InstalledAppAdapter adapter = new InstalledAppAdapter(backup.this,
                    appList);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            scnView.setVisibility(View.GONE);
            scnrel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            super.onPostExecute(result);
        }
    }



    public class InstalledAppAdapter extends  BaseAdapter {

        Activity activity;
        ArrayList<BackApkStructure> appStrucutreList;

        public InstalledAppAdapter(Activity act, ArrayList<BackApkStructure> appStructure) {

            this.activity = act;
            this.appStrucutreList = appStructure;



        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return appStrucutreList.size();
        }

        @Override
        public Object getItem(int arg0) {

            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {

            LayoutInflater inflator =(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.adapter_layout, null);

            TextView appName = (TextView)view.findViewById(R.id.applicationName);
            ImageView appIcon = (ImageView)view.findViewById(R.id.appIcon);
            Button backBtn = (Button)view.findViewById(R.id.back_btn);
            appName.setText(appStrucutreList.get(position).apkName);
            appIcon.setBackgroundDrawable(appStrucutreList.get(position).icons);

            backBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    selectedPosition = position;
                    BackUpSingleAppAsyncTask backApp = new BackUpSingleAppAsyncTask();
                    backApp.execute();
                }
            });
            return view;
        }

    }

    public class BackUpSingleAppAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(backup.this);
            progress.setMessage("Please wait...\nBacking Up Your App!");
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            backupSingleApp(selectedPosition);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            Toast.makeText(backup.this, "App Successfully Backed Up!", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

    public class BackUpAllAppAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(backup.this);
            progress.setMessage("Please wait...\nBacking Up Your Apps!");
            progress.setCancelable(true);
            progress.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            backupAllApp();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            Toast.makeText(backup.this,"All Apps Successfully Backed Up!", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }


    private void backupSingleApp(int position){
        File apkSourceDirec = new File(appList.get(position).apkSourceDir);
        try {
            String file_name =appList.get(position).apkName;
            File fileDirectry = new File(Environment.getExternalStorageDirectory()
                    .toString() + "/Backup Apps(Single)");
            fileDirectry.mkdirs();
            fileDirectry = new File(fileDirectry.getPath() + "/" + file_name + ".apk");
            fileDirectry.createNewFile();
                InputStream in = new FileInputStream(apkSourceDirec);
                OutputStream out = new FileOutputStream(fileDirectry);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.e("file_name--", "File copied. ");
        } catch (FileNotFoundException ex) {

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    private void backupAllApp(){

        for(int i = 0; i<appList.size(); i++){
            File apkSourceDirec = new File(appList.get(i).apkSourceDir);
            try {
                String file_name =appList.get(i).apkName;
                File fileDirectry = new File(Environment.getExternalStorageDirectory()
                        .toString() + "/All Apps Backup");
                fileDirectry.mkdirs();
                fileDirectry = new File(fileDirectry.getPath() + "/" + file_name + ".apk");
                fileDirectry.createNewFile();
                InputStream in = new FileInputStream(apkSourceDirec);
                OutputStream out = new FileOutputStream(fileDirectry);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.e("file_name--", "File copied. ");

            } catch (FileNotFoundException ex) {

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
