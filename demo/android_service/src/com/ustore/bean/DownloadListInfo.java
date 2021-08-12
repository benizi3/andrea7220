package com.ustore.bean;

/**
 * 下载列表信息
 */
public class DownloadListInfo {
	
	private String apk_id;
	private String name;
	private String icon;
	private String progress;

	public DownloadListInfo(String apk_id, String name,
			String icon, String progress) {
		
		this.apk_id = apk_id;
		this.name = name;
		this.icon = icon;
		this.progress = progress;

	}

	public String getApk_id() {
		return apk_id;
	}


	public void setApk_id(String apk_id) {
		this.apk_id = apk_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getProgress() {
		return progress;
	}


	public void setProgress(String progress) {
		this.progress = progress;
	}

}
