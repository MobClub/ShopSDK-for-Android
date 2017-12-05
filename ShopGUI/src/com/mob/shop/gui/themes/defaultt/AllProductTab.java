package com.mob.shop.gui.themes.defaultt;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mob.shop.biz.ShopLog;
import com.mob.shop.datatype.PriceSort;
import com.mob.shop.datatype.SalesSort;
import com.mob.shop.datatype.TimeSort;
import com.mob.shop.datatype.builder.ProductQuerier;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.pages.SearchOrderPage;
import com.mob.shop.gui.tabs.Tab;
import com.mob.shop.gui.themes.defaultt.adapter.ProductGridViewAdapter;
import com.mob.shop.gui.themes.defaultt.adapter.ProductListBaseAdapter;
import com.mob.shop.gui.themes.defaultt.adapter.ProductListViewAdapter;
import com.mob.shop.gui.themes.defaultt.components.AllProductTabSelectedConfig;
import com.mob.shop.gui.themes.defaultt.components.NormalTextView;
import com.mob.shop.gui.themes.defaultt.components.PricesTextView;
import com.mob.shop.gui.themes.defaultt.components.ShowModeImageView;
import com.mob.shop.gui.themes.defaultt.components.searchbar.FadeInTransition;
import com.mob.shop.gui.themes.defaultt.components.searchbar.MainSearchBar;
import com.mob.shop.gui.themes.defaultt.entity.DrawerCondition;
import com.mob.shop.gui.utils.Const;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllProductTab implements Tab, View.OnClickListener, ShowModeImageView.ShowModeGridList {

	private static final String TAG = AllProductTab.class.getSimpleName();
	private PullToRequestView goodList;
	private View mainView;
	private ProductListBaseAdapter listViewAdapter;
	private NormalTextView salesCounts;
	private NormalTextView newGoods;
	private PricesTextView goodPrices;
	private ShowModeImageView showModes;
	private TextView selectedMode;
	private MainSearchBar searchGood;
	private TextView searchTv;
	private MainPage mainPage;
	private MainPageAdapter mainPageAdapter;
	private ProductQuerier querier;

	private AllProductTabSelectedConfig tabConfig = new AllProductTabSelectedConfig();
	private boolean salesSorting = false;
	private boolean timeSorting = false;
	private String keyword;
	private int minPrice;
	private int maxPrice;
	private long transportStrategyId;
	private List<Long> labels;
	private boolean forceSalesSort = false;
	private OnProductTabEventListener onProductTabEventListener;
	private ProductListBaseAdapter.OnItemClickListener onItemClickListener = new MyListItemClickListener();

	public AllProductTab(MainPage mainPage, MainPageAdapter mainPageAdapter) {
		this.mainPage = mainPage;
		this.mainPageAdapter = mainPageAdapter;
		this.mainPageAdapter.setOnRequestProductsListener(new MyRequestProductsListener());
	}

	@Override
	public String getSelectedIconResName() {
		return "shopsdk_default_orange_product";
	}

	@Override
	public String getUnselectedIconResName() {
		return "shopsdk_default_grey_product";
	}

	@Override
	public String getTitleResName() {
		return "shopsdk_default_allproduct";
	}

	@Override
	public String getSelectedTitleColor() {
		return "select_tab";
	}

	@Override
	public String getUnselectedTitleColor() {
		return "unselect_tab";
	}

	@Override
	public View getTabView(final Context context) {
		if (mainView == null) {
			mainView = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_tab_allproduct, null);
			goodList = (PullToRequestView) mainView.findViewById(R.id.shopsdk_tab_all_product_lv);
			listViewAdapter = new ProductListViewAdapter(goodList, mainPageAdapter);
			listViewAdapter.setOnItemClickListener(onItemClickListener);
			goodList.setAdapter(listViewAdapter);
			salesCounts = (NormalTextView) mainView.findViewById(R.id.salesCountsSelected);
			salesCounts.setOnClickListener(this);
			newGoods = (NormalTextView) mainView.findViewById(R.id.newGood);
			newGoods.setOnClickListener(this);
			goodPrices = (PricesTextView) mainView.findViewById(R.id.prices);
			goodPrices.setOnClickListener(new PricesRequest());
			goodPrices.setConfig(new AllProductTabSelectedConfig(newGoods, salesCounts));
			showModes = (ShowModeImageView) mainView.findViewById(R.id.showMode);
			showModes.setOnClickListener(this);
			showModes.setShowModeGridList(this);
			selectedMode = (TextView) mainView.findViewById(R.id.all_tab_filter);
			selectedMode.setOnClickListener(this);
			searchTv = (TextView) mainView.findViewById(R.id.shopsdk_all_product_search_tv);
			searchGood = (MainSearchBar) mainView.findViewById(R.id.main_toolbar);
			searchGood.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SearchOrderPage searchOrderPage = new SearchOrderPage(mainPage.getTheme());
					Intent i = new Intent();
					i.putExtra(SearchOrderPageAdapter.EXTRA_ORIGIN_PAGE, SearchOrderPageAdapter.PAGE_PRODUCT_LIST);
					searchOrderPage.showForResult(mainPage.getContext(), i, new FakeActivity() {
						@Override
						public void onResult(HashMap<String, Object> data) {
							super.onResult(data);
							if (data != null && !data.isEmpty()) {
								String inputted = (String) data.get(SearchOrderPageAdapter.EXTRA_SEARCH_KEYWORD);
								if (inputted != null && !inputted.equals(keyword)) {
									keyword = inputted;
									searchTv.setText(keyword);
									// Perform Sales button to load search result, SalesSort.DESC by default
									forceSalesSort = true;
									salesCounts.performClick();
								}
							} else {
								/*
								 * 'data' is null when back to AllProductPage from SearchOrderPage by clicking 'BACK' button of the phone
								 */
								if (!TextUtils.isEmpty(keyword)) {
									keyword = null;
									// Perform Sales button to load search result, SalesSort.DESC by default
									forceSalesSort = true;
									salesCounts.performClick();
									searchTv.setText("");
								}
							}
						}
					});
				}
			});
			/* Load product list data, SalesSort.DESC by default(Only reload data when app first launched, do not reload data
			 * when tab shifting
			 */
