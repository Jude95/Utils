package com.jude.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

public class Utils {
	public static String TAG;
	public static boolean DEBUG = false;
	private static Context mApplicationContent;

	public static void initialize(Application app,String outOfDateKey){
		mApplicationContent = app.getApplicationContext();
        if (!getPreference().getString("outOfDateKey","NULL").equals(outOfDateKey)){
			getPreference().edit().clear().commit();
        }
        getPreference().edit().putString("outOfDateKey",outOfDateKey).commit();
	}
	
	public static void setDebug(boolean isDebug,String TAG){
		Utils.TAG = TAG;
		Utils.DEBUG = isDebug;
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

	public static void Log(boolean bool){
		if(DEBUG){
			Log.i(TAG, bool ? "true" : "false");
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
		
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
		    c = Class.forName("com.android.internal.R$dimen");
		    obj = c.newInstance();
		    field = c.getField("status_bar_height");
		    x = Integer.parseInt(field.get(obj).toString());
		    sbar = mApplicationContent.getResources().getDimensionPixelSize(x);
		} catch(Exception e1) {
		}
		
		return dm.heightPixels-sbar;
	}

	/**
	 * 关闭输入法
	 * @param act
	 */
	public static void closeInputMethod(Activity act){
		View view = act.getCurrentFocus();
		if(view!=null){
			((InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE)).
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
		ClipboardManager cbm = (ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
		cbm.setPrimaryClip(ClipData.newPlainText("jude", text));
	}

	/**
	 * 获取SharedPreferences
	 * @return SharedPreferences
	 */
	public static SharedPreferences getPreference() {
		return mApplicationContent.getSharedPreferences(mApplicationContent.getPackageName(), Activity.MODE_PRIVATE);
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
	 * 产生不重复的随机数
	 * @param count 产生随机数的个数
	 * @param range  范围:0—range
	 */
	public static int[] getRandomNumbers(int count,int range ){
			int randomCount =count;
			int randomRange = range;
			int[] intRet = new int[randomCount]; 
	           int intRd = 0; //存放随机数
	           int number = 0; //记录生成的随机数个数
	           int flag = 0; //是否已经生成过标志
	           while(number<randomCount){
	                Random rdm = new Random(System.currentTimeMillis());
	                intRd = Math.abs(rdm.nextInt())%(randomRange+1);
	                for(int i=0;i<number;i++){
	                    if(intRet[i]==intRd){
	                        flag = 1;
	                        break;
	                    }else{
	                        flag = 0;
	                    }
	                }
	                if(flag==0){
	                    intRet[number] = intRd;
	                    number++;
	                }
	               
	       }
	          
		return intRet;
	}


	
	
	/**
	 * 保存图片
	 * @param bitmap
	 * @param path
	 */
	public static void BitmapSave(Bitmap bitmap, String path){
		File file = new File(path);
        Utils.Log(file.getAbsolutePath());
		try {
			if (!file.exists()) {
				new File(path.substring(0, path.lastIndexOf('/'))).mkdirs();
			}else{
				file.delete();
			}
			file.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
	
	/**
	 * 文件复制
	 * @param s
	 * @param t
	 */
	public static void fileCopy(File s, File t) {
		t.delete();
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            	}

        }

    }
	
	/**
	 * 删除文件，可删除文件夹
	 * @param file
	 */
    public static void fileDelete(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
            	fileDelete(f);
            }
//            file.delete();
        }
    }

    /**
     * 创建文件父目录
     * @param path
     */
    public static File fileCreat(String path){
        File f = new File(path);
        if (f.exists()){
            f.delete();
        }else{
            new File(path.substring(0, path.lastIndexOf('/'))).mkdir();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            Utils.Toast("File Error:"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return f;
    }

    public static void witeObjectToFile(Object object, File file) {

        ObjectOutputStream objectOut = null;
        FileOutputStream fileOut = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOut = new FileOutputStream(file,false);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }

    public static Object readObjectFromFile(File file) {

        ObjectInputStream objectIn = null;
        Object object = null;
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(file);//context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            // Do nothing
        }catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
            if(fileIn != null){
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
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

    public static void cancelListViewBounceShadow(ListView listView){
        try {
            Class<?> c = (Class<?>) Class.forName(AbsListView.class.getName());
            Field egtField = c.getDeclaredField("mEdgeGlowTop");
            Field egbBottom = c.getDeclaredField("mEdgeGlowBottom");
            egtField.setAccessible(true);
            egbBottom.setAccessible(true);
            Object egtObject = egtField.get(listView); // this 指的是ListiVew实例
            Object egbObject = egbBottom.get(listView);

            // egtObject.getClass() 实际上是一个 EdgeEffect 其中有两个重要属性 mGlow mEdge
            // 并且这两个属性都是Drawable类型
            Class<?> cc = (Class<?>) Class.forName(egtObject.getClass()
					.getName());
            Field mGlow = cc.getDeclaredField("mGlow");
            mGlow.setAccessible(true);
            mGlow.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mGlow.set(egbObject, new ColorDrawable(Color.TRANSPARENT));

            Field mEdge = cc.getDeclaredField("mEdge");
            mEdge.setAccessible(true);
            mEdge.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mEdge.set(egbObject, new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {

        }
    }

    public static void resizeView(final View view, int width, int height){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (width >= 0)params.width = width;
        if (height >= 0)params.height = height;
        view.setLayoutParams(params);
    }

    public static String MD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        byte[] m = md5.digest();//加密
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < m.length; i ++){
            sb.append(m[i]);
        }
        return sb.toString();
    }
}
