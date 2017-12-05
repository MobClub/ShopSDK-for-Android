package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.CommentLevel;
import com.mob.shop.datatype.CommentPic;
import com.mob.shop.datatype.CommentStarts;
import com.mob.shop.datatype.builder.CommentQuerier;
import com.mob.shop.datatype.entity.Comment;
import com.mob.shop.datatype.entity.CommentList;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.shop.gui.pages.AppraiseListPage;
import com.mob.shop.gui.themes.defaultt.components.AccessTabsView;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.ExactGridView;
import com.mob.shop.gui.themes.defaultt.components.ScrolledPullToRequestView;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 评价列表页
 */
public class AppraiseListPageAdapter extends PageAdapter<AppraiseListPage> implements View.OnClickListener {
	private ImageView ivBack;
	private AsyncImageView ivLogo;
	private TextView tvName;
	private AccessTabsView accessTabsView;
	private MobViewPager mobViewPage;

	@Override
	public void onCreate(AppraiseListPage page, Activity activity) {
		super.onCreate(page, activity);
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_appraiselist, null);
		activity.setContentView(view);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
		ivBack.setOnClickListener(this);

		ivLogo = (AsyncImageView) view.findViewById(R.id.ivLogo);
		ivLogo.execute(page.getProductImg().getSrc(), ResHelper.getColorRes(page.getContext(), "order_bg"));

		tvName = (TextView) view.findViewById(R.id.tvName);
		tvName.setText(page.getProductName());

		accessTabsView = (AccessTabsView) view.findViewById(R.id.accessTabsView);
		accessTabsView.setDataRes(new String[]{"shopsdk_default_all", "shopsdk_default_good_appraise",
				"shopsdk_default_common_appraise", "shopsdk_default_bad_appraise", "shopsdk_default_img_appraise"},
				new int[]{0, 0, 0, 0, 0});
		accessTabsView.setOnTabClickListener(new AccessTabsView.OnTabClickListener() {
			@Override
			public void onTabClick(int index) {
				mobViewPage.scrollToScreen(index, true, true);
			}
		});

