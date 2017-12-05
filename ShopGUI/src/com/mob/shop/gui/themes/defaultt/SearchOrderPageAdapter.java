package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.shop.gui.pages.RefundListPage;
import com.mob.shop.gui.pages.SearchOrderPage;
import com.mob.shop.gui.pages.SearchOrderResultPage;
import com.mob.shop.gui.themes.defaultt.components.CollspanBarTitle;
import com.mob.shop.gui.themes.defaultt.components.searchbar.SearchBar;
import com.mob.shop.gui.themes.defaultt.components.searchbar.impl.InputResultCallBack;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;
import com.mob.shop.gui.utils.SGUISPDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 搜索订单页
 */
public class SearchOrderPageAdapter extends PageAdapter<SearchOrderPage> implements View.OnClickListener {
	public static final String EXTRA_ORIGIN_PAGE = "extra_origin_page";
	public static final int PAGE_PRODUCT_LIST = 1;
	public static final int PAGE_REFUND_LIST = 2;
	public static final String EXTRA_SEARCH_KEYWORD = "extra_search_keyword";
	private SearchBar searchBar;
	private CollspanBarTitle history;
	private ImageView ivDelete;
	private int originPage = -1;

	@Override
	public void onCreate(final SearchOrderPage page, Activity activity) {
		super.onCreate(page, activity);
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0x50000000));
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_searchorder, null);
		activity.setContentView(view);

		Intent i = activity.getIntent();
		if (i != null) {
			originPage = i.getIntExtra(EXTRA_ORIGIN_PAGE, -1);
		}

		searchBar = (SearchBar) view.findViewById(R.id.searchBar);
		history = (CollspanBarTitle) view.findViewById(R.id.history);
		ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

		ivDelete.setOnClickListener(this);

		searchBar.setActivity(activity);
		searchBar.setInputResultCallBack(new InputResultCallBack() {

			@Override
			public void inputOnResult(String res) {
				cacheSearch(res);
				getSearchHistory();
				if (originPage == PAGE_PRODUCT_LIST) {
					HashMap<String, Object> keywordMap = new HashMap<String, Object>();
					keywordMap.put(EXTRA_SEARCH_KEYWORD, res);
					getPage().setResult(keywordMap);
					getPage().finish();
				} else if (originPage == PAGE_REFUND_LIST) {
					RefundListPage refundListPage = new RefundListPage(page.getTheme(), res);
					refundListPage.show(page.getContext(), null);
					finish();
				} else {
					SearchOrderResultPage resultPage = new SearchOrderResultPage(page.getTheme(), res);
					resultPage.show(page.getContext(), null);
					finish();
				}
			}

			@Override
			public void requestSearch() {

			}
		});

		getSearchHistory();
		history.setSelector(false);
		history.setGroupClickListener("", new CollspanBarTitle.OnGroupItemClickListener() {
			@Override
			public void onGroupItemClick(String type, int itemPos, String key, String value) {
				searchBar.setText(value);
				if (originPage == PAGE_PRODUCT_LIST) {
					HashMap<String, Object> keywordMap = new HashMap<String, Object>();
					keywordMap.put(EXTRA_SEARCH_KEYWORD, value);
					getPage().setResult(keywordMap);
					getPage().finish();
				} else if (originPage == PAGE_REFUND_LIST) {
					RefundListPage refundListPage = new RefundListPage(page.getTheme(), value);
					refundListPage.show(page.getContext(), null);
					finish();
				} else {
					SearchOrderResultPage resultPage = new SearchOrderResultPage(page.getTheme(), value);
					resultPage.show(page.getContext(), null);
					finish();
				}
			}

			@Override
			public void unSelected(String type, int itemPos, String key, String value) {
			}
		});
	}


	private void cacheSearch(String search) {
		if (originPage == PAGE_PRODUCT_LIST) {
			SGUISPDB.setProductSearchHistory(new GoodSelectedTypeItem(search, search));
		} else if (originPage == PAGE_REFUND_LIST) {
			SGUISPDB.setSearchRefundHistory(new GoodSelectedTypeItem(search, search));
		} else {
			SGUISPDB.setSearchHistory(new GoodSelectedTypeItem(search, search));
		}
	}

	private void getSearchHistory() {
		ArrayList<GoodSelectedTypeItem> list;
		if (originPage == PAGE_PRODUCT_LIST) {
			list = SGUISPDB.getProductSearchHistory();
		} else if (originPage == PAGE_REFUND_LIST) {
			list = SGUISPDB.getSearchRefundHistory();
		} else {
			list = SGUISPDB.getSearchHistory();
		}
		if (list != null) {
			history.addItemViews("", list);
		}
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivDelete) {
			if (originPage == PAGE_PRODUCT_LIST) {
				SGUISPDB.clearProductSearchHistory();
			} else if (originPage == PAGE_REFUND_LIST) {
				SGUISPDB.clearSearchRefundHistory();
			} else {
				SGUISPDB.clearSearchHistory();
			}
			history.removeAllViews();
		}
	}
}