//			salesCounts.performClick();
		}

		// Reset query conditions when first come into AllProductTab or back from other tabs
		resetQueryConditions();
		// Update UI
		searchTv.setText("");
		// Load product list data, SalesSort.DESC by default
		salesCounts.performClick();
		fadeToolbarIn(context);

		return mainView;
	}

	@Override
	public void onSelected() {
		ShopLog.getInstance().d(TAG, "onSelected");
	}

	@Override
	public void onUnselected() {
		ShopLog.getInstance().d(TAG, "onUnselected");
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.salesCountsSelected) {
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "onClick", "SalesSort clicked");
			if (!salesSorting || forceSalesSort) {
				forceSalesSort = false;
				salesSorting = true;
				timeSorting = false;
				// Update UI
				tabConfig.changeSales(newGoods, goodPrices);
				// Refresh product list data
				if (listViewAdapter != null) {
					querier = initDefaultQuerier();
					listViewAdapter.queryProduct(querier);
				}
			}
		} else if (id == R.id.newGood) {
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "onClick", "TimeSort clicked");
			if (!timeSorting) {
				timeSorting = true;
				salesSorting = false;
				// Update UI
				tabConfig.changeNew(salesCounts, goodPrices);
				// Refresh product list data
				if (listViewAdapter != null) {
					querier = initTimeSortQuerier();
					listViewAdapter.queryProduct(querier);
				}
			}
		} else if (id == R.id.showMode) {
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "onClick", "Shift showMode clicked");
			/* Do not re-request data when shift show mode between grid and list */