		mobViewPage = (MobViewPager) view.findViewById(R.id.MobViewPage);
		AppraicesViewPagerAdapter viewPagerAdapter = new AppraicesViewPagerAdapter(page, this, page.getProductId());
		mobViewPage.setAdapter(viewPagerAdapter);
		viewPagerAdapter.setScreenChangeListener(new AppraicesViewPagerAdapter.ScreenChangeListener() {
			@Override
			public void onScreenChange(int currentScreen, int lastScreen) {
				accessTabsView.changeTab(currentScreen);
			}
		});

	}

	public void setSubTabs(int[] sTabs) {
		if (sTabs == null) {
			return;
		}
		accessTabsView.setSubTab(sTabs);
	}


	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivBack) {
			finish();
		}
	}

	private static class AppraicesViewPagerAdapter extends ViewPagerAdapter {
		private CommentLevel[] commentLevels = new CommentLevel[]{CommentLevel.ALL, CommentLevel.PRAISE, CommentLevel
				.AVERAGE, CommentLevel.BAD};
		private AppraiseListPage page;
		private long productId;
		private ScreenChangeListener screenChangeListener;
		private HashMap<String, ScrolledPullToRequestView> viewMap = new HashMap<String, ScrolledPullToRequestView>();
		private AppraiseListPageAdapter pageAdapter;

		public AppraicesViewPagerAdapter(AppraiseListPage page, AppraiseListPageAdapter pageAdapter, long productId) {
			this.page = page;
			this.pageAdapter = pageAdapter;
			this.productId = productId;
		}

		public void setScreenChangeListener(ScreenChangeListener screenChangeListener) {
			this.screenChangeListener = screenChangeListener;
		}

		public int getCount() {
			return 5;
		}

		public void onScreenChange(int currentScreen, int lastScreen) {
			super.onScreenChange(currentScreen, lastScreen);
			if (screenChangeListener != null) {
				screenChangeListener.onScreenChange(currentScreen, lastScreen);
			}
		}

		public View getView(int index, View convertView, ViewGroup parent) {
			ScrolledPullToRequestView pullToRequestView = viewMap.get(String.valueOf(index));
			if (pullToRequestView == null) {
				ScrolledPullToRequestView requestView = null;
				if (index == 0) {
					requestView = createPanel(parent.getContext(), productId, null, null);
				} else if (index >= commentLevels.length) {
					requestView = createPanel(parent.getContext(), productId, null, CommentPic.HAVE);
				} else {
					requestView = createPanel(parent.getContext(), productId, commentLevels[index], null);
				}
				convertView = requestView;
				viewMap.put(String.valueOf(index), requestView);
			} else {
				convertView = pullToRequestView;
			}
			return convertView;
		}

		//创建下拉刷新列表
		private ScrolledPullToRequestView createPanel(Context context, long productId, CommentLevel commentLevel,
				CommentPic commentPic) {
			ScrolledPullToRequestView requestView = new ScrolledPullToRequestView(context);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			requestView.setLayoutParams(lp);
			AppraisesAdapter pullRequestAdapter = new AppraisesAdapter(page, requestView, productId, commentLevel,
					commentPic);
			requestView.setAdapter(pullRequestAdapter);
			requestView.performPullingDown(true);
			return requestView;
		}

		/**
		 * 页面滑动监听
		 */
		public interface ScreenChangeListener {
			void onScreenChange(int currentScreen, int lastScreen);
		}


		private class AppraisesAdapter extends DefaultRTRListAdapter {
			private List<Comment> comments = new ArrayList<Comment>();
			private Page page;
			private static final int PAGE_SIZE = 20;
			private int pageIndex = 1;
			private CommentLevel commentLevel;
			private CommentPic commentPic;
			private long productId;

			public AppraisesAdapter(Page page, PullToRequestView view, long productId, CommentLevel commentLevel,
					CommentPic commentPic) {
				super(view);
				this.page = page;
				this.productId = productId;
				this.commentLevel = commentLevel;
				this.commentPic = commentPic;
				getListView().setDividerHeight(0);
			}

			@Override
			protected void onRequest(boolean firstPage) {
				if (firstPage) {
					pageIndex = 1;
				}
				getAppraises();
			}

			@Override
			public View getView(int i, View convertView, final ViewGroup viewGroup) {
				ViewHolder vh = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
							.shopsdk_default_item_appraise, null);
					vh = new ViewHolder();
					vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
					vh.tvAccess = (TextView) convertView.findViewById(R.id.tvAccess);
					vh.tvAttr = (TextView) convertView.findViewById(R.id.tvAttr);
					vh.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
					vh.accessImgView = (ExactGridView) convertView.findViewById(R.id.accessImgView);
					convertView.setTag(vh);
				} else {
					vh = (ViewHolder) convertView.getTag();
				}
				Comment comment = comments.get(i);
				if (comment.isAnonymity()) {
					String buyerName = comment.getBuyerName();
					if (!TextUtils.isEmpty(buyerName) && buyerName.length() > 1) {
						vh.tvName.setText(buyerName.substring(0, 1) + "***" + buyerName.substring(buyerName.length() -
								1));
					}
				} else {
					vh.tvName.setText(comment.getBuyerName());
				}
				vh.tvAccess.setText(comment.getComment());
				vh.tvAttr.setText(comment.getPropertyDescribe());
				vh.ratingBar.setRating(comment.getCommentStars().getValue());
				AppraiseImgAdapter imgAdapter = new AppraiseImgAdapter(comment.getCommentImgUrls());
				vh.accessImgView.setAdapter(imgAdapter);
				return convertView;
			}

			@Override
			public long getItemId(int i) {
				return i;
			}

			@Override
			public Object getItem(int i) {
				return comments.get(i);
			}

			@Override
			public int getCount() {
				return comments == null ? 0 : comments.size();
			}

			private void getAppraises() {
				CommentQuerier querier = new CommentQuerier(PAGE_SIZE, pageIndex, productId, commentLevel, null,
						commentPic);
				ShopSDK.getComments(querier, new SGUIOperationCallback<CommentList>(page.getCallback()) {
					@Override
					public void onSuccess(CommentList data) {
						super.onSuccess(data);
						getParent().stopPulling();
						if (pageIndex == 1) {
							comments.clear();
							HashMap<CommentStarts, Integer> startMap = data.getCountByStars();
							int[] sTabs = new int[5];
							if (startMap != null) {
								sTabs[1] = startMap.get(CommentStarts.FIVE);
								sTabs[2] = startMap.get(CommentStarts.THREE) + startMap.get(CommentStarts.FOUR) +
										startMap.get(CommentStarts.TWO);
								sTabs[3] = startMap.get(CommentStarts.ONE);
								sTabs[4] = data.getPicCommentCount();
								sTabs[0] = sTabs[1] + sTabs[2] + sTabs[3];
							}
							pageAdapter.setSubTabs(sTabs);
						}
						if (data == null || data.getList() == null || data.getList().isEmpty()) {
							getParent().lockPullingUp();
						} else {
							comments.addAll(data.getList());
							getParent().releasePullingUpLock();
							pageIndex++;

						}
						notifyDataSetChanged();
					}
					@Override
					public boolean onResultError(Throwable t) {
						getParent().stopPulling();
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_apprise_list_failed"));
						return super.onResultError(t);
					}
				});
			}

			private class ViewHolder {
				private TextView tvName;
				private RatingBar ratingBar;
				private TextView tvAccess;
				private ExactGridView accessImgView;
				private TextView tvAttr;
			}

			private class AppraiseImgAdapter extends BaseAdapter {
				ArrayList<ImgUrl> imgs = null;

				public AppraiseImgAdapter(ArrayList<ImgUrl> imgs) {
					if (imgs == null) {
						imgs = new ArrayList<ImgUrl>();
					}
					this.imgs = imgs;
				}

				@Override
				public int getCount() {
					return imgs.size();
				}

				@Override
				public Object getItem(int i) {
					return imgs.get(i);
				}

				@Override
				public long getItemId(int i) {
					return i;
				}

				@Override
				public View getView(int i, View view, ViewGroup viewGroup) {
					ViewHolder vh = null;
					if (view == null) {
						view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
								.shopsdk_default_item_appraise_img, null);
						vh = new ViewHolder();
						vh.iv = (AsyncImageView) view.findViewById(R.id.iv);
						view.setTag(vh);
					} else {
						vh = (ViewHolder) view.getTag();
					}
					vh.iv.execute(imgs.get(i).getSrc(), ResHelper.getColorRes(page.getContext(), "order_bg"));
					return view;
				}

				private class ViewHolder {
					AsyncImageView iv;
				}
			}

		}
	}

}
