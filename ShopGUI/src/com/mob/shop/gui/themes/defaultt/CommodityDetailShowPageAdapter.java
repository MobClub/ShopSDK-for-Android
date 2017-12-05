package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.biz.api.exception.ShopException;
import com.mob.shop.datatype.CommentStarts;
import com.mob.shop.datatype.builder.CommentQuerier;
import com.mob.shop.datatype.builder.PreOrderInfoBuilder;
import com.mob.shop.datatype.entity.BaseBuyingItem;
import com.mob.shop.datatype.entity.CommentList;
import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.PreOrder;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.datatype.entity.ProductAdditionalInfo;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.AppraiseListPage;
import com.mob.shop.gui.pages.CartPage;
import com.mob.shop.gui.pages.CommodityDetailPage;
import com.mob.shop.gui.pages.CommodityDetailShowPage;
import com.mob.shop.gui.pages.OrderComfirmPage;
import com.mob.shop.gui.pages.dialog.ChoiceProductAttrDialog;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.adapter.AppraisesAdapter;
import com.mob.shop.gui.themes.defaultt.adapter.GoodDetailViewPagerAdapter;
import com.mob.shop.gui.themes.defaultt.components.DetailScrollView;
import com.mob.shop.gui.themes.defaultt.components.ExactListView;
import com.mob.shop.gui.themes.defaultt.components.ShopImageView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yjin on 2017/9/6.
 */

