package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.biz.ShopLog;
import com.mob.shop.datatype.CouponStatus;
import com.mob.shop.datatype.builder.CouponQuerier;
import com.mob.shop.datatype.builder.ReceivableCouponQuerier;
import com.mob.shop.datatype.entity.Coupon;
import com.mob.shop.datatype.entity.ReceivableCoupons;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.MineCouponsPage;
import com.mob.shop.gui.pages.ReceiveCouponPage;
import com.mob.shop.gui.themes.defaultt.adapter.CouponListViewAdapter;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.TabsView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.shop.gui.utils.Utils;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 领取优惠券页
 */

public class ReceiveCouponPageAdapter extends DefaultThemePageAdapter<ReceiveCouponPage> {
	private static final String TAG = ReceiveCouponPageAdapter.class.getSimpleName();
	private static final int DEFAULT_PAGE_SIZE = 20;

	private View view;
	private TitleView titleView;
	private PullToRequestView listView;
	private ReceivableCouponViewAdapter listViewAdapter;

	@Override
	public void onCreate(ReceiveCouponPage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_receive_coupon, null);
		activity.setContentView(view);
		titleView = (TitleView) view.findViewById(R.id.shopsdk_receive_coupon_title_view);
		titleView.initTitleView(page, "shopsdk_default_receive_coupon_title", null, null, true);
		listView = (PullToRequestView) view.findViewById(R.id.shopsdk_receive_coupon_list);
		listViewAdapter = new ReceivableCouponViewAdapter(listView);
		listView.setAdapter(listViewAdapter);
		getReceivableCoupons(1);
	}

	private void getReceivableCoupons(final int pageIndex) {
		ReceivableCouponQuerier querier = new ReceivableCouponQuerier(DEFAULT_PAGE_SIZE, pageIndex);
		ShopSDK.getReceivableCoupons(querier, new SGUIOperationCallback<ReceivableCoupons>(getPage().getCallback()) {
			@Override
			public void onSuccess(ReceivableCoupons data) {
				super.onSuccess(data);
				ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "getReceivableCoupons", "onSuccess. data=" + data== null ? data : data.toJSONString());
				if (data != null) {
					if (data.getList() == null || data.getList().isEmpty()) {
						listViewAdapter.lockPullingUp();
					} else {
						if (pageIndex == 1) {
							listViewAdapter.setData(data.getList());
						} else {
							listViewAdapter.appendData(data.getList());
						}
						listViewAdapter.increasePageIndex();
						listViewAdapter.releasePullingUpLock();
					}
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				listViewAdapter.stopPulling();
				ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "getReceivableCoupons","onFailed. t=" + t);
				getPage().toastNetworkError();
				return super.onResultError(t);
			}
		});
	}

	private class ReceivableCouponViewAdapter extends DefaultRTRListAdapter {
		private List<Coupon> list = new ArrayList<Coupon>();
		private int pageIndex = 1;

		public ReceivableCouponViewAdapter(PullToRequestView view) {
			super(view);
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				resetPageIndex();
			}
			getReceivableCoupons(pageIndex);
		}

		@Override
		public View getView(final int i, View convertView, ViewGroup viewGroup) {
			final ViewHolder vh;
			if (convertView == null) {
				convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_page_my_coupons_item,
						null);
				vh = new ViewHolder();
				vh.priceTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_price_tv);
				vh.descriptionTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_description_tv);
				vh.nameTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_name_tv);
				vh.validTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_valid_tv);
				vh.receiveBtn = (Button) convertView.findViewById(R.id.shopsdk_my_coupon_receive_btn);
				vh.receiveBtn.setVisibility(View.VISIBLE);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			if (list != null && !list.isEmpty()) {
				Coupon coupon = list.get(i);
				vh.priceTv.setText(MoneyConverter.conversionString(coupon.getCouponMoney()));
				vh.descriptionTv.setText(coupon.getCouponDescribe());
				vh.nameTv.setText(coupon.getCouponName());
				String format = getContext().getResources().getString(ResHelper.getStringRes(getContext(),
						"shopsdk_default_my_coupon_valid"));
				String validBegin = Utils.long2Time(coupon.getValidBeginAt());
				String validEnd = Utils.long2Time(coupon.getValidEndAt());
				vh.validTv.setText(String.format(format, validBegin, validEnd));
			}
			vh.receiveBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "ReceivableCouponViewAdapter.getView()", "btn clicked.");
					ShopSDK.receiveCoupon(list.get(i).getCouponId(), new SGUIOperationCallback<Void>(getPage().getCallback()) {
						@Override
						public void onSuccess(Void data) {
							super.onSuccess(data);
							if (vh.receiveBtn.isEnabled()) {
								vh.receiveBtn.setEnabled(false);
								vh.receiveBtn.setText(getPage().getContext().getResources().getString(
										ResHelper.getStringRes(getPage().getContext(), "shopsdk_default_receive_coupon_received")));
							}
						}

						@Override
						public boolean onResultError(Throwable t) {
							getPage().toastNetworkError();
							return super.onResultError(t);
						}
					});
				}
			});
			return convertView;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Object getItem(int i) {
			return list.get(i);
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		public void setData(List<Coupon> data) {
			list = data;
			notifyDataSetChanged();
		}

		public void appendData(List<Coupon> data) {
			if (list == null) {
				list = new ArrayList<Coupon>();
			}
			list.addAll(data);
			notifyDataSetChanged();
		}

		public void clearData() {
			setData(null);
			notifyDataSetChanged();
		}

		public void resetPageIndex() {
			pageIndex = 1;
		}

		public void increasePageIndex() {
			pageIndex ++;
		}

		public void lockPullingUp() {
			getParent().lockPullingUp();
		}

		public void releasePullingUpLock() {
			getParent().releasePullingUpLock();
		}

		public void stopPulling() {
			getParent().stopPulling();
		}

		class ViewHolder {
			private TextView priceTv;
			private TextView descriptionTv;
			private TextView nameTv;
			private TextView validTv;
			private Button receiveBtn;
		}
	}
}
