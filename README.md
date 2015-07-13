# Utils
android 轻武器库

###JUtils
>void initialize(Application app)  
在Application的OnCreate里面初始化    

>void setDebug(boolean isDebug,String TAG)  
最好这样写JUtils.setDebug(BuildConfig.DEBUG, "DefaultTag");  

>void Log(String text)  
void Log(String TAG,String text)  
简便的全局Log，根据Debug模式是否log  

>void Toast(String text)  
void ToastLong(String text)  
简便的全局Toast.无论哪个线程都可以。

>int dip2px(float dpValue)  
int px2dip(float pxValue)  
dp与px的转换

>int getScreenWidth()  
int getScreenHeight()  
取屏幕高宽。去掉了状态栏的高度。  

>void closeInputMethod(Activity act)  
关闭输入法。输入法焦点所在activity

>boolean isBackground()  
判断应用是否在后台

>void copyToClipboard(String text)  
复制文本到剪贴版

>getSharedPreference()  
取默认SharedPreference

>double distance(double jingdu1, double weidu1, double jingdu2, double weidu2)  
测量2个经纬度坐标之间的距离。

>boolean isNetWorkAvilable()  
网络是否有效

>int getAppVersionCode()  
取APP版本号

>String getAppVersionName()  
取APP版本名

>Bitmap BitmapZoom(Bitmap b, float x, float y)
将b拉伸到宽x，高y

>String MD5(byte[] data)  
md5签名

>String getStringFromAssets(String fileName)  
从Assets里读文本文件

>String sendPost(String url, String param)
最直接的post请求
