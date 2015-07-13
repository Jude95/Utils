# Utils-android开发轻武器库  
JUtils:小功能集合  
JActivityManager:Activity的管理类。保持所有存在activity引用  
JFileManager:完全管理本地文件存储  
JTimeTransform:时间格式转换器

###JUtils###
* void initialize(Application app)  
在Application的OnCreate里面初始化    

* void setDebug(boolean isDebug,String TAG)  
最好这样写JUtils.setDebug(BuildConfig.DEBUG, "DefaultTag");  

* void Log(String text)  
void Log(String TAG,String text)  
简便的全局Log，根据Debug模式是否log  

* void Toast(String text)  
void ToastLong(String text)  
简便的全局Toast.无论哪个线程都可以。

* int dip2px(float dpValue)  
int px2dip(float pxValue)  
dp与px的转换

* int getScreenWidth()  
int getScreenHeight()  
取屏幕高宽。去掉了状态栏的高度。  

* void closeInputMethod(Activity act)  
关闭输入法。输入法焦点所在activity

* boolean isBackground()  
判断应用是否在后台

* void copyToClipboard(String text)  
复制文本到剪贴版

* getSharedPreference()  
取默认SharedPreference

* double distance(double jingdu1, double weidu1, double jingdu2, double weidu2)  
测量2个经纬度坐标之间的距离。

* boolean isNetWorkAvilable()  
网络是否有效

* int getAppVersionCode()  
取APP版本号

* String getAppVersionName()  
取APP版本名

* Bitmap BitmapZoom(Bitmap b, float x, float y)
将b拉伸到宽x，高y

* String MD5(byte[] data)  
md5签名

* String getStringFromAssets(String fileName)  
从Assets里读文本文件

* String sendPost(String url, String param)  
最直接的post请求

***
###JFileManager
Manager会自动根据你传进来的枚举类型名字初始化文件目录。  
并把目录作为对象提供常用文件操作。  
JFileManager应该在Application里初始化。  
用法示例
          
          //文件目录列表
          enum Dir{
              Image,Text,Object,
          }
          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              tvTitle = (TextView) findViewById(R.id.title);
              
              //初始化传入文件目录列表，并初始化。
              FileManager.getInstance().init(this,Dir.values());
              
              //根据枚举类型获取目录。
              FileManager.Folder folder = FileManager.getInstance().getFolder(Dir.Image);
              
              //在目录下进行文件操作
              folder.writeObjectToFile("对象存储", "test");
              tvTitle.setText((String) folder.readObjectFromFile("test"));
          }

***
###JTimeTransform
不仅有时间戳，格式文本的解析。
`String toString(DateFormat format)`可自定义的解析方式.
自定义示例：

        public class RecentDateFormater implements TimeTransform.DateFormater{
            @Override
            public String format(TimeTransform date, long delta) {
                if (delta>0){
                    if (delta / TimeTransform.SECOND < 1){
                        return delta +"秒前";
                    }else if (delta / TimeTransform.HOUR < 1){
                        return delta / TimeTransform.SECOND+"分钟前";
                    }else if (delta / TimeTransform.DAY < 2 && new TimeTransform().getDay() == date.getDay()){
                        return delta / TimeTransform.HOUR+"小时前";
                    }else if (delta / TimeTransform.DAY < 3 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()+TimeTransform.DAY).getDay()){
                        return "昨天"+date.toString("HH:mm");
                    }else if (delta / TimeTransform.DAY < 4 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()+TimeTransform.DAY*2).getDay()){
                        return "前天"+date.toString("HH:mm");
                    }else{
                        return date.toString("yyyy/MM/dd  hh:mm");
                    }
                }else{
                    delta = -delta;
                    if (delta / TimeTransform.SECOND < 1){
                        return delta +"秒后";
                    }else if (delta / TimeTransform.HOUR < 1){
                        return delta / TimeTransform.SECOND+"分钟后";
                    }else if (delta / TimeTransform.DAY > -2 && new TimeTransform().getDay() == date.getDay()){
                        return delta / TimeTransform.HOUR+"小时后";
                    }else if (delta / TimeTransform.DAY > -3 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()-TimeTransform.DAY).getDay()){
                        return "明天"+date.toString("HH:mm");
                    }else if (delta / TimeTransform.DAY > -4 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()-TimeTransform.DAY*2).getDay()){
                        return "后天"+date.toString("HH:mm");
                    }else{
                        return date.toString("yyyy/MM/dd  hh:mm");
                    }
                }
            }
        }
        
***
###JActivityManager
给每个activity

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              JActivityManager.getInstance().pushActivity(this);
          }
          
          @Override
          protected void onDestroy() {
              super.onDestroy();
              JActivityManager.getInstance().popActivity(this);
          }
          
然后就可以在任何地方  
`JActivityManager.getInstance().currentActivity()`获取当前最顶层activity  
`JActivityManager.getInstance().closeActivity(Activity activity)`关闭activity  
`JActivityManager.getInstance().closeAllActivity()`关闭所有activity  
