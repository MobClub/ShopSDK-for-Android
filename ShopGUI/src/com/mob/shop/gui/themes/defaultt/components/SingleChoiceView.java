package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.shop.datatype.RefundType;
import com.mob.shop.datatype.entity.City;
import com.mob.shop.datatype.entity.District;
import com.mob.shop.datatype.entity.Province;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pickers.SingleValuePicker;
import com.mob.shop.gui.pickers.SingleValuePicker.Choice;
import com.mob.shop.gui.pickers.SingleValuePicker.OnSelectedListener;
import com.mob.shop.gui.themes.defaultt.entity.RefundReason;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

public class SingleChoiceView extends LinearLayout {
	private TextView tvTitle;
	private boolean showTitle;
	private TextView tvChoice;
	private ArrayList<Choice> choices;
	private int[] selections;
	private String separator;
	private ImageView ivNext;
	private OnSelectionChangeListener onSelectionChangeListener;
	private Page page;

	public SingleChoiceView(Context context) {
		this(context, null);
	}

	public SingleChoiceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SingleChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		setOrientation(VERTICAL);

		LinearLayout ll = new LinearLayout(context);
		int dp53 = ResHelper.dipToPx(context, 44);
		int dp15 = ResHelper.dipToPx(context, 15);
		ll.setPadding(0, 0, dp15, 0);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp53);
		addView(ll, lp);

		tvTitle = new TextView(context);
		tvTitle.setMinEms(4);
		tvTitle.setTextColor(0xff3b3947);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		lp = new LayoutParams(ResHelper.dipToPx(context, 84), LayoutParams.WRAP_CONTENT);
		lp.leftMargin = dp15;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(tvTitle, lp);

		tvChoice = new TextView(context);
		tvChoice.setTextColor(0xff969696);
		tvChoice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvChoice.setText(ResHelper.getStringRes(context, "shopsdk_default_shippingArea"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
//		lp.leftMargin = ResHelper.dipToPx(context, 10);
		lp.weight = 1;
		ll.addView(tvChoice, lp);

		ivNext = new ImageView(context);
		ivNext.setImageResource(ResHelper.getBitmapRes(context, "shopsdk_default_more"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = ResHelper.dipToPx(context, 5);
		ll.addView(ivNext, lp);
		ivNext.setVisibility(GONE);

		View vSep = new View(context);
		vSep.setBackgroundColor(context.getResources().getColor(R.color.grey_line));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		addView(vSep, lp);
	}

	public void setPage(final Page page) {
		this.page = page;
		separator = ", ";
		setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SingleValuePicker.Builder builder = new SingleValuePicker.Builder(page.getContext(), page.getTheme());
				if (showTitle) {
					builder.showTitle();
				}
				builder.setChoices(choices);
				builder.setSelections(selections);
				builder.setOnSelectedListener(new OnSelectedListener() {
					public void onSelected(int[] selections) {
						setSelections(selections);
					}
				});
				builder.show();
			}
		});
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void showTitle() {
		showTitle = true;
	}

	public synchronized void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}

	public ArrayList<Choice> getChoices() {
		return choices;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setSelections(int[] selections) {
		this.selections = selections;
		ArrayList<Choice> list = choices;
		StringBuilder sb = new StringBuilder();
		if (list != null) {
			for (int i = 0; i < selections.length; i++) {
				if (list.size() > selections[i]) {
					if (sb.length() > 0) {
						sb.append(separator);
					}
					sb.append(list.get(selections[i]).text);
					list = list.get(selections[i]).children;
				} else {
					break;
				}
			}
		}

		if (sb.length() > 0) {
			tvChoice.setTextColor(0xff3b3947);
			tvChoice.setText(sb.toString());
		} else {
			tvChoice.setTextColor(0xff969696);
			tvChoice.setText(ResHelper.getStringRes(tvChoice.getContext(), "umssdk_default_unedit"));
		}
		if (onSelectionChangeListener != null) {
			onSelectionChangeListener.onSelectionsChange();
		}
	}

	public void setTvChoiceHint(String choiceStr){
		tvChoice.setTextColor(0xff969696);
		tvChoice.setText(choiceStr);
	}

	public Choice[] getSelections() {
		if (selections == null) {
			return null;
		}

		Choice[] cc = new Choice[selections.length];
		ArrayList<Choice> list = choices;
		for (int i = 0; i < selections.length; i++) {
			if (list.size() > selections[i]) {
				cc[i] = list.get(selections[i]);
				list = list.get(selections[i]).children;
			} else {
				break;
			}
		}

		return cc;
	}

	public static <T> ArrayList<Choice> createChoice(Class<T> type, List<T> list) {
		ArrayList<Choice> cs = new ArrayList<Choice>();
		if (type.equals(Province.class)) {
			List<Province> provinces = (List<Province>) list;
			for (Province province : provinces) {
				Choice c = new Choice();
				c.ext = province;
				c.text = province.getName();
				cs.add(c);
				List<City> cities = province.getChildren();
				if (cities != null) {
					for (City city : cities) {
						Choice cp = new Choice();
						cp.ext = city;
						cp.text = city.getName();
						c.children.add(cp);
						List<District> regionList = city.getChildren();
						if (regionList != null) {
							for (District region : regionList) {
								Choice cc = new Choice();
								cc.ext = region;
								cc.text = region.getName();
								cp.children.add(cc);
							}
						}
					}
				}
			}
		} else if(type.equals(RefundType.class)){
			for (RefundType refundType : RefundType.values()) {
				Choice c = new Choice();
				c.ext = refundType;
				c.text = refundType.getDesc();
				cs.add(c);
			}
		} else if(type.equals(RefundReason.class)){
			for (RefundReason refundReason : RefundReason.values()) {
				Choice c = new Choice();
				c.ext = refundReason;
				int resid = ResHelper.getStringRes(MobSDK.getContext(), refundReason.getResName());
				c.text = MobSDK.getContext().getString(resid);
				cs.add(c);
			}
		}
		return cs;
	}

	public void showNext() {
		ivNext.setVisibility(VISIBLE);
	}

	public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
		this.onSelectionChangeListener = onSelectionChangeListener;
	}

	public interface OnSelectionChangeListener {
		void onSelectionsChange();
	}
}
