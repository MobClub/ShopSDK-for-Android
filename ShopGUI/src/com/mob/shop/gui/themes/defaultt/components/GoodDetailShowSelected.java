package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.ProductProperty;
import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yjin on 2017/9/6.
 */

public class GoodDetailShowSelected extends RelativeLayout implements View.OnClickListener {
	private LinearLayout typeSelect;
	private TextView tvMinPrice;
	private TextView tvPriceSpace;
	private TextView tvMaxPriceKey;
	private TextView tvMaxPrice;
	private TextView storeCounts;
	private TextView selectStr;
	private TextView submit;
	private ImageView ivClose;
	private AsyncImageView imageIcon;
	private View line;
	private ScrollView scrollView;
	private HashMap<String, CollspanBarTitle> map = new HashMap<String, CollspanBarTitle>();
	private CollspanBarTitle[] collspanBarTitles;
	private GoodSelectedTypeItem[] selected = null;
	private OnSelectedChangeListener onSelectedChangeListener;
	private Context context;
	private CountView salesCounts;
	private int buyCount = 1;

	public GoodDetailShowSelected(Context context) {
		super(context);
		init(context);
	}

	public GoodDetailShowSelected(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GoodDetailShowSelected(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.detail_selected_type_main, null);
		typeSelect = (LinearLayout) view.findViewById(R.id.typeSelect);
		tvMinPrice = (TextView) view.findViewById(R.id.tvMinPrice);
		tvPriceSpace = (TextView) view.findViewById(R.id.tvPriceSpace);
		tvMaxPriceKey = (TextView) view.findViewById(R.id.tvMaxPriceKey);
		tvMaxPrice = (TextView) view.findViewById(R.id.tvMaxPrice);
		storeCounts = (TextView) view.findViewById(R.id.storesCounts);
		selectStr = (TextView) view.findViewById(R.id.selectedGoodId);
		submit = (TextView) view.findViewById(R.id.submit);
		line = view.findViewById(R.id.line);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		ivClose = (ImageView) view.findViewById(R.id.ivClose);
		imageIcon = (AsyncImageView) view.findViewById(R.id.goodIcon);
		salesCounts = (CountView) view.findViewById(R.id.salesCounts);
		salesCounts.setOnCountClickListener(new CountView.OnCountClickListener() {
			@Override
			public void onFailed(String msg) {

			}

			@Override
			public void changeCount(int count) {
				buyCount = count;
			}
		});
		submit.setOnClickListener(this);
		ivClose.setOnClickListener(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ResHelper
				.getScreenHeight(context) / 4 * 3);
		addView(view, lp);
	}

	public void setCollspanBarTitleCount(int count) {
		collspanBarTitles = new CollspanBarTitle[count];
		selected = new GoodSelectedTypeItem[count];
	}

	public void initData(int minPrice, int maxPrice, ImgUrl imgUrl, int stockCount) {
		setBuyPrice(minPrice, maxPrice);
		imageIcon.execute(imgUrl.getSrc(), ResHelper.getColorRes(context, "order_bg"));
		String format = context.getString(ResHelper.getStringRes(context, "shopsdk_default_stock_count"));
		storeCounts.setText(String.format(format, stockCount));
	}

