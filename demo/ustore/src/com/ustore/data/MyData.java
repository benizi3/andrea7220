package com.ustore.data;

import java.io.File;
import java.util.Vector;

import android.app.Activity;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.Toast;

import com.jayqqaa12.abase.util.common.L;
import com.ustore.adapter.MySimpleAdapter;
import com.ustore.download.Dao;
import com.ustore.download.Database;
import com.ustore.http.Website;
import com.ustore.service.DownloadService;

public class MyData
{
	private String netdates;

	/** 获得服务器的版本信息 **/
	private String version = null;
	/** 获得列表的总数 **/
	private int totleNum = 0;
	/** 保存解析的数据 **/
	private Vector<CatchData> mModels = new Vector<CatchData>();
	/** Activity的名称 **/
	private Activity ac_name = null;
	private ListView view = null;
	private String flag;

	/**
	 * 设置要解析的数组数据
	 * 
	 * @param dates
	 */
	public void setNetdates(String dates)
	{
		this.netdates = dates;
	}

	/**
	 * 返回版本信息
	 * 
	 * @return
	 */
	public String getVersion()
	{

		if (netdates != null)
		{

			try
			{

				String[] replaceString = netdates.split("\\}\\{");
				String conteStrings = replaceString[1];
				String[] conteStr = conteStrings.split("\\}");
				String str = conteStr[1];

				return str;
			}
			catch (Exception e)
			{
				e.printStackTrace();

			}

		}

		return null;
	}

	/**
	 * 接收列表要显示的数据
	 */
	public void sqlAddDate(Activity cont, ListView view, String flag)
	{
		this.ac_name = cont;
		this.view = view;

		Analytic(netdates);

	}

	/**
	 * 解析出列表要显示的数据
	 */
	private void Analytic(String date)
	{
		int i =0;
		
		String consString = null;
		String[] conStrings = null;

		if (date != null)
		{

			try
			{

				String[] replaceString = date.split("\\}\\{");
				String conteStrings = replaceString[1];
				String[] conteStr = conteStrings.split("\\}");
				String datastr = conteStr[0];

				if (flag == "home") version = (conteStr[1]);

				String[] contentStrings = datastr.split("\\|");

				totleNum = contentStrings.length;
				mModels.clear();
				String iconUrl = null;
 
		
				for (int c = 0; c < totleNum; c++)
				{
					consString = contentStrings[c];
					conStrings = consString.split("\\^");

					if(conStrings.length<9) {
						i++;
						continue; 
					}
						
					iconUrl = conStrings[8];// 图标下载地址
					
					
					
					if (conStrings[1].length() > 10)
					{
						conStrings[1] = conStrings[1].substring(0, 10) + "...";
					}

					addData(conStrings[1], conStrings[0], "", iconUrl, conStrings[5], conStrings[6], conStrings[7], conStrings[9]);

				}
				MySimpleAdapter adapter = new MySimpleAdapter(ac_name, view, mModels, flag);

				view.setAdapter(adapter);
				view.setSelection(adapter.location);

			}
			catch (Exception e)
			{
				e.printStackTrace();
				
			}
			
			L.e(" continue count = "+i);

		}
	}

	/**
	 * 添加ListView每个Item的数据
	 * 
	 * @param downloadUrl
	 **/
	private void addData(String name, String id, String num, String iconurl, String size, String downloadTimes, String version,
			String downloadUrl)
	{
		if (id.equals("0"))
		{
			Toast.makeText(ac_name, "没有您要找的软件信息哦,换个试试吧!", Toast.LENGTH_LONG).show();
			return;
		}
		CatchData model = new CatchData();
		model.name = name;
		model.id = id;
		model.num = num;
		model.icon = iconurl;
		model.url = downloadUrl;

		// add
		model.size = size;
		model.downloadTimes = downloadTimes;
		model.version = version;

		// 软件未下载则下载，已下载则启动 state=0未下载 state=1已下载 state=2更新
		StringBuffer mSavePath = new StringBuffer(Website.sd_path);
		mSavePath.append("download");
		StringBuffer apkname = new StringBuffer(id);
		apkname.append(".apk");// 软件安装包的名称
		File apkFile = new File(mSavePath.toString(), apkname.toString());

		// 已下载
		if (apkFile.exists())
		{

			Database db = null;
			Cursor c1 = null;
			String str1 = null;
			Dao d = null;

			try
			{

				db = new Database(ac_name);

				d = Dao.getInstance(ac_name);

				c1 = db.queryApkVersion(id);

				// 数据库中有对应apk id的版本号
				if (c1.moveToFirst())
				{
					str1 = c1.getString(0);
				}
				else
				{
					db.insertApkVersion(model.id, model.version);
					str1 = model.version;
				}

				if (str1 != null)
				{

					// 更新
					if (!str1.equals(version))
					{
						model.state = 2;
						// 版本号相同，不用更新
					}
					else
					{

						// 上次未下载完成
						if (!d.notHasInfors(model.id))
						{

							if (DownloadService.downloadingSet.contains(model.id))
							{
								model.state = 3;
							}
							else
							{
								model.state = 0;
							}
							// 下载完成，启动状态
						}
						else
						{
							model.state = 1;
						}

					}
				}

				str1 = null;

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{

				if (db != null)
				{

					db.close();
				}
				if (c1 != null)
				{
					c1.close();

				}
				d = null;

			}

		}
		else
		{
			model.state = 0;
		}

		mModels.add(model);

	}

}
