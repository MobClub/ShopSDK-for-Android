package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.CustomerServicePage;


/**
 * Created by yjin on 2017/9/12.
 */

public class CustomerServicePageAdapter	extends DefaultThemePageAdapter<CustomerServicePage> {

	private View view = null;
	private WebView listView = null;
	@Override
	public void onCreate(CustomerServicePage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_custom_service,null);
		listView = (WebView) view.findViewById(R.id.customListView);
	}
}
