package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.CommentStarts;
import com.mob.shop.datatype.builder.CommentBuilder;
import com.mob.shop.datatype.entity.BaseComment;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.AppraisePage;
import com.mob.shop.gui.pickers.ImagePicker;
import com.mob.shop.gui.themes.defaultt.components.ExactGridView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

/**
 * 评价页面
 */

public class AppraisePageAdapter extends DefaultThemePageAdapter<AppraisePage> implements View.OnClickListener {
	private AppraisePage page;
	private TextView tvSubmit;
	private ListView listView;
	private ImagePicker.Builder builder;
	private AddAppraiseAdapter adapter;

	@Override
	public void onCreate(AppraisePage page, Activity activity) {
		super.onCreate(page, activity);
		activity.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
		this.page = page;
		View view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_appraise, null);
		activity.setContentView(view);
		initView(view);
		initData();
	}

	private void initView(View view) {
		tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
		TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
		titleView.initTitleView(page, "shopsdk_default_evaluate", null, null, true);
		listView = (ListView) view.findViewById(R.id.listView);
		tvSubmit.setOnClickListener(this);
	}

	private void initData() {
		Order order = page.getOrder();
		if (order != null && order.getOrderCommodityList() != null) {
			List<OrderCommodity> list = order.getOrderCommodityList();
			List<BaseComment> comments = new ArrayList<BaseComment>();
			for (OrderCommodity orderCommodity : list) {
				BaseComment baseComment = new BaseComment();
				baseComment.setAnonymity(true);
				baseComment.setCommentStars(CommentStarts.ALL);
				baseComment.setOrderCommodityId(orderCommodity.getOrderCommodityId());
				baseComment.setCommodityId(orderCommodity.getCommodityId());
				comments.add(baseComment);
			}
			adapter = new AddAppraiseAdapter(this);
			listView.setAdapter(adapter);
			adapter.setData(list, comments);
		}
	}

	private String initAppriaseInfo(CommentStarts commentStarts) {
		switch (commentStarts) {
			case TWO:
			case THREE:
			case FOUR:
				return getString("shopsdk_default_middle_appriase");
			case FIVE:
				return getString("shopsdk_default_good_appriase");
			default:
				return getString("shopsdk_default_bad_appriase");
		}
	}

	private void submit() {
		List<BaseComment> list = adapter.getList();
		if (list == null) {
			return;
		}
		boolean isCommentStar = false;
		for (BaseComment comment : list) {
			if (comment.getCommentStars() != CommentStarts.ALL) {
				isCommentStar = true;
				if (TextUtils.isEmpty(comment.getComment())) {
					comment.setComment(initAppriaseInfo(comment.getCommentStars()));
				}
			}
		}
		if (!isCommentStar) {
			page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_add_appriase_tip"));
			return;
		}

		CommentBuilder builder = new CommentBuilder(page.getOrder().getOrderId(), list);
		ShopSDK.addComment(builder, new SGUIOperationCallback<Void>(page.getCallback()) {
			@Override
			public void onSuccess(Void data) {
				super.onSuccess(data);
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_appriase_success"));
				finish();
			}

			@Override
			public boolean onResultError(Throwable t) {
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_appriase_failed"));
				return super.onResultError(t);
			}
		});
	}

	private void showImagePicker(final int index, final int imgIndex) {
		if (builder == null) {
			builder = new ImagePicker.Builder(getPage().getContext(), getPage().getTheme());
		}
		builder.setOnImageGotListener(new ImagePicker.OnImageGotListener() {
			public void onOmageGot(Bitmap bm) {
				adapter.addBm(index, bm);
			}

			@Override
			public void onImageUploadSuccess(String id, String url) {
				if (url != null) {
					adapter.addImgUrl(index, new ImgUrl(url));
				}
			}

			@Override
			public void onImageUploadFailed() {
				adapter.removeImgUrl(index, imgIndex);
			}
		});
		builder.show();
	}


	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tvSubmit) {
			submit();
		}
	}

	class AddAppraiseAdapter extends BaseAdapter {
		private List<BaseComment> list = new ArrayList<BaseComment>();
		private List<OrderCommodity> orderCommodities = new ArrayList<OrderCommodity>();
		private AppraisePageAdapter pageAdapter;
		private HashMap<Integer, ArrayList<Bitmap>> bmMap = new HashMap<Integer, ArrayList<Bitmap>>();
		private HashMap<Integer, ArrayList<ImgUrl>> imgUrlMap = new HashMap<Integer, ArrayList<ImgUrl>>();
		int index = -1;

		public AddAppraiseAdapter(AppraisePageAdapter pageAdapter) {
			this.pageAdapter = pageAdapter;
		}

		@Override
		public int getCount() {
			return orderCommodities.size();
		}

		@Override
		public Object getItem(int i) {
			return orderCommodities.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		public void addBm(int index, Bitmap bm) {
			ArrayList<Bitmap> bms = null;
			if (bmMap.containsKey(index)) {
				bms = bmMap.get(index);
			}
			if (bms == null) {
				bms = new ArrayList<Bitmap>();
			}
			bms.add(bm);
			bmMap.put(index, bms);
			notifyDataSetChanged();
		}

		public void addImgUrl(int index, ImgUrl url) {
			ArrayList<ImgUrl> imgUrls = null;
			if (imgUrlMap.containsKey(index)) {
				imgUrls = imgUrlMap.get(index);
			}
			if (imgUrls == null) {
				imgUrls = new ArrayList<ImgUrl>();
			}
			imgUrls.add(url);
			imgUrlMap.put(index, imgUrls);
			BaseComment baseComment = list.get(index);
			if (baseComment != null) {
				baseComment.setCommentImgUrls(imgUrls);
			}
			notifyDataSetChanged();
		}

		public void removeImgUrl(int index, int imgIndex) {
			ArrayList<ImgUrl> imgUrls = null;
			if (imgUrlMap.containsKey(index)) {
				imgUrls = imgUrlMap.get(index);
			}
			if (imgUrls == null) {
				imgUrls = new ArrayList<ImgUrl>();
			}
			imgUrls.remove(imgIndex);
			imgUrlMap.put(index, imgUrls);
			BaseComment baseComment = list.get(index);
			if (baseComment != null) {
				baseComment.setCommentImgUrls(imgUrls);
			}
			notifyDataSetChanged();
		}

		public void setList(List<BaseComment> list) {
			this.list = list;
		}

		public void setData(List<OrderCommodity> orderCommodities, List<BaseComment> list) {
			setOrderCommodities(orderCommodities);
			setList(list);
			notifyDataSetChanged();
		}

		public void setOrderCommodities(List<OrderCommodity> orderCommodities) {
			this.orderCommodities = orderCommodities;
		}

		public List<BaseComment> getList() {
			return list;
		}

		@Override
		public View getView(final int i, View convertView, final ViewGroup viewGroup) {
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
						.shopsdk_default_item_add_appraise, null);
				vh = new ViewHolder();
				vh.ivLogo = (AsyncImageView) convertView.findViewById(R.id.ivLogo);
				vh.ratBar = (RatingBar) convertView.findViewById(R.id.ratBar);
				vh.etAppraise = (EditText) convertView.findViewById(R.id.etAppraise);
				vh.gridView = (ExactGridView) convertView.findViewById(R.id.gridView);
				vh.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			OrderCommodity orderCommodity = orderCommodities.get(i);
			final BaseComment baseComment = list.get(i);

			vh.ivLogo.execute(orderCommodity.getImgUrl().getSrc(), ResHelper.getBitmapRes(viewGroup.getContext(),
					"order_bg"));
			vh.ratBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
				@Override
				public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
					int rating = (int) ratingBar.getRating();
					switch (rating) {
						case 0:
							baseComment.setCommentStars(CommentStarts.ALL);
							break;
						case 1:
							baseComment.setCommentStars(CommentStarts.ONE);
							break;
						case 2:
							baseComment.setCommentStars(CommentStarts.TWO);
							break;
						case 3:
							baseComment.setCommentStars(CommentStarts.THREE);
							break;
						case 4:
							baseComment.setCommentStars(CommentStarts.FOUR);
							break;
						case 5:
							baseComment.setCommentStars(CommentStarts.FIVE);
							break;
					}

				}
			});

			final ViewHolder finalVh = vh;
			finalVh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					baseComment.setAnonymity((finalVh.checkBox.isChecked()) ? true : false);
				}
			});

			if (vh.etAppraise.getTag() instanceof TextWatcher) {
				vh.etAppraise.removeTextChangedListener((TextWatcher) vh.etAppraise.getTag());
			}

			vh.etAppraise.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = i;
					}
					return false;
				}
			});

			TextWatcher textWatcher = new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					baseComment.setComment(finalVh.etAppraise.getText().toString());
				}
			};

			vh.etAppraise.addTextChangedListener(textWatcher);
			vh.etAppraise.setTag(textWatcher);

			UploadImgsAdapter adapter = new UploadImgsAdapter(i, pageAdapter);
			vh.gridView.setAdapter(adapter);
			adapter.setImgUrlList(imgUrlMap.get(i));


			vh.etAppraise.clearFocus();
			if (index != -1 && index == i) {
				vh.etAppraise.requestFocus();
			}
			vh.etAppraise.setSelection(vh.etAppraise.getText().length());
			return convertView;
		}

		class ViewHolder {
			AsyncImageView ivLogo;
			RatingBar ratBar;
			EditText etAppraise;
			ExactGridView gridView;
			CheckBox checkBox;
		}

		class UploadImgsAdapter extends BaseAdapter {
			private List<ImgUrl> list = new ArrayList<ImgUrl>();
			private ArrayList<Bitmap> bmlist = new ArrayList<Bitmap>();
			private AppraisePageAdapter pageAdapter;
			private int index;

			public UploadImgsAdapter(int index, AppraisePageAdapter pageAdapter) {
				this.index = index;
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

			public void setBmlist(ArrayList<Bitmap> bmlist) {
				if (bmlist != null) {
					this.bmlist = bmlist;
				}
				notifyDataSetChanged();
			}

			public void setImgUrlList(ArrayList<ImgUrl> imgUrlList) {
				if (imgUrlList != null) {
					this.list = imgUrlList;
				}
				notifyDataSetChanged();
			}

			@Override
			public View getView(final int i, View view, ViewGroup viewGroup) {
				ViewHolder vh = null;
				if (view == null) {
					view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
							.shopsdk_default_item_refund_img, null);
					vh = new ViewHolder();
					vh.iv = (AsyncImageView) view.findViewById(R.id.iv);
					vh.ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
					view.setTag(vh);
				} else {
					vh = (ViewHolder) view.getTag();
				}
				int width = ResHelper.getScreenWidth(viewGroup.getContext());
				int itemWidth = width / 4;
				int padding = vh.ivCancel.getPaddingLeft();
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemWidth - padding * 3, itemWidth -
						padding * 3);
				vh.iv.setLayoutParams(lp);

				if (getCount() == list.size()) {
					vh.iv.execute(list.get(i).getSrc(), ResHelper.getColorRes(viewGroup.getContext(), "order_bg"));
					vh.ivCancel.setVisibility(View.VISIBLE);
				} else {
					if (i + 1 == getCount()) {
						vh.ivCancel.setVisibility(View.INVISIBLE);
						vh.iv.setImageResource(ResHelper.getBitmapRes(viewGroup.getContext(),
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
							pageAdapter.showImagePicker(index, i);
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
}
