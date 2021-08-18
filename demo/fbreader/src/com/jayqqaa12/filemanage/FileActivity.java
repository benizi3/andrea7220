package com.jayqqaa12.filemanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.geometerplus.android.fbreader.FBReader;

import android.app.ListActivity;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayqqaa12.abase.core.AbaseDao;
import com.jayqqaa12.abase.util.IntentUtil;
import com.jayqqaa12.abase.util.common.T;
import com.jayqqaa12.abase.util.sys.SdCardUtil;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.engine.BookEngine;
import com.jayqqaa12.reader.model.Book;

@EActivity(R.layout.activity_file)
public class FileActivity extends ListActivity
{
	@ViewById
	TextView tv;
	@Bean
	BookEngine engine;
	private AbaseDao db = AbaseDao.create();
	private List<String> mFileName = new ArrayList<String>();
	private List<String> mFilePaths = new ArrayList<String>();
	private static final List<String> FILE_END = Arrays.asList(new String[] { "txt", "html", "epub", "oeb", "fb2", "mobi", "rtf" });
	public static String mCurrentFilePath = "";
	private String mSDCard = Environment.getExternalStorageDirectory().getPath();

	@AfterViews
	public void init()
	{
		String[] paths = SdCardUtil.getSdCardPath();
		if (paths != null) mSDCard = "/storage";
		initFileListInfo(mSDCard);

	}

	private void initFileListInfo(String filePath)
	{
		mFileName.clear();
		mFilePaths.clear();
		mCurrentFilePath = filePath;
		tv.setText(filePath);
		File[] files = new File(filePath).listFiles();
		if (!mCurrentFilePath.equals(mSDCard)) initAddBackUp(filePath, mSDCard);

		if (files == null)
		{
			T.ShortToast("sd 卡不可用");
			return;
		}
		for (File file : files)
		{
			if (file.isDirectory())
			{
				mFileName.add(file.getName());
				mFilePaths.add(file.getPath());
			}
			else
			{
				String fileEnd = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
				if (FILE_END.contains(fileEnd))
				{
					mFileName.add(file.getName());
					mFilePaths.add(file.getPath());
				}
			}
		}
		setListAdapter(new FileAdapter(FileActivity.this, mFileName, mFilePaths));
	}

	private void initAddBackUp(String filePath, String phone_sdcard)
	{
		if (!filePath.equals(phone_sdcard))
		{
			mFileName.add("BacktoUp");
			mFilePaths.add(new File(filePath).getParent());
		}
	}

	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
		final File f = new File(mFilePaths.get(position));
		if (f.canRead())
		{
			if (f.isDirectory()) initFileListInfo(mFilePaths.get(position));
			else
			{
				Book book = new Book(f);
				if (!db.isFindByWhere(Book.class, " path= '" + book.path+"'")) db.save(book);
				
				IntentUtil.startIntent(this, FBReader.class, FBReader.BOOK_KEY, f.getPath());

			}
		}
		else T.ShortToast("对不起，您的访问权限不足!");
	}

}
