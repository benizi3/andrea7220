

##abase简介 
> abase集成了本人常用的 util <br>
> 以及 androidannotations afinal xutils 开源框架 <br>
> 然后按我个人习惯修改了一下 <br>
> 追求的是最快速的项目构建 <br>



##abase组成

#xUtils 
> 替换xUtils ViewUtils模块为 androidannotations <br>
> 更为强大的ioc annotations写法 <br>
> 集成了其他3大模块 DbUtils模块：HttpUtils模块：BitmapUtils模块：<br>


#abase-util
> 100多个util包含个人目前为止用到的所有可复用方法 <br>
 






##学习

> 使用前请自行研究 androidannotations xUtils <br>

> 只使用abase-util 只要在之前加上  <br>

```java
Abase.setContext(context);

//当然一些权限啥的自己加
```


##demo
demo 目录下是一些 以前写的项目实例可参考（abase版本可能不同）

##例子

```java

// androidannotations 

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity
{
	@ViewById
	GridView gv;
	AbaseBaseAdapter<Book> adapter;
	
	private AbaseDao db = AbaseDao.create();

        /**
	这个方法可代替 onCreate（）
	**/
	@AfterViews
	public void init()
	{
	         // 使用BookItemView 来代替 adapter
		adapter = new AbaseBaseAdapter<Book>(BookItemView.class,this);
		gv.setAdapter(adapter);
		setData();
	}



@EViewGroup(R.layout.gv_item)
public class BookItemView extends ItemView<Book>
{
	@ViewById
	Button gv_bt;

	public BookItemView(Context context)
	{
		super(context);
	}

	@AfterInject
	public void init()
	{}

        // bind 方法来设置 相应的属性 即可
	public void bind(final Book book)
	{
		gv_bt.setText(book.name);
	}
}


// 常用utils  一些有用没用的东西


/**
 * SharedPredferences util 
 * 
 */
public class ConfigUtil



/**
 * 有关 handler  msg 的 工具 
* @author jayqqaa12 
* @date 2013-6-8
 */
public class MsgUtil 


/***
 * 时间 戳
 * 
 * @author 12
 * 
 *         SystemClock.elapsedRealtime
 */
public class TimeUtil



/**
 * 字符串操作工具类.
 * 
 */
public final class Txt


/***
 * 用来 判断 版本
 * 
 * @author 12
 * 
 */
public class VersionUtil 


/***
 * 日期操作 工具
 * @author 12
 *
 */
public class DateUtil


/**
 * 打印 log
 * 
 * @author jayqqaa12
 * @date 2013-6-5
 */
public class L


/**
 * 获得 logcat 的 日志 信息
 * 
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class LogcatUtil  


/**
 * 
 * Toast
 * 
 * @author  jayqqaa12
 *
 */
public class T 


/**
 * 校验工具类
 */
public class Validate {


/**
 * 文件 工具栏
 * 
 */
public class FileUtil



/**
 * Java utils 实现的Zip工具
 * 
 */
public class ZipUtils

/**
 * 媒体类型工具包
 * 
 * @author 12
 * 
 */
public class MediaTypeUtil  

/***
 * 判断网络类型  网络连接等
 * @author 12
 *
 */
public class NetworkUtil  

/**
 * 1.点击"Network"将输出本机所处的网络环境。 2.点击"WAP"将设定 移动网络接入点为CMWAP。 3.点击"GPRS"将设定
 * 移动网络接入点为CMNET。 注：自定义移动网络接入点的前提是“设置”→“无线和网络”→“移动网络”处已打勾。
 * 
 * 必需 为 系统应用 或者有系统权限
 */
public class ApnUtil  


/**
 *  打电话 相关 工具
* @author jayqqaa12 
* @date 2013-6-8
 */
public class CallUtil 



/**
 * 获取联系人信息等
 * @author 12
 *
 */
public class ContactUtil  


/***
 * 获取手机信息
 * @author 12
 *
 */
public class TelUtil

/***
 * 加密的
 * @author 12
 *
 */
public class CipherUtil

/**
 * apk 的 相关 信息
 * 
 * @author 12
 * 
 */
public class AppInfoUtil  


/**
 * 手机和SD卡内存获取
 *
 * */
public class MemoryUtil 

/**
 * 提供各种 系统 广播的 动态 注册
 * 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class ReceiverUtil  


/**
 *root 工具包
 * 
 * @author jayqqaa12
 * @date 2013-5-17
 */
public class RootUtil

/**
 * 获得 android.os.SystemProperties 相关 属性
 * 
 * @author jayqqaa12
 *
 */
public class SysPropUtil  


/**
 * 系统 工具 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class SysUtil  
/**
 * 当前 手机  进程的 相关 信息的 工具
 * @author  jayqqaa12
 *
 */
public class TaskUtil  

/**
 * 常见动画 工具
* @author jayqqaa12 
* @date 2013-6-5
 */
public class AnimUitl  

/**
 * @Title ImageUtils
 * @Package com.ta.util.extend.draw
 * @Description 处理图片的工具类.
 */
public class ImageUtil


/**
 * 
 * notification 工具集
 *
 */
public class NotificationUtil   

/**
 * 获取、设置控件信息
 */
public class ViewParmUtil 


// 对框架的进一步封装



/***
 * 结合 androidannotations 
 * 
 * 使用方法  new AbasePopup(BindView.class)
 * 
 * 其中 bindview 为继承 bindview的子类
 * 
 * 原理类似 adapter的 itemview
 * 
 * @author 12
 *
 */
public class AbasePopup extends PopupWindow

/**
 * 配合 android annotations使用 配合 itemView 使用
 * 
 * 可在 @afterInject 注入之后 设置 setItemView 传入 itemview 实例化类
 * 
 * 也可直接 new  AbaseBaseAdapter<T>( itemview.class,context)
 * 
 * @author 12
 * 
 * @param <T>
 */
public   class AbaseBaseAdapter<T> extends BaseAdapter
 

//还有一些东西 也挺简单的 就不多挺了 呵呵 大家看着办

```



#其他开源项目  <a  href="http://git.oschina.net/jayqqaa12/JFinal_Authority">JFinal_Authority </a>



# 关于作者 @12叔 <http://weibo.com/jayqqaa12>


