package com.geekydreams.devicetester;
  
import java.lang.reflect.Method;  
  
import android.app.Activity;  
import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.IntentFilter;  
import android.os.PowerManager;  
import android.util.Log;  
  
/** 
 *  
 * @author zhangyg 
 * 
 */  
public class ScreenObserver{  
    private static String TAG = "ScreenObserver";  
    private Context mContext;  
    private ScreenBroadcastReceiver mScreenReceiver;  
    private ScreenStateListener mScreenStateListener;  
    private static Method mReflectScreenState;  
      
    public ScreenObserver(Context context){  
        mContext = context;  
        mScreenReceiver = new ScreenBroadcastReceiver();  
        try {  
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn",  
                    new Class[] {});  
        } catch (NoSuchMethodException nsme) {  
            Log.d(TAG, "API < 7," + nsme);  
        }  
    }  
      
    /** 
     * screen״̬�㲥������ 
     * @author zhangyg 
     * 
     */  
    private class ScreenBroadcastReceiver extends BroadcastReceiver{  
        private String action = null;  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            action = intent.getAction();  
            if(Intent.ACTION_SCREEN_ON.equals(action)){  
                mScreenStateListener.onScreenOn();  
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){  
                mScreenStateListener.onScreenOff();  
            }  
        }  
    }  
      
      
    /** 
     * ����screen״̬���� 
     * @param screenStateListener 
     */  
    public void requestScreenStateUpdate(ScreenStateListener screenStateListener) {  
        mScreenStateListener = screenStateListener;  
        startScreenBroadcastReceiver();  
          
        firstGetScreenState();  
    }  
      
    /** 
     * ��һ������screen״̬ 
     */  
    private void firstGetScreenState(){  
        PowerManager manager = (PowerManager) mContext  
                .getSystemService(Activity.POWER_SERVICE);  
        if (isScreenOn(manager)) {  
            if (mScreenStateListener != null) {  
                mScreenStateListener.onScreenOn();  
            }  
        } else {  
            if (mScreenStateListener != null) {  
                mScreenStateListener.onScreenOff();  
            }  
        }  
    }  
      
    /** 
     * ֹͣscreen״̬���� 
     */  
    public void stopScreenStateUpdate(){  
        mContext.unregisterReceiver(mScreenReceiver);  
    }  
      
    /** 
     * ����screen״̬�㲥������ 
     */  
    private void startScreenBroadcastReceiver(){  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_SCREEN_ON);  
        filter.addAction(Intent.ACTION_SCREEN_OFF);  
        mContext.registerReceiver(mScreenReceiver, filter);  
    }  
      
    /** 
     * screen�Ƿ��״̬ 
     * @param pm 
     * @return 
     */  
    private static boolean isScreenOn(PowerManager pm) {  
        boolean screenState;  
        try {  
            screenState = (Boolean) mReflectScreenState.invoke(pm);  
        } catch (Exception e) {  
            screenState = false;  
        }  
        return screenState;  
    }  
      
    public interface ScreenStateListener {  
        public void onScreenOn();  
        public void onScreenOff();  
    }  
}  