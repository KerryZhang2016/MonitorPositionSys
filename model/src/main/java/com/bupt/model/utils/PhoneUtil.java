package com.bupt.model.utils;
  
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneUtil {  
  
    public static String TAG = PhoneUtil.class.getSimpleName();
      
    /** 
     * 挂断电话 
     */
    public static void endCall(Context context) {
        try {    
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {    
                Class telephonyClass = telephonyObject.getClass();
    
                Method endCallMethod = telephonyClass.getMethod("endCall");
                endCallMethod.setAccessible(true);    
    
                endCallMethod.invoke(telephonyObject);    
            }    
        } catch (SecurityException e) {
            e.printStackTrace();    
        } catch (NoSuchMethodException e) {
            e.printStackTrace();    
        } catch (IllegalArgumentException e) {
            e.printStackTrace();    
        } catch (IllegalAccessException e) {
            e.printStackTrace();    
        } catch (InvocationTargetException e) {
            e.printStackTrace();    
        }    
    
    }    
    
    /** 
     * 接听电话 
     * */
    public static void answerRingCall(Context context) {
            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
            context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
	}
        
    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {    
            // 初始化iTelephony    
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection    
            // Get the current object implementing ITelephony interface    
            Class telManager = telephonyManager.getClass();
            Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);    
            telephonyObject = getITelephony.invoke(telephonyManager);    
        } catch (SecurityException e) {
            e.printStackTrace();    
        } catch (NoSuchMethodException e) {
            e.printStackTrace();    
        } catch (IllegalArgumentException e) {
            e.printStackTrace();    
        } catch (IllegalAccessException e) {
            e.printStackTrace();    
        } catch (InvocationTargetException e) {
            e.printStackTrace();    
        }    
        return telephonyObject;    
    }    
}