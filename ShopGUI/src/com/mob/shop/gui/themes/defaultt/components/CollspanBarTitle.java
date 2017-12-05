package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yjin on 2017/9/4.
 */

public class CollspanBarTitle extends ViewGroup {
	//    private LinearLayout Main;
	public CollspanBarTitle(Context context) {
		this(context, null, false);
	}

	public CollspanBarTitle(Context context, AttributeSet attrs) {
		this(context, attrs, false);
	}

	public CollspanBarTitle(Context context, boolean enableMultiChoice) {
		this(context, null, enableMultiChoice);
	}

	public CollspanBarTitle(Context context, AttributeSet attrs, boolean enableMultiChoice) {
		super(context, attrs);
		this.multiChoice = enableMultiChoice;
		init(context, attrs);
	}

	private List<GoodSelectedTypeItem> mItems = new ArrayList<GoodSelectedTypeItem>();
	private Context mContext;

	private int horInterval; //水平间隔
	private int verInterval; //垂直间隔

	private int viewWidth;   //控件的宽度
	private int viewHeight;  //控件的高度

	//按钮水平跟垂直内边距
	private int horPadding;
	private int verPadding;

	//正常样式
	private float textSize;
	private int bgResoureNor;
	private int textColorNor;

	//选中的样式
	private int bgResoureSel;
	private int textColorSel;

	//不可选中的样式
	private int bgResoureUnSel;
	private int textColorUnSel;

	private boolean isSelector; //是否做选择之后的效果
	private boolean isSelected;	// 是否有任意一个子控件处于被选中状态（即当前CollspanBarTitle是否处于被选中状态）
	private int selectIndex = -1;
	private boolean multiChoice = false;

	private void init(Context context, AttributeSet set) {
		mContext = context;
		TypedArray attrs = mContext.obtainStyledAttributes(set, R.styleable.CollspanBarTitle);
		isSelector = attrs.getBoolean(R.styleable.CollspanBarTitle_isSelector, true);
		textSize = attrs.getDimensionPixelSize(R.styleable.CollspanBarTitle_itemTextSize, 0);
		if (textSize == 0) {
			textSize = getResources().getDimensionPixelSize(R.dimen.shopsdk_default_common_txt);//14sp
		}
		horInterval = attrs.getDimensionPixelSize(R.styleable.CollspanBarTitle_horInterval, 20);
		verInterval = attrs.getDimensionPixelSize(R.styleable.CollspanBarTitle_verInterval, 20);
		horPadding = attrs.getDimensionPixelSize(R.styleable.CollspanBarTitle_horPadding, 20);
		verPadding = attrs.getDimensionPixelSize(R.styleable.CollspanBarTitle_verPadding, 10);
		bgResoureNor = attrs.getResourceId(R.styleable.CollspanBarTitle_normal_drawable, R.drawable
				.goods_item_btn_normal);
		bgResoureSel = attrs.getResourceId(R.styleable.CollspanBarTitle_selected_drawable, R.drawable
				.goods_item_btn_selected);
		textColorNor = attrs.getColor(R.styleable.CollspanBarTitle_normal_textColor, getColorResoure(R.color
				.goods_item_text_normal));
		textColorSel = attrs.getColor(R.styleable.CollspanBarTitle_selected_textColor, getColorResoure(R.color
				.goods_item_text_selected));
		attrs.recycle();
	}

