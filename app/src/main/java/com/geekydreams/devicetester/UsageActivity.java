package com.geekydreams.devicetester;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import com.example.usagestatistics.ScreenObserver.ScreenStateListener;
import com.example.usagestatistics.Kernal;
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private TextView textView;
    private String TAG = "ScreenObserverActivity";  
    private ScreenObserver mScreenObserver;
    private ArrayList<Date>  lastScreenon  = new ArrayList<Date>();
    private ArrayList<Date>  lastScreenoff = new ArrayList<Date>();
    private Kernal kernal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
            
        });
        mScreenObserver = new ScreenObserver(this);  
        
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        //From there programed by Zealseeker
        mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {  
            @Override  
            public void onScreenOn() {  
                doSomethingOnScreenOn();
                
            }  
  
            @Override  
            public void onScreenOff() {  
                doSomethingOnScreenOff();  
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
        	//edited by Zealseeker
        	Fragment fragment;
        	switch(position){
        	default:
        		fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                return fragment;
        	case 1:
        		fragment = new DetailStatistic();
        		return fragment;        		
        		
        	}
            
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            return rootView;
        }
    }
  
 //Following designed by Zealseeker
    public static class DetailStatistic extends Fragment{
    	public DetailStatistic(){
    		
    	}
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO �Զ���ɵķ������
    		View rootView = inflater.inflate(R.layout.detailstatistic,container,false);
    	return rootView;
    	}
    }
    public void updateUsage(View theButton){
 /*   	long diff = 0;
    	int sec = 0;
    	if(lastScreenon.size()>lastScreenoff.size()){
    		
    		for(int i=0;i<lastScreenoff.size();i++){
    			//diff += Time.compare(lastScreenoff.get(i), lastScreenon.get(i));
    			diff += lastScreenoff.get(i).getTime()-lastScreenon.get(i).getTime();
    			
    		}
    		
    		diff += new Date().getTime()-lastScreenon.get(lastScreenon.size()-1).getTime();
    		sec = (int) (diff / 1000);
    	}
  */
    	kernal = new Kernal();
    	Date usage = kernal.today_usage(1,lastScreenon,lastScreenoff);
    	int sec = (int)(usage.getTime() / 1000);
    	textView = (TextView)this.findViewById(R.id.today_usetime);
    	textView.setText(String.valueOf(sec));
    	usage = kernal.today_usage(2, lastScreenon, lastScreenoff);
    	sec = (int)(usage.getTime()/1000);
    	textView = (TextView)this.findViewById(R.id.thisweek_usetime);
    	textView.setText(String.valueOf(sec));
    	usage = kernal.today_usage(3, lastScreenon, lastScreenoff);
    	sec = (int)(usage.getTime()/1000);
    	textView = (TextView)this.findViewById(R.id.thismonth_usetime);
    	textView.setText(String.valueOf(sec));
    	
    	
    	
    	
    }
    public void buttontest(View theButton){
		textView = (TextView)this.findViewById(R.id.t7);
		//Date lasttime = lastScreenon.get(lastScreenon.size()-1);
		Date lasttime = new Kernal().today_thisweek_month(2);
		try{
		textView.setText(new SimpleDateFormat("yy��MM��dd�� HHʱmm��ss",Locale.getDefault()).format(lasttime));
		}catch(Exception e){Log.i(TAG,"this wrong");}
		
		if(lastScreenoff.size()== 0){
			lasttime = new Kernal().today_thisweek_month(3);
			textView = (TextView)this.findViewById(R.id.t5);
			//lasttime = lastScreenoff.get(lastScreenoff.size()-1);
			textView.setText(new SimpleDateFormat("yy-MM-dd HH:mm:ss",Locale.US).format(lasttime));
		}
	}
    public void ratestatistics(View theButton){
    	int rate[] = new Kernal().occupancy_rate(lastScreenon, lastScreenoff);
    	textView = (TextView)this.findViewById(R.id.time1);
    	textView.setText(String.valueOf(rate[0]));
    	textView = (TextView)this.findViewById(R.id.time2);
    	textView.setText(String.valueOf(rate[1]));
    	textView = (TextView)this.findViewById(R.id.time3);
    	textView.setText(String.valueOf(rate[2]));
    	textView = (TextView)this.findViewById(R.id.time4);
    	textView.setText(String.valueOf(rate[3]));
    	textView = (TextView)this.findViewById(R.id.time5);
    	textView.setText(String.valueOf(rate[4]));
    	textView = (TextView)this.findViewById(R.id.time6);
    	textView.setText(String.valueOf(rate[5]));
    	textView = (TextView)this.findViewById(R.id.time7);
    	textView.setText(String.valueOf(rate[6]));
    	textView = (TextView)this.findViewById(R.id.time8);
    	textView.setText(String.valueOf(rate[7]));
    }
    
    
	private void doSomethingOnScreenOn() {
     	Date curtime = new Date();
    	lastScreenon.add(curtime);
        Log.i(TAG, "Screen is on");  
    }  
  
    private void doSomethingOnScreenOff() {
    	Date curtime = new Date();
    	lastScreenoff.add(curtime);
        Log.i(TAG, "Screen is off");  
    }  
      
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //ֹͣ����screen״̬  
        mScreenObserver.stopScreenStateUpdate();  
    } 

}
