package com.mob.shop.gui.base;

import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.mob.shop.biz.ShopLog;
import com.mob.shop.gui.pages.dialog.ErrorDialog;
import com.mob.tools.utils.ResHelper;

public abstract class PageAdapter<P extends Page<P>> {
	private P page;

	final void setPage(P page) {
		this.page = page;
	}

	public void onCreate(P page, Activity activity) {

	}

	public void onStart(P page, Activity activity) {

	}

	public void onPause(P page, Activity activity) {

	}

	public void onResume(P page, Activity activity) {

	}

	public void onStop(P page, Activity activity) {

	}

	public void onRestart(P page, Activity activity) {

	}

	public void onDestroy(P page, Activity activity) {

	}

	public void onSetContentView(View view, P page, Activity activity) {

	}

	public P getPage() {
		return page;
	}

	public final void finish() {
		if (page != null) {
			page.finish();
		}
	}

	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		return false;
	}
}
