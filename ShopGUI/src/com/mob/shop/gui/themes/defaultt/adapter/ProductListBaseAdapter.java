package com.mob.shop.gui.themes.defaultt.adapter;

import android.content.Context;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.builder.ProductQuerier;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.themes.defaultt.MainPageAdapter;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weishj on 2017/10/25.
 */

public abstract class ProductListBaseAdapter extends DefaultRTRListAdapter {
	protected Context context;
	protected List<Product> list = new ArrayList<Product>();
	protected int pageIndex = 1;
	protected ProductQuerier querier = new ProductQuerier();
	protected MainPageAdapter mainPageAdapter;
	protected OnItemClickListener onItemClickListener;
	protected String salesFormatter;

	public ProductListBaseAdapter(PullToRequestView view, MainPageAdapter mainPageAdapter) {
		super(view);
		this.mainPageAdapter = mainPageAdapter;
		this.context = mainPageAdapter.getPage().getContext();
		this.salesFormatter = context.getString(ResHelper.getStringRes(context, "shopsdk_default_all_product_sales_desc"));
	}

	@Override
	protected void onRequest(boolean firstPage) {
		if (firstPage) {
			resetPageIndex();
		}
		if (querier != null) {
			querier.pageIndex = pageIndex;
		}
		queryProduct(querier);
	}

	public void resetPageIndex() {
		pageIndex = 1;
	}

	public void increasePageIndex() {
		pageIndex ++;
	}

	public int getCurrentPageIndex() {
		return this.pageIndex;
	}

	public void setPageIndex(int index) {
		if (index >= 0) {
			this.pageIndex = index;
		}
	}

	public ProductQuerier getCurrentQuerier() {
		return querier;
	}

	/**
	 * Directly modify the querier used by this adapter.
	 * Notice: Do not use this method if not necessary, use queryProduct() to modify querier & update data
	 * @param querier
	 */
	public void setQuerier(ProductQuerier querier) {
		this.querier = querier;
	}

	public List<Product> getData() {
		return list;
	}

	public void setData(List<Product> data) {
		list = data;
		notifyDataSetChanged();
	}

	public void appendData(List<Product> data) {
		if (list == null) {
			list = new ArrayList<Product>();
		}
		list.addAll(data);
		notifyDataSetChanged();
	}

	public void clearData() {
		setData(null);
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		this.onItemClickListener = l;
	}

	public void queryProduct(ProductQuerier productQuerier) {
		if (productQuerier != null) {
			querier = productQuerier;
			pageIndex = querier.pageIndex;
			ShopSDK.getProducts(querier, new OperationCallback<List<Product>>() {
				@Override
				public void onSuccess(List<Product> data) {
					super.onSuccess(data);
					//TODO only for test
//					listProduct.addAll(RawResUtils.getProducts(mainPage.getContext()));
					if (pageIndex == 1) {
						setData(data);
					} else {
						appendData(data);
					}
					if (data == null || data.isEmpty()) {
						getParent().lockPullingUp();
					} else {
						increasePageIndex();
						getParent().releasePullingUpLock();
					}
				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					getParent().stopPulling();
					mainPageAdapter.getPage().toastNetworkError();
					//TODO only for test
//					onSuccess(RawResUtils.getProducts(getContext()));
				}
			});
		}
	}

	public interface OnItemClickListener {
		void onItemClick(int i);
	}
}