public class CommodityDetailShowPageAdapter extends DefaultThemePageAdapter<CommodityDetailShowPage> implements View
		.OnClickListener, DetailScrollView.OnScrollListener {
	private MobViewPager viewPager;
	private View view;
	private TextView detailName;
	private TextView detailBuyers;
	private LinearLayout detailImgs;
	private ImageView ivBack;
	private Product productDetail;
	private GoodDetailViewPagerAdapter viewPagerAdapter;
	private TextView tvSeeMore;
	private TextView tvNowBuy;
	private TextView tvMinPrice;
	private TextView tvPriceSpace;
	private TextView tvMaxPriceKey;
	private TextView tvMaxPrice;
	private TextView tvAddCart;
	private TextView tvDetailSelecte;
	private TextView tvGoodRate;
	private TextView tvAppraiseKey;
	private LinearLayout llCart;
	private LinearLayout detailselecte;
	private LinearLayout llDot;
	private ImageView[] ivDots;
	private ExactListView appraiseListView;
	private Page page;
	private Commodity buyCommodity;
	private int buyCount;
	private ProgressDialog pd;
	private ChoiceProductAttrDialog attrDialog;
	private ChoiceProductAttrDialog.Builder builder;
	private DetailScrollView detailScrollView;
	private TitleView titleView;

	private int viewPagerHeight;
	private int screenWidth;

	@Override
	public void onCreate(CommodityDetailShowPage page, Activity activity) {
		super.onCreate(page, activity);
		if (view == null) {
			view = LayoutInflater.from(activity).inflate(R.layout.commoditydetailshowpager, null);
		}
		this.page = page;
		view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		activity.setContentView(view);

		findView(view);
		initView();
		initListener();

		productDetail = page.getProduct();
		getProductDetail();
		getComments();
	}

	private void findView(View view) {
		viewPager = (MobViewPager) view.findViewById(R.id.ImgPager);
		detailName = (TextView) view.findViewById(R.id.detailgoodName);
		detailBuyers = (TextView) view.findViewById(R.id.detailbuyers);
		detailImgs = (LinearLayout) view.findViewById(R.id.detailImgs);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
		tvAddCart = (TextView) view.findViewById(R.id.tvAddCart);
		tvSeeMore = (TextView) view.findViewById(R.id.tvSeeMore);
		tvNowBuy = (TextView) view.findViewById(R.id.tvNowBuy);
		tvMinPrice = (TextView) view.findViewById(R.id.tvMinPrice);
		tvPriceSpace = (TextView) view.findViewById(R.id.tvPriceSpace);
		tvMaxPriceKey = (TextView) view.findViewById(R.id.tvMaxPriceKey);
		tvMaxPrice = (TextView) view.findViewById(R.id.tvMaxPrice);
		llCart = (LinearLayout) view.findViewById(R.id.llCart);
		detailselecte = (LinearLayout) view.findViewById(R.id.detailselecte);
		tvDetailSelecte = (TextView) view.findViewById(R.id.tvDetailSelecte);
		tvAppraiseKey = (TextView) view.findViewById(R.id.tvAppraiseKey);
		tvGoodRate = (TextView) view.findViewById(R.id.tvGoodRate);
		llDot = (LinearLayout) view.findViewById(R.id.llDot);
		appraiseListView = (ExactListView) view.findViewById(R.id.appraiseListView);
		detailScrollView = (DetailScrollView) view.findViewById(R.id.detailScrollView);
		titleView = (TitleView) view.findViewById(R.id.titleView);
	}

	private void initView() {
		titleView.initTitleView(page, "shopsdk_default_product_detail", null, null, true);
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
		lp.height = ResHelper.getScreenHeight(page.getContext()) / 2 + 60;
		viewPagerHeight = lp.height;
		titleView.setAlpha(0);
		screenWidth = ResHelper.getScreenWidth(page.getContext());
		appraiseListView.setFocusable(false);
		detailScrollView.setScrollHeight(viewPagerHeight);
	}

	private void initListener() {
		detailImgs.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tvNowBuy.setOnClickListener(this);
		llCart.setOnClickListener(this);
		tvAddCart.setOnClickListener(this);
		detailScrollView.setOnScrollListener(this);
		detailselecte.setOnClickListener(this);
		tvDetailSelecte.setOnClickListener(this);
		tvSeeMore.setOnClickListener(this);
	}


	private void initData() {
		if (productDetail != null) {
			detailName.setText(productDetail.getProductName());
			setBuyPrice(productDetail.getMinPrice());
			if (productDetail.getMinPrice() != productDetail.getMaxPrice()) {
				tvMaxPrice.setText(MoneyConverter.conversionString(productDetail.getMaxPrice()));
				tvMaxPrice.setVisibility(View.VISIBLE);
				tvMaxPriceKey.setVisibility(View.VISIBLE);
				tvPriceSpace.setVisibility(View.VISIBLE);
			}
			String format = page.getContext().getString(ResHelper.getStringRes(page.getContext(),
					"shopsdk_default_buyers"));
			detailBuyers.setText(String.format(format, productDetail.getProductSales()));
			List<ProductAdditionalInfo> imgs = productDetail.getProductAdditionalInfos();
			if (imgs != null) {
				ShopImageView imageView = null;
				for (final ProductAdditionalInfo img : imgs) {
					imageView = new ShopImageView(page.getContext());
					imageView.setScaleType(ImageView.ScaleType.FIT_XY);
					float scale = (float) screenWidth / img.getImgUrl().getWidth();
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, (int) (img.getImgUrl()
							.getHeight() * scale));
					imageView.setCompressOptions(screenWidth,(int) (img.getImgUrl().getHeight() * scale),80,10240L);
					imageView.execute(img.getImgUrl().getSrc(), ResHelper.getColorRes(page.getContext(), "order_bg"));
					imageView.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							return false;
						}
					});
					imageView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							CommodityDetailPage commodityDetailPage = new CommodityDetailPage(page.getTheme(), img
									.getLink());
							commodityDetailPage.show(page.getContext(), null);
						}
					});
					detailImgs.addView(imageView, lp);
				}
			}
			if (productDetail.getProductImgUrls() != null) {
				viewPagerAdapter = new GoodDetailViewPagerAdapter(page.getContext(), productDetail.getProductImgUrls
						());
				viewPager.setAdapter(viewPagerAdapter);
				viewPagerAdapter.setScreenChangeListener(new GoodDetailViewPagerAdapter.ScreenChangeListener() {
					@Override
					public void onScreenChange(int currentScreen, int lastScreen) {
						changeDot(currentScreen, lastScreen);
					}
				});
				initDot(productDetail.getProductImgUrls().size());
			}
			if (productDetail.getProductPropertyList() == null || productDetail.getProductPropertyList().size() == 0) {
				detailselecte.setVisibility(View.GONE);
			}
		}

	}

	private void initDot(int size) {
		ivDots = new ImageView[size];
		for (int i = 0; i < size; i++) {
			ImageView iv = new ImageView(page.getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = ResHelper.dipToPx(page.getContext(), 3);
			lp.rightMargin = ResHelper.dipToPx(page.getContext(), 3);
			ivDots[i] = iv;
			if (i == 0) {
				iv.setImageResource(ResHelper.getBitmapRes(page.getContext(), "shopsdk_default_dot_white"));
			} else {
				iv.setImageResource(ResHelper.getBitmapRes(page.getContext(), "shopsdk_default_dot_grey"));
			}
			llDot.addView(iv, lp);
		}
	}

	private void changeDot(int change, int current) {
		ivDots[change].setImageResource(ResHelper.getBitmapRes(page.getContext(), "shopsdk_default_dot_white"));
		ivDots[current].setImageResource(ResHelper.getBitmapRes(page.getContext(), "shopsdk_default_dot_grey"));
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

	private void getProductDetail() {
		showProgressDialog();
		ShopSDK.getProductDetail(productDetail.getProductId(), new SGUIOperationCallback<Product>(page.getCallback()) {
			@Override
			public void onSuccess(Product data) {
				super.onSuccess(data);
				dissmissProgressDialog();
				productDetail = data;
				initData();
			}

			@Override
			public boolean onResultError(Throwable t) {
				page.toastMessage(ResHelper.getStringRes(page.getContext(),
						"shopsdk_default_commoditydetail_failed_title"));
				dissmissProgressDialog();
				return super.onResultError(t);
			}
		});
	}

	private void getComments() {
		CommentQuerier commentQuerier = new CommentQuerier(5, 1, productDetail.getProductId(), null, null, null);
		ShopSDK.getComments(commentQuerier, new SGUIOperationCallback<CommentList>(page.getCallback()) {
			@Override
			public void onSuccess(CommentList data) {
				super.onSuccess(data);
				if (data != null) {
					String formatGR = getString("shopsdk_default_good_rate");
					String formatAC = getString("shopsdk_default_product_appraise_count");
					tvGoodRate.setText(String.format(formatGR, data.getPraiseRate()));
					if (data.getCountByStars() != null) {
						HashMap<CommentStarts, Integer> map = data.getCountByStars();
						int totalCount = map.get(CommentStarts.ONE) + map.get(CommentStarts.TWO) + map.get
								(CommentStarts.THREE) + map.get(CommentStarts.FOUR) + map.get(CommentStarts.FIVE);
						tvAppraiseKey.setText(String.format(formatAC, totalCount));
					} else {
						tvAppraiseKey.setText(getString("shopsdk_default_evaluate"));
					}

					data.getPraiseRate();
					AppraisesAdapter appraisesAdapter = new AppraisesAdapter(data.getList());
					appraiseListView.setAdapter(appraisesAdapter);
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				return super.onResultError(t);
			}
		});
	}

	private void addCart() {
		if (buyCommodity == null) {
			page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_select_product_model"));
			return;
		}
		showProgressDialog();
		ShopSDK.addIntoShoppingCart(buyCommodity.getCommodityId(), buyCount, new SGUIOperationCallback<Long>(page
				.getCallback()) {
			@Override
			public void onSuccess(Long data) {
				super.onSuccess(data);
				dissmissProgressDialog();
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_add_cart_success"));
			}

			@Override
			public boolean onResultError(Throwable t) {
				dissmissProgressDialog();
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_addcart_failed_title"));
				return super.onResultError(t);
			}
		});
	}

	private void setBuyPrice(int price) {
		tvMinPrice.setText(MoneyConverter.conversionString(price));
		if (tvMaxPrice.getVisibility() == View.VISIBLE) {
			tvMaxPrice.setVisibility(View.GONE);
			tvMaxPriceKey.setVisibility(View.GONE);
			tvPriceSpace.setVisibility(View.GONE);
		}
	}

	private void getProductAttr(final int type) {
		if (attrDialog == null) {
			builder = new ChoiceProductAttrDialog.Builder(page.getContext(), page.getTheme());
			builder.setProduct(productDetail);
			try {
				attrDialog = builder.create();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		builder.setOnDismissListener(new ChoiceProductAttrDialog.Builder.OnDismissListener() {
			@Override
			public void onDismiss(String selectStr, Commodity commodity, int count) {
				if (count < 0) {
					return;
				}

				int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_select_product_model");
				String res = page.getContext().getString(resId);
				if (commodity == null || res.equals(selectStr)) {
					tvDetailSelecte.setText(res);
					buyCommodity = null;
					return;
				}

				tvDetailSelecte.setText(selectStr);
				buyCount = count;
				buyCommodity = commodity;
				setBuyPrice(buyCommodity.getCurrentCost());

				if (type == 1) {
					preOrder();
				} else if (type == 2) {
					addCart();
				}
			}
		});
		if (!attrDialog.isShowing()) {
			attrDialog.show();
		}
	}

	private void preOrder() {
		if (ShopSDK.getUser().isAnonymous()) {
			if (page.getCallback() != null) {
				page.getCallback().login();
			}
			return;
		}
		PreOrderInfoBuilder preOrderInfoBuilder = new PreOrderInfoBuilder();
		preOrderInfoBuilder.shippingAddrId = 0;
		preOrderInfoBuilder.totalMoney = ((int) (Float.parseFloat(tvMinPrice.getText().toString()) * 100)) * buyCount;
		preOrderInfoBuilder.totalCount = buyCount;
		preOrderInfoBuilder.enableCoupon = true;

		List<BaseBuyingItem> list = new ArrayList<BaseBuyingItem>();
		BaseBuyingItem baseBuyingItem = new BaseBuyingItem();
		baseBuyingItem.setCommodityId(buyCommodity.getCommodityId());
		baseBuyingItem.setCount(buyCount);
		list.add(baseBuyingItem);

		preOrderInfoBuilder.orderCommodityList = list;
		showProgressDialog();
		ShopSDK.preOrder(preOrderInfoBuilder, new SGUIOperationCallback<PreOrder>(getPage().getCallback()) {
			@Override
			public void onSuccess(PreOrder data) {
				super.onSuccess(data);
				dissmissProgressDialog();
				OrderComfirmPage orderComfirmPage = new OrderComfirmPage(page.getTheme(), data, getPage().getCallback());
				orderComfirmPage.show(page.getContext(), null);
			}

			@Override
			protected boolean onResultError(Throwable t) {
				dissmissProgressDialog();
				if(t instanceof ShopException){
					page.toastMessage(t.getMessage());
				} else {
					page.toastMessage(ResHelper.getStringRes(page.getContext(),"shopsdk_default_preorder_failed_title"));
				}
				return super.onResultError(t);
			}
		});
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivBack) {
			finish();
		} else if (v.getId() == R.id.llCart) {
			CartPage cartPage = new CartPage(page.getTheme());
			cartPage.show(page.getContext(), null);
		} else if (v.getId() == R.id.tvAddCart) {
			if (buyCommodity == null) {
				getProductAttr(2);
			} else {
				addCart();
			}
		} else if (v.getId() == R.id.tvNowBuy) {
			if (buyCommodity == null) {
				getProductAttr(1);
			} else {
				preOrder();
			}
		} else if (v.getId() == R.id.detailselecte || v.getId() == R.id.tvDetailSelecte) {
			getProductAttr(0);
		} else if (v.getId() == R.id.tvSeeMore) {
			AppraiseListPage appraiseListPage = new AppraiseListPage(page.getTheme(), productDetail.getProductId(),
					(productDetail.getProductImgUrls() == null || productDetail.getProductImgUrls().size() == 0) ? new
							ImgUrl() : productDetail.getProductImgUrls().get(0), productDetail.getProductName());
			appraiseListPage.show(page.getContext(), null);
		}
	}

	@Override
	public void onScroll(int scrollY) {
		if (scrollY >= viewPagerHeight) {
			titleView.setAlpha(1);
			ivBack.setAlpha(0f);
		} else if (scrollY == 0) {
			ivBack.setAlpha(1f);
		} else if (scrollY < viewPagerHeight) {
			titleView.setAlpha((float) scrollY / (float) viewPagerHeight);
			ivBack.setAlpha(1 - ((float) scrollY / (float) viewPagerHeight));
		}
	}

	@Override
	public void onDestroy(CommodityDetailShowPage page, Activity activity) {
		super.onDestroy(page, activity);
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
	}
}