//			listViewAdapter.queryProduct(querier);
		} else if (id == R.id.all_tab_filter) {
			if (onProductTabEventListener != null) {
				onProductTabEventListener.onFilterClicked();
			}
		}
	}

	/**
	 * GridView展示形式
	 */
	@Override
	public void showGridView() {
		listViewAdapter = newAdapterInstanceWithCurrentAdapterStatus(ProductGridViewAdapter.class);
		goodList.setAdapter(listViewAdapter);
	}

	/**
	 * ListView展示形式
	 */
	@Override
	public void showListView() {
		listViewAdapter = newAdapterInstanceWithCurrentAdapterStatus(ProductListViewAdapter.class);
		goodList.setAdapter(listViewAdapter);
	}

	private ProductListBaseAdapter newAdapterInstanceWithCurrentAdapterStatus(Class<? extends ProductListBaseAdapter> clazz) {
		List<Product> currentData = new ArrayList<Product>();
		int currentIndex = 0;
		// Store current adapter status
		if (listViewAdapter != null) {
			currentIndex = listViewAdapter.getCurrentPageIndex();
			currentData = listViewAdapter.getData();
		}
		// Create a new adapter instance
		ProductListBaseAdapter newAdapter = clazz == ProductListViewAdapter.class ? new ProductListViewAdapter(goodList, mainPageAdapter) : new ProductGridViewAdapter(goodList, mainPageAdapter);
		// Restore current adapter status to the new adapter instance
		newAdapter.setQuerier(querier);
		newAdapter.setPageIndex(currentIndex);
		newAdapter.setData(currentData);

		return newAdapter;
	}

	public void setOnProductTabEventListener(OnProductTabEventListener l) {
		this.onProductTabEventListener = l;
	}

	private void resetQueryConditions() {
		salesSorting = false;
		timeSorting = false;
		keyword = null;
		forceSalesSort = false;
	}

	private void fadeToolbarIn(Context context) {
		if (Build.VERSION.SDK_INT >= 19) {
			TransitionManager.beginDelayedTransition(searchGood, FadeInTransition.createTransition());
		}
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) searchGood.getLayoutParams();
		int margin = ResHelper.dipToPx(context, 12);
		layoutParams.setMargins(margin * 2, margin, margin * 2, margin);
		searchGood.showContent();
		searchGood.setLayoutParams(layoutParams);
	}

	/**
	 * 根据价格获取不同列表
	 */
	class PricesRequest implements PricesTextView.PriceSelector {

		@Override
		public void onAsc(View view) {
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "PricesRequest", "onAsc");
			timeSorting = false;
			salesSorting = false;
			// Refresh product list data
			if (listViewAdapter != null) {
				querier = initPriceSortQuerier(PriceSort.ASC);
				listViewAdapter.queryProduct(querier);
			}
		}

		@Override
		public void onDesc(View view) {
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "PricesRequest", "onDesc");
			timeSorting = false;
			salesSorting = false;
			// Refresh product list data
			if (listViewAdapter != null) {
				querier = initPriceSortQuerier(PriceSort.DESC);
				listViewAdapter.queryProduct(querier);
			}
		}

		@Override
		public void onClick(View v) {
			// Nothing to do
		}
	}

	private class MyListItemClickListener implements ProductListBaseAdapter.OnItemClickListener {
		@Override
		public void onItemClick(int i) {
			if (onProductTabEventListener != null) {
				onProductTabEventListener.onListItemClicked(i);
			}
		}
	}

	private SalesSort defaultSalesSortWithUI() {
		newGoods.reset();
		goodPrices.reset();
		return SalesSort.DESC;
	}

	private TimeSort defaultTimeSortWithUI() {
		salesCounts.reset();
		goodPrices.reset();
		return TimeSort.DESC;
	}

	private PriceSort priceSortWithUI(PriceSort priceSort) {
		salesCounts.reset();
		newGoods.reset();
		return priceSort;
	}

	private ProductQuerier initDefaultQuerier() {
		return new ProductQuerier(Const.DEFAULT_PAGE_SIZE, 1, keyword, minPrice, maxPrice, transportStrategyId, labels,
				TimeSort.DISABLE, PriceSort.DISABLE, SalesSort.DESC);
	}

	private ProductQuerier initTimeSortQuerier() {
		return new ProductQuerier(Const.DEFAULT_PAGE_SIZE, 1, keyword, minPrice, maxPrice, transportStrategyId, labels,
				TimeSort.DESC, PriceSort.DISABLE, SalesSort.DISABLE);
	}

	private ProductQuerier initPriceSortQuerier(PriceSort priceSort) {
		return new ProductQuerier(Const.DEFAULT_PAGE_SIZE, 1, keyword, minPrice, maxPrice, transportStrategyId, labels,
				TimeSort.DISABLE, priceSort, SalesSort.DISABLE);
	}

	private class MyRequestProductsListener implements MainPageAdapter.OnRequestProductsListener {
		@Override
		public void onRequestProducts(DrawerCondition drawerCondition) {
			if (drawerCondition != null) {
				minPrice = drawerCondition.minPrice;
				maxPrice = drawerCondition.maxPrice;
				transportStrategyId = drawerCondition.transportStrategyId;
				labels = drawerCondition.labelList;
				querier = initDefaultQuerier();
				if (listViewAdapter != null) {
					listViewAdapter.queryProduct(querier);
				}
			}
		}
	}

	public interface OnProductTabEventListener {
		void onListItemClicked(int i);
		void onFilterClicked();
	}
}