	public void setTextColorNor(int textColorNor) {
		this.textColorNor = textColorNor;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	private int getColorResoure(int resId) {
		return getResources().getColor(resId);
	}

	/**
	 * 计算控件的大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = measureWidth(widthMeasureSpec);
		viewHeight = measureHeight(heightMeasureSpec);
		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		// 设置自定义的控件ViewGroup的大小
		setMeasuredDimension(viewWidth, getViewHeight());
	}

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);
		switch (widthMode) {
			case MeasureSpec.AT_MOST:
			case MeasureSpec.EXACTLY:
				result = widthSize;
				break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;
		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
		switch (heightMode) {
			case MeasureSpec.UNSPECIFIED:
				result = getSuggestedMinimumHeight();
				break;
			case MeasureSpec.AT_MOST:
			case MeasureSpec.EXACTLY:
				result = heightSize;
				break;
		}
		return result;
	}

	/**
	 * 计算控件的自适应高度
	 */
	private int getViewHeight() {
		int viewwidth = horInterval;
		int viewheight = verInterval;
		//初始化控件的高度等于第一个元素
		if (getChildCount() > 0) {
			viewheight = getChildAt(0).getMeasuredHeight() + verInterval;
		}
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();
			//------------当前按钮按钮是否在水平上够位置(2017/7/10)------------
			if (viewwidth + getChildCurrentWidth(i) > viewWidth) {
				viewwidth = (measuredWidth + horInterval * 2);
				viewheight += (measureHeight + verInterval);
			} else {
				viewwidth += (measuredWidth + horInterval);
			}
		}
		return viewheight;
	}

	/**
	 * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
	 * 才能确定怎么摆放
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 遍历所有子视图
		int posLeft = 0;
		int posTop = verInterval;
		int posRight = horInterval;
		int posBottom;
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();
			if (posRight + getChildCurrentWidth(i) > viewWidth) {
				posLeft = 0;
				posTop += (measureHeight + verInterval);
			}
			posRight = posLeft + measuredWidth;
			posBottom = posTop + measureHeight;
			childView.layout(posLeft, posTop, posRight, posBottom);
			posLeft += (measuredWidth + horInterval);
		}
	}


	/**
	 * 获得当前按钮所需的宽度
	 *
	 * @param i
	 * @return
	 */
	private int getChildCurrentWidth(int i) {
		return getChildAt(i).getMeasuredWidth() + horInterval;
	}

	private OnGroupItemClickListener onGroupItemClickListener;

