package com.jayqqaa12.reader.ui.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.reader.kit.StringCodeKit;

public class PageFactory
{

	private MappedByteBuffer map = null;
	private int size = 0;
	private int mbBufBegin = 50; // 50
	private int mbBufEnd = 0;
	private String charset = "utf-8";
	private Bitmap bg = null;
	private int w;
	private int h;

	private Vector<String> lines = new Vector<String>();

	private int fontSize = 30;
	private int textColor = Color.BLACK;
	private int backColor = 0xffff9e85; // 背景颜色
	private int marginWidth = 15; // 左右与边缘的距离
	private int marginHeight = 20; // 上下与边缘的距离

	private int advHeight = 0;// 广告条的狂度

	private int lineCount; // 每页可以显示的行数
	private float visibleHeight; // 绘制内容的宽
	private float visibleWidth; // 绘制内容的宽
	private boolean isfirstPage, islastPage;
	private int b_fontSize = 16;// 底部文字大小
	private int e_fontSize = 5;
	private int spaceSize = 20;// 行间距大小
	private int curProgress = 0;// 当前的进度

	private String fileName = "";

	// private int m_nLineSpaceing = 5;

	private Paint mPaint;
	private Paint bPaint;// 底部文字绘制
	private Paint spactPaint;// 行间距绘制
	private Paint titlePaint;// 标题绘制

	public PageFactory(int w, int h)
	{
		this.w = w;
		this.h = h;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(fontSize);
		mPaint.setColor(textColor);

		// mPaint.setTextSkewX(0.1f);//设置斜体
		visibleWidth = w - marginWidth * 2;
		visibleHeight = h - marginHeight * 2 - advHeight;
		int totalSize = fontSize + spaceSize;
		lineCount = (int) ((visibleHeight) / totalSize); // 可显示的行数
		// 底部文字绘制
		bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bPaint.setTextAlign(Align.LEFT);
		bPaint.setTextSize(b_fontSize);
		bPaint.setColor(textColor);
		// 行间距设置
		spactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		spactPaint.setTextAlign(Align.LEFT);
		spactPaint.setTextSize(spaceSize);
		spactPaint.setColor(textColor);
		//
		titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titlePaint.setTextAlign(Align.LEFT);
		titlePaint.setTextSize(30);
		titlePaint.setColor(textColor);

	}

