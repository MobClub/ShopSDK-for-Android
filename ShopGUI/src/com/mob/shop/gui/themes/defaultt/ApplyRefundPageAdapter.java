package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.LimitType;
import com.mob.shop.datatype.RefundType;
import com.mob.shop.datatype.builder.RefundBuilder;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.datatype.entity.PreRefund;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.ApplyRefundPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.pickers.ImagePicker;
import com.mob.shop.gui.pickers.ImagePicker.OnImageGotListener;
import com.mob.shop.gui.pickers.SingleValuePicker;
import com.mob.shop.gui.themes.defaultt.components.ExactGridView;
import com.mob.shop.gui.themes.defaultt.components.OrderProductView;
import com.mob.shop.gui.themes.defaultt.components.SingleChoiceView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.themes.defaultt.entity.RefundReason;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 申请退货退款页
 */
public class ApplyRefundPageAdapter extends DefaultThemePageAdapter<ApplyRefundPage> implements View.OnClickListener {
	private EditText etBuyerMessage;
	private EditText etRefundExplain;
	private TextView tvTip;
	private TextView tvCommit;
	private SingleChoiceView refundTypeView;
	private SingleChoiceView refundReasonView;
	private OrderCommodity orderCommodity;
	private ApplyRefundPage page;
	private int maxMoney = 0;
	private RefundType refundType;
	private String refundReason;
	private List<Bitmap> imgBms = new ArrayList<Bitmap>();
	private ExactGridView gridView;
	private RefundImagesAdapter adapter;
	private ImagePicker.Builder builder;
	private OrderProductView orderProductView;
	private ProgressDialog pd;

	@Override
	public void onCreate(ApplyRefundPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_applyrefund, null);
		activity.setContentView(view);

		TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
		titleView.initTitleView(page, "shopsdk_default_apply_refund", null, null, true);

		orderProductView = (OrderProductView) view.findViewById(R.id.orderProductView);
		etBuyerMessage = (EditText) view.findViewById(R.id.etBuyerMessage);
		etRefundExplain = (EditText) view.findViewById(R.id.etRefundExplain);
		refundTypeView = (SingleChoiceView) view.findViewById(R.id.refundTypeView);
		refundReasonView = (SingleChoiceView) view.findViewById(R.id.refundReasonView);
		tvTip = (TextView) view.findViewById(R.id.tvTip);
		tvCommit = (TextView) view.findViewById(R.id.tvCommit);
		gridView = (ExactGridView) view.findViewById(R.id.gridView);

		tvCommit.setOnClickListener(this);

		refundTypeView.setPage(page);
		refundTypeView.showNext();
		int resTypeId = ResHelper.getStringRes(activity, "shopsdk_default_refund_type");
		refundTypeView.setTitle(activity.getString(resTypeId));
		int resChoiceTypeId = ResHelper.getStringRes(activity, "shopsdk_default_select_refund_type_hint");
		refundTypeView.setTvChoiceHint(activity.getString(resChoiceTypeId));
		refundTypeView.setPage(page);

		refundReasonView.setPage(page);
		refundReasonView.showNext();
		int resReasonId = ResHelper.getStringRes(activity, "shopsdk_default_refund_reason");
		refundReasonView.setTitle(activity.getString(resReasonId));
		int resChoiceReasonId = ResHelper.getStringRes(activity, "shopsdk_default_refund_reason_hint");
		refundReasonView.setTvChoiceHint(activity.getString(resChoiceReasonId));

		refundTypeView.setOnSelectionChangeListener(new SingleChoiceView.OnSelectionChangeListener() {
			@Override
			public void onSelectionsChange() {
				SingleValuePicker.Choice[] choice = refundTypeView.getSelections();
				if (choice != null) {
					refundType = choice[0] == null ? null : (RefundType) choice[0].ext;
				}
			}
		});
		refundReasonView.setOnSelectionChangeListener(new SingleChoiceView.OnSelectionChangeListener() {
			@Override
			public void onSelectionsChange() {
				SingleValuePicker.Choice[] choice = refundReasonView.getSelections();
				if (choice != null) {
					refundReason = choice[0] == null ? null : choice[0].text;
				}
			}
		});

