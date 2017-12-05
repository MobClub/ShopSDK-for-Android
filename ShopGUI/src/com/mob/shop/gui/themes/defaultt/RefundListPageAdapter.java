package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.builder.RefundQuerier;
import com.mob.shop.datatype.entity.Refund;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.RefundListPage;
import com.mob.shop.gui.pages.SearchOrderPage;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.RefundView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.gui.PullToRequestView;

import java.util.ArrayList;
import java.util.List;

/**
 * 退货售后列表页
 */
public class RefundListPageAdapter extends DefaultThemePageAdapter<RefundListPage> {
	private PullToRequestView listView;
	private RefundsAdapter adapter;
	private TitleView titleView;

	@Override
	public void onCreate(final RefundListPage page, Activity activity) {
		super.onCreate(page, activity);
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0x50000000));
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_refundlist, null);
		activity.setContentView(view);

		titleView = (TitleView) view.findViewById(R.id.titleView);
		listView = (PullToRequestView) view.findViewById(R.id.listView);
		if (TextUtils.isEmpty(page.getKeyword())) {
			titleView.initTitleView(page, "shopsdk_default_refund_list", "shopsdk_default_search", new View
					.OnClickListener() {
				@Override
				public void onClick(View v) {
					SearchOrderPage searchOrderPage = new SearchOrderPage(page.getTheme());
					Intent i = new Intent();
					i.putExtra(SearchOrderPageAdapter.EXTRA_ORIGIN_PAGE, SearchOrderPageAdapter.PAGE_REFUND_LIST);
					searchOrderPage.show(page.getContext(), i);
				}
			}, true, true);
		} else {
			titleView.initTitleView(page, "shopsdk_default_search_result", null, null, true);
		}

		adapter = new RefundsAdapter(page, listView, page.getKeyword());
		listView.setAdapter(adapter);
		listView.performPullingDown(true);
	}

	private static class RefundsAdapter extends DefaultRTRListAdapter {
		private List<Refund> refunds = new ArrayList<Refund>();
		private Page page;
		private static final int PAGE_SIZE = 20;
		private int pageIndex = 1;
		private String keyword = "";

		public RefundsAdapter(Page page, PullToRequestView view, String keyword) {
			super(view);
			this.page = page;
			this.keyword = keyword;
			getListView().setDividerHeight(0);
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				pageIndex = 1;
			}
			getRefunds();
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		@Override
		public View getView(int i, View convertView, final ViewGroup viewGroup) {
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
						.shopsdk_default_item_refund, null);
				vh = new ViewHolder();
				vh.refundView = (RefundView) convertView.findViewById(R.id.refundView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			Refund refund = refunds.get(i);
			vh.refundView.setRefundData(page, refund);
			return convertView;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Object getItem(int i) {
			return refunds.get(i);
		}

		@Override
		public int getCount() {
			return refunds == null ? 0 : refunds.size();
		}

		private void getRefunds() {
			RefundQuerier querier = new RefundQuerier(PAGE_SIZE, pageIndex, keyword);
			ShopSDK.getRefunds(querier, new SGUIOperationCallback<List<Refund>>(page.getCallback()) {
				@Override
				public void onSuccess(List<Refund> data) {
					if (pageIndex == 1) {
						refunds.clear();
					}

					refunds.addAll(data);
					notifyDataSetChanged();

					if (data == null || data.isEmpty()) {
						getParent().lockPullingUp();
					} else {
						pageIndex++;
						getParent().releasePullingUpLock();
					}

				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					getParent().stopPulling();
				}
			});
		}


		private class ViewHolder {
			private RefundView refundView;
		}
	}
}