	public void setGroupClickListener(final String type, OnGroupItemClickListener listener) {
		onGroupItemClickListener = listener;
		for (int i = 0; i < getChildCount(); i++) {
			final TextView childView = (TextView) getChildAt(i);
			final int itemPos = i;
			childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String valueName = ((TextView) view).getText().toString(); //白色
					if (!multiChoice) {
						if (selectIndex != itemPos || !isSelected) {
							onGroupItemClickListener.onGroupItemClick(type, itemPos, getItemKey(valueName), valueName);
							if (isSelector) {
								chooseItemStyle(selectIndex, itemPos);
								selectIndex = itemPos;
							}
							isSelected = true;
							view.setTag(true);
						} else {
							if (isSelected && isSelector) {
								onGroupItemClickListener.unSelected(type, itemPos, getItemKey(valueName), valueName);
								clearItemStyle(selectIndex);
								isSelected = false;
								view.setTag(false);
							}
						}
					} else {
						if ((Boolean)view.getTag()) {
							view.setTag(false);
							onGroupItemClickListener.unSelected(type, itemPos, getItemKey(valueName), valueName);
							clearItemStyle(itemPos);
						} else {
							view.setTag(true);
							onGroupItemClickListener.onGroupItemClick(type, itemPos, getItemKey(valueName), valueName);
							if (isSelector) {
								chooseItemStyle(itemPos);
							}
						}
					}
				}
			});
		}
	}

	/**
	 * 获取按钮的颜色的按钮去找1 , value->key
	 *
	 * @param itemBtnText
	 * @return
	 */
	private String getItemKey(String itemBtnText) {
		for (int i = 0; i < mItems.size(); i++) {
			if (mItems.get(i).getValue().equals(itemBtnText)) {
				return mItems.get(i).getKey();
			}
		}
		return "";
	}

	/**
	 * 不可操作那个的样式
	 *
	 * @param pos
	 */
	public void unableItemStyle(int pos) {
		clearItemsStyle();
		if (pos < getChildCount()) {
			TextView childView = (TextView) getChildAt(pos);
			childView.setBackgroundResource(bgResoureUnSel);
			childView.setTextColor(textColorUnSel);
			setItemPadding(childView);
			childView.setEnabled(false);
		}
	}

	/**
	 * 选中那个的样式
	 *
	 * @param pos
	 */
	public void chooseItemStyle(int prePos, int pos) {
		if (prePos >= 0 && !multiChoice) {
			clearItemStyle(prePos);
		}
		if (pos < getChildCount()) {
			TextView childView = (TextView) getChildAt(pos);
			childView.setBackgroundResource(bgResoureSel);
			childView.setTextColor(textColorSel);
			setItemPadding(childView);
		}
	}

	/**
	 * 改变当前pos的view的样式为“选中”
	 * @param pos
	 */
	private void chooseItemStyle(int pos) {
		chooseItemStyle(0, pos);
	}

	/**
	 * 清除ViewGroup所有的样式
	 */
	public void clearItemsStyle() {
		for (int i = 0; i < getChildCount(); i++) {
			clearItemStyle(i);
		}
	}

	private void clearItemStyle(int poi) {
		TextView childView = (TextView) getChildAt(poi);
		childView.setBackgroundResource(bgResoureNor);
		childView.setTextColor(textColorNor);
		setItemPadding(childView);
		childView.setEnabled(true);
	}

	public void setHorPadding(int horPadding) {
		this.horPadding = horPadding;
	}

	public void setVerPadding(int verPadding) {
		this.verPadding = verPadding;
	}

	public void setHorInterval(int horInterval) {
		this.horInterval = horInterval;
	}

	public void setBgResoureNor(int bgResoureNor) {
		this.bgResoureNor = bgResoureNor;
	}

	public void setBgResoureSel(int bgResoureSel) {
		this.bgResoureSel = bgResoureSel;
	}

	public void setTextColorSel(int textColorSel) {
		this.textColorSel = textColorSel;
	}

	public void setBgResoureUnSel(int bgResoureUnSel) {
		this.bgResoureUnSel = bgResoureUnSel;
	}

	public void setTextColorUnSel(int textColorUnSel) {
		this.textColorUnSel = textColorUnSel;
	}

	private void setItemPadding(TextView view) {
		view.setPadding(horPadding, verPadding, horPadding, verPadding);
	}

	public void addItemViews(String key, List<GoodSelectedTypeItem> items) {
		if (items != null) {
			mItems = items;
			removeAllViews();
			for (GoodSelectedTypeItem item : items) {
				addItemView(key, item);
			}
		}
	}

	/**
	 * addItemView必须在setGroupClickListener()方法之前被调用，否则子控件的onClickListener不会被设置，
	 * 进而导致OnGroupItemClickListener无法被触发
	 *
	 * @param key
	 * @param item
	 */
	private void addItemView(final String key, GoodSelectedTypeItem item) {
		TextView childView = new TextView(mContext);
		/** 用于标记该TextView是否被选中 */
		childView.setTag(false);
		childView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		childView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		childView.setBackgroundResource(bgResoureNor);
		setItemPadding(childView);
		childView.setTextColor(textColorNor);
		childView.setText(item.getValue());

//		childView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				String valueName = ((TextView) view).getText().toString(); //白色
//				view.setTag(true);
//				if (onGroupItemClickListener != null) {
//					onGroupItemClickListener.onGroupItemClick(key, getChildCount(), getItemKey(valueName), valueName);
//				}
//				isSelected = true;
//				if (isSelector) {
//					chooseItemStyle(selectIndex, getChildCount());
//					selectIndex = getChildCount();
//				}
//			}
//		});

		this.addView(childView);
	}

	@Override
	public boolean isSelected() {
//		return isSelected;
		boolean selected = false;
		int size = getChildCount();
		if (size > 0) {

		}
		for(int i = 0; i < getChildCount(); i ++) {
			selected = (Boolean)getChildAt(i).getTag();
			if (selected) {
				break;
			}
		}
		return selected;
	}

	public void setSelectableItem(HashMap<String, String> items) {
		for (int i = 0; i < getChildCount(); i++) {
			TextView childView = (TextView) getChildAt(i);

			String key = childView.getText().toString();
			if (!items.containsKey(key)) {
				unableItemStyle(i);
			}
		}
	}

	public void setSelector(boolean selector) {
		isSelector = selector;
	}

	public interface OnGroupItemClickListener {
		void onGroupItemClick(String type, int itemPos, String key, String value);

		void unSelected(String type, int itemPos, String key, String value);
	}
}