		etBuyerMessage.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (!TextUtils.isEmpty(etBuyerMessage.getText().toString().trim())) {
					float input = Float.parseFloat(etBuyerMessage.getText().toString().trim());
					if (input * 100 > maxMoney) {
						etBuyerMessage.setText("");
					}
				}
			}
		});

		setRefundType();
		setRefundReason();
		adapter = new RefundImagesAdapter(this);
		gridView.setAdapter(adapter);

		preRefund();
	}

	private void setRefundType() {
		ArrayList<SingleValuePicker.Choice> choices = refundTypeView.createChoice(RefundType.class, null);
		refundTypeView.setChoices(choices);
	}

	private void setRefundReason() {
		ArrayList<SingleValuePicker.Choice> choices = refundReasonView.createChoice(RefundReason.class, null);
		refundReasonView.setChoices(choices);
	}

	private void initOrderCommodity(int freightMoney, LimitType limitType) {

		tvTip.setText(String.format(getString("shopsdk_default_apply_refund_tip"), MoneyConverter.conversionString
				(maxMoney), MoneyConverter.conversionString(freightMoney)));
		if (limitType == LimitType.REFUND_ONLY_LIMITED) {
			refundType = RefundType.REFUND_ONLY;
			refundTypeView.setEnabled(false);
			refundTypeView.setSelections(new int[1]);
		} else {
			refundType = null;
			refundTypeView.setEnabled(true);
		}
		if (orderCommodity != null) {
			orderProductView.initData(orderCommodity.getImgUrl(), orderCommodity.getProductName(), String.valueOf
					(orderCommodity.getCount()), orderCommodity.getPropertyDescribe(), MoneyConverter.conversionString
					(orderCommodity.getCurrentCost()), false, orderCommodity.getStatus(), true);
		}
	}

	private void preRefund() {
		showProgressDialog();
		ShopSDK.preRefund(page.getOrderCommodityId(), new SGUIOperationCallback<PreRefund>(page.getCallback()) {
			@Override
			public void onSuccess(PreRefund data) {
				super.onSuccess(data);
				dissmissProgressDialog();
				if (data == null) {
					return;
				}
				orderCommodity = data.getOrderCommodity();
				maxMoney = data.getMaxRefundMoney();
				initOrderCommodity(data.getFreight(), data.getType());
			}
			@Override
			public boolean onResultError(Throwable t) {
				dissmissProgressDialog();
				return super.onResultError(t);
			}
		});
	}

	private void refund() {
		if (orderCommodity == null) {
			return;
		}
		if (refundType == null) {
			page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_select_refund_type_hint"));
			return;
		}
		if (refundReason == null) {
			page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_refund_reason_hint"));
			return;
		}
		if (TextUtils.isEmpty(etBuyerMessage.getText().toString().trim())) {
			page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_input_money_hint"));
			return;
		}
		RefundBuilder refundBuilder = new RefundBuilder(orderCommodity.getOrderCommodityId(), refundType,
				refundReason, MoneyConverter.toCent(etBuyerMessage.getText().toString()), etRefundExplain.getText()
				.toString().trim(), adapter.getList());
		ShopSDK.refund(refundBuilder, new SGUIOperationCallback<Long>(page.getCallback()) {
			@Override
			public void onSuccess(Long data) {
				super.onSuccess(data);
				page.setResult(new HashMap<String, Object>());
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_apply_refund_success"));
				finish();
			}

			@Override
			public boolean onResultError(Throwable t) {
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_apply_refund_failed"));
				return super.onResultError(t);
			}
		});
	}

	private void showImagePicker(final int index) {
		if (builder == null) {
			builder = new ImagePicker.Builder(getPage().getContext(), getPage().getTheme());
			builder.setOnImageGotListener(new OnImageGotListener() {
				public void onOmageGot(Bitmap bm) {
					adapter.addBm(bm);
				}

				@Override
				public void onImageUploadSuccess(String id, String url) {
					if (!TextUtils.isEmpty(url)) {
						adapter.addUrl(new ImgUrl(url));
					}
				}

				@Override
				public void onImageUploadFailed() {
					adapter.removeUrl(index);
				}
			});

		}
		builder.show();
	}

	private void showProgressDialog() {
		if (pd == null) {
			pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
		} else if (!pd.isShowing()) {
			pd.show();
		}
	}

	private void dissmissProgressDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tvCommit) {
			refund();
		}
	}

	@Override
	public void onDestroy(ApplyRefundPage page, Activity activity) {
		super.onDestroy(page, activity);
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
	}

	class RefundImagesAdapter extends BaseAdapter {
		private List<ImgUrl> list = new ArrayList<ImgUrl>();
		private List<Bitmap> bmlist = new ArrayList<Bitmap>();
		private ApplyRefundPageAdapter pageAdapter;

		public RefundImagesAdapter(ApplyRefundPageAdapter pageAdapter) {
			this.pageAdapter = pageAdapter;
		}

		@Override
		public int getCount() {
			return list.size() == 10 ? list.size() : list.size() + 1;
		}

		@Override
		public Object getItem(int i) {
			return list.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		public void setList(List<ImgUrl> list) {
			this.list = list;
		}

		public void addBm(Bitmap bm) {
			bmlist.add(bm);
			notifyDataSetChanged();
		}

		public void addUrl(ImgUrl url) {
			list.add(url);
			notifyDataSetChanged();
		}

		public void removeUrl(int index) {
			list.remove(index);
			notifyDataSetChanged();
		}

		public List<ImgUrl> getList() {
			return list;
		}

		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			ViewHolder vh = null;
			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_item_refund_img,
						null);
				vh = new ViewHolder();
				vh.iv = (AsyncImageView) view.findViewById(R.id.iv);
				vh.ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			int width = ResHelper.getScreenWidth(viewGroup.getContext());
			int itemWidth = (width-40) / 4;
			int padding = vh.ivCancel.getPaddingLeft();
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemWidth - padding * 4, itemWidth -
					padding * 4);
			vh.iv.setLayoutParams(lp);

			if (getCount() == list.size()) {
				vh.iv.execute(list.get(i).getSrc(), ResHelper.getColorRes(viewGroup.getContext(), "order_bg"));
				vh.ivCancel.setVisibility(View.VISIBLE);
			} else {
				if (i + 1 == getCount()) {
					vh.ivCancel.setVisibility(View.INVISIBLE);
					vh.iv.execute(null,ResHelper.getBitmapRes(viewGroup.getContext(),
							"shopsdk_default_upload_img"));
				} else {
					vh.iv.execute(list.get(i).getSrc(), ResHelper.getColorRes(viewGroup.getContext(), "order_bg"));
					vh.ivCancel.setVisibility(View.VISIBLE);
				}
			}

			vh.iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (list.size() != getCount() && i + 1 == getCount()) {
						pageAdapter.showImagePicker(i);
					}
				}
			});

			vh.ivCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					list.remove(i);
					notifyDataSetChanged();
				}
			});

			return view;
		}


		class ViewHolder {
			private AsyncImageView iv;
			private ImageView ivCancel;
		}


	}
}
