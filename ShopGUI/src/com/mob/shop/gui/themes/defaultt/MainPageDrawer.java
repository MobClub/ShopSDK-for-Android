package com.mob.shop.gui.themes.defaultt;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.Label;
import com.mob.shop.datatype.entity.TransportStrategy;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.themes.defaultt.components.CollspanBarTitle;
import com.mob.shop.gui.themes.defaultt.entity.DrawerCondition;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weishj on 2017/10/26.
 */

public class MainPageDrawer extends LinearLayout {
	private static final String TAG = MainPageDrawer.class.getSimpleName();
	private static final int DEFAULT_SIZE = 10;
	private MainPage mainPage;
	private View view;
	private EditText minPrice;
	private EditText maxPrice;
	private LinearLayout strategyLl;
	private LinearLayout labelLl;
	private TextView reset;
	private TextView confirm;
	private OnDrawerResultListener onDrawerResult;
	private CollspanBarTitle strategyCollspanBar;
	private CollspanBarTitle labelCollspanBar;
	private long strategyId;
	private ArrayList<Long> labels;

	public MainPageDrawer(MainPage mainPage, OnDrawerResultListener l) {
		super(mainPage.getContext());
		this.mainPage = mainPage;
		this.onDrawerResult = l;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		view = LayoutInflater.from(mainPage.getContext()).inflate(R.layout.shopsdk_default_item_all_good_selected, null);
		addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		initView();
		getLabels();
		getStrategy();
	}

	private void initView() {
		minPrice = (EditText) view.findViewById(R.id.minEdPrices);
		maxPrice = (EditText) view.findViewById(R.id.highEdPrices);
		strategyLl = (LinearLayout) view.findViewById(R.id.shopsdk_drawer_content_strategy_ll);
		labelLl = (LinearLayout) view.findViewById(R.id.shopsdk_drawer_content_labels_ll);
		reset = (TextView) view.findViewById(R.id.shopsdk_drawer_reset);
		reset.setOnClickListener(new MyResetClickListener());
		confirm = (TextView) view.findViewById(R.id.shopsdk_drawer_confirm);
		confirm.setOnClickListener(new MyConfirmClickListener());
	}

	private void getLabels() {
		ShopSDK.getLabels(DEFAULT_SIZE, new OperationCallback<List<Label>>() {
			@Override
			public void onSuccess(List<Label> data) {
				super.onSuccess(data);
				showLabelsUI(data);
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				mainPage.toastNetworkError();
			}
		});
	}

	private void getStrategy() {
		ShopSDK.getTransportStrategy(new OperationCallback<List<TransportStrategy>>() {
			@Override
			public void onSuccess(List<TransportStrategy> data) {
				super.onSuccess(data);
				showStrategyUI(data);
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				mainPage.toastNetworkError();
			}
		});
	}

