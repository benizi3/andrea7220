package com.jayqqaa12.reader.model;

import java.io.File;
import java.io.Serializable;

import net.tsz.afinal.db.annotation.sqlite.Transient;

import com.jayqqaa12.abase.util.Txt;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.reader.R;

public class Book implements Serializable
{

	@Transient
	private static final long serialVersionUID = 3818662966565810856L;

	public int id;
	public String name ;
//	public String des;
	public String path ;
	
	public int icon;
	
	
	public Book(){
	}

	public Book(File f)
	{
		this.path =f.getPath();
		this.name=f.getName();
		if (name.contains(".")){
			String [] names=Txt.split(name, ".") ;
			name = names[0];
			if("txt".equals(names[1]))icon= R.drawable.cover_txt;
			else if("epub".equals(names[1]))icon= R.drawable.cover_epub;
			else if("pdf".equals(names[1]))icon= R.drawable.cover_pdf;
			else if("ebk".equals(names[1]))icon= R.drawable.cover_ebk;
			else if("umd".equals(names[1]))icon= R.drawable.cover_umd;
			else if("chm".equals(names[1]))icon= R.drawable.cover_chm;
			else icon= R.drawable.cover_default_new;
			
				
		}
		L.i("name = "+name);
		
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

	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}
	
	
	
	
	

}