	public void addSelectedType(final int index, String name, final List<GoodSelectedTypeItem> goodSelected, final
	Context context) {
		if(index ==0){
			line.setVisibility(VISIBLE);
			scrollView.setVisibility(VISIBLE);
		}
		TextView textView = new TextView(context);
		textView.setText(name);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		textView.setTextColor(Color.parseColor("#000000"));
		int dp13 = ResHelper.dipToPx(context, 8);
		textView.setPadding(0, dp13, 0, dp13);
		typeSelect.addView(textView);
		final CollspanBarTitle collspanBarTitle = new CollspanBarTitle(context);
		collspanBarTitle.setHorPadding(20);
		collspanBarTitle.setVerPadding(20);
		collspanBarTitle.setTextColorNor(0xFF000000);
		collspanBarTitle.setTextSize(ResHelper.dipToPx(context, 13));
		collspanBarTitle.setVerPadding(ResHelper.dipToPx(context, 8));
		collspanBarTitle.setHorPadding(ResHelper.dipToPx(context, 15));
		collspanBarTitle.setHorInterval(ResHelper.dipToPx(context, 15));
		collspanBarTitle.setBgResoureNor(ResHelper.getBitmapRes(context, "shopsdk_default_round_grey_bg"));
		collspanBarTitle.setBgResoureUnSel(ResHelper.getBitmapRes(context, "shopsdk_default_round_grey_bg"));
		collspanBarTitle.setBgResoureSel(ResHelper.getBitmapRes(context, "shopsdk_default_round_orange_bg"));
		collspanBarTitle.setTextColorUnSel(0xFFD0D0D0);
		collspanBarTitle.addItemViews(name, goodSelected);
		collspanBarTitle.setGroupClickListener(name, new CollspanBarTitle.OnGroupItemClickListener() {
			@Override
			public void onGroupItemClick(String type, int itemPos, String key, String value) {
				selected[index] = goodSelected.get(itemPos);
				if (selected.length == 0) {
					selectStr.setText(ResHelper.getStringRes(context, "shopsdk_default_select_product_model"));
				}
				if (selected.length == 1) {
					selectStr.setText(ResHelper.getStringRes(context, "shopsdk_default_selected"));
				}
				String str = getContext().getResources().getString(ResHelper.getStringRes(context,
						"shopsdk_default_selected"));
				for (int i = 0; i < selected.length; i++) {
					if (selected[i] != null) {
						str += selected[i].getValue();
						if (i != selected.length - 1) {
							str += ",";
						}
					}
				}
				selectStr.setText(str);
				if (onSelectedChangeListener != null) {
					onSelectedChangeListener.onSelectedChange(type, selected);
				}
			}

			@Override
			public void unSelected(String type, int itemPos, String key, String value) {
				selected[index] = null;
				selectStr.setText(ResHelper.getStringRes(context, "shopsdk_default_select_product_model"));
				if (onSelectedChangeListener != null) {
					onSelectedChangeListener.onSelectedChange(type, selected);
				}
			}
		});
		map.put(name, collspanBarTitle);
		collspanBarTitles[index] = collspanBarTitle;
		typeSelect.addView(collspanBarTitle);
	}


	public void show() {

	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setData(Commodity commodity) {
		if (commodity != null) {
			imageIcon.execute(commodity.getImgUrl().getSrc(), ResHelper.getColorRes(context, "order_bg"));
			setBuyPrice(commodity.getCurrentCost(), commodity.getCurrentCost());
			String format = context.getResources().getString(ResHelper.getStringRes(context,
					"shopsdk_default_usableStock"));
			storeCounts.setText(String.format(format, commodity.getUsableStock()));
			setSalesCounts(commodity.getUsableStock());
			if (commodity.getUsableStock() <= 0) {
				setSubmitEnabled(false);
			} else {
				setSubmitEnabled(true);
			}
		}
	}

	private void setBuyPrice(int minPrice, int maxPrice) {
		tvMinPrice.setText(MoneyConverter.conversionString(minPrice));
		if (tvMaxPrice.getVisibility() == View.VISIBLE && minPrice == maxPrice) {
			tvMaxPrice.setVisibility(View.GONE);
			tvMaxPriceKey.setVisibility(View.GONE);
			tvPriceSpace.setVisibility(View.GONE);
		} else if (minPrice < maxPrice) {
			tvMaxPrice.setText(MoneyConverter.conversionString(maxPrice));
			if (tvMaxPrice.getVisibility() != VISIBLE) {
				tvMaxPrice.setVisibility(View.VISIBLE);
				tvMaxPriceKey.setVisibility(View.VISIBLE);
				tvPriceSpace.setVisibility(View.VISIBLE);
			}
		}
	}

	public void setSalesCounts(int count) {
		salesCounts.setMaxCount(count);
	}

	public String getSelectedStr() {
		return selectStr.getText().toString();
	}
	public void setSelectedStr(String str){
		selectStr.setText(str);
	}

	public void setSubmitEnabled(boolean submitEnabled) {
		submit.setEnabled(submitEnabled);
		if (submitEnabled) {
			submit.setBackgroundResource(ResHelper.getBitmapRes(context, "shopsdk_default_title_bg"));
		} else {
			submit.setBackgroundResource(ResHelper.getBitmapRes(context, "shopsdk_default_rectangle_deep_grey_bg"));
		}
	}

	public void changeSelectedType(String name, HashMap<String, String> goodSelected) {
		CollspanBarTitle collspanBarTitle = map.get(name);

		collspanBarTitle.setSelectableItem(goodSelected);
	}


	public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
		this.onSelectedChangeListener = onSelectedChangeListener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.submit) {
			if (onSelectedChangeListener != null) {
				onSelectedChangeListener.submit();
			}
		} else if (v.getId() == R.id.ivClose) {
			if (onSelectedChangeListener != null) {
				onSelectedChangeListener.close();
			}
		}
	}

	public interface OnSelectedChangeListener {
		void onSelectedChange(String type, GoodSelectedTypeItem[] list);

		void submit();

		void close();
	}
}
