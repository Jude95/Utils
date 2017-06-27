# Utils-android开发轻武器库  
JUtils:小功能集合  
JActivityManager:Activity的管理类。保持所有存在activity引用  
JFileManager:data目录下文件管理  
JTimeTransform:时间格式转换器

### 添加依赖
`compile 'com.jude:utils:1.2.2'`


### API
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
int getScreenHeightWithStatusBar()  
int getStatusBarHeight()  
int getActionBarHeight()  
int getNavigationBarHeight()  
取各种视图的默认高度。  

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

* Uri getUriFromRes(int id)  
读取资源文件Uri

* String sendPost(String url, String param)  
最直接的post请求


### JFileManager
Manager会自动根据你传进来的枚举类型名字初始化data目录。  
并把目录作为对象提供常用文件操作。  
JFileManager应该在Application里初始化。  
用法示例
```java    
          //文件目录列表
          enum Dir{
              Image,Text,Object,
          }
          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              tvTitle = (TextView) findViewById(R.id.title);
              
              //初始化传入文件目录列表，并初始化。此处data目录下会生成Image,Text,Object3个文件夹
              FileManager.getInstance().init(this,Dir.values());
              
              //根据枚举类型获取目录。Folder对象提供本目录下多种文件存取操作
              FileManager.Folder folder = FileManager.getInstance().getFolder(Dir.Image);
              
              //eg:对象序列化存取
              folder.writeObjectToFile("对象存储", "test");
              tvTitle.setText((String) folder.readObjectFromFile("test"));
          }
```


### JTimeTransform
不仅有时间戳，格式文本的解析。
`String toString(DateFormat format)`可自定义的解析方式.
自带一种实现`JTimeTransform.RecentDateFormat`,这样使用就好了：
    
    new JTimeTransform(data.getTime()).toString(new JTimeTransform.RecentDateFormat())
    
可以自动判断`x秒前`,`x分钟前`,`x小时前`,`昨天`,`x天前`
### JActivityManager
在Application中注册
```java
registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());
```
然后就可以在任何地方  
`JActivityManager.getInstance().currentActivity()`获取当前最顶层activity  
`JActivityManager.getInstance().closeActivity(Activity activity)`关闭activity  
`JActivityManager.getInstance().closeAllActivity()`关闭所有activity  


License
-------

    Copyright 2015 Jude

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