	private void showStrategyUI(List<TransportStrategy> data) {
		if (data != null && !data.isEmpty()) {
			TextView strategyTitle = new TextView(getContext());
			String name = getResources().getString(ResHelper.getStringRes(getContext(), "shopsdk_drawer_strategy"));
			strategyTitle.setText(name);
			strategyTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			strategyTitle.setTextColor(Color.parseColor("#000000"));
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 15);
			strategyTitle.setLayoutParams(params);

			strategyCollspanBar = new CollspanBarTitle(getContext());
			strategyCollspanBar.setTextColorNor(0xFF000000);
			strategyCollspanBar.setTextSize(ResHelper.dipToPx(getContext(), 13));
			strategyCollspanBar.setVerPadding(ResHelper.dipToPx(getContext(), 8));
			strategyCollspanBar.setHorPadding(ResHelper.dipToPx(getContext(), 15));
			strategyCollspanBar.setHorInterval(ResHelper.dipToPx(getContext(), 15));
			strategyCollspanBar.setBgResoureNor(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_grey_bg"));
			strategyCollspanBar.setBgResoureUnSel(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_grey_bg"));
			strategyCollspanBar.setBgResoureSel(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_orange_bg"));
			strategyCollspanBar.setTextColorUnSel(0xFFD0D0D0);
			List<GoodSelectedTypeItem> strategies = new ArrayList<GoodSelectedTypeItem>();
			GoodSelectedTypeItem item;
			for (TransportStrategy cell : data) {
				item = new GoodSelectedTypeItem(String.valueOf(cell.getStrategyId()), cell.getStrategyName());
				strategies.add(item);
			}
			strategyCollspanBar.addItemViews(name, strategies);
			strategyCollspanBar.setGroupClickListener(name, new CollspanBarTitle.OnGroupItemClickListener() {
				@Override
				public void onGroupItemClick(String type, int itemPos, String key, String value) {
					strategyId = Long.parseLong(key);
				}

				@Override
				public void unSelected(String type, int itemPos, String key, String value) {
					long keyLong = Long.parseLong(key);
					if (strategyId == keyLong) {
						strategyId = 0;
					}
				}
			});
			strategyLl.addView(strategyTitle);
			strategyLl.addView(strategyCollspanBar);
		}
	}

	private void showLabelsUI(final List<Label> data) {
		if (data != null && !data.isEmpty()) {
			TextView labelTitle = new TextView(getContext());
			String name = getResources().getString(ResHelper.getStringRes(getContext(), "shopsdk_drawer_label"));
			labelTitle.setText(name);
			labelTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			labelTitle.setTextColor(Color.parseColor("#000000"));
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 15);
			labelTitle.setLayoutParams(params);

			labelCollspanBar = new CollspanBarTitle(getContext(), true);
			labelCollspanBar.setTextColorNor(0xFF000000);
			labelCollspanBar.setTextSize(ResHelper.dipToPx(getContext(), 13));
			labelCollspanBar.setVerPadding(ResHelper.dipToPx(getContext(), 8));
			labelCollspanBar.setHorPadding(ResHelper.dipToPx(getContext(), 15));
			labelCollspanBar.setHorInterval(ResHelper.dipToPx(getContext(), 15));
			labelCollspanBar.setBgResoureNor(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_grey_bg"));
			labelCollspanBar.setBgResoureUnSel(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_grey_bg"));
			labelCollspanBar.setBgResoureSel(ResHelper.getBitmapRes(getContext(), "shopsdk_default_round_orange_bg"));
			labelCollspanBar.setTextColorUnSel(0xFFD0D0D0);
			final List<GoodSelectedTypeItem> items = new ArrayList<GoodSelectedTypeItem>();
			GoodSelectedTypeItem item;
			for (Label cell : data) {
				item = new GoodSelectedTypeItem(String.valueOf(cell.getLabelId()), cell.getLabelName());
				items.add(item);
			}
			labelCollspanBar.addItemViews(name, items);
			labelCollspanBar.setGroupClickListener(name, new CollspanBarTitle.OnGroupItemClickListener() {
				@Override
				public void onGroupItemClick(String type, int itemPos, String key, String value) {
					long labelId = Long.parseLong(key);
					if (labels == null) {
						labels = new ArrayList<Long>();
					}
					if (!labels.contains(labelId)) {
						labels.add(labelId);
					}
				}

				@Override
				public void unSelected(String type, int itemPos, String key, String value) {
					long labelId = Long.parseLong(key);
					if (labels == null) {
						labels = new ArrayList<Long>();
					}
					if (labels.contains(labelId)) {
						labels.remove(labelId);
					}
				}
			});
			labelLl.addView(labelTitle);
			labelLl.addView(labelCollspanBar);
		}
	}

	private class MyResetClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			minPrice.setText("");
			maxPrice.setText("");
			if (strategyCollspanBar != null) {
				strategyCollspanBar.clearItemsStyle();
				strategyId = 0;
			}
			if (labelCollspanBar != null) {
				labelCollspanBar.clearItemsStyle();
				labels = null;
			}
			if (onDrawerResult != null) {
				onDrawerResult.onDrawerReset(new DrawerCondition(0, 0, 0, null));
			}
		}
	}

	private class MyConfirmClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int min = MoneyConverter.toCent(TextUtils.isEmpty(minPrice.getText().toString()) ? "0" : minPrice.getText().toString());
			int max = MoneyConverter.toCent(TextUtils.isEmpty(maxPrice.getText().toString()) ? "0" : maxPrice.getText().toString());
			DrawerCondition drawerCondition = new DrawerCondition(min, max, strategyId, labels);
			if (onDrawerResult != null) {
				onDrawerResult.onDrawerConfirm(drawerCondition);
			}
		}
	}

	public interface OnDrawerResultListener {
		void onDrawerConfirm(DrawerCondition drawerCondition);
		void onDrawerReset(DrawerCondition drawerCondition);
	}
}
