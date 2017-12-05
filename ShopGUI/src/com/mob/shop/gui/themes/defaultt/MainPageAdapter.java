package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.WindowManager;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.tabs.Tab;
import com.mob.shop.gui.themes.defaultt.entity.DrawerCondition;
import com.mob.tools.gui.MobDrawerLayout;

public class MainPageAdapter extends DefaultThemePageAdapter<MainPage> implements MainPageBody.OnTabChangeListener,
		MainPageBody.OnRequestDrawerListener, MainPageDrawer.OnDrawerResultListener {
	private MobDrawerLayout drawer;
	private OnRequestProductsListener onRequestProductsListener;
	private MainPageBody mainPageBody;

	@Override
	public void onCreate(MainPage page, Activity activity) {
		super.onCreate(page, activity);
		// 软件盘覆盖View，不做resize
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.shopsdk_default_page_main);

		mainPageBody = new MainPageBody(getPage(), this);
		mainPageBody.setOnTabChangeListener(this);
		mainPageBody.setOnRequestDrawerListener(this);
		MainPageDrawer mainPageDrawer = new MainPageDrawer(getPage(), this);
		drawer = (MobDrawerLayout) activity.findViewById(R.id.shopsdk_main_page_drawer);
		drawer.setDrawerType(MobDrawerLayout.DrawerType.RIGHT_COVER);
		drawer.setBody(mainPageBody);
		drawer.setDrawer(mainPageDrawer);
	}

	@Override
	public void onTabChange(Tab tab) {
		if (tab instanceof AllProductTab) {
			drawer.setLockScroll(false);
		} else {
			if (drawer.isOpened()) {
				drawer.close();
			}
			drawer.setLockScroll(true);
		}
	}

	@Override
	public void onRequestDrawerOpen() {
		if (drawer != null && !drawer.isOpened()) {
			drawer.open();
		}
	}

	@Override
	public void onRequestDrawerClose() {
		if (drawer != null && drawer.isOpened()) {
			drawer.close();
		}
	}

	@Override
	public void onDrawerConfirm(DrawerCondition drawerCondition) {
		if (drawer != null && drawer.isOpened()) {
			drawer.close();
		}
		if (this.onRequestProductsListener != null) {
			this.onRequestProductsListener.onRequestProducts(drawerCondition);
		}
	}

	@Override
	public void onDrawerReset(DrawerCondition drawerCondition) {
		if (drawer != null && drawer.isOpened()) {
			drawer.close();
		}
		if (this.onRequestProductsListener != null) {
			this.onRequestProductsListener.onRequestProducts(drawerCondition);
		}
	}

	public void setOnRequestProductsListener(OnRequestProductsListener l) {
		this.onRequestProductsListener = l;
	}

	public interface OnRequestProductsListener {
		void onRequestProducts(DrawerCondition drawerCondition);
	}
}
