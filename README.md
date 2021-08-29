

##abase简介 
> *abase集成了本人常用的 util <br>
> *以及 androidannotations afinal xutils 开源框架然后 <br>
> *按我个人习惯修改了一下 <br>
> *追求的是最快速的项目构建 <br>



##abase组成

#xUtils 
> *替换xUtils ViewUtils模块为 androidannotations <br>
> *更为强大的ioc annotations写法 <br>
> *集成了其他3大模块 DbUtils模块：HttpUtils模块：BitmapUtils模块：<br>


#abase-util
> *100多个util包含个人目前为止用到的所有可复用方法 <br>
 






##学习

> *使用前请自行研究 androidannotations xUtils <br>

> *只使用abase-util 只要在之前加上  <br>

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
	｝
｝
```






# 关于作者 @12叔 <http://weibo.com/jayqqaa12>


