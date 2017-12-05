package com.mob.shop.gui.themes.defaultt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.MyOrdersPageAdapter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.BitmapProcessor;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjin on 2017/9/6.
 */

public class GoodDetailViewPagerAdapter extends ViewPagerAdapter {
	private List<ImgUrl> lists;
	private LayoutInflater layoutInflater;
	private ScreenChangeListener screenChangeListener;

	public GoodDetailViewPagerAdapter(Context context, List<ImgUrl> urls) {
		this.lists = urls;
		if(lists ==null){
			lists = new ArrayList<ImgUrl>();
		}
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = layoutInflater.inflate(R.layout.viewpage_item, null);
		AsyncImageView imageView = (AsyncImageView) view.findViewById(R.id.viewPagerItem);
		imageView.execute(lists.get(i).getSrc(), ResHelper.getColorRes(viewGroup.getContext(), "order_bg"));
		return view;
	}
	public void onScreenChange(int currentScreen, int lastScreen) {
		super.onScreenChange(currentScreen, lastScreen);
		if (screenChangeListener != null) {
			screenChangeListener.onScreenChange(currentScreen, lastScreen);
		}
	}
	public void setScreenChangeListener(ScreenChangeListener screenChangeListener) {
		this.screenChangeListener = screenChangeListener;
	}

	/**
	 * 页面滑动监听
	 */
	public interface ScreenChangeListener {
		void onScreenChange(int currentScreen, int lastScreen);
	}
}
