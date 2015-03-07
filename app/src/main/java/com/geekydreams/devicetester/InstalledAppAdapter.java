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

/**
 * Created by Nilay on 24/12/2014.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstalledAppAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<AppStructure> appStrucutreList;

    public InstalledAppAdapter(Activity act, ArrayList<AppStructure> appStructure) {

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
    public View getView(int arg0, View view, ViewGroup arg2) {

        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        view = inflator.inflate(R.layout.customadapter, null);

        TextView appName = (TextView) view.findViewById(R.id.applicationName);
        ImageView appIcon = (ImageView) view.findViewById(R.id.appIcon);
        appName.setText(appStrucutreList.get(arg0).appName);
        appIcon.setBackgroundDrawable(appStrucutreList.get(arg0).appIcons);

        return view;
    }

}