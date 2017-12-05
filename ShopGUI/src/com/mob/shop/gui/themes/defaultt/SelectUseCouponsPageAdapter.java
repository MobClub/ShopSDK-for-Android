package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.biz.ShopLog;
import com.mob.shop.datatype.entity.BaseBuyingItem;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.datatype.entity.OrderCoupon;
import com.mob.shop.datatype.entity.PreOrder;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.SelectUseCouponsPage;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.shop.gui.utils.Utils;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  选择优惠券
 */

public class SelectUseCouponsPageAdapter extends DefaultThemePageAdapter<SelectUseCouponsPage> {
	private static final String TAG = SelectUseCouponsPageAdapter.class.getSimpleName();
	public static final String RESULT_SELECTED_COUPONS = "result_selected_coupons";

	private View view = null;
	private TitleView titleView;
	private ListView listView;
	private TextView confirm;
	private OrderCouponsListViewAdapter listViewAdapter = new OrderCouponsListViewAdapter();
	private PreOrder preOrder;

	@Override
	public void onCreate(SelectUseCouponsPage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_select_use_coupons,null);
		activity.setContentView(view);
		titleView = (TitleView)view.findViewById(R.id.shopsdk_select_coupon_titleView);
		titleView.initTitleView(getPage(), "shopsdk_default_select_coupon_title", null, null, true);
		listView = (ListView) view.findViewById(R.id.shopsdk_select_coupon_list);
		listView.setAdapter(listViewAdapter);
		confirm = (TextView)view.findViewById(R.id.shopsdk_select_coupon_confirm_tv);
		confirm.setOnClickListener(new ConfirmClickListener());

		Intent i = activity.getIntent();
		if (i != null) {
			preOrder = (PreOrder)i.getSerializableExtra(OrderComfirmPageAdapter.EXTRA_PREORDER);
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "onCreate", "preOrder=" + preOrder.toJSONString());
		}
		getOrderRelatedCoupons();
	}

	private class OrderCouponsListViewAdapter extends BaseAdapter {

		private List<OrderCoupon> list = new ArrayList<OrderCoupon>();
		private Map<Integer, Boolean> cbStatusMap = new HashMap<Integer, Boolean>();

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public OrderCoupon getItem(int position) {
			if (list == null || list.isEmpty() || position >= list.size()) {
				return null;
			} else {
				return list.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup viewGroup) {
			final ViewHolder vh;
			if (convertView == null) {
				convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_page_my_coupons_item,
						null);
				vh = new ViewHolder();
				vh.priceTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_price_tv);
				vh.descriptionTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_description_tv);
				vh.nameTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_name_tv);
				vh.validTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_valid_tv);
				vh.checkCb = (CheckBox) convertView.findViewById(R.id.shopsdk_my_coupon_select_cb);
				vh.checkCb.setVisibility(View.VISIBLE);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			if (list != null && !list.isEmpty()) {
				OrderCoupon coupon = list.get(position);
				if (coupon != null) {
					vh.priceTv.setText(MoneyConverter.conversionString(coupon.getCouponMoney()));
					vh.descriptionTv.setText(coupon.getCouponDescribe());
					vh.nameTv.setText(coupon.getCouponName());
					String format = getPage().getContext().getResources().getString(ResHelper.getStringRes(getPage().getContext(),
							"shopsdk_default_my_coupon_valid"));
					String validBegin = Utils.long2Time(coupon.getValidBeginAt());
					String validEnd = Utils.long2Time(coupon.getValidEndAt());
					vh.validTv.setText(String.format(format, validBegin, validEnd));
					if (cbStatusMap != null && !cbStatusMap.isEmpty()) {
						vh.checkCb.setChecked(cbStatusMap.get(position));
					}
				}
			}

			View.OnClickListener l = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (preOrder != null) {
						if (list.get(position).getLowerLimit() > preOrder.getTotalMoney()) {
							int msgId = ResHelper.getStringRes(getPage().getContext(), "shopsdk_default_select_coupon_msg_below_limit");
							getPage().toastMessage(msgId);
						} else if (!list.get(position).isAvailable()) {
							int msgId = ResHelper.getStringRes(getPage().getContext(), "shopsdk_default_select_coupon_msg_unavailable");
							getPage().toastMessage(msgId);
						} else {
							// Make checkbox checked when the whole coupon view clicked
							vh.checkCb.performClick();
							cbStatusMap.put(position, vh.checkCb.isChecked());
						}
					}
				}
			};
			convertView.setOnClickListener(l);
			return convertView;
		}

		public void setData(List<OrderCoupon> data) {
			if (data != null) {
				list = data;
				// Initiate cbStatusMap, default value is 'false', default size equals to 'list'
				for (int i = 0; i < list.size(); i ++) {
					cbStatusMap.put(i, false);
				}
				notifyDataSetChanged();
			}
		}

		public void appendData(List<OrderCoupon> data) {
			if (list == null) {
				list = new ArrayList<OrderCoupon>();
			}
			list.addAll(data);
			notifyDataSetChanged();
		}

		public void clearData() {
			setData(null);
			notifyDataSetChanged();
		}

		public ArrayList<OrderCoupon> getCheckedItems() {
			ArrayList<OrderCoupon> list = null;
			if (cbStatusMap != null && !cbStatusMap.isEmpty()) {
				list = new ArrayList<OrderCoupon>();
				for (Integer position : cbStatusMap.keySet()) {
					if (cbStatusMap.get(position) == true) {
						list.add(getItem(position));
					}
				}
			}
			return list;
		}

		class ViewHolder {
			private TextView priceTv;
			private TextView descriptionTv;
			private TextView nameTv;
			private TextView validTv;
			private CheckBox checkCb;
		}
	}

	private class ConfirmClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			ArrayList<OrderCoupon> checked = null;
			if (listViewAdapter != null) {
				checked = listViewAdapter.getCheckedItems();
			}
			ShopLog.getInstance().d(ShopLog.FORMAT, TAG, "ConfirmClickListener", "checked.size=" + (checked == null ? 0 : checked.size()));
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put(RESULT_SELECTED_COUPONS, checked);
			getPage().setResult(result);
			getPage().finish();
		}
	}

	private void getOrderRelatedCoupons() {
		if (ShopSDK.getUser().isAnonymous()) {
			if (getPage().getCallback() != null) {
				getPage().getCallback().login();
			}
			return;
		}
		List<BaseBuyingItem> buyingItemList = new ArrayList<BaseBuyingItem>();
		if (preOrder != null) {
			List<OrderCommodity> orderCommodityList = preOrder.getOrderCommodityList();
			if (orderCommodityList != null && !orderCommodityList.isEmpty()) {
				for (OrderCommodity orderCommodity : orderCommodityList) {
					buyingItemList.add(new BaseBuyingItem(orderCommodity));
				}
			}

		}
		ShopSDK.getOrderRelatedCoupons(buyingItemList, new SGUIOperationCallback<List<OrderCoupon>>(getPage().getCallback()) {
			@Override
			public void onSuccess(List<OrderCoupon> data) {
				super.onSuccess(data);
				listViewAdapter.setData(data);
			}

			@Override
			public boolean onResultError(Throwable t) {
				getPage().toastNetworkError();
				return super.onResultError(t);
			}
		});
	}
}
