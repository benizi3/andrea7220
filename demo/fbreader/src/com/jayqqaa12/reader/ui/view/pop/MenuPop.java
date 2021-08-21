package com.jayqqaa12.reader.ui.view.pop;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.jayqqaa12.abase.util.ManageUtil;
import com.jayqqaa12.abase.view.AbasePopup;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.MenuItem;
import com.jayqqaa12.reader.ui.adapter.MenuAdapter;

public class MenuPop extends AbasePopup implements OnItemClickListener
{
	MenuAdapter adapter ;

	@Override
	protected View initView()
	{
		View view = ManageUtil.getInflater().inflate(R.layout.test_menu, null);
		GridView gv = (GridView) view.findViewById(R.id.gv);
		adapter= new MenuAdapter();
		gv.setAdapter(adapter);
		this.setAnimationStyle(R.style.AnimBottom);
		gv.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FBReaderApp fbreaderApp = (FBReaderApp) FBReaderApp.Instance();
		
		switch (adapter.getItem(position).ation)
		{
		case MenuItem.ATION_TOC:
			this.dismiss();
			fbreaderApp.runAction(ActionCode.SHOW_TOC);
			break;
		case MenuItem.ATION_SETTING:
			this.dismiss();
			fbreaderApp.runAction(ActionCode.SHOW_PREFERENCES);
			break;
		case MenuItem.ATION_FONT_ADD:
			fbreaderApp.runAction(ActionCode.INCREASE_FONT);
			break;
		case MenuItem.ATION_FONT_DIM:
			fbreaderApp.runAction(ActionCode.DECREASE_FONT);
			break;
		case MenuItem.ATION_NIGHT:
			fbreaderApp.runAction(ActionCode.SWITCH_TO_NIGHT_PROFILE);
			break;
		case MenuItem.ATION_PROGRESS:
			this.dismiss();
			fbreaderApp.runAction(ActionCode.SHOW_NAVIGATION);
			break;

		}

	}

}
