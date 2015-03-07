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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;


public class wmanager extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmanager);
        Toolbar wmTool = (Toolbar) findViewById(R.id.toolWman);
        setSupportActionBar(wmTool);


        //Layout components
        Button vid = (Button) findViewById(R.id.deleteVid);
        Button img = (Button) findViewById(R.id.deleteImg);
        Button aud = (Button) findViewById(R.id.deleteAud);
        final ScrollView scr = (ScrollView) findViewById(R.id.scrollWmanager);
        final TextView imgSize = (TextView) findViewById(R.id.imgSize);
        final TextView audSize = (TextView) findViewById(R.id.audSize);
        final TextView vidSize = (TextView) findViewById(R.id.vidSize);

        //Miscellaneous Components
        final File vidSource = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video/");
        final File imgSource = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images/");
        final File audSource = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio");

        //Button OnClickListeners
        vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDirectory(vidSource);
                Toast.makeText(getApplicationContext(), "All WhatsApp Videos Deleted !", Toast.LENGTH_SHORT).show();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDirectory(imgSource);
                Toast.makeText(getApplicationContext(), "All WhatsApp Images Deleted !", Toast.LENGTH_SHORT).show();
            }
        });
        aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDirectory(audSource);
                Toast.makeText(getApplicationContext(), "All WhatsApp Audios Deleted !", Toast.LENGTH_SHORT).show();

            }
        });
        //Warning
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("WARNING: Clicking the buttons will permanently delete all the corresponding data. It cannot be restored");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                scr.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                finish();
                dialog.dismiss();
            }
        });


        File empty = new File(Environment.getExternalStorageDirectory().toString() + "ktool.txt");
        File gpxfile = new File(empty, "ktool.txt");


        AlertDialog alert = builder.create();
        if (!empty.exists()) {
            alert.show();
            try {
                FileWriter writer = new FileWriter(gpxfile);
                writer.write("First Time");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Dir Sizes
        long imgSizeInt = dirSize(imgSource);
        String imgSizeString = imgSizeInt + "MB";
        imgSize.setText(imgSizeString);
        long vidSizeInt = dirSize(vidSource);
        String vidSizeString = vidSizeInt + "MB";
        vidSize.setText(vidSizeString);
        long audSizeInt = dirSize(audSource);
        String audSizeString = audSizeInt + "MB";
        audSize.setText(audSizeString);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wmanager, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private static long dirSize(File dir) {
        long result = 0;

        Stack<File> dirlist = new Stack<File>();
        dirlist.clear();

        dirlist.push(dir);


        while (!dirlist.isEmpty()) {
            File dirCurrent = dirlist.pop();

            File[] fileList = dirCurrent.listFiles();
            try {
                for (File f : fileList) {
                    if (f.isDirectory())
                        dirlist.push(f);
                    else
                        result += f.length();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        String resultString = result + "";
        Integer intSize = Integer.parseInt(resultString) / 1048576;
        return intSize;
    }
}
