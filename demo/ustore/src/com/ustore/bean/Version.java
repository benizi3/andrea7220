package com.ustore.bean;

public class Version
{
	public String id ="id";
	public String name ="name";
	public String version ="version";
	public int version_code;
	public String des;
	public String data ;
	public String size;
	public String tableName ="version";

	
	public int getVersion_code()
	{
		return version_code;
	}
	public void setVersion_code(int version_code)
	{
		this.version_code = version_code;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
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
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getDes()
	{
		return des;
	}
	public void setDes(String des)
	{
		this.des = des;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getSize()
	{
		return size;
	}
	public void setSize(String size)
	{
		this.size = size;
	}
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
	
	
	

}
