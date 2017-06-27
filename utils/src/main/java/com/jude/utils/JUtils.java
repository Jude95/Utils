package com.jude.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
public class JUtils {
	public static String TAG;
	public static boolean DEBUG = false;
	private static Application mApplicationContent;

	public static void initialize(Application app){
		mApplicationContent = app;
	}

	
	public static void setDebug(boolean isDebug,String TAG){
		JUtils.TAG = TAG;
		JUtils.DEBUG = isDebug;
	}

	public static Application getApplication(){
		return mApplicationContent;
	}


	public static void Log(String TAG,String text){
		if(DEBUG){
			Log.i(TAG, text);
		}
	}

	public static void Log(String text){
		if(DEBUG){
			Log.i(TAG, text);
		}
	}


	public static void Toast(String text){
            android.widget.Toast.makeText(mApplicationContent, text, android.widget.Toast.LENGTH_SHORT).show();
	}

	public static void ToastLong(String text){
            android.widget.Toast.makeText(mApplicationContent, text, android.widget.Toast.LENGTH_LONG).show();
	}


	/** 
	 * dp转px
	 * 
	 */  
	public static int dip2px(float dpValue) {  
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);  
	} 


	/** 
	 *	px转dp
	 */  
	public static int px2dip(float pxValue) {  
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);  
	}


	/**
	 * 取屏幕宽度
	 * @return
	 */
	public static int getScreenWidth(){
		DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 取屏幕高度
	 * @return
	 */
	public static int getScreenHeight(){
		DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
		return dm.heightPixels-getStatusBarHeight();
	}

	/**
	 * 取屏幕高度包含状态栏高度
	 * @return
	 */
	public static int getScreenHeightWithStatusBar(){
		DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 取导航栏高度
	 * @return
	 */
	public static int getNavigationBarHeight() {
		int result = 0;
		int resourceId = mApplicationContent.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	/**
	 * 取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight() {
		int result = 0;
		int resourceId = mApplicationContent.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

    public static int getActionBarHeight() {
		int actionBarHeight = 0;

		final TypedValue tv = new TypedValue();
        if (mApplicationContent.getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
					tv.data, mApplicationContent.getResources().getDisplayMetrics());
        }
		return actionBarHeight;
	}


	/**
	 * 关闭输入法
	 * @param act
	 */
	public static void closeInputMethod(Activity act){
		View view = act.getCurrentFocus();
		if(view!=null){
			((InputMethodManager)mApplicationContent.getSystemService(Context.INPUT_METHOD_SERVICE)).
			hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}


	/**
	 * 判断应用是否处于后台状态
	 * @return
	 */
	public static boolean isBackground() {
		ActivityManager am = (ActivityManager) mApplicationContent.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(mApplicationContent.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 复制文本到剪贴板
	 * @param text
	 */
	public static void copyToClipboard(String text){
		if(Build.VERSION.SDK_INT >= 11){
			ClipboardManager cbm = (ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
			cbm.setPrimaryClip(ClipData.newPlainText(mApplicationContent.getPackageName(), text));
		}else {
			android.text.ClipboardManager cbm = (android.text.ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
			cbm.setText(text);
		}
	}

	/**
	 * 获取SharedPreferences
	 * @return SharedPreferences
	 */
	public static SharedPreferences getSharedPreference() {
		return mApplicationContent.getSharedPreferences(mApplicationContent.getPackageName(), Activity.MODE_PRIVATE);
	}

	/**
	 * 获取SharedPreferences
	 * @return SharedPreferences
	 */
	public static SharedPreferences getSharedPreference(String name) {
		return mApplicationContent.getSharedPreferences(name, Activity.MODE_PRIVATE);
	}

	/**
	 * 获取SharedPreferences
	 * @return SharedPreferences
	 */
	public static SharedPreferences getSharedPreference(String name,int mode) {
		return mApplicationContent.getSharedPreferences(name, mode);
	}

	/**
	 * 经纬度测距
	 * @param jingdu1
	 * @param weidu1
	 * @param jingdu2
	 * @param weidu2
	 * @return
	 */
	public static double distance(double jingdu1, double weidu1, double jingdu2,   double weidu2) {
		double a, b, R;  
		R = 6378137; // 地球半径  
		weidu1 = weidu1 * Math.PI / 180.0;
		weidu2 = weidu2 * Math.PI / 180.0;
		a = weidu1 - weidu2;  
		b = (jingdu1 - jingdu2) * Math.PI / 180.0;
		double d;  
		double sa2, sb2;  
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2  
				* R  
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
				* Math.cos(weidu2) * sb2 * sb2));
		return d;  
	} 

	/**
	 * 是否有网络
	 * @return
	 */
	public static boolean isNetWorkAvilable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mApplicationContent
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 取APP版本号
	 * @return
	 */
	public static int getAppVersionCode(){
		try {
			PackageManager mPackageManager = mApplicationContent.getPackageManager();
			PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName(),0);
			return _info.versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}
	
	/**
	 * 取APP版本名
	 * @return
	 */
	public static String getAppVersionName(){
		try {
			PackageManager mPackageManager = mApplicationContent.getPackageManager();
			PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName(),0);
			return _info.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

    public static Bitmap BitmapZoom(Bitmap b, float x, float y)
    {
        int w=b.getWidth();
        int h=b.getHeight();
        float sx=x/w;
        float sy=y/h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
				h, matrix, true);
        return resizeBmp;
    }


	public static String MD5(byte[] data) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(data);
		byte[] m = md5.digest();//加密
		return Base64.encodeToString(m, Base64.DEFAULT);
	}

	public static String getStringFromAssets(String fileName){
		try {
			InputStreamReader inputReader = new InputStreamReader(mApplicationContent.getResources().getAssets().open(fileName) );
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Uri getUriFromRes(int id){
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ mApplicationContent.getResources().getResourcePackageName(id) + "/"
				+ mApplicationContent.getResources().getResourceTypeName(id) + "/"
				+ mApplicationContent.getResources().getResourceEntryName(id));
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
