package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.biz.ShopLog;
import com.mob.shop.biz.api.exception.ShopException;
import com.mob.shop.datatype.CouponStatus;
import com.mob.shop.datatype.builder.CouponQuerier;
import com.mob.shop.datatype.builder.ReceivableCouponQuerier;
import com.mob.shop.datatype.entity.Coupon;
import com.mob.shop.datatype.entity.ReceivableCoupons;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.MineCouponsPage;
import com.mob.shop.gui.pages.ReceiveCouponPage;
import com.mob.shop.gui.pages.dialog.ErrorDialog;
import com.mob.shop.gui.themes.defaultt.adapter.CouponListViewAdapter;
import com.mob.shop.gui.themes.defaultt.components.TabsView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;
import java.util.List;

/**
 * 我的优惠券页
 */

public class MineCouponsPageAdapter extends DefaultThemePageAdapter<MineCouponsPage> {
	private static final String TAG = MineCouponsPageAdapter.class.getSimpleName();
	private static final int DEFAULT_PAGE_SIZE = 20;

	private View view;
	private TabsView tabsView = null;
	private String[] tabs = new String[]{"shopsdk_default_my_coupon_unused",
			"shopsdk_default_my_coupon_expired", "shopsdk_default_my_coupon_used"};
	private TitleView titleView;
	private PullToRequestView listView;
	private CouponListViewAdapter listViewAdapter;
	private LinearLayout receivableCouponsLl;
	private TextView numTv;

	@Override
	public void onCreate(MineCouponsPage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_mine_coupons, null);
		activity.setContentView(view);
		titleView = (TitleView) view.findViewById(R.id.shop_gui_my_coupon_title_view);
		titleView.initTitleView(page, "shopsdk_default_my_coupon_title", null, null, true);
		tabsView = (TabsView) view.findViewById(R.id.tabhostView);
		tabsView.setDataRes(activity, tabs);
		tabsView.setOnTabClickListener(new MyTabClickListener());
		listView = (PullToRequestView) view.findViewById(R.id.shop_gui_my_coupon_list);
		CouponStatus couponStatus = getCouponStatus(tabsView == null ? tabsView.getCurrentPosition() : 0);
		listViewAdapter = new CouponListViewAdapter(listView, couponStatus);
		listViewAdapter.setEmptyRes(page.getContext(),"shopsdk_default_empty_coupon_img","shopsdk_default_empty_coupon_tip");
		listView.setAdapter(listViewAdapter);
		tabsView.performClick(tabsView == null ? tabsView.getCurrentPosition() : 0);
		receivableCouponsLl = (LinearLayout) view.findViewById(R.id.shop_gui_my_coupon_bottom_ll);
		receivableCouponsLl.setOnClickListener(new ReceivableCouponsClickListener());
		numTv = (TextView) view.findViewById(R.id.shop_gui_my_coupon_num_tv);

	}

	private class MyTabClickListener implements TabsView.OnTabClickListener {

		@Override
		public void onTabClick(int index) {
			listViewAdapter.resetPageIndex();
			listViewAdapter.clearData();
			listViewAdapter.shiftCouponStatus(getCouponStatus(index));
			ShopSDK.getCoupons(new CouponQuerier(DEFAULT_PAGE_SIZE, 1, getCouponStatus(index)),
					new SGUIOperationCallback<List<Coupon>>(getPage().getCallback()) {
				@Override
				public void onSuccess(List<Coupon> data) {
					super.onSuccess(data);
					listViewAdapter.increasePageIndex();
					listViewAdapter.setData(data);
					getReceivableCoupons();
				}

				@Override
				public boolean onResultError(Throwable t) {
					ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "getCoupons","onFailed. t=" + t);
					getPage().toastNetworkError();
					return super.onResultError(t);
				}
			});
		}
	}

	private class ReceivableCouponsClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			ReceiveCouponPage receiveCouponPage = new ReceiveCouponPage(getPage().getTheme());
			receiveCouponPage.showForResult(getPage().getContext(), null, new FakeActivity() {
				@Override
				public void onResult(HashMap<String, Object> data) {
					super.onResult(data);
					tabsView.performClick(tabsView == null ? tabsView.getCurrentPosition() : 0);
					getReceivableCoupons();
				}
			});
		}
	}

	private CouponStatus getCouponStatus(int tabPosition) {
		CouponStatus status;
		switch (tabPosition) {
			case 0: {
				status = CouponStatus.RECEIVED_AND_USABLE;
				break;
			}
			case 1: {
				status = CouponStatus.EXPIRED;
				break;
			}
			case 2:
			{
				status = CouponStatus.USED;
				break;
			}
			default: {
				status = CouponStatus.RECEIVED_AND_USABLE;
			}
		}
		return status;
	}

	private void getReceivableCoupons() {
		ReceivableCouponQuerier querier = new ReceivableCouponQuerier(DEFAULT_PAGE_SIZE, 1);
		ShopSDK.getReceivableCoupons(querier, new SGUIOperationCallback<ReceivableCoupons>(getPage().getCallback()) {
			@Override
			public void onSuccess(ReceivableCoupons data) {
				super.onSuccess(data);
				ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "getReceivableCoupons", "onSuccess. data=" + data== null ? data : data.toJSONString());
				if (data != null) {
					int count = data.getCount();
					if (count > 0) {
						receivableCouponsLl.setVisibility(View.VISIBLE);
						numTv.setText(String.valueOf(count));
					} else {
						receivableCouponsLl.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "getReceivableCoupons","onFailed. t=" + t);
				getPage().toastNetworkError();
				return super.onResultError(t);
			}
		});
	}
}
