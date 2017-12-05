package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.tabs.Tab;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

/**
 * Created by weishj on 2017/10/26.
 */

public class MainPageBody extends LinearLayout implements AllProductTab.OnProductTabEventListener {
	private MainPage mainPage;
	private MainPageAdapter mainPageAdapter;
	private View view;
	private LinearLayout llBody;
	private LinearLayout llTab;
	private ArrayList<Tab> tabs = new ArrayList<Tab>();
	private int selected = -1;
	private OnTabChangeListener onTabChangeListener;
	private OnRequestDrawerListener onRequestDrawerListener;

	public MainPageBody(MainPage mainPage, MainPageAdapter mainPageAdapter) {
		super(mainPage.getContext());
		this.mainPage = mainPage;
		this.mainPageAdapter = mainPageAdapter;
	}

	public void setOnTabChangeListener(OnTabChangeListener l) {
		this.onTabChangeListener = l;
	}

	public void setOnRequestDrawerListener(OnRequestDrawerListener l) {
		this.onRequestDrawerListener = l;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		view = LayoutInflater.from(mainPage.getContext()).inflate(R.layout.shopsdk_default_page_main_body, null);
		addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		initView();
		initTabs();
		changeTab(0);
	}

	private void initView() {
		llBody = (LinearLayout) view.findViewById(R.id.llBody);
		llTab = (LinearLayout) view.findViewById(R.id.llTab);
	}

	private void initTabs() {
		AllProductTab allProductTab = new AllProductTab(mainPage, mainPageAdapter);
		allProductTab.setOnProductTabEventListener(this);
		tabs.add(allProductTab);
		tabs.add(new CartTab(mainPage, false));
		tabs.add(new MineTab(mainPage));

		for (int i = 0; i < tabs.size(); i++) {
			LinearLayout view = (LinearLayout) LayoutInflater.from(mainPage.getContext()).inflate(R.layout
					.shopsdk_default_item_tab, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			view.setLayoutParams(lp);
			ImageView ivTab = (ImageView) view.findViewById(R.id.ivTab);
			TextView tvTab = (TextView) view.findViewById(R.id.tvTab);
			ivTab.setImageResource(ResHelper.getBitmapRes(mainPage.getContext(), tabs.get(i).getUnselectedIconResName
					()));
			tvTab.setText(mainPage.getContext().getResources().getString(ResHelper.getStringRes(mainPage.getContext(),
					tabs.get(i).getTitleResName())));
			tvTab.setTextColor(mainPage.getContext().getResources().getColor(ResHelper.getColorRes(mainPage.getContext
					(), tabs.get(i).getUnselectedTitleColor())));
			llTab.addView(view);
			final int finalI = i;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					changeTab(finalI);
				}
			});
		}
	}

	private void changeTab(int change) {
		if (selected == change) {
			return;
		}

		Tab tab = tabs.get(change);
		llBody.removeAllViews();
		llBody.addView(tab.getTabView(mainPage.getContext()), new LinearLayout.LayoutParams(LinearLayout.LayoutParams
				.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

		if (selected != -1) {
			tabs.get(selected).onUnselected();
		}
		tab.onSelected();
		if (this.onTabChangeListener != null) {
			onTabChangeListener.onTabChange(tab);
		}
		if (selected >= 0) {
			setTab(selected, false);
		}
		setTab(change, true);

		selected = change;
	}

	private void setTab(int index, boolean select) {
		Tab tab = tabs.get(index);
		LinearLayout llselected = (LinearLayout) llTab.getChildAt(index);
		ImageView ivLogo = (ImageView) llselected.getChildAt(0);
		TextView tvTitle = (TextView) llselected.getChildAt(1);
		int resId = ResHelper.getBitmapRes(mainPage.getContext(), select ? tab.getSelectedIconResName() : tab
				.getUnselectedIconResName());
		ivLogo.setImageResource(resId);
		tvTitle.setTextColor(mainPage.getContext().getResources().getColor(ResHelper.getColorRes(mainPage.getContext()
				, select ? tab.getSelectedTitleColor() : tab.getUnselectedTitleColor())));
	}

	public int getSelected() {
		return selected;
	}

	@Override
	public void onFilterClicked() {
		if (this.onRequestDrawerListener != null) {
			this.onRequestDrawerListener.onRequestDrawerOpen();
		}
	}

	@Override
	public void onListItemClicked(int i) {
		if (this.onRequestDrawerListener != null) {
			this.onRequestDrawerListener.onRequestDrawerClose();
		}
	}

	public interface OnTabChangeListener {
		void onTabChange(Tab tab);
	}

	public interface OnRequestDrawerListener {
		void onRequestDrawerOpen();

		void onRequestDrawerClose();
	}
}
