package com.geekydreams.devicetester;

import android.app.Activity;  
import android.os.Bundle;  
import android.util.Log;  
import android.widget.TextView;

import com.example.usagestatistics.ScreenObserver.ScreenStateListener;

public class ScreenObserverActivity extends Activity {  
    private String TAG = "ScreenObserverActivity";  
    private ScreenObserver mScreenObserver;  
    private TextView textView;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        Log.i("TAG","Succeed in ScreenObserver...");
        mScreenObserver = new ScreenObserver(this);  
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
  
    private void doSomethingOnScreenOn() {
    	
        Log.i(TAG, "Screen is on");  
    }  
  
    private void doSomethingOnScreenOff() {
    	textView = (TextView)this.findViewById(R.id.t5);
    	textView.setText("Success");
        Log.i(TAG, "Screen is off");  
    }  
      
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //ֹͣ����screen״̬  
        mScreenObserver.stopScreenStateUpdate();  
    }  
}  
