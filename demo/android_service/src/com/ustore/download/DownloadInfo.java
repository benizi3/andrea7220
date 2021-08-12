package com.ustore.download;

/**
 * ����һ��������Ϣ��ʵ����
 */
public class DownloadInfo {
	private String threadId;// ������id
	private int startPos;// ��ʼ��
	private int endPos;// �����
	private int compeleteSize;// ��ɶ�
	private String url;// �����������ʶ
	private int fileSize;// �ļ���С
	private String localfile;//�ļ�����·��

//	private long contentid;// ��������ID
//	private String title;// �ļ����
	
//	private int sumcompelete_size;//����ɶ�
	
	
//	private String playtime;//����ʱ��

	public DownloadInfo(String threadId, int startPos, int endPos,
			int compeleteSize, String url, int fileSize, String localfile) {
		this.threadId = threadId;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compeleteSize = compeleteSize;
		this.url = url;
		this.fileSize = fileSize;
//		this.contentid = contentid;
//		this.title = title;
//		this.sumcompelete_size = sumcompelete_size;
//		this.playtime = playtime;
		this.localfile = localfile;
	}

/*	public int getSumcompelete_size() {
		return sumcompelete_size;
	}

	public void setSumcompelete_size(int sumcompelete_size) {
		this.sumcompelete_size = sumcompelete_size;
	}

	public String getPlaytime() {
		return playtime;
	}

	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}

	public int getSumcompeleteSize() {
		return sumcompelete_size;
	}

	public void setSumcompeleteSize(int sumcompeleteSize) {
		this.sumcompelete_size = sumcompeleteSize;
	}*/

	public DownloadInfo() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public int getCompeleteSize() {
		return compeleteSize;
	}

	public void setCompeleteSize(int compeleteSize) {
		this.compeleteSize = compeleteSize;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

/*	public long getContentid() {
		return contentid;
	}

	public void setContentid(long contentid) {
		this.contentid = contentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}*/

	public String getLocalfile() {
		return localfile;
	}

	public void setLocalfile(String localfile) {
		this.localfile = localfile;
	}

	@Override
	public String toString() {
		return "DownloadInfo [threadId=" + threadId + ", startPos=" + startPos
				+ ", endPos=" + endPos + ", compeleteSize=" + compeleteSize
				+ "]";
	}
}