	public void openbook(String strFilePath)
	{
		File file = null;
		try
		{
			charset= StringCodeKit.getTextCharset(strFilePath);
			
			L.i("chart set ="+charset);
			
			file = new File(strFilePath);
			long length = file.length();
			size = (int) length;
			map = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected byte[] readParagraphBack(int nFromPos)
	{
		int end = nFromPos;
		int i;
		byte b0, b1;
		if (charset.equals("UTF-16LE"))
		{
			i = end - 2;
			while (i > 0)
			{
				b0 = map.get(i);
				b1 = map.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != end - 2)
				{
					i += 2;
					break;
				}
				i--;
			}

		}
		else if (charset.equals("UTF-16BE"))
		{
			i = end - 2;
			while (i > 0)
			{
				b0 = map.get(i);
				b1 = map.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != end - 2)
				{
					i += 2;
					break;
				}
				i--;
			}
		}
		else
		{
			i = end - 1;
			while (i > 0)
			{
				b0 = map.get(i);
				if (b0 == 0x0a && i != end - 1)
				{
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0) i = 0;
		int nParaSize = end - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++)
		{
			buf[j] = map.get(i + j);
		}
		return buf;
	}

	// 读取上一段落
	protected byte[] readParagraphForward(int nFromPos)
	{
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (charset.equals("UTF-16LE"))
		{
			while (i < size - 1)
			{
				b0 = map.get(i++);
				b1 = map.get(i++);
				if (b0 == 0x0a && b1 == 0x00)
				{
					break;
				}
			}
		}
		else if (charset.equals("UTF-16BE"))
		{
			while (i < size - 1)
			{
				b0 = map.get(i++);
				b1 = map.get(i++);
				if (b0 == 0x00 && b1 == 0x0a)
				{
					break;
				}
			}
		}
		else
		{
			while (i < size)
			{
				b0 = map.get(i++);
				if (b0 == 0x0a)
				{
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++)
		{
			buf[i] = map.get(nFromPos + i);
		}
		return buf;
	}

	protected Vector<String> pageDown()
	{
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < lineCount && mbBufEnd < size)
		{
			byte[] paraBuf = readParagraphForward(mbBufEnd); // 读取一个段落
			mbBufEnd += paraBuf.length;
			try
			{
				strParagraph = new String(paraBuf, charset);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1)
			{
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			}
			else if (strParagraph.indexOf("\n") != -1)
			{
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0)
			{
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0)
			{
				int nSize = mPaint.breakText(strParagraph, true, visibleWidth, null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= lineCount)
				{
					break;
				}
			}
			if (strParagraph.length() != 0)
			{
				try
				{
					mbBufEnd -= (strParagraph + strReturn).getBytes(charset).length;
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	protected void pageUp()
	{
		if (mbBufBegin < 0) mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < lineCount && mbBufBegin > 0)
		{
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(mbBufBegin);
			mbBufBegin -= paraBuf.length;
			try
			{
				strParagraph = new String(paraBuf, charset);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0)
			{
				// paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0)
			{
				int nSize = mPaint.breakText(strParagraph, true, visibleWidth, null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > lineCount)
		{
			try
			{
				mbBufBegin += lines.get(0).getBytes(charset).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		mbBufEnd = mbBufBegin;
		return;
	}

	public void prePage() throws IOException
	{
		if (mbBufBegin <= 0)
		{
			mbBufBegin = 0;
			isfirstPage = true;
			return;
		}
		else isfirstPage = false;

		lines.clear();
		pageUp();
		lines = pageDown();
	}

	public void nextPage() throws IOException
	{
		if (mbBufEnd >= size)
		{
			islastPage = true;
			return;
		}
		else islastPage = false;
		lines.clear();
		mbBufBegin = mbBufEnd;
		lines = pageDown();
	}

	public void onDraw(Canvas c)
	{
//		if (mbBufBegin == 0)
//		{
//			setCover(c);
//			return ;
//		}

		if (lines.size() == 0) lines = pageDown();
		if (lines.size() > 0)
		{
			if (bg == null) c.drawColor(backColor);
			else c.drawBitmap(bg, 0, 0, null);
			int y = marginHeight + advHeight;
			int i = 0;
			for (String strLine : lines)
			{
				y += fontSize;
				c.drawText(strLine, marginWidth, y, mPaint);
				y += spaceSize;
				if (i != lines.size() - 1)
				{
					c.drawText("", marginWidth, y, spactPaint);
				}
				i++;
			}
		}
		float fPercent = (float) (mbBufBegin * 1.0 / size);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";

		curProgress = (int) round1(fPercent * 100, 0);
		int nPercentWidth = (int) bPaint.measureText("99.9%") + 1;
		c.drawText(strPercent, w - nPercentWidth, h - 5, bPaint);

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		c.drawText(str, 5, h - 5, bPaint);
		int titleWidth = (int) bPaint.measureText("《" + fileName + "》") + 1;
		c.drawText("《" + fileName + "》", (w - titleWidth) / 2, h - 5, bPaint);
	}

	private void setCover(Canvas c)
	{
		String coverName=  fileName ;
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextAlign(Align.CENTER);
		p.setTextSize(70);
		p.setColor(textColor);
		if (bg == null) c.drawColor(backColor);
		else c.drawBitmap(bg, 0, 0, null);
		c.drawText(coverName, w/2, h/2, p);
		
	}


	private static double round1(double v, int scale)
	{
		if (scale < 0) return v;
		String temp = "#####0.";
		for (int i = 0; i < scale; i++)
		{
			temp += "0";
		}
		return Double.valueOf(new java.text.DecimalFormat(temp).format(v));
	}

	public void setBgBitmap(Bitmap BG)
	{
		if (BG.getWidth() != w || BG.getHeight() != h) bg = Bitmap.createScaledBitmap(BG, w, h, true);
		else bg = BG;
	}

	public boolean isfirstPage()
	{
		return isfirstPage;
	}

	public void setIslastPage(boolean islast)
	{
		islastPage = islast;
	}

	public boolean islastPage()
	{
		return islastPage;
	}

	public int getCurPostion()
	{
		return mbBufEnd;
	}

	public int getCurPostionBeg()
	{
		return mbBufBegin;
	}

	public void setBeginPos(int pos)
	{
		mbBufEnd = pos;
		mbBufBegin = pos;
	}

	public int getBufLen()
	{
		return size;
	}

	public int getCurProgress()
	{
		return curProgress;
	}

	public String getOneLine()
	{
		return lines.toString().substring(0, 10);
	}

	public void changBackGround(int color)
	{
		mPaint.setColor(color);
	}

	public void setFontSize(int size)
	{
		fontSize = size;
		mPaint.setTextSize(size);
		int totalSize = fontSize + spaceSize;
		lineCount = (int) (visibleHeight / totalSize); // 可显示的行数
	}

	public void setFileName(String fileName)
	{
		fileName = fileName.substring(0, fileName.indexOf("."));
		this.fileName = fileName;
	}

}
