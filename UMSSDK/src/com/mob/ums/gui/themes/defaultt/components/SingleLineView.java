package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.jimu.gui.Page;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.pickers.TextPicker;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SingleLineView extends LinearLayout {
	private TextView tvTitle;
	private TextView tvText;
	private String text;
	private String titleResName;
	private String hintResName;
	private ImageView ivNext;
	private boolean numeral;
	private boolean isEmail;
	private Page page;
	private int minLenght;
	private int maxLenght;

	public SingleLineView(final Page<?> page) {
		super(page.getContext());
		this.page = page;
		init(getContext());
		setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TextPicker picker = new TextPicker(page.getTheme());
				picker.setDefaultValue(text);
				picker.setTitleResName(titleResName);
				picker.setHintResName(hintResName);
				if (numeral) {
					picker.setNumeral();
				}
				picker.showForResult(page.getContext(), null, new FakeActivity() {
					public void onResult(HashMap<String, Object> data) {
						if (data != null) {
							final String nText = (String) data.get("text");
							post(new Runnable() {
								public void run() {
									setText(nText);
								}
							});
						}
					}
				});
			}
		});
	}
	
	private void init(Context context) {
		setOrientation(VERTICAL);
		LinearLayout ll = new LinearLayout(context);
		int dp53 = ResHelper.dipToPx(context, 44);
		int dp15 = ResHelper.dipToPx(context, 15);
		ll.setPadding(0, 0, dp15, 0);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(ll, lp);
		
		tvTitle = new TextView(context);
		tvTitle.setMinEms(4);
		tvTitle.setTextColor(0xff3b3947);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp53);
		lp.leftMargin = dp15;
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(tvTitle, lp);
		
		tvText = new TextView(context);
		tvText.setTextColor(0xff969696);
		tvText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvText.setGravity(Gravity.RIGHT);
		int dp2=ResHelper.dipToPx(context,2);
		tvText.setPadding(0,dp2,0,dp2);
		tvText.setMaxLines(1);
		tvText.setEllipsize(TextUtils.TruncateAt.END);

		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		ll.addView(tvText, lp);

		ivNext = new ImageView(context);
		ivNext.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_go_details"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = ResHelper.dipToPx(context, 5);
		ll.addView(ivNext, lp);
		ivNext.setVisibility(GONE);
		
		View vSep = new View(context);
		int resid = ResHelper.getBitmapRes(context, "umssdk_defalut_list_sep");
		vSep.setBackground(context.getResources().getDrawable(resid));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		addView(vSep, lp);
	}
	
	public void setTitleResName(String resName) {
		tvTitle.setText(ResHelper.getStringRes(getContext(), resName));
		titleResName = resName;
	}
	
	public void setHintResName(String resName) {
		hintResName = resName;
		tvText.setText(ResHelper.getStringRes(getContext(), hintResName));
	}
	
	public void setText(String cText) {
		if(minLenght != 0 && maxLenght != 0 && !cText.isEmpty()){
			if(cText.length() < minLenght || cText.length() > maxLenght){
				OKDialog.Builder builder = new OKDialog.Builder(getContext(), page.getTheme());
				int resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_tip");
				builder.setTitle(page.getContext().getString(resId));
				resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_nickname_format_tip");
				builder.setMessage(String.format(page.getContext().getString(resId), minLenght, maxLenght));
				builder.show();
				return;
			}
		}

		if(isEmail && !checkEmail(cText)){
			OKDialog.Builder builder = new OKDialog.Builder(getContext(), page.getTheme());
			int resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_tip");
			builder.setTitle(page.getContext().getString(resId));
			resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_email_format_tip");
			builder.setMessage(page.getContext().getString(resId));
			builder.show();
			return;
		}

		if (TextUtils.isEmpty(cText)) {
			setHintContent();
		} else {
			setContent(cText);
		}
		this.text = cText;
		onTextChange();
	}

	private void setContent(String text){
		tvText.setTextColor(0xff3b3947);
		tvText.setText(text);
	}

	private void setHintContent(){
		if (hintResName != null) {
			tvText.setTextColor(0xff969696);
			tvText.setText(ResHelper.getStringRes(tvText.getContext(), hintResName));
		}
	}

	private boolean checkEmail(String cText){
		Pattern p = Pattern.compile("\\w+@\\w+\\.(com|cn)");
		if(p.matcher(cText).matches()){
			return true;
		}
		return false;
	}
	
	public String getText() {
		return text;
	}
	
	public void showNext() {
		ivNext.setVisibility(VISIBLE);
	}
	
	protected void onTextChange() {
		
	}
	
	public void setNumeral() {
		numeral = true;
	}

	public void setMinLenght(int minLenght){
		this.minLenght = minLenght;
	}

	public void setMaxLenght(int maxLenght){
		this.maxLenght = maxLenght;
	}

	public void setEmail(){
		this.isEmail = true;
	}

}
