package com.ustore.data;

import java.util.Map;
import java.util.WeakHashMap;

import com.jayqqaa12.abase.util.common.L;
import com.ustore.http.Website;

public class DetailsData{
	//声明一个用来放置获取服务器下载详情页面的数据流
   private String detailstdata=null; 
    
    //声明用来放入缓存中的下载详情页面的列表项
   private Map<String, String> datamap=null; 
    //声明获取软件的安装包名称
   private String apkname = null;  
   
   public DetailsData(){}
 
   
  //设置要解析的数组数据
   public void setDetailstData(String  data){
	   this.detailstdata=data;
   }
  
   //返回list的值
   public  Map<String, String> getDetailsDate(){	  
	  return this.datamap;
   }
  //获得安装包的名字id.apk
   public  String getApkname(){		 
	  return this.apkname;
   }
  
   public void detailsAddDate(){
	 
	           try { 
	        	    detailstdata=detailstdata.replaceAll("\\\\r", "");
			        detailstdata=detailstdata.replaceAll("\\\\n", "");
			        detailstdata=detailstdata.replaceAll("\\\\u003e", "");
			        detailstdata=detailstdata.replaceAll("\\\\u003c", "");
			        detailstdata=detailstdata.replaceAll("u003e", "");
			        detailstdata=detailstdata.replaceAll("u003c", "");
			        detailstdata=detailstdata.replaceAll("u0026", "");
			       
			        detailstdata=detailstdata.replaceAll("nbsp\\;", "");
			        detailstdata=detailstdata.replaceAll("\\\\u0026", "");
			        detailstdata=detailstdata.replaceAll("br\\/", "");
			        detailstdata=detailstdata.replaceAll("amp\\;", "");
			        detailstdata=detailstdata.replaceAll("\\\\u0", "");
			        detailstdata=detailstdata.replaceAll("\\\\*", "");
			        
                    String[]replaceString=detailstdata.split("\\}");
		            String conteStrings=replaceString[0];
		            String[] contString= conteStrings.split("\\{");
		            String conteString=contString[1];
		            	String[]appData=conteString.split("\\^");
		            	String scoreString=appData[2];
		            	String[]scoreStrings=scoreString.split("\\%");
		            	 Map<String, String> map = new WeakHashMap<String,String>();
		            	 if( 0 == Integer.parseInt(appData[0])){
		            		   this.datamap=null;
		            	 }else{
		            	 //获得app的服务器id
		            	   map.put("id",appData[0] );
		            	   //获得app的服务器name
		            	   if (appData[1].length()>=10){
								appData[1]=appData[1].substring(0,10)+"...";
							}	
		 	               map.put("name", appData[1]);
		 	              //获得软件的下载次数
		 	               map.put("downloadnum",scoreStrings[0]);
		 	               //获得软件的评论人数
		 	               map.put("commentnum",scoreStrings[1]);
		 	               //获得软件的总评分
		 	               map.put("totlescore",scoreStrings[2]+"分");
		 	               //获得软件的五星评分百分比
		 	               map.put("fivescore",scoreStrings[3]+"%");
		 	              //获得软件的四星评分百分比
		 	               map.put("fourscore",scoreStrings[4]+"%");
		 	              //获得软件的三星评分百分比
		 	               map.put("threescore",scoreStrings[5]+"%");
		 	              //获得软件的二星评分百分比
		 	               map.put("twoscore",scoreStrings[6]+"%");
		 	              //获得软件的一星评分百分比
		 	               map.put("onescore",scoreStrings[7]+"%");
		 	              //获得app的服务器使用情况，0为免费，1为购买，2为限次使用
		 	              if(appData[3].equals("0")){
		 	            	 map.put("ischarge","免费");
			              }else
			               if(appData[3].equals("1")){
			            	 map.put("ischarge","需要购买");
				          }else{
				        	 map.put("ischarge","限次使用"+appData[3]);
				          }	
		 	               //获得软件详细描述内容
		 	                map.put("information", appData[4]);
		 	               //获得软件发布日期
		 	                map.put("date", appData[5].substring(0,appData[5].length()-5));
		 	               //获得软件大小
		 	                map.put("size", appData[6]);
		 	               //获得软件版本信息
		 	                map.put("version", appData[7]);
		 	               //获得软件作者
		 	                map.put("author", appData[8]);		 	                
		 	               //获得app的服务器价格
//		 	                map.put("price",conStrings[9]+"元");
		 	                //获得软件的出处
//		 	             if(conStrings[11].equals("0")){
//		 	            	  map.put("filetype","java");
//			              }else
//			               if(conStrings[11].equals("1")){
//			            	   map.put("filetype","快乐风");
//				          }else{
//				        	  map.put("filetype","掌炫应用");
//				          }	
 	                     //加载软件图标地址//		 	       
		 	                
		 	              map.put("icon", appData[12]);
		 	             //下载详情中软件的展示大图地址
		 	              
		 	              String strurl = appData[13];
		 	             
		 	             map.put("image", strurl);	
		 	          			 	        
		 	             //获得缓存中列表的信息	
		 	             datamap = map; 
		 	             //获得软件的安装包的名称
		 	             this.apkname=appData[1];
		            }
		            	 
			} catch (Exception e) {
			}
	         detailstdata=null;
	     
   }
}
