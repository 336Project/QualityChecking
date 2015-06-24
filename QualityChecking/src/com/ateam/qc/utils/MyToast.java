package com.ateam.qc.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	private static Toast mToast;
	public static final int LENGTH_LONG=Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT=Toast.LENGTH_SHORT;
	public static void showLong(Context context,String msg){
		if(mToast == null) {  
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);  
        } else {  
            mToast.setText(msg);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();  
	}
	
	public static void showLong(Context context,int resId){
		Toast.makeText(context, resId, LENGTH_LONG).show();
		if(mToast == null) {  
            mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);  
        } else {  
            mToast.setText(resId);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();
	}
	
	public static void showShort(Context context,String msg){
		if(mToast == null) {  
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);  
        } else {  
            mToast.setText(msg);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();  
	}
	
	public static void showShort(Context context,int resId){
		if(mToast == null) {  
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);  
        } else {  
            mToast.setText(resId);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();
	}
}
