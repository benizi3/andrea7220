

##abase简介 
> abase集成了本人常用的 util <br>
> 以及 androidannotations  xutils 等开源框架 <br>
> 然后按我个人习惯修改了一下 <br>
> 追求的是快速的项目构建 <br>



##abase组成

#androidannotations xUtils 
> 替换xUtils ViewUtils模块为 androidannotations <br>
> 更为强大的ioc annotations写法 <br>
> 集成了其他3大模块并进行部分优化 Db模块 Http模块 Bitmap模块<br>
> 基于abase 的个人定制

#abase-util
> 100多个util包含个人目前为止用到的所有可复用方法 <br>
 
#abase-ext 
> 定位sdk firewall 等扩展支持 需要的






##学习

> 使用前可自行研究 <a href="https://github.com/excilys/androidannotations/wiki/Cookbook"> androidannotations</a> <a href="https://github.com/wyouflf/xUtils" >xUtils </a> <br>

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


//activity 形式  一般使用方式直接绑定对象 AActivity + AAdapter +ItemView<T>

@EActivity(R.layout.activity_main)
public class MainActivity extends AActivity
{
	@ViewById
	GridView gv;
	ABaseAdapter<Book> adapter;
	
	@Bean
        ADao db ;

        /**
	这个方法可代替 onCreate（）
	**/
	 
	public void init()
	{
	         // 使用BookItemView 来代替 adapter
		adapter = new AeBaseAdapter<Book>(BookItemView.class,this);
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


//fragment tabs 使用形式 

AFragmentActivity+AFragmentPagerAdapter+Viewpage+TabPageIndicator+AFragment


@EActivity(R.layout.activity_viewpage)
@NoTitle
public class RankActivity extends AFragmentActivity
{
	@ViewById
	TextView tv_title;
	@ViewById
	ViewPager vp;

	@ViewById  // viewindicator 开源项目 
	TabPageIndicator tpi;
	
	private AFragmentPagerAdapter adapter;
	
	protected void init()
	{
		tv_title.setText("排行");
		adapter = new AFragmentPagerAdapter(getSupportFragmentManager());
		adapter.setFragment(AFragment.newInstances(MsgKit.getBundle("url", new String[]{Config.apprank_url,Config.gamerank_url}),
				RankFragment.class,RankFragment.class));
		adapter.setLable("应用排行","游戏排行");
		vp.setAdapter(adapter);
		tpi.setViewPager(vp);
		
		
	}
}

@EFragment(R.layout.lay)
public class RankFragment extends AFragment  
{
	@ViewById
	ListView lv;
	@ViewById
	ProgressBar pb;
	 
	@Bean
	AppAdapter adapter;
	
	@FragmentArg
	String url;
	
	@Override
	protected void init()
	{
		 
		pb.setVisibility(View.VISIBLE);

		setDate(xxx);
	}
	
	
	 

	@Override
	public void setDate(String data)
	{
		adapter.setData(AppInfo.paraseData(data));
		adapter.setListView(lv);
		lv.setAdapter(adapter);
		pb.setVisibility(View.GONE);
	}
}









// 常用 kit 一些有用没用的东西


/**
 * SharedPredferences kit 
 * 
 */
public class ConfigKit



/**
 * 有关 handler  msg 的 工具 
* @author jayqqaa12 
* @date 2013-6-8
 */
public class MsgKit 


/***
 * 时间 戳
 * 
 * @author 12
 * 
 *         SystemClock.elapsedRealtime
 */
public class TimeKit



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
public class VersionKit 


/***
 * 日期操作 工具
 * @author 12
 *
 */
public class DateKit


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
public class LogcatKit  


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
public class FileKit



/**
 * Java Kits 实现的Zip工具
 * 
 */
public class ZipKits

/**
 * 媒体类型工具包
 * 
 * @author 12
 * 
 */
public class MediaTypeKit  

/***
 * 判断网络类型  网络连接等
 * @author 12
 *
 */
public class NetworkKit  

/**
 * 1.点击"Network"将输出本机所处的网络环境。 2.点击"WAP"将设定 移动网络接入点为CMWAP。 3.点击"GPRS"将设定
 * 移动网络接入点为CMNET。 注：自定义移动网络接入点的前提是“设置”→“无线和网络”→“移动网络”处已打勾。
 * 
 * 必需 为 系统应用 或者有系统权限
 */
public class ApnKit  


/**
 *  打电话 相关 工具
* @author jayqqaa12 
* @date 2013-6-8
 */
public class CallKit 



/**
 * 获取联系人信息等
 * @author 12
 *
 */
public class ContactKit  


/***
 * 获取手机信息
 * @author 12
 *
 */
public class TelKit

/***
 * 加密的
 * @author 12
 *
 */
public class CipherKit

/**
 * apk 的 相关 信息
 * 
 * @author 12
 * 
 */
public class AppInfoKit  


/**
 * 手机和SD卡内存获取
 *
 * */
public class MemoryKit 

/**
 * 提供各种 系统 广播的 动态 注册
 * 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class ReceiverKit  


/**
 *root 工具包
 * 
 * @author jayqqaa12
 * @date 2013-5-17
 */
public class RootKit

/**
 * 获得 android.os.SystemProperties 相关 属性
 * 
 * @author jayqqaa12
 *
 */
public class SysPropKit  


/**
 * 系统 工具 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class SysKit  
/**
 * 当前 手机  进程的 相关 信息的 工具
 * @author  jayqqaa12
 *
 */
public class TaskKit  

/**
 * 常见动画 工具
* @author jayqqaa12 
* @date 2013-6-5
 */
public class AnimKit  

/**
 * @Title ImageKits
 * @Package com.ta.Kit.extend.draw
 * @Description 处理图片的工具类.
 */
public class ImageKit


/**
 * 
 * notification 工具集
 *
 */
public class NotificationKit   

/**
 * 获取、设置控件信息
 */
public class ViewParmKit 


// 对框架的进一步封装



/***
 * 可结合 androidannotations 
 * 
 * 使用方法  new APopup(BindView.class)
 * 
 * 其中 bindview 为继承 bindview的子类
 * 
 * 原理类似 adapter的 itemview
 * 
 * @author 12
 *
 */
public class APopup extends PopupWindow

/**
 * 配合 android annotations使用 配合 itemView 使用
 * 
 * 可在 @afterInject 注入之后 设置 setItemView 传入 itemview 实例化类
 * 
 * 也可直接 new  AAdapter<T>( itemview.class,context)
 * 
 * @author 12
 * 
 * @param <T>
 */
public   class ABaseAdapter<T> extends BaseAdapter
 

//还有一些东西 也挺简单的 就不多挺了 呵呵 大家看着办

```

<br>
## 捐赠
如果您喜欢本项目<br> 
认为本项目确实给您带来方便和帮助非常感谢 <br>
您的捐赠，是我们前进的动力<br>

支付宝捐赠帐号 jayqqaa12@yahoo.com.cn


扫描二维码捐赠
![Screenshot 12shu-zfb](http://git.oschina.net/jayqqaa12/JFinal_Authority/raw/master/Screenshot/12shu-zfb.png "Screenshot 12shu-zfb") <br> <br>


<br>


#其他开源项目 
#<a  href="http://git.oschina.net/jayqqaa12/abase-reader">abase-reader android 开源阅读器</a> <br>
#<a  href="http://git.oschina.net/jayqqaa12/JFinal_Authority">JFinal_Authority Jfinal权限后台</a>
# 关于作者 @12叔 <http://weibo.com/jayqqaa12>
#联系方式 476335667@qq.com


