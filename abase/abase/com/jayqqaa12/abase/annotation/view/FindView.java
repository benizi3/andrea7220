package com.jayqqaa12.abase.annotation.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 最好 继承  AbaseActivity 的时候 才能使用<br>
 * 
 * 也可以 通过 Abase。initView（）生效  但是 当前的activity 必须 实现 Lister 接口或者 响应的接口 
 * 
* @author jayqqaa12 
* @date 2013-5-5
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindView
{
	public int id();
	public int textId() default -1;
	public int imageId() default -1;
	public String tag() default "";
	
	/**
	 * 当前  布局 文件 includ引用 了相同 的布局文件的 时候 使用 （相信你会用到）
	 * 这时候 因为 用了 parid  id 设置为 0  就可以了
	 */
	public int parId()  default -1;
	
	
	
	/**
	 * 就可用相当于 OnClickListener
	 * @return
	 */
	public boolean click() default  false;
	/**
	 * 就可用相当于OnLongClickListener
	 * @return
	 */
	public boolean longClick() default  false;
	/**
	 * 就可用相当于OnItemClickListener
	 * @return
	 */
	public boolean itemClick() default  false;
	/**
	 * 就可用 相当于OnItemLongClickListener
	 * @return
	 */
	public boolean itemLongClick() default  false;
	/**
	 * 就可用  OnItemSelectedListener
	 * 
	 * @return
	 */
	public boolean itemSelect() default false;
	
	/**
	 * 相当于ListView 的  OnScrollListener
	 * @return
	 */
	public boolean scroll() default false;
	
	
	public boolean checkedChange() default  false;
	
	
	/**
	 * 功能 就是  注册  菜单
	 * @return
	 */
	public boolean contextMenu() default  false;
	

	
////////////////////////////////////扩展 功能//////////////////////////////////////////////////////
	
	
	/**
	 *   相当于OnTouchListener
	 * @return
	 */
	public boolean touch() default false;
	/**
	 *  相当于 OnGestureListener
	 * @return
	 */
	public boolean gesture() default  false;
	
	
	
	/**
	 *   相当于TextWatcher 接口
	 * @return
	 */
	public boolean textChanged() default  false;
	
	
	/**
	 *  OnFocusChangeListener
	 * @return
	 */
	public boolean focusChange() default  false;
	
	
	/**
	 * 相当于 OnChildClickListener
	 * @return
	 */
	public boolean childClick() default false;
	
	

/////////////////////////VIEW PAGE ///////////////////////////////////////////
	
	
	/**
	 * 有viewpage 的时候 使用  当只寻找 pageId 的时候 设置 id=0 即可
	 */
	public int pageId() default -1; 
	/**
	 * 
	 * 有viewpage 的时候 使用  设置 当前 控件是 第几页
	 */
	public int pageNum() default -1;
	
	/**
	 * 相当于  OnPageChangeListener 接口
	 */
	public boolean pagerChange() default false;
	



}
