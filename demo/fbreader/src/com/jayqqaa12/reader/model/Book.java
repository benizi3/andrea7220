package com.jayqqaa12.reader.model;

import java.io.File;
import java.io.Serializable;

import net.tsz.afinal.db.annotation.sqlite.Transient;

import com.jayqqaa12.abase.util.Txt;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.reader.R;

public class Book implements Serializable
{

	public int id;
	public String name ;
	public String path ;
	public int seq=10;
	public String iconUrl;
	public String icon;
	
	public Book(){
	}

	public Book(File f)
	{
		this.path =f.getPath();
		this.name=f.getName();
		if (name.contains(".")){
			String [] names=Txt.split(name, ".") ;
			icon=names[1];
			name=names[0];
		}
		
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getIconUrl()
	{
		return iconUrl;
	}

	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}

	public int getIconRes()
	{
		if(icon.equals("txt")) return R.drawable.cover_txt;
		else if(icon.equals("epub")) return R.drawable.cover_epub;
		else if(icon.equals("ebk")) return R.drawable.cover_ebk;
		else if(icon.equals("pdf")) return R.drawable.cover_pdf;
		else if(icon.equals("umd")) return R.drawable.cover_umd;
		else if(icon.equals("new")) return R.drawable.cover_net;
		else return R.drawable.cover_default_new;
		
	}

	
	

}
